package com.example.achacha

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.example.achacha.helpers.Protocol.MAIN_CONTENTS_CONTAINER
import com.example.achacha.ui.MainFragment
import com.example.helpers.FragmentChanger
import com.example.helpers.ScreenManager
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    // note. widgets

    // note. fragments
    lateinit var mainFragment: MainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        display()
    }

    private fun init() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        initWidgets()
        initFragments()
        initScreenSettings()
    }

    private fun initScreenSettings() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        ScreenManager.alwaysOn(this)
    }

    private fun initWidgets() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

    }

    private fun initFragments() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        mainFragment = MainFragment()
    }

    private fun display() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        FragmentChanger.replace(supportFragmentManager, MAIN_CONTENTS_CONTAINER, mainFragment, false, null)
    }

    companion object {
        val TAG = MainActivity::class.simpleName
    }

    init {
        Timber.plant(Timber.DebugTree())
    }
}