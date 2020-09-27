package com.example.achacha

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.achacha.helpers.Protocol.MAIN_CONTENTS_CONTAINER
import com.example.achacha.helpers.Protocol.USERNAME
import com.example.achacha.helpers.Protocol.USER_PROFILE
import com.example.achacha.ui.MainFragment
import com.example.achacha.ui.SetUsernameFragment
import com.example.helpers.BooleanVariable
import com.example.helpers.FragmentChanger
import com.example.helpers.PreferencesManager
import com.example.helpers.ScreenManager
import com.jakewharton.threetenabp.AndroidThreeTen


class MainActivity : AppCompatActivity() {

    // note. widgets

    // note. fragments
    lateinit var mainFragment: MainFragment
    lateinit var setUsernameFragment: SetUsernameFragment

    // note. vars
    var username: String? = null

    // note. vars-listener
    var usernameIsSetted: BooleanVariable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        display()
    }

    private fun init() {
        

        initLibraries()
        initVars()
        initFragments()
        initWidgets()
        initScreenSettings()
        initListeners()
        initUsername()
    }

    private fun initLibraries() {

        AndroidThreeTen.init(this)
    }

    private fun initVars() {
        

        usernameIsSetted = BooleanVariable()
    }

    private fun initWidgets() {
        

    }

    private fun initFragments() {
        

        mainFragment = MainFragment()
        setUsernameFragment = SetUsernameFragment()
    }

    private fun initScreenSettings() {
        

        ScreenManager.alwaysOn(this)
    }

    private fun initListeners() {
        
        try {
            usernameIsSetted!!.setListener {
                display()
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun initUsername() {
        

        username = PreferencesManager(this, USER_PROFILE)[USERNAME]
    }

    private fun display() {
        

        if (username.isNullOrBlank())
        FragmentChanger.replace(
            supportFragmentManager,
            MAIN_CONTENTS_CONTAINER,
            setUsernameFragment,
            false,
            null
        )
        else
        FragmentChanger.replace(
            supportFragmentManager,
            MAIN_CONTENTS_CONTAINER,
            mainFragment,
            false,
            null
        )
    }

    // note. @override
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        
        Log.i(TAG, "keyCode:$keyCode, event:${event!!.repeatCount}")
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
                finish()
            }
        } catch (e: Exception) {e.printStackTrace()}
        return true
    }

    // note. @companion object
    companion object {
        val TAG = MainActivity::class.simpleName
    }
}