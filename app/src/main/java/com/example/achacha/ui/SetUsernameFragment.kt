package com.example.achacha.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.achacha.MainActivity
import com.example.achacha.R
import com.example.achacha.helpers.Protocol.BLANK
import com.example.achacha.helpers.Protocol.USERNAME
import com.example.achacha.helpers.Protocol.USER_PROFILE
import com.example.helpers.PreferencesManager

class SetUsernameFragment : Fragment(), TextView.OnEditorActionListener {

    companion object {

    }

    // note. widgets-header
    lateinit var setUsernameFragment__logo_image: ImageView
    // note. widgets-body
    lateinit var setUsernameFragment__username_answer: EditText

    // note. other-vars
    lateinit var sourceActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_set_username, container, false)

        init(v)
        display()

        return v
    }

    private fun display() {
        displayHeaders()
    }

    private fun displayHeaders() {
        try {
//            Glide.with(context!!).load(resources.getDrawable(R.drawable.alpaca_001)).circleCrop().into(setUsernameFragment__logo_image)

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun init(v: View) {
        initSource()
        initWidgets(v)
    }

    private fun initSource() {
        
        try {
            sourceActivity = context!! as MainActivity

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun initWidgets(v: View) {
        // note. assignment
        // note. widgets-header
        setUsernameFragment__logo_image = v.findViewById(R.id.setUsernameFragment__logo_image)
        // note. widgets-body
        setUsernameFragment__username_answer = v.findViewById(R.id.setUsernameFragment__username_answer)

        // note. listener
        setUsernameFragment__username_answer.setOnEditorActionListener(this)

    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        when (actionId) {
            EditorInfo.IME_ACTION_DONE -> {
                finishWithData(v!!.text.toString())
                return true
            }
        }
        return false
    }

    private fun finishWithData(text: String) {
        try {
            PreferencesManager(activity!!, USER_PROFILE).add(USERNAME, text)
            sourceActivity.username = text
            sourceActivity.usernameIsSetted!!.isBoo = true

            // note. init edit widget
            setUsernameFragment__username_answer.setText(BLANK)
            setUsernameFragment__username_answer.clearFocus()
        } catch (e: Exception) {e.printStackTrace()}
    }
}