package com.example.achacha.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.achacha.R
import com.example.achacha.adapters.MainViewPagerAdapter

class MainFragment: Fragment() {

    // note. widgets
    lateinit var mainFocusFragment__viewPager: ViewPager

    // note. fragments
    var toDoFragment: TodoFragment? = null
    var mainFocusFragment: MainFocusFragment? = null

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
        initWidgets(v)
        initFragments()
        initAdapters()
    }

    private fun initWidgets(v: View) {
        mainFocusFragment__viewPager = v.findViewById(R.id.mainFocusFragment__viewPager)
    }

    private fun initFragments() {
        toDoFragment = TodoFragment()
        mainFocusFragment = MainFocusFragment()
    }

    private fun initAdapters() {
        initViewPagerAdapters()
    }

    private fun initViewPagerAdapters() {
        try {
            mainViewPagerAdapter = MainViewPagerAdapter(fragmentManager!!)

            mainViewPagerAdapter.fragmentCollection = ArrayList()
            mainViewPagerAdapter.fragmentCollection.add(mainFocusFragment!!)
            mainViewPagerAdapter.fragmentCollection.add(toDoFragment!!)

            mainFocusFragment__viewPager.adapter = mainViewPagerAdapter

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun display() {
        mainViewPagerAdapter.notifyDataSetChanged()
    }

    // note. @companion object
    companion object {
        val TAG = MainFragment::class.simpleName

        // note. view pager
        lateinit var mainViewPagerAdapter: MainViewPagerAdapter
    }
}