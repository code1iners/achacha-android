package com.example.achacha.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.example.achacha.MainActivity
import com.example.achacha.R
import com.example.achacha.helpers.Protocol
import com.example.achacha.helpers.Protocol.STATUS
import com.example.achacha.helpers.Protocol.STATUS_NOT_FOUND
import com.example.achacha.helpers.Protocol.STATUS_OK
import com.example.achacha.helpers.Protocol.TITLE
import com.example.achacha.helpers.Protocol.VALUE
import com.example.helpers.Keypad
import com.example.helpers.PreferencesManager
import com.example.helpers.ScreenManager

class NewTodoListEditorActivity : AppCompatActivity()
  , TextView.OnEditorActionListener
  , View.OnClickListener{

  // note. widgets
  private lateinit var newTodoListEditorActivity__layout: RelativeLayout
  private lateinit var newTodoListEditorActivity__header_title: TextView
  private lateinit var newTodoListEditorActivity__body_editor: EditText
  private lateinit var newTodoListEditorActivity__footer_cancel: Button
  private lateinit var newTodoListEditorActivity__footer_confirm: Button

  // note. editor
  private lateinit var title: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_new_todo_list_editor)

    init()
    setBackground()
    display()
  }

  private fun display() {
    displayHeader()
  }

  private fun displayHeader() {
    // note. set title
    newTodoListEditorActivity__header_title.text = title
  }

  private fun init() {
    checkArgs()
    initWidgets()
    initScreenSettings()
  }

  private fun checkArgs() {
    Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
    try {
      intent?.run {
        this.getStringExtra(TITLE)?.let {
          title = it
          Log.i(TAG, "title:$title")
        }
      }
    } catch (e: Exception) {e.printStackTrace()}
  }

  private fun initWidgets() {
    newTodoListEditorActivity__layout = findViewById(R.id.newTodoListEditorActivity__layout)
    newTodoListEditorActivity__header_title = findViewById(R.id.newTodoListEditorActivity__header_title)
    newTodoListEditorActivity__body_editor = findViewById(R.id.newTodoListEditorActivity__body_editor)
    newTodoListEditorActivity__body_editor.setOnEditorActionListener(this)
    Keypad(this).up(newTodoListEditorActivity__body_editor)
    newTodoListEditorActivity__footer_cancel = findViewById(R.id.newTodoListEditorActivity__footer_cancel)
    newTodoListEditorActivity__footer_cancel.setOnClickListener(this)
    newTodoListEditorActivity__footer_confirm = findViewById(R.id.newTodoListEditorActivity__footer_confirm)
    newTodoListEditorActivity__footer_confirm.setOnClickListener(this)
  }

  private fun initScreenSettings() {
    ScreenManager.alwaysOn(this)
    ScreenManager.fullScreen(this)
  }

  private fun setBackground() {
    try {
      val status = PreferencesManager(this, Protocol.DISPLAY_MODE)[Protocol.DARK_MODE]
      if (status.isNullOrBlank()) return

      if (!status.toBoolean()) {
        newTodoListEditorActivity__layout.setBackgroundDrawable(MainActivity.getBackGroundImageByRandom())
      }
    } catch (e: Exception) {e.printStackTrace()}
  }

  private fun finishWithResult() {
    
    try {

      Keypad(this).down(newTodoListEditorActivity__body_editor)

      val newTodoListEditorActivityResult = Intent()
      newTodoListEditorActivityResult.putExtra(STATUS, STATUS_OK)
      newTodoListEditorActivityResult.putExtra(VALUE, newTodoListEditorActivity__body_editor.text.toString())
      setResult(RESULT_OK, newTodoListEditorActivityResult)
      finish()
    } catch (e: Exception) {e.printStackTrace()}
  }

  private fun finishWithNull() {
    
    try {
      Keypad(this).down(newTodoListEditorActivity__body_editor)

      val newTodoListEditorActivityResult = Intent()
      newTodoListEditorActivityResult.putExtra(STATUS, STATUS_NOT_FOUND)
      setResult(RESULT_OK, newTodoListEditorActivityResult)
      finish()
    } catch (e: Exception) {e.printStackTrace()}
  }

  override fun onClick(v: View) {
    
    Log.i(TAG, resources.getResourceEntryName(v.id))
    when (v.id) {
      R.id.newTodoListEditorActivity__footer_confirm -> {
        finishWithResult()
      }

      R.id.newTodoListEditorActivity__footer_cancel -> {
        finishWithNull()
      }
    }
  }

  override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
    
    Log.i(TAG, "${resources.getResourceEntryName(v.id)}")
    when (v.id) {
      R.id.newTodoListEditorActivity__body_editor -> {
        when (actionId) {
          EditorInfo.IME_ACTION_DONE -> {
            finishWithResult()
          }
        }
      }
    }
    return true
  }

  override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
    
    Log.i(TAG, "keyCode:$keyCode, event:$event")
    try {
      if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0) {
        finishWithNull()
      }
    } catch (e: Exception) {e.printStackTrace()}
    return true
  }

  companion object {
    val TAG = NewTodoListEditorActivity::class.simpleName
  }
}