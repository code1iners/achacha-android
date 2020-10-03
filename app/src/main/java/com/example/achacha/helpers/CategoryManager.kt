package com.example.achacha.helpers

import android.app.Activity
import android.util.Log
import com.example.achacha.helpers.Protocol.CATEGORY
import com.example.achacha.helpers.Protocol.WORK
import com.example.achacha.models.CategoryModel
import com.example.achacha.models.TodoModel
import com.example.helpers.PreferencesManager
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

    fun deleteCategoryById(activity: Activity, model: CategoryModel): JSONObject? {
      Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
      val categoriesAsString = PreferencesManager(activity, WORK)[CATEGORY]
      Log.i(TAG, "categoriesAsString:$categoriesAsString")
      model.log()

      // note. null check
      if(categoriesAsString.isNullOrBlank()) return null

      // note. get array
      val array = JSONArray(categoriesAsString)
      var result: JSONObject? = null

      // note. inspections & delete
      for (idx in 0 until array.length()) {
        if (array.getJSONObject(idx).get("pk") == model.pk) {
//          Log.d(TAG, "objPk:${array.getJSONObject(idx).get("pk")}")
          // note. set result for return
          result = array.getJSONObject(idx)

          // note. delete by index
          array.remove(idx)
          break
        }
      }

      // note. delete data
      PreferencesManager(activity, WORK).remove(CATEGORY)

      // note. save new data in device
      PreferencesManager(activity, WORK).add(CATEGORY, array.toString())

      return result
    }

    fun clearCategory(activity: Activity) {
      PreferencesManager(activity, WORK).remove(CATEGORY)
    }
  }
}