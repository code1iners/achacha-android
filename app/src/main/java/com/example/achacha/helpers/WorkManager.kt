package com.example.achacha.helpers

import android.app.Activity
import android.util.Log
import com.example.achacha.helpers.Protocol.MAIN_FOCUS
import com.example.helpers.PreferencesManager
import com.example.achacha.helpers.Protocol.TODO
import com.example.achacha.helpers.Protocol.WORK
import com.example.achacha.models.TodoModel
import com.example.helpers.PasswordGenerator
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import org.threeten.bp.LocalDateTime

class WorkManager {
    companion object {
        val TAG = WorkManager::class.simpleName

        // note. todo
        fun createTodo(activity: Activity, mapData: HashMap<String, String>): TodoModel? {
            
            try {
                // note. data setting
                val generator = PasswordGenerator.PasswordGeneratorBuilder().useDigits(true).useLower(true).useUpper(true).usePunctuation(true).build()
                val model = TodoModel().apply {
                    category = mapData["category"]
                    categoryPosition = mapData["categoryPosition"]
                    value = mapData["value"]
                    status = Protocol.PENDING
                    created = LocalDateTime.now().toString()
                    updated = created.toString()
                    key = generator.generate(Protocol.UNIQUE_KEY_LENGTH)
                }
                model.log()

                val arr = readTodos(activity)
                // note. model to jsonObject
                arr.put(model.toJson())
                PreferencesManager(activity, WORK).add(TODO, arr.toString())

                return model

            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        fun readTodos(activity: Activity): JSONArray {
            PreferencesManager(activity, WORK)[TODO]?.run {
                Log.i(TAG, "todoAsString:$this")
                return JSONArray(this)
            }
            return JSONArray()
        }

        fun updateTodo(activity: Activity, position: Int, model: TodoModel) {
            val arr = readTodos(activity)
            arr.put(position, Gson().toJson(model))
            PreferencesManager(activity, WORK).add(TODO, arr.toString())
        }

        fun deleteTodo(activity: Activity, position: Int) {
            val arr = readTodos(activity)
            arr.remove(position)
            PreferencesManager(activity, WORK).add(TODO, arr.toString())
        }

        // note. main focus
        fun createMainFocus(activity: Activity, mainFocus: String) {
            PreferencesManager(activity, WORK).add(MAIN_FOCUS, mainFocus)
        }

        fun clearMainFocus(activity: Activity) {
            PreferencesManager(activity, WORK).remove(MAIN_FOCUS)
            PreferencesManager(activity, WORK).remove(TODO)
        }
    }
}