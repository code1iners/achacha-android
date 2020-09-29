package com.example.achacha

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.achacha.helpers.CategoryManager
import com.example.achacha.helpers.Protocol.MAIN_CONTENTS_CONTAINER
import com.example.achacha.helpers.Protocol.USERNAME
import com.example.achacha.helpers.Protocol.USER_PROFILE
import com.example.achacha.helpers.WorkManager
import com.example.achacha.ui.MainFragment
import com.example.achacha.ui.SetUsernameFragment
import com.example.achacha.ui.TodoFragment
import com.example.helpers.BooleanVariable
import com.example.helpers.FragmentChanger
import com.example.helpers.PreferencesManager
import com.example.helpers.ScreenManager
import com.jakewharton.threetenabp.AndroidThreeTen


class MainActivity : AppCompatActivity()
    , DrawerLayout.DrawerListener
    , View.OnClickListener
    , View.OnTouchListener
{
    companion object {
        val TAG = MainActivity::class.simpleName

        // note. navigation-vars
        lateinit var drawerLayout: DrawerLayout
        lateinit var drawerView: View

        fun openMenu() {
            drawerLayout.openDrawer(drawerView)
        }
    }

    // note. widgets
    lateinit var drawerView__data_clear_container: RelativeLayout
    lateinit var drawerView__options_darkMode_container: RelativeLayout

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
        drawerLayout = findViewById(R.id.drawer_layout)
        drawerView = findViewById(R.id.drawer_view)
        drawerLayout.setDrawerListener(this)

        drawerView__data_clear_container = findViewById(R.id.drawerView__data_clear_container)
        drawerView__data_clear_container.setOnClickListener(this)
        drawerView__options_darkMode_container = findViewById(R.id.drawerView__options_darkMode_container)
        drawerView__options_darkMode_container.setOnClickListener(this)
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

            drawerLayout.setDrawerListener(this)
            drawerView.setOnTouchListener(this)
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

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
    }

    override fun onDrawerOpened(drawerView: View) {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
    }

    override fun onDrawerClosed(drawerView: View) {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
    }

    override fun onDrawerStateChanged(newState: Int) {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
        return true
    }

    override fun onClick(v: View) {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            Log.i(TAG, resources.getResourceEntryName(v.id))
            when (v.id) {
                R.id.drawerView__data_clear_container -> {
                    WorkManager.clearMainFocus(this)
                    CategoryManager.clearCategory(this)

                    val fragment = MainFragment.mainViewPagerAdapter.fragmentCollection[1] as TodoFragment
                    fragment.resetCategories()
                    fragment.resetTodos()
                }
            }
        } catch (e: Exception) {e.printStackTrace()}
    }
}