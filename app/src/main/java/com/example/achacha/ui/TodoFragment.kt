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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.achacha.R
import com.example.achacha.adapters.TodoAdapter
import com.example.achacha.helpers.CategoryManager.Companion.createCategory
import com.example.achacha.helpers.CategoryManager.Companion.readCategoryAllAsJsonArray
import com.example.achacha.helpers.CategoryManager.Companion.readCategoryAsJsonObject
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
import com.google.gson.Gson
import org.json.JSONArray
import org.threeten.bp.LocalDateTime
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


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
    private lateinit var categories: ArrayList<CategoryModel>
    private lateinit var listOfSpinner: ArrayList<String>

    // note. kind
    private var selectedKind: String? = null
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_to_do, container, false)

        init(v)
        getData()
        display()
        refresh()

        return v
    }

    private fun getData() {
        try {
            val arr= WorkManager.readTodo(activity!!)
            for (idx in 0 until arr.length()) {
                val obj = arr.getJSONObject(idx)
                Log.d(TAG, "obj:$obj")
                val model = Gson().fromJson(obj.toString(), TodoModel::class.java)
                todos.add(model)
                todoAdapter.notifyItemInserted(idx)
            }

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun display() {
        if (todos.size == 0) {
            toDoFragment__body_list_container.visibility = View.GONE
            toDoFragment__body_blank_container.visibility = View.VISIBLE
        } else {
            toDoFragment__body_list_container.visibility = View.VISIBLE
            toDoFragment__body_blank_container.visibility = View.GONE
        }
    }

    private fun refresh() {
        Log.i("TAG", "asdfasdf")
        val temp = todos
        Log.i(TAG, "todosSize:${todos.size}")
        todos.clear()
        todoAdapter.notifyDataSetChanged()
        Log.i(TAG, "todosSize:${todos.size}")
        var idx = 0
        for (todo in temp) {
            if (todo.category == toDoFragment__header_kind.selectedItem) {
                todo.log()
                todos.add(todo)
                todoAdapter.notifyItemInserted(idx)
                idx++
            }
        }
    }

    private fun init(v: View) {
        initVars()
        initWidgets(v)
        initAdapters()
    }

    private fun initVars() {
        todos = ArrayList()
        categories = ArrayList()
        listOfSpinner = ArrayList()
    }

    private fun initWidgets(v: View) {
        toDoFragment__header_kind = v.findViewById(R.id.toDoFragment__header_kind)
        toDoFragment__header_kind.onItemSelectedListener = this

        toDoFragment__header_option = v.findViewById(R.id.toDoFragment__header_option)
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

        // note. listeners
        toDoFragment__body_editor_writer.setOnEditorActionListener(this)
        toDoFragment__body_editor_submit.setOnClickListener(this)
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
            // note. init categories & spinner list
            initCategories()

            // note. set adapter
            spinnerAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, listOfSpinner)
            toDoFragment__header_kind.adapter = spinnerAdapter
            spinnerAdapter.notifyDataSetChanged()

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun initCategories() {
        var arr = readCategoryAllAsJsonArray(activity!!)
        Log.i(TAG, "arrayLength:$${arr.length()}")

        // note. check non null
        if (arr.length() == 0) arr.put(createCategory(activity!!, 0, "Today"))

        // note. check new list item
        val newList = readCategoryAsJsonObject(activity!!, resources.getString(R.string.kind_new_list))
        if (newList == null) arr.put(createCategory(activity!!, 1, resources.getString(R.string.kind_new_list)))
        Log.i(TAG, "arr:$arr")

        // note. set category list
        for (idx in 0 until arr.length()) {
            categories.add(CategoryModel().apply {
                pk = arr.getJSONObject(idx).getInt("pk")
                category = arr.getJSONObject(idx).getString("category")
                created = arr.getJSONObject(idx).getString("created")
                updated = arr.getJSONObject(idx).getString("updated")
                this.log()
            })
        }

        // note. set spinner list
        for (item in categories) {
            item.category?.let { listOfSpinner.add(it) }
        }
    }

    private fun createTodo() {
        val mapData: HashMap<String, String> = HashMap()
        mapData["category"] = toDoFragment__header_kind.selectedItem.toString()
        mapData["categoryPosition"] = toDoFragment__header_kind.selectedItemPosition.toString()
        mapData["value"] = toDoFragment__body_editor_writer.text.toString()
        Log.i(TAG,"mapData:${mapData.entries}")
        val model = WorkManager.createTodo(activity!!, mapData)
        model?.run {
            todos.add(this)
            todoAdapter.notifyItemInserted(todos.size)
        }

        // note. clear focus
        toDoFragment__body_editor_writer.setText(BLANK)

        display()
    }

    fun resetCategories() {
        Log.i(TAG,"categoriesSize:${categories.size}")
        categories.clear()
        listOfSpinner.clear()
        initCategories()
        spinnerAdapter.notifyDataSetChanged()
        Log.i(TAG,"categoriesSize:${categories.size}")

    }

    override fun onClick(v: View) {
        Log.i(TAG, resources.getResourceEntryName(v.id))
        when (v.id) {
            R.id.toDoFragment__body_editor_submit -> {
                createTodo()
            }
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
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
                                    val obj = createCategory(activity!!, categories.lastIndex, it)
                                    val m = Gson().fromJson(obj.toString(), CategoryModel::class.java)
                                    m.log()
                                    categories.add(m)
                                    m.category?.run {
                                        listOfSpinner.add(categories.lastIndex - 1, this)
                                        spinnerAdapter.notifyDataSetChanged()
                                    }

                                    toDoFragment__header_kind.setSelection(0)
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        STATUS_NOT_FOUND -> {
                            toDoFragment__header_kind.setSelection(0)
                        }
                    }
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        try {
            Log.i(TAG, "position:$position, id:$id, parent:${parent?.id}")

            parent?.let {
                selectedKind = it.selectedItem.toString()
                Log.i(TAG, "selectedItem:${it.selectedItem}")
                when (it.id) {
                    R.id.toDoFragment__header_kind -> {
                        when (selectedKind) {
                            resources.getString(R.string.kind_new_list) -> {
                                val editorActivity = Intent(activity, EditorActivity::class.java)
                                editorActivity.putExtra(TITLE, "New kind list")
                                startActivityForResult(editorActivity, REQUEST_CODE_EDITOR_ACTIVITY)
                            }
                        }
                    }

                    else -> {
                        refresh()
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

            display()

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