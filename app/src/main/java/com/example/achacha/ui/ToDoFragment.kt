package com.example.achacha.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class ToDoFragment : Fragment() {

    // note. vars


    // note. widgets
    lateinit var toDoFragment__header_option: ImageButton
    lateinit var toDoFragment__body_editor: EditText
    lateinit var toDoFragment__footer_list: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    // note. @companion object
    companion object {
        val TAG = ToDoFragment::class.simpleName
    }
}