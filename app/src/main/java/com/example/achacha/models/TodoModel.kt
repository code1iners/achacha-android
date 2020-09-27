package com.example.achacha.models

import android.util.Log
import org.json.JSONObject

class TodoModel {
    var category: String? = null
    var categoryPosition: String? = null
    var value: String? = null
    var status: String? = null
    var created: String? = null
    var updated: String? = null
    var key: String? = null

    fun log() {
        Log.i(TAG, "\ncategory:$category\n" +
            "categoryPosition:$categoryPosition\n" +
            "value:$value\n" +
            "status:$status\n" +
            "created:$created\n" +
            "updated:$updated\n" +
            "key:$key")
    }

    fun toJson(): JSONObject{
        
        val obj = JSONObject()
        try {
            obj.put("category", this.category)
            obj.put("categoryPosition", this.categoryPosition)
            obj.put("value", this.value)
            obj.put("status", this.status)
            obj.put("created", this.created)
            obj.put("updated", this.updated)
            obj.put("key", this.key)
        } catch (e: Exception) {e.printStackTrace()}

        Log.i( TAG, "obj:${obj}")

        return obj
    }

    companion object {
        val TAG = TodoModel::class.simpleName
    }
}