package com.example.achacha.models

import org.json.JSONObject
import timber.log.Timber

class TodoModel {
    var category: String? = null
    var categoryPosition: String? = null
    var value: String? = null
    var status: String? = null
    var created: String? = null
    var updated: String? = null
    var key: String? = null

    fun logger() {
        Timber.i("category:$category")
        Timber.i("categoryPosition:$categoryPosition")
        Timber.i("value:$value")
        Timber.i("status:$status")
        Timber.i("created:$created")
        Timber.i("updated:$updated")
        Timber.i("key:$key")
    }

    fun toJson(): JSONObject{
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
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

        Timber.i( "obj:${obj}")

        return obj
    }

    companion object {
        val TAG = TodoModel::class.simpleName
    }
}