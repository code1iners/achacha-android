package com.example.achacha.helpers

import java.util.*

class CurrentTimer: TimerTask() {

    // note. vars-boolean
    var isStarted = false
    var isPause = false
    // note. vars-listener
    lateinit var currentTimerListener: CurrentTimerListener

    fun start() {
        Timer().schedule(this, 0,1000)
        isStarted = true
    }

    override fun run() {
        if (isPause) return

        val date = Calendar.getInstance().time
        val year = date.year
        val hours = date.hours
        val minutes = date.minutes

        currentTimerListener.print(year = year, hours = hours, minutes = minutes)
    }

    companion object {
        val TAG = Timer::class.simpleName
    }

    interface CurrentTimerListener {
        fun print(year: Int, hours: Int, minutes: Int)
    }
}