package com.example.achacha.helpers

import android.app.Activity
import com.example.achacha.R
import com.example.achacha.helpers.Protocol.CATEGORY
import com.example.achacha.helpers.Protocol.WORK
import com.example.achacha.models.CategoryModel
import com.example.helpers.PreferencesManager
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.orhanobut.logger.Logger
import org.json.JSONArray
import org.json.JSONObject
import org.threeten.bp.LocalDateTime

class CategoryManager {
  companion object {
    fun createCategory(activity: Activity, pk: Int, category: String): JSONObject {
      Logger.i("pk:$pk, category:$category")
      val created = LocalDateTime.now()
      val arr = readCategoryAllAsJsonArray(activity)
      val result = JSONObject()
      try {
        result.put("pk", pk)
        result.put("category", category)
        result.put("created", created)
        result.put("updated", created)

        // note. save in device
        arr.put(pk, result)


        PreferencesManager(activity, WORK).add(CATEGORY, arr.toString())

      } catch (e: Exception) {e.printStackTrace()}
      return result
    }

    fun readCategoryAllAsJsonArray(activity: Activity): JSONArray {
      return if (PreferencesManager(activity, WORK)[CATEGORY].isNullOrBlank()) JSONArray() else JSONArray(PreferencesManager(activity, WORK)[CATEGORY])
    }

    fun readCategoryAsJsonObject(activity: Activity, category: String): JSONObject? {
      val arr = readCategoryAllAsJsonArray(activity)
      for (idx in 0 until arr.length()) {
        val obj = arr.getJSONObject(idx)
        if (obj.getString("category") == category) return obj
      }
      return null
    }

    fun clearCategory(activity: Activity) {
      PreferencesManager(activity, WORK).remove(CATEGORY)
    }
  }
}