package com.app.ebfitapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.app.ebfitapp.databinding.ItemCalendarToDoBinding
import com.app.ebfitapp.model.ArticleModel
import com.app.ebfitapp.model.ToDoModel
import com.app.ebfitapp.viewmodel.CalendarViewModel
import com.google.android.material.snackbar.Snackbar

class CalendarToDoAdapter(
    var todoArray : ArrayList<ToDoModel>,
    private val calendarViewModel: CalendarViewModel
) : RecyclerView.Adapter<CalendarToDoAdapter.ToDoHolder>() {
    class ToDoHolder(val bindingToDo: ItemCalendarToDoBinding) :
        RecyclerView.ViewHolder(bindingToDo.root) {
    }
    inner class SwipeToDeleteCallback: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        @SuppressLint("SuspiciousIndentation")
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val swipeFlags = ItemTouchHelper.LEFT
            return makeMovementFlags(0,swipeFlags)
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val todoModel = todoArray[position]
                val id = todoModel.todoId
                calendarViewModel.deleteToDoItem(id)
                deleteToDoItem(position)
                val snackBar = Snackbar.make(viewHolder.itemView, "Item deleted", Snackbar.LENGTH_SHORT)
                snackBar.setAction("Undo") {
                    restoreData(todoModel,position)
                    calendarViewModel.addToDoItem(todoModel){
                    }
                }
                snackBar.show()
        }

    }

    fun deleteToDoItem(i : Int)
    {
        todoArray.removeAt(i)
        notifyDataSetChanged()
        if (todoArray.isEmpty()) {  calendarViewModel.indexExists.value = false }
    }
    fun restoreData(model: ToDoModel, position: Int)
    {
        todoArray.add(position,model)
        notifyItemInserted(position)
        if (todoArray.isNotEmpty()) {  calendarViewModel.indexExists.value = true }
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

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newTodoText: List<ToDoModel>) {
        todoArray = newTodoText as ArrayList<ToDoModel>
        notifyDataSetChanged()
    }

}

