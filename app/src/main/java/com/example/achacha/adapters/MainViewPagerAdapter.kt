package com.example.achacha.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MainViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    lateinit var fragmentCollection: ArrayList<Fragment>

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return fragmentCollection[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "OBJECT ${(position + 1)}"
    }
}