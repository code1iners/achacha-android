package com.example.achacha.adapters

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.achacha.R
import com.example.achacha.models.TodoModel
import com.orhanobut.logger.Logger

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.CustomViewHolder>() {

    lateinit var todos: ArrayList<TodoModel>
    lateinit var context: Context
    lateinit var handler: Handler
    lateinit var onTodoListener: OnTodoListener

    inner class CustomViewHolder(v: View): RecyclerView.ViewHolder(v)
        , View.OnClickListener
        , CompoundButton.OnCheckedChangeListener {
        var todosRecyclerView_check: CheckBox? = null
        var todosRecyclerView_todo_read_mode: TextView? = null
        var todosRecyclerView_todo_write_mode: EditText? = null
        var todosRecyclerView_delete: ImageButton? = null
        var todosRecyclerView_update: ImageButton? = null
        init {
            todosRecyclerView_check = v.findViewById(R.id.todosRecyclerView_check)
            todosRecyclerView_check?.setOnCheckedChangeListener(this)
            todosRecyclerView_todo_read_mode = v.findViewById(R.id.todosRecyclerView_todo_read_mode)
            todosRecyclerView_todo_read_mode?.setOnClickListener(this)
            todosRecyclerView_todo_write_mode = v.findViewById(R.id.todosRecyclerView_todo_write_mode)
            todosRecyclerView_delete = v.findViewById(R.id.todosRecyclerView_delete)
            todosRecyclerView_delete?.setOnClickListener(this)
            todosRecyclerView_update = v.findViewById(R.id.todosRecyclerView_update)
            todosRecyclerView_update?.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Logger.i(context.resources.getResourceEntryName(v.id))
            val p = adapterPosition
            val h = this
            when (v.id) {
                R.id.todosRecyclerView_todo_read_mode -> {
                    // note. set widgets
                    todosRecyclerView_todo_read_mode?.visibility = View.GONE
                    todosRecyclerView_delete?.visibility = View.GONE
                    todosRecyclerView_todo_write_mode?.visibility = View.VISIBLE
                    todosRecyclerView_update?.visibility = View.VISIBLE
                }

                R.id.todosRecyclerView_delete -> {
//                    val anim = WidgetManager.AnimationManager.getRemoveLeft(context)
//                    todosRecyclerView_delete?.startAnimation(anim)
//                    handler.postDelayed({
//                        onTodoListener.todoDelete(p)
//                    }, anim.duration)
                    onTodoListener.todoDelete(p)
                }

                R.id.todosRecyclerView_update -> {
                    // note. set widgets
                    todosRecyclerView_todo_read_mode?.visibility = View.VISIBLE
                    todosRecyclerView_delete?.visibility = View.VISIBLE
                    todosRecyclerView_todo_write_mode?.visibility = View.GONE
                    todosRecyclerView_update?.visibility = View.GONE

                    todosRecyclerView_todo_write_mode?.setText(todos[p].value)
                    
                    onTodoListener.todoUpdate(h, p, todos[p])
                }
            }
        }

        override fun onCheckedChanged(v: CompoundButton, b: Boolean) {
            Logger.i(context.resources.getResourceEntryName(v.id))
            when (v.id) {
                R.id.todosRecyclerView_check -> {
                    if (b) {
                        Logger.d("status:true")
                    } else {
                        Logger.d("status:false")
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodoAdapter.CustomViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_todos, parent, false)
        return CustomViewHolder(v)
    }

    override fun onBindViewHolder(h: TodoAdapter.CustomViewHolder, p: Int) {
        val m = todos[p]
        applyData(h, m)
    }

    private fun applyData(h: TodoAdapter.CustomViewHolder, m: TodoModel) {
        h.todosRecyclerView_todo_read_mode?.text = m.value
    }

    override fun getItemCount(): Int { return todos.size }

    interface OnTodoListener {
        fun todoDelete(p: Int)
        fun todoUpdate(h: CustomViewHolder, p: Int, m: TodoModel)
    }

}