package com.app.ebfitapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.ItemCalendarToDoBinding
import com.app.ebfitapp.databinding.ItemsArticleBinding

class CalendarToDoAdapter(
    var todoList: ArrayList<String>,
    var dayDateList:MutableList<Pair<String?, String?>>,
) : RecyclerView.Adapter<CalendarToDoAdapter.ToDoHolder>() {

    class ToDoHolder(val bindingToDo: ItemCalendarToDoBinding) : RecyclerView.ViewHolder(bindingToDo.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoHolder {
        val itemBinding = ItemCalendarToDoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: ToDoHolder, position: Int) {
        val itemBinding = holder.bindingToDo

        // ViewHolder içindeki TextView'lere değerleri yerleştir
        itemBinding.toDoText.text = todoList[position]

        // Eğer dayDateList ve position uygunsa Pair'den değerleri çek ve yerleştir
        if (position < dayDateList.size) {
            val (day, date) = dayDateList[position]
            itemBinding.itemDay.text = day
            itemBinding.itemDate.text = date

        }
    }
}
