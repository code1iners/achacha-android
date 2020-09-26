package com.example.achacha.models

import com.orhanobut.logger.Logger

class CategoryModel {
  var pk: Int = -1
  var category: String? = null
  var created: String? = null
  var updated: String? = null

  fun log() {
    Logger.i("pk:${this.pk}" +
        "\ncategory:${this.category}" +
        "\ncreated:${this.created}" +
        "\nupdated:${this.updated}"
    )
  }
}