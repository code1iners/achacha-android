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
import com.example.achacha.MainActivity
import com.example.achacha.R
import com.example.helpers.BooleanVariable
import com.example.helpers.CustomTimer
import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap

class MainFragment : Fragment(), TextView.OnEditorActionListener, CustomTimer.CurrentTimer.CurrentTimerListener {

    // note. widgets-header
    lateinit var mainFragment__header_container: LinearLayout
    lateinit var mainFragment__header_timer_hours: TextView
    lateinit var mainFragment__header_timer_minutes: TextView
    lateinit var mainFragment__header_greeting: TextView
    lateinit var mainFragment__header_username: TextView
    // note. widgets-body
    lateinit var mainFragment__body_container: LinearLayout
    lateinit var mainFragment__body_question: TextView
    lateinit var mainFragment__body_answer: EditText
    // note. widgets-footer
    lateinit var mainFragment__footer_phrase_container: LinearLayout
    lateinit var mainFragment__footer_phrase: TextView

    // note. vars-timer
    lateinit var timer: CustomTimer.CurrentTimer
    // note. vars-greetings
    lateinit var time: HashMap<String, Int>
    // note. vars
    lateinit var sourceActivity: MainActivity
    lateinit var handler: Handler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_main, container, false)

        init(v)
        setCurrentTimer()
        setGreetings()
        return v
    }

    private fun init(v: View) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        initVars()
        initWidgets(v)
        initListeners()
    }

    private fun initVars() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        // note. source activity
        try {sourceActivity = context!! as MainActivity}
        catch (e: Exception) {e.printStackTrace()}

        handler = Handler()
        time = HashMap()
    }

    private fun initWidgets(v: View) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            // note. assignment
            mainFragment__header_container = v.findViewById(R.id.mainFragment__header_container)
            mainFragment__header_timer_hours = v.findViewById(R.id.mainFragment__header_timer_hours)
            mainFragment__header_timer_minutes = v.findViewById(R.id.mainFragment__header_timer_minutes)
            mainFragment__header_greeting = v.findViewById(R.id.mainFragment__header_greeting)
            mainFragment__header_username = v.findViewById(R.id.mainFragment__header_username)

            mainFragment__body_container = v.findViewById(R.id.mainFragment__body_container)
            mainFragment__body_question = v.findViewById(R.id.mainFragment__body_question)
            mainFragment__body_answer = v.findViewById(R.id.mainFragment__body_answer)

            mainFragment__footer_phrase_container = v.findViewById(R.id.mainFragment__footer_phrase_container)
            mainFragment__footer_phrase = v.findViewById(R.id.mainFragment__footer_phrase)

            // note. listener
            mainFragment__body_answer.setOnEditorActionListener(this)
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun initListeners() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

    }

    private fun setCurrentTimer() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        timer = CustomTimer.CurrentTimer()
        timer.currentTimerListener = this
        timer.start()
    }

    private fun setGreetings() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. greeting
        greetingAsArray()
//        greetingAsTime()
        // note. username
        setUsername()
    }

    private fun greetingAsArray() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            val greetingArrays = arrayOf(
                resources.getString(R.string.greeting_at_001),
                resources.getString(R.string.greeting_at_002),
                resources.getString(R.string.greeting_at_003),
                resources.getString(R.string.greeting_at_004),
                resources.getString(R.string.greeting_at_005),
                resources.getString(R.string.greeting_at_006),
                resources.getString(R.string.greeting_at_007),
                resources.getString(R.string.greeting_at_008),
                resources.getString(R.string.greeting_at_009),
                resources.getString(R.string.greeting_at_010)
            )

            val selected = greetingArrays[Random().nextInt(greetingArrays.size)]
            mainFragment__header_greeting.text = selected
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun greetingAsTime() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            val hours = time["hours"]!!
            when (hours) {
                in 0..5 -> {

                }
                in 5..9 -> {

                }
                in 9..17 -> {

                }
                in 17..21 -> {

                }
                in 21..24 -> {

                }
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun setUsername() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        mainFragment__header_username.text = sourceActivity.username
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

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        timer.isPause = true
    }

    // note. @companion object
    companion object {
        val TAG = MainFragment::class.simpleName
    }

    // note. @listener
    override fun print(year: Int, hours: Int, minutes: Int) {
//        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
//        Timber.i("year:$year, hours:$hours, minutes:$minutes")

        // note. for current time
        handler.post {
            mainFragment__header_timer_hours.text = hours.toString()
            mainFragment__header_timer_minutes.text = minutes.toString()
        }

        // note. assignment by hash map
        time["year"] = year
        time["hours"] = hours
        time["minutes"] = minutes
    }
}