package com.example.achacha.ui

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.achacha.MainActivity
import com.example.achacha.R
import com.example.achacha.helpers.CategoryManager
import com.example.achacha.helpers.CustomTimerTest
import com.example.achacha.helpers.Protocol
import com.example.achacha.helpers.Protocol.BLANK
import com.example.achacha.helpers.Protocol.MAIN_FOCUS
import com.example.achacha.helpers.Protocol.WORK
import com.example.achacha.helpers.WorkManager
import com.example.helpers.CustomTimer
import com.example.helpers.PreferencesManager
import java.util.*
import kotlin.collections.HashMap

class MainFocusFragment : Fragment()
    , TextView.OnEditorActionListener
    , View.OnClickListener
//    , CustomTimer.CurrentTimer.CurrentTimerListener
    , CompoundButton.OnCheckedChangeListener {

    // note. widgets-header
    lateinit var mainFocusFragment__layout: RelativeLayout
    lateinit var mainFragment__header_container: LinearLayout
    lateinit var mainFragment__header_timer_hours: TextView
    lateinit var mainFragment__header_timer_minutes: TextView
    lateinit var mainFragment__header_timer_seconds: TextView
    lateinit var mainFragment__header_greeting: TextView
    lateinit var mainFragment__header_username: TextView
    // note. widgets-body
    lateinit var mainFragment__body_container: LinearLayout
    // note. widgets-body-QnA
    lateinit var mainFragment__body_QnA_container: LinearLayout
    lateinit var mainFragment__body_question: TextView
    lateinit var mainFragment__body_answer: EditText
    // note. widgets-body-focus-contents
    lateinit var mainFragment__body_focus_contents_container: LinearLayout
    lateinit var mainFragment__body_focus_contents_data_container: LinearLayout
    lateinit var mainFragment__body_focus_contents_checkbox: CheckBox
    lateinit var mainFragment__body_focus_contents_data: TextView
    lateinit var mainFragment__body_focus_contents_delete: ImageButton
    // note. widgets-footer-phrase
    lateinit var mainFragment__footer_phrase_container: LinearLayout
    lateinit var mainFragment__footer_phrase: TextView

    // note. vars-timer
//    lateinit var timer: CustomTimerTest.CurrentTimer
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
        val v = inflater.inflate(R.layout.fragment_main_focus, container, false)

        init(v)
        setBackground()
        setCurrentTimer()
        setGreetings()
        refreshBodyUI()

        return v
    }

    fun setBackground() {
        try {
            val status = PreferencesManager(activity!!, Protocol.DISPLAY_MODE)[Protocol.DARK_MODE]
            Log.i(TAG, "status:$status")
            if (status.isNullOrBlank()) return

            if (status.toBoolean()) {
                mainFocusFragment__layout.background = null
            } else {
                mainFocusFragment__layout.setBackgroundDrawable(MainActivity.getBackGroundImageByRandom())
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun init(v: View) {
        

        initVars()
        initWidgets(v)
        initListeners()
    }

    private fun initVars() {
        
        // note. source activity
        try {sourceActivity = context!! as MainActivity}
        catch (e: Exception) {e.printStackTrace()}

        handler = Handler()
        time = HashMap()
    }

    private fun initWidgets(v: View) {
        
        try {
            // note. assignment
            mainFocusFragment__layout = v.findViewById(R.id.mainFocusFragment__layout)
            mainFragment__header_container = v.findViewById(R.id.mainFragment__header_container)
            mainFragment__header_timer_hours = v.findViewById(R.id.mainFragment__header_timer_hours)
            mainFragment__header_timer_minutes = v.findViewById(R.id.mainFragment__header_timer_minutes)
            mainFragment__header_timer_seconds = v.findViewById(R.id.mainFragment__header_timer_seconds)
            mainFragment__header_greeting = v.findViewById(R.id.mainFragment__header_greeting)
            mainFragment__header_username = v.findViewById(R.id.mainFragment__header_username)
            // note. widgets-body-QnA
            mainFragment__body_container = v.findViewById(R.id.mainFragment__body_container)
            mainFragment__body_QnA_container = v.findViewById(R.id.mainFragment__body_QnA_container)
            mainFragment__body_question = v.findViewById(R.id.mainFragment__body_question)
            mainFragment__body_answer = v.findViewById(R.id.mainFragment__body_answer)
            // note. widgets-body-focus-contents
            mainFragment__body_focus_contents_container = v.findViewById(R.id.mainFragment__body_focus_contents_container)
            mainFragment__body_focus_contents_data_container = v.findViewById(R.id.mainFragment__body_focus_contents_data_container)
            mainFragment__body_focus_contents_checkbox = v.findViewById(R.id.mainFragment__body_focus_contents_checkbox)
            mainFragment__body_focus_contents_data = v.findViewById(R.id.mainFragment__body_focus_contents_data)
            mainFragment__body_focus_contents_delete = v.findViewById(R.id.mainFragment__body_focus_contents_delete)
            // note. widgets-footer-phrase
            mainFragment__footer_phrase_container = v.findViewById(R.id.mainFragment__footer_phrase_container)
            mainFragment__footer_phrase = v.findViewById(R.id.mainFragment__footer_phrase)

            // note. listener
            mainFragment__body_answer.setOnEditorActionListener(this)
            mainFragment__body_focus_contents_checkbox.setOnCheckedChangeListener(this)
            mainFragment__body_focus_contents_delete.setOnClickListener(this)
            mainFragment__body_focus_contents_data.setOnClickListener(this)
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun initListeners() {
        

    }

    private fun setCurrentTimer() {
        

//        timer = CustomTimer.CurrentTimer()
//        timer = CustomTimerTest.CurrentTimer()
//        timer.currentTimerListener = this
//        timer.start()
    }

    private fun setGreetings() {
        

        // note. greeting
        greetingAsArray()
//        greetingAsTime()
        // note. username
        setUsername()
    }

    private fun greetingAsArray() {
        
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

        mainFragment__header_greeting.text = "${mainFragment__header_greeting.text} ${sourceActivity.username}"
//        mainFragment__header_username.text = sourceActivity.username
    }

    private fun refreshBodyUI() {
        
        try {
            // note. check mainFocus non-null
            val mainFocus = PreferencesManager(activity!!, WORK)[MAIN_FOCUS]
            Log.i(TAG, "mainFocus:$mainFocus")

            if (mainFocus.isNullOrBlank()) {
                mainFragment__body_QnA_container.visibility = View.VISIBLE
                mainFragment__body_focus_contents_container.visibility = View.GONE

                // note. set text
                mainFragment__body_focus_contents_data.text = BLANK
            }
            else {
                mainFragment__body_QnA_container.visibility = View.GONE
                mainFragment__body_focus_contents_container.visibility = View.VISIBLE

                // note. set text
                mainFragment__body_focus_contents_data.text = mainFocus
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun deleteMainFocus() {
        
        try {
            resetMainFocus()
//            // note. delete main focus
//            PreferencesManager(activity!!, WORK).remove(MAIN_FOCUS)
//            // note. change UI
//            refreshBodyUI()
            // note. clear widgets
            mainFragment__body_focus_contents_data.text = BLANK
            mainFragment__body_focus_contents_checkbox.isChecked = false
        } catch (e: Exception) {e.printStackTrace()}
    }

    fun resetMainFocus() {
        try {
            WorkManager.clearMainFocus(activity!!)
            refreshBodyUI()
        } catch (e: Exception) {e.printStackTrace()}
    }

    // note. @life-cycle
    override fun onPause() {
        super.onPause()

//        timer.isPause = true
    }

    override fun onResume() {
        super.onResume()

//        timer.isPause = false
    }

    override fun onDestroyView() {
        super.onDestroyView()

//        timer.isPause = true
    }

    // note. @listener
    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        
        Log.i(TAG, "actionId:$actionId, event:$event")

        try {
            when (v!!.id) {
                R.id.mainFragment__body_answer -> {
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE -> {
                            // note. save main focus data in device
                            WorkManager.createMainFocus(activity!!, v.text.toString())
                            // note. clear text
                            mainFragment__body_answer.setText(BLANK)
                            // note. change UI
                            refreshBodyUI()
                        }
                    }
                }
            }

        } catch (e: Exception) {e.printStackTrace()}

        return false
    }

    override fun onCheckedChanged(v: CompoundButton, isChecked: Boolean) {
        Log.i(TAG, "${resources.getResourceEntryName(v.id)}")
        when (v.id) {
            R.id.mainFragment__body_focus_contents_checkbox -> {
                if (isChecked) {

                } else {

                }
            }
        }
    }

    override fun onClick(v: View) {
        
        Log.i(TAG, "${resources.getResourceEntryName(v.id)}")
        when (v.id) {
            R.id.mainFragment__body_focus_contents_data -> {
                mainFragment__body_focus_contents_checkbox.performClick()
            }

            R.id.mainFragment__body_focus_contents_delete -> {
                deleteMainFocus()
            }
        }
    }

//    override fun print(year: Int, hours: Int, minutes: Int) {
////
////        Log.i("year:$year, hours:$hours, minutes:$minutes")
//
//        // note. for current time
//        handler.post {
//            mainFragment__header_timer_hours.text = hours.toString()
//            mainFragment__header_timer_minutes.text = minutes.toString()
//        }
//
//        // note. assignment by hash map
//        time["year"] = year
//        time["hours"] = hours
//        time["minutes"] = minutes
//    }

    // note. @companion object
    companion object {
        val TAG = MainFocusFragment::class.simpleName
    }
}