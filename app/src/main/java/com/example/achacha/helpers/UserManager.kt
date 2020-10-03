package com.example.achacha.helpers

import android.app.Activity
import com.example.achacha.helpers.Protocol.USERNAME
import com.example.achacha.helpers.Protocol.USER_PROFILE
import com.example.helpers.PreferencesManager

class UserManager {
  companion object {
    fun clearUser(activity: Activity) {
      PreferencesManager(activity, USER_PROFILE).remove(USERNAME)
    }
  }
}