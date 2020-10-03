package com.example.achacha

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import com.example.achacha.helpers.CategoryManager
import com.example.achacha.helpers.Protocol.MAIN_CONTENTS_CONTAINER
import com.example.achacha.helpers.Protocol.USERNAME
import com.example.achacha.helpers.Protocol.USER_PROFILE
import com.example.achacha.helpers.UserManager
import com.example.achacha.helpers.WorkManager
import com.example.achacha.ui.MainFocusFragment
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
    , CompoundButton.OnCheckedChangeListener
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

    // note. widgets-data
    lateinit var drawerView__data_clear_container: RelativeLayout
    // note. widgets-options
    lateinit var drawerView__options_darkMode_container: RelativeLayout
    lateinit var drawerView__options_darkMode_trigger: CheckBox
    // note. widgets-etc
    lateinit var drawerView__etc_report_container: RelativeLayout

    // note. fragments
    lateinit var mainFragment: MainFragment
    lateinit var setUsernameFragment: SetUsernameFragment

    // note. vars
    var username: String? = null

    // note. vars-listener
    var usernameIsSetted: BooleanVariable? = null

    // note. day/night mode
    private var mDayNightMode = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        display()
    }

    private fun init() {
        initLibraries()
        initVars()
        initWidgets()
        initScreenSettings()
        initListeners()
        initUsername()
        initSettings()
    }

    private fun initLibraries() {

        AndroidThreeTen.init(this)
    }

    private fun initVars() {
        usernameIsSetted = BooleanVariable()
        mDayNightMode = AppCompatDelegate.getDefaultNightMode()
    }

    private fun initWidgets() {
        initDrawerLayout()
    }

    private fun initDrawerLayout() {
        drawerLayout = findViewById(R.id.drawer_layout)
        drawerView = findViewById(R.id.drawer_view)
        drawerLayout.setDrawerListener(this)

        // note. data
        drawerView__data_clear_container = findViewById(R.id.drawerView__data_clear_container)
        drawerView__data_clear_container.setOnClickListener(this)
        // note. options-darkMode
        drawerView__options_darkMode_container = findViewById(R.id.drawerView__options_darkMode_container)
        drawerView__options_darkMode_container.setOnClickListener(this)
        drawerView__options_darkMode_trigger = findViewById(R.id.drawerView__options_darkMode_trigger)
        drawerView__options_darkMode_trigger.setOnCheckedChangeListener(this)
        // note. etc
        drawerView__etc_report_container = findViewById(R.id.drawerView__etc_report_container)
        drawerView__etc_report_container.setOnClickListener(this)
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

    private fun initSettings() {
        // note. options-darkMode
        initDarkMode()
    }

    private fun initDarkMode() {
        Log.e(TAG, "mDayNightMode:$mDayNightMode")
        when (mDayNightMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> {
                changeMode(false)
            }

            AppCompatDelegate.MODE_NIGHT_YES -> {
                changeMode(true)
            }
        }
    }

    private fun changeMode(isDarkMode: Boolean) {
        if (isDarkMode) {
            // note. options-darkMode
            drawerView__options_darkMode_trigger.isChecked = true

            // note. to do fragment
        } else {
            // note. options-darkMode
            drawerView__options_darkMode_trigger.isChecked = false

//            todofrag
            // note. to do fragment
        }
    }

    private fun display() {
        val fragmentManager = supportFragmentManager
        for (idx in 0 until fragmentManager.backStackEntryCount) {
            fragmentManager.popBackStack()
        }

        mainFragment = MainFragment()
        setUsernameFragment = SetUsernameFragment()

        if (username.isNullOrBlank()) {
            FragmentChanger.replace(
                supportFragmentManager,
                MAIN_CONTENTS_CONTAINER,
                setUsernameFragment,
                false,
                null
            )
        } else {
            mainFragment = MainFragment()

            FragmentChanger.replace(
                supportFragmentManager,
                MAIN_CONTENTS_CONTAINER,
                mainFragment,
                false,
                null
            )
        }
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
        Log.w(TAG, object : Any() {}.javaClass.enclosingMethod!!.name)
    }

    override fun onDrawerOpened(drawerView: View) {
        Log.w(TAG, object : Any() {}.javaClass.enclosingMethod!!.name)
    }

    override fun onDrawerClosed(drawerView: View) {
        Log.w(TAG, object : Any() {}.javaClass.enclosingMethod!!.name)
    }

    override fun onDrawerStateChanged(newState: Int) {
        Log.w(TAG, object : Any() {}.javaClass.enclosingMethod!!.name)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Log.w(TAG, object : Any() {}.javaClass.enclosingMethod!!.name)
        return true
    }

    override fun onClick(v: View) {
        Log.w(TAG, object : Any() {}.javaClass.enclosingMethod!!.name)
        try {
            Log.i(TAG, resources.getResourceEntryName(v.id))
            when (v.id) {
                R.id.drawerView__data_clear_container -> {
                    val b = AlertDialog.Builder(this)
                    b.setTitle("데이터 초기화")
                    b.setMessage("초기화된 정보는 다시 되돌릴 수 없습니다. 정말로 진행 하시겠습니까?")
                    b.setNegativeButton("취소") { _, _ -> }
                    b.setPositiveButton("삭제") { _, _ ->
                        UserManager.clearUser(this)
                        WorkManager.clearMainFocus(this)
                        WorkManager.clearTodo(this)
                        CategoryManager.clearCategory(this)

                        // note. init username
                        initUsername()

                        // note. init mainFocus
                        val mainFocus =
                            MainFragment.mainViewPagerAdapter.fragmentCollection[0] as MainFocusFragment
                        mainFocus.resetMainFocus()

                        // note. init to do
                        val todo =
                            MainFragment.mainViewPagerAdapter.fragmentCollection[1] as TodoFragment
                        todo.resetCategories()
                        todo.resetTodos()

                        display()
                    }
                    val d = b.create()
                    d.show()
                }

                R.id.drawerView__options_darkMode_container -> {
                    drawerView__options_darkMode_trigger.performClick()
                }

                R.id.drawerView__etc_report_container -> {
                    Toast.makeText(this, resources.getString(R.string.menu_service_information), Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
//        Log.i(TAG, "newConfig:$newConfig")
//
//        val isDarkTheme = isDarkTheme(newConfig)
//        Log.e(TAG, "isDarkTheme:$isDarkTheme")
////        if (isDarkTheme) {
////            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
////        } else {
////            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
////        }
////        recreate()
//    }

    override fun onCheckedChanged(v: CompoundButton, isChecked: Boolean) {
        Log.w(TAG, object : Any() {}.javaClass.enclosingMethod!!.name)
        Log.i(TAG, "${resources.getResourceEntryName(v.id)}, isChecked:$isChecked")

        when (v.id) {
            R.id.drawerView__options_darkMode_trigger -> {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                delegate.applyDayNight()
            }
        }
    }
}