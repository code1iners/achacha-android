package com.example.achacha.helpers

import android.annotation.SuppressLint
import android.os.Handler
import android.widget.TextView
import java.util.*
import kotlin.collections.HashMap

class CustomTimerTest {

  // note. increase
  class Increase : TimerTask() {
    lateinit var onTimerIncreaseListener: OnTimerIncreaseListener

    // note. for initialization
    var isStarted: Boolean = false
    var isPause: Boolean = false

    // note. value
    private var second: Int = 0
    private var minute: Int = 0
    private var hour: Int = 0
    var triggerSecond: Int = -1

    // note. views
    lateinit var viewSecond: TextView
    lateinit var viewMinute: TextView
    lateinit var viewHour: TextView

    // note. handler
    private val handler: Handler = Handler()
    private val cycleTime: Long = 1000

    fun setViews(sec: TextView?, min: TextView?, hour: TextView?) {
      try {
        if (sec != null) this.viewSecond = sec
        if (min != null) this.viewMinute = min
        if (hour != null) this.viewHour = hour
      } catch (e: Exception) {e.printStackTrace()}
    }

    fun start() {
      Timer().schedule(this, 0, cycleTime)
      isStarted = true
    }

    private fun triggerAnEvent() {
      try {
        if (second == triggerSecond) {
          onTimerIncreaseListener.timerTriggerEvent(triggerSecond)
        }

      } catch (e: Exception) {e.printStackTrace()}
    }

    private fun setValuesBySecond() {
      setValueSecond()
      setValueMinute()
      setValueHour()
    }

    private fun setValueSecond() {
      try {
        if (viewSecond == null) return

        var second = (if (this.second >= 60) this.second % 60 else this.second).toString()
        if (second.toInt() < 10) second = "0$second"
//                Log.d(TAG, "second:$second")

        handler.post {
          viewSecond.text = second
        }
      } catch (e: Exception) {e.printStackTrace()}
    }

    private fun setValueMinute() {
      try {
        if (viewMinute == null) return

        this.minute = this.second / 60
        var minute = (if (this.minute >= 60) this.minute % 60 else this.minute).toString()
        if (minute.toInt() < 10) minute = "0$minute"
//                Log.d(TAG, "minute:$minute")

        handler.post {
          viewMinute.text = minute
        }

      } catch (e: Exception) {e.printStackTrace()}
    }

    private fun setValueHour() {
      try {
        if (viewMinute == null) return

        this.hour = this.minute / 60
        var hour = (if (this.hour >= 60) this.hour % 60 else this.hour).toString()
        if (hour.toInt() < 10) hour = "0$hour"
//                Log.d(TAG, "hour:$hour")

        handler.post {
          viewHour.text = hour
        }
      } catch (e: Exception) {e.printStackTrace()}
    }

    override fun run() {
      try {
        // note. check is pause
        if (isPause) return
        // note. trigger an event
        if (triggerSecond != -1) triggerAnEvent()
        // note. set values
        setValuesBySecond()
        // note. decrease second value
        second++
      } catch (e: Exception) {e.printStackTrace()}
    }

    interface OnTimerIncreaseListener {
      fun timerTriggerEvent(setValue: Int)
    }

    companion object {
      val TAG = Increase::class.simpleName
    }
  }

  // note. decrease
  class Decrease : TimerTask() {
    private val TAG = Decrease::class.simpleName

    // note. listener
    lateinit var onTimerDecreaseListener: OnTimerDecreaseListener

    // note. values
    private var second: Int = 0
    private var minute: Int = 0
    private var hour: Int = 0
    var triggerSecond: Int = -1

    // note. views
    lateinit var viewSecond: TextView
    lateinit var viewMinute: TextView
    lateinit var viewHour: TextView

    // note. other vars
    private val handler: Handler = Handler()
    private val cycleTime: Long = 1000
    var isStarted: Boolean = false
    var isPause: Boolean = false

    fun setViews(second: TextView?, minute: TextView?, hour: TextView?) {
      try {
        if (second != null) this.viewSecond = second
        if (minute != null) this.viewMinute = minute
        if (hour != null) this.viewHour = hour
      } catch (e: Exception) {e.printStackTrace()}
    }

    fun setLimitTimeBySecond(inputSecond: Int) {
      try  {
        // note. set second
        second = inputSecond
        // note. set minute by second
        minute = second / 60
        // note. set hour by second
        hour = minute / 60
      } catch (e: Exception) {e.printStackTrace()}
    }

    fun setLimitTimeByMinute(inputMinute: Int) {
      try  {
        // note. set minute
        minute = inputMinute
        // note. set second by minute
        second = minute * 60
        // note. set hour by minute
        hour = minute / 60

      } catch (e: Exception) {e.printStackTrace()}
    }

    fun setLimitTimeByHour(inputHour: Int) {
      try  {
        // note. set hour
        hour = inputHour
        // note. set minute by hour
        minute = hour * 60
        // note. set second by hour
        second = minute * 60
      } catch (e: Exception) {e.printStackTrace()}
    }

    fun start() {
      Timer().schedule(this, 0, cycleTime)
      isStarted = true
    }

    fun clear() {
      try {
        handler.post {
          viewSecond.text = "00"
          viewMinute.text = "00"
          viewHour.text = "00"
        }
      } catch(e: Exception) {e.printStackTrace()}
    }

    override fun run() {
      try {
//                Log.d(TAG, "second:$second")

        if (isPause) return

        if (second >= 0) {
          // note. trigger an event
          if (triggerSecond != -1) triggerAnEvent()
          // note. set values
          setValuesBySecond(second)
          // note. decrease second value
          second--
        } else {
          onTimerDecreaseListener.timerStop()
        }

      } catch (e: Exception) {e.printStackTrace()}
    }

    private fun triggerAnEvent() {
      try {
        if (second == triggerSecond) {
          onTimerDecreaseListener.timerTriggerEvent(triggerSecond)
        }

      } catch (e: Exception) {e.printStackTrace()}
    }

    private fun setValuesBySecond(second: Int) {
      setSecondBySecond(second)
      setMinuteBySecond(second)
      setHourBySecond(second)
    }

    @SuppressLint("SetTextI18n")
    private fun setSecondBySecond(inputSecond: Int) {
      try {
        if (viewSecond == null) return

        val second = inputSecond % 60
        handler.post{
          if (second < 10) viewSecond.text = "0$second"
          else viewSecond.text = "$second"
        }
      } catch (e: Exception) {e.printStackTrace()}
    }

    private fun setMinuteBySecond(inputSecond: Int) {
      try {
        if (viewMinute == null) return

        val minute = inputSecond / 60
        handler.post{
          if (minute < 10) viewMinute.text = "0$minute"
          else viewMinute.text = "$minute"
        }
      } catch (e: Exception) {e.printStackTrace()}
    }

    private fun setHourBySecond(inputSecond: Int) {
      try {
        if (viewHour == null) return

        val hour = inputSecond / 600
        handler.post{
          if (hour < 10) viewHour.text = "0$hour"
          else viewHour.text = "$hour"
        }
      } catch (e: Exception) {e.printStackTrace()}
    }

    interface OnTimerDecreaseListener {
      fun timerStop()
      fun timerTriggerEvent(setValue: Int)
    }

    companion object {
      val TAG = Decrease::class.simpleName
    }
  }

  // note. current time
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

      currentTimerListener.print(date)
    }

    companion object {
      val TAG = Timer::class.simpleName
    }

    interface CurrentTimerListener {
//      fun print(year: Int, hours: Int, minutes: Int)
      fun print(date: Date)
    }
  }
}