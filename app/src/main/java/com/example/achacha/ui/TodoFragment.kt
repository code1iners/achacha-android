package com.example.achacha.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.achacha.R
import com.example.achacha.adapters.TodoAdapter
import com.example.achacha.helpers.Protocol
import com.example.achacha.helpers.Protocol.BLANK
import com.example.achacha.helpers.Protocol.PENDING
import com.example.achacha.helpers.Protocol.UNIQUE_KEY_LENGTH
import com.example.achacha.helpers.WorkManager
import com.example.achacha.helpers.WorkManager.Companion.deleteTodo
import com.example.achacha.models.TodoModel
import com.example.helpers.PasswordGenerator
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.threeten.bp.LocalDateTime
import timber.log.Timber

class TodoFragment : Fragment()
    , TextView.OnEditorActionListener
    , View.OnClickListener
    , AdapterView.OnItemSelectedListener
    , TodoAdapter.OnTodoListener {

    // note. widgets
    private lateinit var toDoFragment__header_kind: Spinner
    private lateinit var toDoFragment__header_option: ImageButton
    private lateinit var toDoFragment__body_editor_writer: EditText
    private lateinit var toDoFragment__body_editor_submit: Button
    private lateinit var toDoFragment__body_list_container: LinearLayout
    private lateinit var toDoFragment__body_list: RecyclerView
    private lateinit var toDoFragment__body_blank_container: RelativeLayout

    // note. adapters
    private lateinit var todoAdapter: TodoAdapter

    // note. lists
    private lateinit var todos: ArrayList<TodoModel>
    private lateinit var todoKinds: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_to_do, container, false)

        init(v)
        getData()
        display()

        return v
    }

    private fun getData() {
        Timber.i(object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            val arr= WorkManager.readTodo(activity!!)
            for (idx in 0 until arr.length()) {
                val obj = arr.getJSONObject(idx)
                Timber.d("obj:$obj")
                val model = Gson().fromJson(obj.toString(), TodoModel::class.java)
                todos.add(model)
                todoAdapter.notifyItemInserted(idx)
            }

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun display() {
        Timber.i(object:Any(){}.javaClass.enclosingMethod!!.name)

        if (todos.size == 0) {
            toDoFragment__body_list_container.visibility = View.GONE
            toDoFragment__body_blank_container.visibility = View.VISIBLE
        } else {
            toDoFragment__body_list_container.visibility = View.VISIBLE
            toDoFragment__body_blank_container.visibility = View.GONE
        }
    }

    private fun init(v: View) {
        Timber.i(object:Any(){}.javaClass.enclosingMethod!!.name)

        initVars()
        initWidgets(v)
        initModels()
        initAdapters()
    }

    private fun initVars() {
        Timber.i(object:Any(){}.javaClass.enclosingMethod!!.name)

        todoKinds = arrayOf("Today", "+ New List")
    }

    private fun initWidgets(v: View) {
        Timber.i(object:Any(){}.javaClass.enclosingMethod!!.name)

        toDoFragment__header_kind = v.findViewById(R.id.toDoFragment__header_kind)
        val arrayAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, todoKinds)
        toDoFragment__header_kind.adapter = arrayAdapter
        toDoFragment__header_kind.onItemSelectedListener = this

        toDoFragment__header_option = v.findViewById(R.id.toDoFragment__header_option)
        toDoFragment__body_editor_writer = v.findViewById(R.id.toDoFragment__body_editor_writer)
        toDoFragment__body_editor_submit = v.findViewById(R.id.toDoFragment__body_editor_submit)
        toDoFragment__body_list_container = v.findViewById(R.id.toDoFragment__body_list_container)
        toDoFragment__body_list = v.findViewById(R.id.toDoFragment__body_list)
        toDoFragment__body_list.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)

        toDoFragment__body_blank_container = v.findViewById(R.id.toDoFragment__body_blank_container)

        // note. listeners
        toDoFragment__body_editor_writer.setOnEditorActionListener(this)
        toDoFragment__body_editor_submit.setOnClickListener(this)
    }

    private fun initModels() {
        Timber.i(object:Any(){}.javaClass.enclosingMethod!!.name)

        todos = ArrayList()
    }

    private fun initAdapters() {
        Timber.i(object:Any(){}.javaClass.enclosingMethod!!.name)

        try {
            todoAdapter = TodoAdapter()
            todoAdapter.context = context!!
            todoAdapter.onTodoListener = this
            todoAdapter.todos = todos

            toDoFragment__body_list.adapter = todoAdapter

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun createTodo() {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        val mapData: HashMap<String, String> = HashMap()
        mapData["category"] = toDoFragment__header_kind.selectedItem.toString()
        mapData["categoryPosition"] = toDoFragment__header_kind.selectedItemPosition.toString()
        mapData["value"] = toDoFragment__body_editor_writer.text.toString()
        Timber.i("mapData:${mapData.entries}")
        val model = WorkManager.createTodo(activity!!, mapData)
        model?.run {
            todos.add(this)
            todoAdapter.notifyItemInserted(todos.size)
        }

        // note. clear focus
        toDoFragment__body_editor_writer.setText(BLANK)
    }

    override fun onClick(v: View) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i(resources.getResourceEntryName(v.id))
        when (v.id) {
            R.id.toDoFragment__body_editor_submit -> {
                createTodo()
            }
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i("actionId:$actionId")
        try {
            when (v!!.id) {
                R.id.toDoFragment__body_editor_writer -> {
                    when (actionId) {
                        EditorInfo.IME_ACTION_DONE -> {
                            createTodo()
                        }
                    }
                }
            }

        } catch(e: Exception) {e.printStackTrace()}
        return false
    }

    override fun onItemSelected(parent: AdapterView<*>?, v: View, p: Int, id: Long) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
        Timber.i(resources.getResourceEntryName(v.id))
        when (v.id) {
            R.id.toDoFragment__header_kind -> {

            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)
    }

    override fun todoDelete(p: Int) {
        Timber.w(object:Any(){}.javaClass.enclosingMethod!!.name)

        // note. delete real data
        deleteTodo(activity!!, p)
        // note. delete ui
        todos.removeAt(p)
        todoAdapter.notifyItemRemoved(p)
    }

    // note. @companion object
    companion object {
        val TAG = TodoFragment::class.simpleName
    }
}