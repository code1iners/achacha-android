package com.example.achacha.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.achacha.R
import com.example.achacha.adapters.MainViewPagerAdapter
import timber.log.Timber

class MainFragment: Fragment() {

    // note. widgets
    lateinit var mainFocusFragment__viewPager: ViewPager

    // note. fragments
    lateinit var toDoFragment: TodoFragment
    lateinit var mainFocusFragment: MainFocusFragment

    // note. view pager
    lateinit var mainViewPagerAdapter: MainViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_main, container, false)

        init(v)
        display()

        return v
    }

    private fun init(v: View) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        initWidgets(v)
        initFragments()
        initAdapters()
    }

    private fun initWidgets(v: View) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        mainFocusFragment__viewPager = v.findViewById(R.id.mainFocusFragment__viewPager)
    }

    private fun initFragments() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        toDoFragment = TodoFragment()
        mainFocusFragment = MainFocusFragment()
    }

    private fun initAdapters() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        initViewPagerAdapters()
    }

    private fun initViewPagerAdapters() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            mainViewPagerAdapter = MainViewPagerAdapter(fragmentManager!!)

            mainViewPagerAdapter.fragmentCollection = ArrayList()
            mainViewPagerAdapter.fragmentCollection.add(mainFocusFragment)
            mainViewPagerAdapter.fragmentCollection.add(toDoFragment)

            mainFocusFragment__viewPager.adapter = mainViewPagerAdapter

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun display() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        mainViewPagerAdapter.notifyDataSetChanged()
    }

    // note. @companion object
    companion object {
        val TAG = MainFragment::class.simpleName
    }
}