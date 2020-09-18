package com.example.achacha.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.achacha.MainActivity
import com.example.achacha.R
import com.example.achacha.helpers.Protocol.USERNAME
import com.example.achacha.helpers.Protocol.USER_PROFILE
import com.example.helpers.PreferencesManager
import timber.log.Timber

class SetUsernameFragment : Fragment(), TextView.OnEditorActionListener {

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

        return v
    }

    private fun init(v: View) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        initSource()
        initWidgets(v)
    }

    private fun initSource() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            sourceActivity = context!! as MainActivity

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun initWidgets(v: View) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. assignment
        setUsernameFragment__username_answer = v.findViewById(R.id.setUsernameFragment__username_answer)

        // note. listener
        setUsernameFragment__username_answer.setOnEditorActionListener(this)

    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        when (actionId) {
            EditorInfo.IME_ACTION_DONE -> {
                finishWithData(v!!.text.toString())
                return true
            }
        }
        return false
    }

    private fun finishWithData(text: String) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            PreferencesManager(activity!!, USER_PROFILE).add(USERNAME, text)
            sourceActivity.username = text
            sourceActivity.usernameIsSetted!!.isBoo = true
        } catch (e: Exception) {e.printStackTrace()}
    }
}