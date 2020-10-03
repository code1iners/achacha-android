package com.example.achacha.helpers

import android.app.Activity
import android.util.Log
import com.example.achacha.helpers.Protocol.MAIN_FOCUS
import com.example.helpers.PreferencesManager
import com.example.achacha.helpers.Protocol.TODO
import com.example.achacha.helpers.Protocol.WORK
import com.example.achacha.models.CategoryModel
import com.example.achacha.models.TodoModel
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
                val model = TodoModel().apply {
                    category = mapData["category"]
                    categoryPosition = mapData["categoryPosition"]!!.toInt()
                    value = mapData["value"]
                    status = Protocol.PENDING
                    created = LocalDateTime.now().toString()
                    updated = created.toString()
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

        fun deleteTodo(activity: TodoModel, todo: TodoModel) {
            Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
            todo.log()

        }

        fun deleteTodoByCategory(activity: Activity, category: CategoryModel): JSONObject? {
            // note. get array
            var array = readTodos(activity)
            val tempArray = readTodos(activity)

            // note. null check
            if (array.length() == 0) return null
            // note. copy

            category.log()

            var result: JSONObject? = null

            val deleteList: ArrayList<Int> = ArrayList()

            Log.i(TAG, "arrayLength:${tempArray.length()}")
            for (idx in 0 until tempArray.length()) {
                Log.v(TAG, "idx:$idx")
                val obj = tempArray[idx] as JSONObject
                if (obj.get("category") == category.category) {
                    Log.d(TAG, "obj:$obj")
                    // note. new code
                    deleteList.add(idx)

                    // note. origin code
//                    // note. set result
//                    result = obj
//
//                    // note. delete array item
//                    array.remove(idx)
//                    array = JSONArray(array.toString())
//                    Log.e(TAG, "array:$array")
                }
            }
            for (item in deleteList) Log.e(TAG, "before:$item")
            deleteList.sort()
            for (item in deleteList) Log.e(TAG, "after:$item")


            // note. delete to do
            PreferencesManager(activity, WORK).remove(TODO)

            // note. re-create do do
            PreferencesManager(activity, WORK).add(TODO, array.toString())

            return result
        }

        fun clearTodo(activity: Activity) {
            PreferencesManager(activity, WORK).remove(TODO)
        }

        // note. main focus
        fun createMainFocus(activity: Activity, mainFocus: String) {
            PreferencesManager(activity, WORK).add(MAIN_FOCUS, mainFocus)
        }

        fun clearMainFocus(activity: Activity) {
            PreferencesManager(activity, WORK).remove(MAIN_FOCUS)
        }
    }
}