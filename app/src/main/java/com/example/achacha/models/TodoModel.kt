package com.example.achacha.models

import com.orhanobut.logger.Logger
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
        Logger.i("category:$category")
        Logger.i("categoryPosition:$categoryPosition")
        Logger.i("value:$value")
        Logger.i("status:$status")
        Logger.i("created:$created")
        Logger.i("updated:$updated")
        Logger.i("key:$key")
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

        Logger.i( "obj:${obj}")

        return obj
    }

    companion object {
        val TAG = TodoModel::class.simpleName
    }
}