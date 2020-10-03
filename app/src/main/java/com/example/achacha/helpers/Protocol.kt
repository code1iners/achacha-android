package com.example.achacha.helpers

import com.example.achacha.R

object Protocol {

    // note. commons
    const val TITLE = "TITLE"
    const val VALUE = "VALUE"
    const val STATUS = "STATUS"

    // note. http response
    const val STATUS_OK = 200
    const val STATUS_CREATED = 201
    const val STATUS_BAD_REQUEST = 400
    const val STATUS_UNAUTHORIZED = 401
    const val STATUS_FORBIDDEN = 403
    const val STATUS_NOT_FOUND = 403

    // note. string status
    const val BLANK = ""
    const val NULL = "null"

    // note. process
    const val PENDING = "PENDING"
    const val FINISHED = "FINISHED"

    // note. fragment
    const val MAIN_CONTENTS_CONTAINER = R.id.mainActivity__body_fragment_container

    // note. main focus fragment
    const val MAIN_FOCUS = "MAIN_FOCUS"
    const val TODO = "TODO"

    // note. category in to do
    const val CATEGORY = "CATEGORY"

    // note. key option
    const val UNIQUE_KEY_LENGTH = 40

    // note. user
    const val USERNAME = "USERNAME"

    // note. mode
    const val WORK = "WORK"
    const val USER_PROFILE = "USER_PROFILE"
    const val DISPLAY_MODE = "DISPLAY_MODE"

    // note. display mode
    const val DARK_MODE = "DARK_MODE"

    // note. request code
    const val REQUEST_CODE_EDITOR_ACTIVITY = 1000
}