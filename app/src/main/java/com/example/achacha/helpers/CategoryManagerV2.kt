package com.example.achacha.helpers

import android.app.Activity
import android.util.Log
import com.example.achacha.helpers.Protocol.CATEGORY
import com.example.achacha.helpers.Protocol.WORK
import com.example.achacha.models.CategoryModel
import com.example.helpers.PreferencesManager
import org.json.JSONArray
import org.json.JSONObject

class CategoryManagerV2 {
  companion object {
    val TAG = CategoryManagerV2::class.simpleName

    fun deleteCategory(activity: Activity, category: CategoryModel): JSONObject? {
      Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)

      val arr = readCategories(activity)
      val resultArray = JSONArray()
      val resultObject = JSONObject().apply {
        this.put("pk", category.pk)
        this.put("category", category.category)
        this.put("created", category.created)
        this.put("updated", category.updated)
        this.put("todos", category.todos)
      }

      if (arr.length() > 0) {
        for (idx in 0 until arr.length()) {
          if (arr.getJSONObject(idx).getString("category") == category.category) {
            Log.e(TAG, "found out same category item")
            category.log()  // note. for test
            continue
          }

          // note. add result array
          resultArray.put(arr.getJSONObject(idx))
        }

        // note. delete category in device
        PreferencesManager(activity, WORK).remove(CATEGORY)
        // note. re-save category in device
        PreferencesManager(activity, WORK).add(CATEGORY, resultArray.toString())

        // note. delete included to do items
        WorkManagerV2.deleteTodoByCategory(activity, category)

        return resultObject
      }

      return null
    }

    fun readCategories(activity: Activity): JSONArray {
      Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
      return if (PreferencesManager(activity, WORK)[CATEGORY].isNullOrBlank()) JSONArray() else JSONArray(PreferencesManager(activity, WORK)[CATEGORY])
    }
  }
}