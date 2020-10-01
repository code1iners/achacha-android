package com.example.achacha.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.achacha.MainActivity
import com.example.achacha.R
import com.example.achacha.adapters.TodoAdapter
import com.example.achacha.helpers.CategoryManager.Companion.createCategoryAsJsonObject
import com.example.achacha.helpers.CategoryManager.Companion.readCategoryAllAsJsonArray
import com.example.achacha.helpers.Protocol.BLANK
import com.example.achacha.helpers.Protocol.REQUEST_CODE_EDITOR_ACTIVITY
import com.example.achacha.helpers.Protocol.STATUS
import com.example.achacha.helpers.Protocol.STATUS_NOT_FOUND
import com.example.achacha.helpers.Protocol.STATUS_OK
import com.example.achacha.helpers.Protocol.TITLE
import com.example.achacha.helpers.Protocol.VALUE
import com.example.achacha.helpers.WorkManager
import com.example.achacha.helpers.WorkManager.Companion.deleteTodo
import com.example.achacha.models.CategoryModel
import com.example.achacha.models.TodoModel
import com.example.helpers.Keypad
import com.google.gson.Gson
import org.threeten.bp.LocalDateTime
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class TodoFragment : Fragment()
    , TextView.OnEditorActionListener
    , View.OnClickListener
    , AdapterView.OnItemSelectedListener
    , TodoAdapter.OnTodoListener {

    // note. widgets
    private lateinit var toDoFragment__header_spinner: Spinner
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
    private lateinit var categories: ArrayList<CategoryModel>
    private lateinit var spinnerList: ArrayList<String>

    // note. kind
    private var selectedKind: String? = null
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_todo, container, false)

        init(v)
        // note. refresh to do list
        refreshTodos()
        // note. refresh categories & spinner list
        refreshCategories()

        return v
    }

    private fun displayTodos() {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
        Log.i(TAG, "todosSize:${todos.size}")
        if (todos.size == 0) {
            toDoFragment__body_list_container.visibility = View.GONE
            toDoFragment__body_blank_container.visibility = View.VISIBLE
        } else {
            toDoFragment__body_list_container.visibility = View.VISIBLE
            toDoFragment__body_blank_container.visibility = View.GONE
        }
    }

    private fun refreshTodos() {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            todos.clear()
            val array = WorkManager.readTodos(activity!!)
            for (idx in 0 until array.length()) {
                val todo = Gson().fromJson(array.getJSONObject(idx).toString(), TodoModel::class.java)
                Log.e(TAG, "category:${todo.category}, selectedItem:${toDoFragment__header_spinner.selectedItem}")
                if (todo.category == toDoFragment__header_spinner.selectedItem) {
                    Log.i(TAG, "entered in")
                    todos.add(todo)
                }
            }

            todoAdapter.notifyDataSetChanged()

            // note. display to do data
            displayTodos()

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun init(v: View) {
        initVars()
        initWidgets(v)
        initNavigations()
        initAdapters()
    }

    private fun initVars() {
        todos = ArrayList()
        categories = ArrayList()
        spinnerList = ArrayList()
    }

    private fun initWidgets(v: View) {

        // note. # header
        // note. spinner
        toDoFragment__header_spinner = v.findViewById(R.id.toDoFragment__header_spinner)
        toDoFragment__header_spinner.onItemSelectedListener = this
        // note. options
        toDoFragment__header_option = v.findViewById(R.id.toDoFragment__header_option)
        toDoFragment__header_option.setOnClickListener(this)

        // note. # body
        // note. editor
        toDoFragment__body_editor_writer = v.findViewById(R.id.toDoFragment__body_editor_writer)
        toDoFragment__body_editor_submit = v.findViewById(R.id.toDoFragment__body_editor_submit)
        toDoFragment__body_list_container = v.findViewById(R.id.toDoFragment__body_list_container)
        toDoFragment__body_list = v.findViewById(R.id.toDoFragment__body_list)
        toDoFragment__body_list.layoutManager = LinearLayoutManager(
            context!!,
            RecyclerView.VERTICAL,
            false
        )

        toDoFragment__body_blank_container = v.findViewById(R.id.toDoFragment__body_blank_container)
        toDoFragment__body_blank_container.setOnClickListener(this)

        // note. listeners
        toDoFragment__body_editor_writer.setOnEditorActionListener(this)
        toDoFragment__body_editor_submit.setOnClickListener(this)
    }

    private fun initNavigations() {

    }

    private fun initAdapters() {
        initTodoAdapter()
        initSpinnerAdapter()
    }

    private fun initTodoAdapter() {
        try {
            todoAdapter = TodoAdapter()
            todoAdapter.context = context!!
            todoAdapter.handler = Handler()
            todoAdapter.onTodoListener = this
            todoAdapter.todos = todos

            toDoFragment__body_list.adapter = todoAdapter

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun initSpinnerAdapter() {
        try {
            // note. set adapter
            spinnerAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, spinnerList)
            toDoFragment__header_spinner.adapter = spinnerAdapter

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun refreshCategories() {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            categories.clear()
            spinnerList.clear()

            val categoriesAsJsonArray = readCategoryAllAsJsonArray(activity!!)
            Log.i(TAG, "size:${categoriesAsJsonArray?.length()}, array:${categoriesAsJsonArray.toString()}")

            // note. size checker
            if (categoriesAsJsonArray?.length() == 0) {
                Log.w(TAG, "categoriesAsJsonArraySize is 0")
                // note. create Today
                categories.add(Gson().fromJson(createCategoryAsJsonObject(activity!!, 0, "Today").toString(), CategoryModel::class.java))
                categories.add(Gson().fromJson(createCategoryAsJsonObject(activity!!, 9999, resources.getString(R.string.kind_new_list)).toString(), CategoryModel::class.java))
                
            } else {
                if (categoriesAsJsonArray != null) {
                    for (idx in 0 until categoriesAsJsonArray.length()) {
                        categories.add(Gson().fromJson(categoriesAsJsonArray.getJSONObject(idx).toString(), CategoryModel::class.java))
                    }
                }
            }

            categories.sortBy { it.pk }

            for ((idx, item) in categories.withIndex()) {
                item.category?.let {
                    item.log()
                    spinnerList.add(it)

                }
            }
            spinnerAdapter.notifyDataSetChanged()

        } catch (e: Exception) {e.printStackTrace()}

    }

    private fun createTodo() {
        val mapData: HashMap<String, String> = HashMap()
        mapData["category"] = toDoFragment__header_spinner.selectedItem.toString()
        mapData["categoryPosition"] = toDoFragment__header_spinner.selectedItemPosition.toString()
        mapData["value"] = toDoFragment__body_editor_writer.text.toString()
        Log.i(TAG,"mapData:${mapData.entries}")
        val model = WorkManager.createTodo(activity!!, mapData)
        model?.run {
            todos.add(this)
            todoAdapter.notifyItemInserted(todos.size)
        }

        // note. clear focus
        toDoFragment__body_editor_writer.setText(BLANK)

        displayTodos()
    }

    fun resetCategories() {
        Log.i(TAG,"categoriesSize:${categories.size}")
        categories.clear()
        spinnerList.clear()
        refreshCategories()
        spinnerAdapter.notifyDataSetChanged()
        Log.i(TAG,"categoriesSize:${categories.size}")

    }

    fun resetTodos() {
        todos.clear()
        todoAdapter.notifyDataSetChanged()
    }

    override fun onClick(v: View) {
        Log.i(TAG, resources.getResourceEntryName(v.id))
        when (v.id) {
            R.id.toDoFragment__body_editor_submit -> {
                createTodo()
            }

            R.id.toDoFragment__header_option -> {
                openMenu()
            }

            R.id.toDoFragment__body_blank_container -> {
                toDoFragment__body_editor_writer.requestFocus()
                try {
                    Keypad(activity!!).up(toDoFragment__body_editor_writer)
                } catch (e: Exception) {e.printStackTrace()}
            }
        }
    }

    private fun openMenu() {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            MainActivity.openMenu()
        } catch (e: Exception) {e.printStackTrace()}
//        val menuActivity = Intent
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
        Log.i(TAG, "actionId:$actionId")
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

        } catch (e: Exception) {e.printStackTrace()}
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
        Log.w(TAG, "requestCode:$requestCode, resultCode:$resultCode, data:$data")
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_EDITOR_ACTIVITY -> {
                    val status = data?.getIntExtra(STATUS, -1)
                    Log.i(TAG, "status:$status")
                    if (status == -1) return

                    val value = data?.getStringExtra(VALUE)

                    when (status) {
                        STATUS_OK -> {
                            try {
                                value?.let {
                                    Log.e(TAG, "categoriesLastIndex:${categories.lastIndex}, size:${categories.size}")
                                    // note. create category
                                    val obj = createCategoryAsJsonObject(activity!!, categories.size, it)

                                    // note. refresh categories
                                    refreshCategories()

                                    selectSpinnerItem(spinnerList.lastIndex - 1)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        STATUS_NOT_FOUND -> {
                            selectSpinnerItem(0)
                        }
                    }
                }
            }
        }
    }

    private fun selectSpinnerItem(position: Int) {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            // note. select item
            toDoFragment__header_spinner.setSelection(position)

            // note. call selected item method
            onItemSelected(toDoFragment__header_spinner, null, position, toDoFragment__header_spinner.adapter.getItemId(position))
        } catch (e: Exception) {e.printStackTrace()}
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.w(TAG, object:Any(){}.javaClass.enclosingMethod!!.name)
        try {
            Log.i(TAG, "position:$position, id:$id, parent:${parent?.id}")

            parent?.let {
                selectedKind = it.selectedItem.toString()
                Log.i(TAG, "selectedItem:${it.selectedItem}")
                when (it.id) {
                    R.id.toDoFragment__header_spinner -> {
                        when (selectedKind) {
                            resources.getString(R.string.kind_new_list) -> {
                                val editorActivity = Intent(activity, EditorActivity::class.java)
                                editorActivity.putExtra(TITLE, "New kind list")
                                startActivityForResult(editorActivity, REQUEST_CODE_EDITOR_ACTIVITY)
                            }

                            else -> {
                                refreshTodos()
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun todoDelete(p: Int) {
        try {
            // note. delete real data
            deleteTodo(activity!!, p)
            // note. delete ui
            todos.removeAt(p)
            todoAdapter.notifyItemRemoved(p)

            displayTodos()

        } catch (e: Exception) {e.printStackTrace()}
    }

    override fun todoUpdate(h: TodoAdapter.CustomViewHolder, p: Int, m: TodoModel) {
        try {
            WorkManager.updateTodo(activity!!, p, m.apply {
                value = h.todosRecyclerView_todo_write_mode?.text.toString()
                updated = LocalDateTime.now().toString()
            })
        } catch (e: Exception) {e.printStackTrace()}
    }

    // note. @companion object
    companion object {
        val TAG = TodoFragment::class.simpleName
    }
}