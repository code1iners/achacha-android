package com.example.achacha.models

import android.util.Log
import org.json.JSONObject

class TodoModel {
    var pk: Int = -1
    var category: String? = null
    var categoryPosition: Int = -1
    var value: String? = null
    var status: String? = null
    var created: String? = null
    var updated: String? = null

    fun log() {
        Log.i(TAG, "pk:$pk" +
            "\ncategory:$category" +
            "\ncategoryPosition:$categoryPosition" +
            "\nvalue:$value" +
            "\nstatus:$status" +
            "\ncreated:$created" +
            "\nupdated:$updated")
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
        } catch (e: Exception) {e.printStackTrace()}

        Log.i( TAG, "obj:${obj}")

        return obj
    }

    companion object {
        val TAG = TodoModel::class.simpleName
    }
}