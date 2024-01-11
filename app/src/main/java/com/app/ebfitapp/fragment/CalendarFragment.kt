package com.app.ebfitapp.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kotlin.random.Random
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.app.ebfitapp.R
import com.app.ebfitapp.adapter.CalendarAdapter
import com.app.ebfitapp.adapter.CalendarToDoAdapter
import com.app.ebfitapp.databinding.FragmentCalendarBinding
import com.app.ebfitapp.model.CalendarDateModel
import com.app.ebfitapp.model.ToDoModel
import com.app.ebfitapp.service.FirebaseAuthService
import com.app.ebfitapp.utils.CustomProgress
import com.app.ebfitapp.viewmodel.CalendarViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : Fragment(), CalendarAdapter.onItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvDateMonth: TextView
    private lateinit var ivCalendarNext: ImageView
    private lateinit var ivCalendarPrevious: ImageView
    private lateinit var toDoAdapter: CalendarToDoAdapter
    private val calendarViewModel: CalendarViewModel by viewModels()
    private lateinit var firebaseAuthService: FirebaseAuthService
    private lateinit var customProgress: CustomProgress
    var selectedDate: String? = null
    var selectedDay: String? = null
    var isSelected: Boolean = false
    var todoArray = arrayListOf<ToDoModel>()


    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)
    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private val dates = ArrayList<Date>()
    private lateinit var adapter: CalendarAdapter
    private val calendarList2 = ArrayList<CalendarDateModel>()
    private lateinit var fragmentCalenderBinding: FragmentCalendarBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        customProgress = CustomProgress(requireContext())
        firebaseAuthService = FirebaseAuthService(requireContext())
        fragmentCalenderBinding = FragmentCalendarBinding.inflate(layoutInflater)
        return fragmentCalenderBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(fragmentCalenderBinding)
        {
            this@CalendarFragment.tvDateMonth = textDateMonth
            this@CalendarFragment.recyclerView = recyclerView
            this@CalendarFragment.ivCalendarNext = ivCalendarNext
            this@CalendarFragment.ivCalendarPrevious = ivCalendarPrevious
            emptyText.visibility = View.GONE
            setUpAdapter()
            setUpClickListener()
            setUpCalendar()
            isEmptyText.visibility = View.GONE
            observeIndexExists()
            calendarSearch()

            toDoAdapter = CalendarToDoAdapter(todoArray, calendarViewModel)
            todoRecyclerView.layoutManager = LinearLayoutManager(this@CalendarFragment.context)
            todoRecyclerView.adapter = toDoAdapter

            val itemTouchHelper = ItemTouchHelper(toDoAdapter.SwipeToDeleteCallback())
            itemTouchHelper.attachToRecyclerView(todoRecyclerView)

            toDoAdapter.notifyDataSetChanged()

            todoText.setOnClickListener() {
                if (isSelected)
                    showToDoDialog()
                else Toast.makeText(
                    this@CalendarFragment.context,
                    "You should select a day first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onItemClick(text: String, day: String) {
        isSelected = true
        selectedDay = day
        selectedDate = text
        customProgress.show()

        calendarViewModel.getToDoItems { toDoList ->
            val filteredToDoList = if (isSelected && selectedDate != null) {
                toDoList.filter { it.selectedDate == selectedDate }

            } else {
                emptyList()
            }

            if (filteredToDoList.isNotEmpty()) {
                //There is something on the screen
                fragmentCalenderBinding.isEmptyText.visibility = View.GONE
                fragmentCalenderBinding.emptyText.visibility = View.GONE

                val sortedToDoList = filteredToDoList.sortedBy { it.createdAt }
                val arrayListToDoList = ArrayList(sortedToDoList)

                toDoAdapter.todoArray.clear()
                toDoAdapter.todoArray.addAll(arrayListToDoList)
                toDoAdapter.notifyDataSetChanged()
            } else {
                //There is not item current day is null can be shown
                //if user uses search bar currenday has to be remove and if items show up
                //emptyText should be viewGone otherwise show empty text
                toDoAdapter.todoArray.clear()
                fragmentCalenderBinding.isEmptyText.visibility = View.VISIBLE
                fragmentCalenderBinding.emptyText.visibility = View.GONE
            }

            customProgress.dismiss()
        }

        todoTextAnimation()
    }


    private fun setUpClickListener() {
        ivCalendarNext.setOnClickListener()
        {
            cal.add(Calendar.MONTH, 1)
            setUpCalendar()
        }
        ivCalendarPrevious.setOnClickListener()
        {
            cal.add(Calendar.MONTH, -1)
            if (cal == currentDate)
                setUpCalendar()
            else
                setUpCalendar()
        }
    }

    private fun setUpAdapter() {
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        adapter = CalendarAdapter(fragmentCalenderBinding.recyclerView) { calendarDateModel: CalendarDateModel, position: Int ->
            calendarList2.forEachIndexed { index, calendarModel ->
                calendarModel.isSelected = index == position
            }
            adapter.setData(calendarList2)
            adapter.setOnItemClickListener(this@CalendarFragment)
        }
        recyclerView.adapter = adapter
    }

    private fun setUpCalendar() {
        val calendarList = ArrayList<CalendarDateModel>()
        tvDateMonth.text = sdf.format(cal.time)
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        while (dates.size < maxDaysInMonth) {
            dates.add(monthCalendar.time)
            calendarList.add(CalendarDateModel(monthCalendar.time))
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        calendarList2.clear()
        calendarList2.addAll(calendarList)
        adapter.setOnItemClickListener(this@CalendarFragment)
        adapter.setData(calendarList)
        adapter.scrollToPosition()
    }


    private fun showToDoDialog() {
        val rootView = requireActivity().window.decorView.rootView
        val overlay = View(requireContext())

        overlay.setBackgroundColor(Color.parseColor("#80000000"))
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        (rootView as ViewGroup).addView(overlay, layoutParams)

        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.todo_dialog_box)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)

        val dialogDate = dialog.findViewById<TextView>(R.id.dialogDate)
        val dialogDay = dialog.findViewById<TextView>(R.id.dialogDay)

        selectedDay?.let {
            dialogDay.text = it
        }

        selectedDate?.let {
            dialogDate.text = it
        }
        val dialogEditText = dialog.findViewById<EditText>(R.id.dialogEditText)
        val dialogCancelBtn = dialog.findViewById<Button>(R.id.dialogCancelBtn)
        val dialogSaveBtn = dialog.findViewById<Button>(R.id.dialogSaveBtn)
        dialog.show()
        dialogCancelBtn.setOnClickListener {
            rootView.removeView(overlay)
            dialog.dismiss()
        }
        dialogSaveBtn.setOnClickListener {
            val dialogEditText = dialogEditText.text.toString()
            val uniqueId = UUID.randomUUID().toString().substring(0, 12)
            val currentTimeStamp = System.currentTimeMillis()
            val newTodoItem =
                ToDoModel(selectedDay, selectedDate, dialogEditText, uniqueId, currentTimeStamp)
            calendarViewModel.addToDoItem(newTodoItem) { isSuccess ->
                if (isSuccess) {
                    fragmentCalenderBinding.isEmptyText.visibility = View.GONE
                    toDoAdapter.todoArray.add(newTodoItem)
                    toDoAdapter.notifyDataSetChanged()
                    rootView.removeView(overlay)
                    dialog.dismiss()
                } else {
                }
            }
        }
    }


    private fun calendarSearch() = with(fragmentCalenderBinding) {
        calendarSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            @SuppressLint("NotifyDataSetChanged")
            override fun afterTextChanged(p0: Editable?) {
                val searchText = p0.toString().trim()

                if (searchText.isNotEmpty()) {
                    calendarViewModel.getToDoItems { toDoList ->
                        val filteredToDoText = if (isSelected)
                            toDoList.filter { it.todoText!!.contains(searchText, ignoreCase = true) }
                        else arrayListOf()

                        if (filteredToDoText.isNotEmpty()) {
                            emptyText.visibility = View.GONE
                            isEmptyText.visibility = View.GONE

                            val sortedToDoList = filteredToDoText.sortedBy { it.todoText }
                            val arrayListToDoList = ArrayList(sortedToDoList)

                            if (toDoAdapter.todoArray != arrayListToDoList) {
                                toDoAdapter.todoArray.clear()
                                toDoAdapter.todoArray.addAll(arrayListToDoList)
                                toDoAdapter.notifyDataSetChanged()
                            }
                        } else {
                            toDoAdapter.todoArray.clear()
                            toDoAdapter.notifyDataSetChanged()
                            emptyText.visibility = View.VISIBLE
                            isEmptyText.visibility = View.GONE
                        }

                        customProgress.dismiss()
                    }
                } else {
                    if (toDoAdapter.todoArray.isNotEmpty()) {
                        toDoAdapter.todoArray.clear()
                        toDoAdapter.notifyDataSetChanged()
                    }

                    emptyText.visibility = View.VISIBLE
                    isEmptyText.visibility = View.GONE
                }
            }
        })
    }



    private fun observeIndexExists() {
        calendarViewModel.indexExists.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { indexExists ->
                if (!indexExists){
                    fragmentCalenderBinding.isEmptyText.visibility = View.VISIBLE
                }
                else fragmentCalenderBinding.isEmptyText.visibility = View.GONE
            })
    }

    private fun todoTextAnimation()
    {
        val blinkAnimForToDoText = AlphaAnimation(1f,0f)
        blinkAnimForToDoText.duration = 500
        blinkAnimForToDoText.repeatMode = Animation.REVERSE
        blinkAnimForToDoText.repeatCount = Animation.INFINITE

        fragmentCalenderBinding.todoText.startAnimation(blinkAnimForToDoText)
    }
}