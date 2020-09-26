package com.example.achacha.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
import com.example.achacha.helpers.CategoryManager
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
import com.orhanobut.logger.Logger
import org.threeten.bp.LocalDateTime
import java.util.*
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

        return v
    }

    private fun getData() {
        Logger.i(object : Any() {}.javaClass.enclosingMethod!!.name)
        try {
            val arr= WorkManager.readTodo(activity!!)
            for (idx in 0 until arr.length()) {
                val obj = arr.getJSONObject(idx)
                Logger.d("obj:$obj")
                val model = Gson().fromJson(obj.toString(), TodoModel::class.java)
                todos.add(model)
                todoAdapter.notifyItemInserted(idx)
            }

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun display() {
        Logger.i(object : Any() {}.javaClass.enclosingMethod!!.name)

        if (todos.size == 0) {
            toDoFragment__body_list_container.visibility = View.GONE
            toDoFragment__body_blank_container.visibility = View.VISIBLE
        } else {
            toDoFragment__body_list_container.visibility = View.VISIBLE
            toDoFragment__body_blank_container.visibility = View.GONE
        }
    }

    private fun init(v: View) {
        Logger.i(object : Any() {}.javaClass.enclosingMethod!!.name)

        initVars()
        initWidgets(v)
        initModels()
        initAdapters()
    }

    private fun initVars() {
        Logger.i(object : Any() {}.javaClass.enclosingMethod!!.name)

    }

    private fun initWidgets(v: View) {
        Logger.i(object : Any() {}.javaClass.enclosingMethod!!.name)

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

    private fun initModels() {
        Logger.i(object : Any() {}.javaClass.enclosingMethod!!.name)

        todos = ArrayList()
    }

    private fun initAdapters() {
        Logger.i(object : Any() {}.javaClass.enclosingMethod!!.name)

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
            categories = ArrayList()

            var arr = readCategoryAllAsJsonArray(activity!!)

            Logger.i("arrayLength:$${arr.length()}")
            // note. check non null
            if (arr.length() == 0) arr.put(createCategory(activity!!, 0, "Today"))

            // note. check new list item
            val newList = readCategoryAsJsonObject(activity!!, resources.getString(R.string.kind_new_list))
            if (newList == null) arr.put(createCategory(activity!!, 1, resources.getString(R.string.kind_new_list)))
            Logger.i("arr:$arr")

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
            listOfSpinner = ArrayList()
            for (item in categories) {
                item.category?.let { listOfSpinner.add(it) }
            }

            // note. set adapter
            spinnerAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, listOfSpinner)
            toDoFragment__header_kind.adapter = spinnerAdapter
            spinnerAdapter.notifyDataSetChanged()

        } catch (e: Exception) {e.printStackTrace()}
    }

    private fun createTodo() {


        val mapData: HashMap<String, String> = HashMap()
        mapData["category"] = toDoFragment__header_kind.selectedItem.toString()
        mapData["categoryPosition"] = toDoFragment__header_kind.selectedItemPosition.toString()
        mapData["value"] = toDoFragment__body_editor_writer.text.toString()
        Logger.i("mapData:${mapData.entries}")
        val model = WorkManager.createTodo(activity!!, mapData)
        model?.run {
            todos.add(this)
            todoAdapter.notifyItemInserted(todos.size)
        }

        // note. clear focus
        toDoFragment__body_editor_writer.setText(BLANK)

        display()
    }

    override fun onClick(v: View) {
        Logger.i(resources.getResourceEntryName(v.id))
        when (v.id) {
            R.id.toDoFragment__body_editor_submit -> {
                createTodo()
            }
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        Logger.i("actionId:$actionId")
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

        Logger.w("requestCode:$requestCode, resultCode:$resultCode, data:$data")
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_EDITOR_ACTIVITY -> {
                    val status = data?.getIntExtra(STATUS, -1)
                    Logger.i("status:$status")
                    if (status == -1) return

                    val value = data?.getStringExtra(VALUE)

                    when (status) {
                        STATUS_OK -> {
                            try {
                                value?.let {
                                    Logger.e("categoriesLastIndex:${categories.lastIndex}, size:${categories.size}")
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

    private fun addTodoKinds(array: Array<String?>, value: String?): Array<String?> {

        Logger.i("array:$array, value:$value")

        var lastIndex = -1
        val resultArray = arrayOfNulls<String>(array.size + 1)
        Logger.i("resultArraySize:${resultArray.size}")

        try {
            for ((idx, value) in array.withIndex()) {
                if (idx == array.size - 1) {
                    lastIndex++
                    break
                }
                Logger.v("idx:$idx, value:$value")
                resultArray[idx] = value
                lastIndex++
            }

            resultArray[lastIndex] = resources.getString(R.string.kind_new_list)
        } catch (e: Exception) {e.printStackTrace()}

        Logger.d("resultArray:$resultArray")

        return resultArray
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        try {
            Logger.i("position:$position, id:$id, parent:${parent?.id}")

            parent?.let {
                selectedKind = it.selectedItem.toString()
                Logger.i("selectedItem:${it.selectedItem}")
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
                }
            }
        } catch (e: Exception) {e.printStackTrace()}
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun todoDelete(p: Int) {


        // note. delete real data
        deleteTodo(activity!!, p)
        // note. delete ui
        todos.removeAt(p)
        todoAdapter.notifyItemRemoved(p)

        display()
    }

    override fun todoUpdate(h: TodoAdapter.CustomViewHolder, p: Int, m: TodoModel) {
        WorkManager.updateTodo(activity!!, p, m.apply {
            value = h.todosRecyclerView_todo_write_mode?.text.toString()
            updated = LocalDateTime.now().toString()
        })
    }

    // note. @companion object
    companion object {
        val TAG = TodoFragment::class.simpleName
    }
}