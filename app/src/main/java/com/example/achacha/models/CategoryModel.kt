package com.example.achacha.models

import android.util.Log

class CategoryModel {
  var pk: Int = -1
  var category: String? = null
  var created: String? = null
  var updated: String? = null

  fun log() {
    Log.i(TAG, "\npk:${this.pk}" +
        "\ncategory:${this.category}" +
        "\ncreated:${this.created}" +
        "\nupdated:${this.updated}"
    )
  }

  companion object {
    val TAG = CategoryModel::class.simpleName
  }
}