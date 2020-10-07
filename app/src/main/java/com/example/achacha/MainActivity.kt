package com.example.achacha

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
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
import kotlin.collections.ArrayList


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

        // note. background-images
        lateinit var backgrounds: ArrayList<Drawable>

        fun openMenu() {
            drawerLayout.openDrawer(drawerView)
        }

        fun getBackGroundImageByRandom(): Drawable? {
//            Log.w(TAG, object : Any() {}.javaClass.enclosingMethod!!.name)
            try {
                backgrounds.run {
//                    Log.e(TAG, "backgroundsSize:${this.size}")
                    if (this.size == 0) return null

                    val selectedNumber = Random().nextInt(this.size)
//                    Log.i(TAG, "selectedNumber:$selectedNumber")

                    return this[selectedNumber]
                }
            } catch (e: Exception) {e.printStackTrace()}
            return null
        }
    }

    // note. widgets-data
    lateinit var drawerView__data_clear_container: RelativeLayout
    // note. widgets-options
    lateinit var drawerView__options_darkMode_container: RelativeLayout
    lateinit var drawerView__options_darkMode_trigger: CheckBox
    lateinit var drawerView__options_background_refresh_container: RelativeLayout
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
        initScreenSettings()
        initLibraries()
        initVars()
        initWidgets()
        initSettings()
        initListeners()
        initBackgrounds()
        initUsername()
    }

    private fun initBackgrounds() {
        val status = PreferencesManager(this, DISPLAY_MODE)[DARK_MODE]
        Log.e(TAG, "status:$status")

        try {
            if (!status.toBoolean() && backgrounds.size == 0) {
                val backgroundList = arrayOf(
                    resources.getDrawable(R.drawable.background_001),
                    resources.getDrawable(R.drawable.background_002),
                    resources.getDrawable(R.drawable.background_003),
                    resources.getDrawable(R.drawable.background_004),
                    resources.getDrawable(R.drawable.background_005),
                    resources.getDrawable(R.drawable.background_006),
                    resources.getDrawable(R.drawable.background_007),
                    resources.getDrawable(R.drawable.background_008),
                    resources.getDrawable(R.drawable.background_009),
                    resources.getDrawable(R.drawable.background_010),
                    resources.getDrawable(R.drawable.background_011),
                    resources.getDrawable(R.drawable.background_012),
                    resources.getDrawable(R.drawable.background_013),
                    resources.getDrawable(R.drawable.background_014),
                    resources.getDrawable(R.drawable.background_015),
                    resources.getDrawable(R.drawable.background_016)
                )

                backgrounds.clear()
                backgrounds.addAll(backgroundList)
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun initLibraries() {

        AndroidThreeTen.init(this)
    }

    private fun initVars() {
        usernameIsSetted = BooleanVariable()
        mDayNightMode = AppCompatDelegate.getDefaultNightMode()

        timer = CustomTimerTest.CurrentTimer()
        timer.currentTimerListener = this

        // note. background list
        backgrounds = ArrayList()
    }

    private fun initWidgets() {
        initDrawerLayout()
    }

    private fun initDrawerLayout() {
        // note. drawer
        drawerLayout = findViewById(R.id.drawer_layout)
        drawerView = findViewById(R.id.drawer_view)

        // note. data-clear
        drawerView__data_clear_container = findViewById(R.id.drawerView__data_clear_container)
        // note. options-darkMode
        drawerView__options_darkMode_container = findViewById(R.id.drawerView__options_darkMode_container)
        drawerView__options_darkMode_trigger = findViewById(R.id.drawerView__options_darkMode_trigger)
        drawerView__options_background_refresh_container = findViewById(R.id.drawerView__options_background_refresh_container)
        // note. etc
        drawerView__etc_report_container = findViewById(R.id.drawerView__etc_report_container)

        // note. listener
        drawerLayout.setDrawerListener(this)
        drawerView__data_clear_container.setOnClickListener(this)
        drawerView__options_darkMode_container.setOnClickListener(this)
        drawerView__options_darkMode_trigger.setOnCheckedChangeListener(this)
        drawerView__options_background_refresh_container.setOnClickListener(this)
        drawerView__etc_report_container.setOnClickListener(this)

    }

    private fun initScreenSettings() {
        ScreenManager.alwaysOn(this)
        ScreenManager.fullScreen(this)
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
        Log.w(TAG, object : Any() {}.javaClass.enclosingMethod!!.name)
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

                        try {
                            // note. init mainFocus
                            val mainFocus =
                                MainFragment.mainViewPagerAdapter.fragmentCollection[0] as MainFocusFragment
                            mainFocus.resetMainFocus()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        try {
                            // note. init to do
                            val todo =
                                MainFragment.mainViewPagerAdapter.fragmentCollection[1] as TodoFragment
                            todo.resetCategories()
                            todo.resetTodos()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        display()
                    }
                    val d = b.create()
                    d.show()
                }

                R.id.drawerView__options_darkMode_container -> {
                    drawerView__options_darkMode_trigger.performClick()
                }

                R.id.drawerView__options_background_refresh_container -> {
                    val background = getBackGroundImageByRandom()
                    background?.run {
                        val currentFragment = supportFragmentManager.findFragmentById(MAIN_CONTENTS_CONTAINER)
                        Log.e(TAG, "currentFragment:$currentFragment" +
                            "\nid:${currentFragment?.id}" +
                            "\nname:${currentFragment?.javaClass?.simpleName}")

                        try {
                            when (currentFragment?.javaClass?.simpleName) {
                                MainFragment.TAG -> {
                                    val currentItem = mainFragment.mainFocusFragment__viewPager.currentItem
                                    Log.e(TAG, "mainFragmentCurrentItem:$currentItem")
                                    when (currentItem) {
                                        0 -> {
                                            mainFragment.mainFocusFragment?.displayBackground()
                                        }

                                        1 -> {
                                            mainFragment.toDoFragment?.displayBackground()
                                        }

                                        else -> {
                                            Log.e(TAG, "something is wrong")
                                        }
                                    }

                                }

                                else -> {
                                    Log.e(TAG, "something is wrong")
                                }
                            }
                        } catch (e: Exception) {e.printStackTrace()}
                    }
                }

                R.id.drawerView__etc_report_container -> {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.menu_service_information),
                        Toast.LENGTH_SHORT
                    ).show()
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
                var mode: Int = -1
                if (isChecked) {
                    mode = AppCompatDelegate.MODE_NIGHT_YES
                    AppCompatDelegate.setDefaultNightMode(mode)

                    // note. save information in device
                    PreferencesManager(this, DISPLAY_MODE).add(DARK_MODE, true.toString())
                } else {
                    mode = AppCompatDelegate.MODE_NIGHT_NO
                    AppCompatDelegate.setDefaultNightMode(mode)
                    // note. save information in device
                    PreferencesManager(this, DISPLAY_MODE).add(DARK_MODE, false.toString())
                }

                Log.e(TAG, "mDayNightMode:$mDayNightMode, updateMode:$mode")
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
                    this.mainFragment__header_timer_seconds.text = seconds
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