package com.app.ebfitapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.ebfitapp.databinding.ItemCalendarToDoBinding
import com.app.ebfitapp.model.ToDoModel
import java.util.UUID

class CalendarToDoAdapter(
    var todoArray : ArrayList<ToDoModel>,
) : RecyclerView.Adapter<CalendarToDoAdapter.ToDoHolder>() {
    class ToDoHolder(val bindingToDo: ItemCalendarToDoBinding) :
        RecyclerView.ViewHolder(bindingToDo.root) {
    }

    interface OnToDoItemClickListener {
        fun onToDoItemClick(id : String?)
    }
    private var mListener: OnToDoItemClickListener? = null
    fun setOnItemClickListener(listener: OnToDoItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoHolder {
        val itemBinding =
            ItemCalendarToDoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoHolder(itemBinding)
    }
    override fun getItemCount(): Int {
        return todoArray.size
    }
    override fun onBindViewHolder(holder: ToDoHolder, position: Int) {
        val itemBinding = holder.bindingToDo

        val toDoModel = todoArray[holder.adapterPosition]

        itemBinding.toDoText.text = toDoModel.todoText
        itemBinding.itemDate.text = toDoModel.selectedDate
        itemBinding.itemDay.text = toDoModel.selectedDay


        holder.itemView.setOnClickListener{
            val clickedId = toDoModel.todoId
            mListener?.onToDoItemClick(clickedId)
        }
    }
}

