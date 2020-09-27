package com.example.achacha.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.achacha.R
import com.example.achacha.helpers.CategoryManager
import com.example.achacha.helpers.Protocol
import com.example.achacha.helpers.Protocol.STATUS
import com.example.achacha.helpers.Protocol.STATUS_NOT_FOUND
import com.example.achacha.helpers.Protocol.STATUS_OK
import com.example.achacha.helpers.Protocol.TITLE
import com.example.achacha.helpers.Protocol.VALUE
import com.example.helpers.Keypad

class EditorActivity : AppCompatActivity()
  , TextView.OnEditorActionListener
  , View.OnClickListener{

  // note. widgets
  private lateinit var editorActivity__header_title: TextView
  private lateinit var editorActivity__body_editor: EditText
  private lateinit var editorActivity__footer_cancel: Button
  private lateinit var editorActivity__footer_confirm: Button

  // note. editor
  private lateinit var title: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_editor)

    init()
  }

  private fun init() {
    

    checkArgs()
    initWidgets()
  }

  private fun checkArgs() {
    

    try {
      intent?.let {
        title = it.getStringExtra(TITLE)
        Log.i(TAG, "title:$title")
      }
    } catch (e: Exception) {e.printStackTrace()}
  }

  private fun initWidgets() {
    

    editorActivity__header_title = findViewById(R.id.editorActivity__header_title)
    editorActivity__body_editor = findViewById(R.id.editorActivity__body_editor)
    editorActivity__body_editor.setOnEditorActionListener(this)
    Keypad(this).up(editorActivity__body_editor)
    editorActivity__footer_cancel = findViewById(R.id.editorActivity__footer_cancel)
    editorActivity__footer_cancel.setOnClickListener(this)
    editorActivity__footer_confirm = findViewById(R.id.editorActivity__footer_confirm)
    editorActivity__footer_confirm.setOnClickListener(this)
  }

  private fun finishWithResult() {
    
    try {

      Keypad(this).down(editorActivity__body_editor)

      val editorActivityResult = Intent()
      editorActivityResult.putExtra(STATUS, STATUS_OK)
      editorActivityResult.putExtra(VALUE, editorActivity__body_editor.text.toString())
      setResult(RESULT_OK, editorActivityResult)
      finish()
    } catch (e: Exception) {e.printStackTrace()}
  }

  private fun finishWithNull() {
    
    try {
      Keypad(this).down(editorActivity__body_editor)

      val editorActivityResult = Intent()
      editorActivityResult.putExtra(STATUS, STATUS_NOT_FOUND)
      setResult(RESULT_OK, editorActivityResult)
      finish()
    } catch (e: Exception) {e.printStackTrace()}
  }

  override fun onClick(v: View) {
    
    Log.i(TAG, resources.getResourceEntryName(v.id))
    when (v.id) {
      R.id.editorActivity__footer_confirm -> {
        finishWithResult()
      }

      R.id.editorActivity__footer_cancel -> {
        finishWithNull()
      }
    }
  }

  override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
    
    Log.i(TAG, "${resources.getResourceEntryName(v.id)}")
    when (v.id) {
      R.id.editorActivity__body_editor -> {
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
    val TAG = EditorActivity::class.simpleName
  }
}