package com.example.achacha.helpers

import android.app.Activity
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
import timber.log.Timber

class WorkManager {
    companion object {

        // note. todo
        fun createTodo(activity: Activity, mapData: HashMap<String, String>): TodoModel? {
            Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
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
                model.logger()

                val arr = readTodo(activity)
                // note. model to jsonObject
                arr.put(model.toJson())
                PreferencesManager(activity, WORK).add(TODO, arr.toString())

                return model

            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        fun readTodo(activity: Activity): JSONArray {
            PreferencesManager(activity, WORK)[TODO]?.run {
                Timber.i("todoAsString:$this")
                return JSONArray(this)
            }
            return JSONArray()
        }

        fun updateTodo(activity: Activity, position: Int, model: TodoModel) {
            val arr = readTodo(activity)
            arr.put(position, Gson().toJson(model))
            PreferencesManager(activity, WORK).add(TODO, arr.toString())
        }

        fun deleteTodo(activity: Activity, position: Int) {
            val arr = readTodo(activity)
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