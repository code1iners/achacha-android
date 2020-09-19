package com.example.achacha

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.viewpager.widget.ViewPager
import com.example.achacha.adapters.MainViewPagerAdapter
import com.example.achacha.helpers.Protocol.MAIN_CONTENTS_CONTAINER
import com.example.achacha.helpers.Protocol.USERNAME
import com.example.achacha.helpers.Protocol.USER_PROFILE
import com.example.achacha.ui.MainFocusFragment
import com.example.achacha.ui.MainFragment
import com.example.achacha.ui.SetUsernameFragment
import com.example.achacha.ui.ToDoFragment
import com.example.helpers.BooleanVariable
import com.example.helpers.FragmentChanger
import com.example.helpers.PreferencesManager
import com.example.helpers.ScreenManager
import timber.log.Timber

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
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        initVars()
        initFragments()
        initWidgets()
        initScreenSettings()
        initListeners()
        initUsername()
    }

    private fun initVars() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        usernameIsSetted = BooleanVariable()
    }

    private fun initWidgets() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

    }

    private fun initFragments() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        mainFragment = MainFragment()
        setUsernameFragment = SetUsernameFragment()
    }

    private fun initScreenSettings() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        ScreenManager.alwaysOn(this)
    }

    private fun initListeners() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            usernameIsSetted!!.setListener {
                Timber.i("username:$username")
                display()
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun initUsername() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        username = PreferencesManager(this, USER_PROFILE)[USERNAME]
        Timber.i("username:$username")
    }

    private fun display() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        if (username.isNullOrBlank())
        FragmentChanger.replace(supportFragmentManager, MAIN_CONTENTS_CONTAINER, setUsernameFragment, false, null)
        else
        FragmentChanger.replace(supportFragmentManager, MAIN_CONTENTS_CONTAINER, mainFragment, false, null)
    }

    // note. @override
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("keyCode:$keyCode, event:${event!!.repeatCount}")
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
        Timber.plant(Timber.DebugTree())
    }
}