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
import com.example.achacha.helpers.CustomTimerTest
import com.example.achacha.helpers.Protocol.DARK_MODE
import com.example.achacha.helpers.Protocol.DISPLAY_MODE
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
import java.util.*


class MainActivity : AppCompatActivity()
    , DrawerLayout.DrawerListener
    , View.OnClickListener
    , View.OnTouchListener
    , CompoundButton.OnCheckedChangeListener
    , CustomTimerTest.CurrentTimer.CurrentTimerListener
{
    companion object {
        val TAG = MainActivity::class.simpleName

        lateinit var timer: CustomTimerTest.CurrentTimer

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
        timer()
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

        timer = CustomTimerTest.CurrentTimer()
        timer.currentTimerListener = this
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
        val status = PreferencesManager(this, DISPLAY_MODE)[DARK_MODE]
        if (status.isNullOrBlank()) return

        changeMode(status.toBoolean())
    }

    private fun changeMode(isDarkMode: Boolean) {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
        Log.i(TAG, "isDarkMode:$isDarkMode")
        drawerView__options_darkMode_trigger.isChecked = isDarkMode
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

    private fun timer() {

        timer.start()
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

                    // note. save information in device
                    PreferencesManager(this, DISPLAY_MODE).add(DARK_MODE, true.toString())
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                    // note. save information in device
                    PreferencesManager(this, DISPLAY_MODE).add(DARK_MODE, false.toString())
                }
                delegate.applyDayNight()
            }
        }
    }

    override fun print(date: Date) {
        try {
            val year = date.year
            val hours = if (date.hours < 10) "0" + date.hours else date.hours.toString()
            val minutes = if (date.minutes < 10) "0" + date.minutes else date.minutes.toString()
            val seconds = if (date.seconds < 10) "0" + date.seconds else date.seconds.toString()

//            Log.i(TAG, "year:$year, hours:$hours, minutes:$minutes, seconds:$seconds")

            mainFragment.mainFocusFragment?.run {
                handler.post {
                    this.mainFragment__header_timer_hours.text = hours
                    this.mainFragment__header_timer_minutes.text = minutes
                }
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

    override fun onResume() {
        super.onResume()

        // note. timer play
        timer.isPause = false
    }

    override fun onPause() {
        super.onPause()

        // note. timer pause
        timer.isPause = true
    }

    override fun onStop() {
        super.onStop()

        // note. timer pause
        timer.isPause = true
    }

    override fun onDestroy() {
        super.onDestroy()

        // note. timer pause
        timer.isPause = true
    }
}