package com.example.achacha

import android.os.Bundle
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
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy


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
        Logger.w(object : Any() {}.javaClass.enclosingMethod!!.name)

        initVars()
        initFragments()
        initWidgets()
        initScreenSettings()
        initListeners()
        initUsername()
    }

    private fun initVars() {
        Logger.w(object : Any() {}.javaClass.enclosingMethod!!.name)

        usernameIsSetted = BooleanVariable()
    }

    private fun initWidgets() {
        Logger.w(object : Any() {}.javaClass.enclosingMethod!!.name)

    }

    private fun initFragments() {
        Logger.w(object : Any() {}.javaClass.enclosingMethod!!.name)

        mainFragment = MainFragment()
        setUsernameFragment = SetUsernameFragment()
    }

    private fun initScreenSettings() {
        Logger.w(object : Any() {}.javaClass.enclosingMethod!!.name)

        ScreenManager.alwaysOn(this)
    }

    private fun initListeners() {
        Logger.w(object : Any() {}.javaClass.enclosingMethod!!.name)
        try {
            usernameIsSetted!!.setListener {
                Logger.i("username:$username")
                display()
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun initUsername() {
        Logger.w(object : Any() {}.javaClass.enclosingMethod!!.name)

        username = PreferencesManager(this, USER_PROFILE)[USERNAME]
        Logger.i("username:$username")
    }

    private fun display() {
        Logger.w(object : Any() {}.javaClass.enclosingMethod!!.name)

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
        Logger.w(object : Any() {}.javaClass.enclosingMethod!!.name)
        Logger.i("keyCode:$keyCode, event:${event!!.repeatCount}")
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

    init {
//        // note. init timber
//        Timber.plant(Timber.DebugTree())
        // note. init logger
        Logger.addLogAdapter(AndroidLogAdapter(PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
            .methodCount(1)         // (Optional) How many method line to show. Default 2
//            .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
            .tag("LOGGER")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()))
        // note. init three ten
        AndroidThreeTen.init(this)
    }
}