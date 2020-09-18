package com.example.achacha.ui

import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.achacha.R
import com.example.achacha.helpers.CurrentTimer
import timber.log.Timber
import java.util.*

class MainFragment : Fragment(), TextView.OnEditorActionListener, CurrentTimer.CurrentTimerListener {

    // note. widgets-header
    lateinit var mainFragment__header_container: LinearLayout
    lateinit var mainFragment__header_timer_hours: TextView
    lateinit var mainFragment__header_timer_minutes: TextView
    // note. widgets-body
    lateinit var mainFragment__body_container: LinearLayout
    lateinit var mainFragment__body_question: TextView
    lateinit var mainFragment__body_answer: EditText
    // note. widgets-footer
    lateinit var mainFragment__footer_phrase_container: LinearLayout
    lateinit var mainFragment__footer_phrase: TextView

    // note. other-vars
    lateinit var timer: CurrentTimer

    lateinit var handler: Handler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_main, container, false)

        init(v)
        setCurrentTimer()

        return v
    }

    private fun setCurrentTimer() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        timer = CurrentTimer()
        timer.currentTimerListener = this
        timer.start()
    }

    private fun init(v: View) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        initVars()
        initWidgets(v)
    }

    private fun initVars() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        handler = Handler()
    }

    private fun initWidgets(v: View) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            // note. assignment
            mainFragment__header_container = v.findViewById(R.id.mainFragment__header_container)
            mainFragment__header_timer_hours = v.findViewById(R.id.mainFragment__header_timer_hours)
            mainFragment__header_timer_minutes = v.findViewById(R.id.mainFragment__header_timer_minutes)
            mainFragment__body_container = v.findViewById(R.id.mainFragment__body_container)
            mainFragment__body_question = v.findViewById(R.id.mainFragment__body_question)
            mainFragment__body_answer = v.findViewById(R.id.mainFragment__body_answer)
            mainFragment__footer_phrase_container = v.findViewById(R.id.mainFragment__footer_phrase_container)
            mainFragment__footer_phrase = v.findViewById(R.id.mainFragment__footer_phrase)

            // note. listener

        } catch (e: Exception) {e.printStackTrace()}
    }

    // note. @override
    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("actionId:$actionId, event:$event")

        when (actionId) {
            EditorInfo.IME_ACTION_DONE -> {

            }
        }

        return false
    }

    override fun onPause() {
        super.onPause()
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        timer.isPause = true
    }

    override fun onResume() {
        super.onResume()
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        timer.isPause = false
    }

    // note. @companion object
    companion object {
        val TAG = MainFragment::class.simpleName
    }

    // note. @listener
    override fun print(year: Int, hours: Int, minutes: Int) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("year:$year, hours:$hours, minutes:$minutes")

        handler.post {
            mainFragment__header_timer_hours.text = hours.toString()
            mainFragment__header_timer_minutes.text = minutes.toString()

        }
    }
}