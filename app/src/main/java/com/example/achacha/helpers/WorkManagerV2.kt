package com.example.achacha.helpers

import android.app.Activity
import android.util.Log
import com.example.achacha.helpers.Protocol.TODO
import com.example.achacha.helpers.Protocol.WORK
import com.example.achacha.models.CategoryModel
import com.example.achacha.models.TodoModel
import com.example.helpers.PreferencesManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONArray
import org.json.JSONObject

class WorkManagerV2 {
  companion object {
    val TAG = WorkManagerV2::class.simpleName

    fun createTodo(activity: Activity, todo: TodoModel): JSONObject? {
      Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)

      todo.log()  // note. for test

      // note. declared & assignment json object
      val obj = JSONObject().apply {
        this.put("category", todo.category)
        this.put("categoryPosition", todo.categoryPosition)
        this.put("value", todo.value)
        this.put("status", todo.status)
        this.put("created", todo.created)
        this.put("updated", todo.updated)
      }

      // note. get todos
      val arr = readTodosAsJsonArray(activity)

      if (arr.length() == 0) {
        Log.i(TAG, "create first todo item")
        // note. set pk
        obj.put("pk", 0)

        // note. set into array
        arr.put(obj)

        // note. save in device
        PreferencesManager(activity, WORK).add(TODO, arr.toString())
      } else {
        // note. when already has item
        Log.i(TAG, "create new todo item")

        // note. set object pk
        val newArray = JSONArray()
        var lastItemPk = -1
        for (idx in 0 until arr.length()) {
          Log.d(TAG, "idx:$idx, item:${arr.getJSONObject(idx)}")
          val obj = arr.getJSONObject(idx)
          newArray.put(obj)

          // note. set result object pk
          lastItemPk = obj.getInt("pk")
        }
        obj.put("pk", lastItemPk + 1)

        newArray.put(obj)

        // note. save in device
        PreferencesManager(activity, WORK).add(TODO, newArray.toString())
      }

      return obj
    }

    fun readTodosAsJsonArray(activity: Activity): JSONArray {
      Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)

      PreferencesManager(activity, WORK)[TODO]?.run {
        return JSONArray(this)
      }

      return JSONArray()
    }

    fun deleteTodo(activity: Activity, todo: TodoModel, category: CategoryModel) {
      val todosAsJsonArray = readTodosAsJsonArray(activity)
      if (todosAsJsonArray.length() > 0) {
        todo.log()      // note. for test
        category.log()  // note. for test

        val resultArray = JSONArray()

        for (idx in 0 until todosAsJsonArray.length()) {
          val obj = todosAsJsonArray.getJSONObject(idx)
          if (obj.get("category") == category.category && obj.get("value") == todo.value) continue
          resultArray.put(obj)
        }

        // note. clear to do list
        PreferencesManager(activity, WORK).remove(TODO)

        // note. re-set to do list
        PreferencesManager(activity, WORK).add(TODO, resultArray.toString())
      }
    }

    fun deleteTodoByCategory(activity: Activity, category: CategoryModel) {
      val todos = readTodosAsJsonArray(activity)
      if (todos.length() > 0) {
        val resultArray = JSONArray()

        for (idx in 0 until todos.length()) {
          if (todos.getJSONObject(idx).getString("category") == category.category) continue
          resultArray.put(todos.getJSONObject(idx))
        }
        // note. delete category in device
        PreferencesManager(activity, WORK).remove(TODO)
        // note. re-save category in device
        PreferencesManager(activity, WORK).add(TODO, resultArray.toString())
      }
    }
  }
}