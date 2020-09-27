package com.example.achacha.helpers

import android.app.Activity
import android.util.Log
import com.example.achacha.R
import com.example.achacha.helpers.Protocol.CATEGORY
import com.example.achacha.helpers.Protocol.WORK
import com.example.achacha.models.CategoryModel
import com.example.helpers.PreferencesManager
import com.google.gson.Gson
import com.google.gson.JsonArray
import org.json.JSONArray
import org.json.JSONObject
import org.threeten.bp.LocalDateTime

class CategoryManager {
  companion object {
    val TAG = CategoryManager::class.simpleName

    fun createCategoryAsJsonObject(activity: Activity, pk: Int, category: String): JSONObject? {
      Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
      val created = LocalDateTime.now()
      val array = readCategoryAllAsJsonArray(activity)
      val result = JSONObject().apply {
        this.put("pk", pk)
        this.put("category", category)
        this.put("created", created)
        this.put("updated", created)
      }
      try {
        array?.let {
          it.put(result)
          PreferencesManager(activity, WORK).add(CATEGORY, it.toString())
        }
      } catch (e: Exception) {e.printStackTrace()}

      return result
    }

    fun readCategoryAllAsJsonArray(activity: Activity): JSONArray? {

      val categoriesAsString = PreferencesManager(activity, WORK)[CATEGORY]
      Log.i(TAG, "categoriesAsString:$categoriesAsString")

      return if (categoriesAsString.isNullOrBlank()) JSONArray() else JSONArray(categoriesAsString)
    }

    fun readCategoryAsJsonObject(activity: Activity, category: String): JSONObject? {



      return null
    }

    fun clearCategory(activity: Activity) {
      PreferencesManager(activity, WORK).remove(CATEGORY)
    }
  }
}