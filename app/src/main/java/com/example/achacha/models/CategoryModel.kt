package com.example.achacha.models

import android.util.Log

class CategoryModel {
  var pk: Int = -1
  var category: String? = null
  var created: String? = null
  var updated: String? = null
  val todos: ArrayList<TodoModel> = ArrayList()

  fun log() {
    Log.i(TAG, "\npk:${this.pk}" +
        "\ncategory:${this.category}" +
        "\ncreated:${this.created}" +
        "\nupdated:${this.updated}" +
        "\ntodosSize:${this.todos.size}"
    )
    for (todo in this.todos) {
      Log.i(TAG, "todo:${todo.log()}")
    }
  }

  companion object {
    val TAG = CategoryModel::class.simpleName
  }
}