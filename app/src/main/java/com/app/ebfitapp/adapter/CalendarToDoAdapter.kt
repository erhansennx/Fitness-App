package com.app.ebfitapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.ItemCalendarToDoBinding
import com.app.ebfitapp.databinding.ItemsArticleBinding
import com.app.ebfitapp.model.ToDoModel

class CalendarToDoAdapter(
    var todoArray : ArrayList<ToDoModel>,
) : RecyclerView.Adapter<CalendarToDoAdapter.ToDoHolder>() {
    class ToDoHolder(val bindingToDo: ItemCalendarToDoBinding) :
        RecyclerView.ViewHolder(bindingToDo.root) {
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
    }
}

