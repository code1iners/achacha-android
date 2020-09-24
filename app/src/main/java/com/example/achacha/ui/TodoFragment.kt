package com.example.achacha.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.achacha.R
import timber.log.Timber

class ToDoFragment : Fragment(), TextView.OnEditorActionListener, AdapterView.OnItemSelectedListener {

    // note. vars


    // note. widgets
    lateinit var toDoFragment__header_kind: Spinner
    lateinit var toDoFragment__header_option: ImageButton
    lateinit var toDoFragment__body_editor: EditText
    lateinit var toDoFragment__footer_list: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_to_do, container, false)

        init(v)

        return v
    }

    private fun init(v: View) {
        Timber.i(object:Any(){}.javaClass.enclosingMethod!!.name)

        initWidgets(v)
    }

    private fun initWidgets(v: View) {
        Timber.i(object:Any(){}.javaClass.enclosingMethod!!.name)

        toDoFragment__header_kind = v.findViewById(R.id.toDoFragment__header_kind)
        val arrayAdapter = ArrayAdapter
            .createFromResource(context!!, R.array.to_do_kind, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                toDoFragment__header_kind.adapter = adapter
            }
        toDoFragment__header_option = v.findViewById(R.id.toDoFragment__header_option)
        toDoFragment__body_editor = v.findViewById(R.id.toDoFragment__body_editor)
        toDoFragment__footer_list = v.findViewById(R.id.toDoFragment__footer_list)
    }

    // note. @companion object
    companion object {
        val TAG = ToDoFragment::class.simpleName
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("actionId:$actionId")
        try {
            when (v!!.id) {
                R.id.toDoFragment__body_editor -> {
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE -> {

                        }
                    }
                }
            }

        } catch(e: Exception) {e.printStackTrace()}
        return false
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
    }
}