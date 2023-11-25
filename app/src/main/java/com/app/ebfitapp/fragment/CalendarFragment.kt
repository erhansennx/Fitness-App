package com.app.ebfitapp.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.app.ebfitapp.R
import com.app.ebfitapp.adapter.CalendarAdapter
import com.app.ebfitapp.adapter.CalendarToDoAdapter
import com.app.ebfitapp.databinding.FragmentCalendarBinding
import com.app.ebfitapp.model.CalendarDateModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : Fragment(), CalendarAdapter.onItemClickListener{

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvDateMonth: TextView
    private lateinit var ivCalendarNext: ImageView
    private lateinit var ivCalendarPrevious: ImageView
    private lateinit var toDoAdapter: CalendarToDoAdapter
    var selectedDate : String? = null
    var selectedDay : String? = null
    var toDoList = ArrayList<String>()
    var isSelected : Boolean = false

    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)
    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private val dates = ArrayList<Date>()
    private lateinit var adapter: CalendarAdapter
    private val calendarList2 = ArrayList<CalendarDateModel>()
    private lateinit var fragmentCalenderBinding : FragmentCalendarBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
            setUpAdapter()
            setUpClickListener()
            setUpCalendar()

            toDoAdapter = CalendarToDoAdapter(toDoList,selectedDay,selectedDate)
            recyclerView2.layoutManager = LinearLayoutManager(this@CalendarFragment.context)
            recyclerView2.adapter = toDoAdapter

            //toDoAdapter.notifyDataSetChanged()

            todoText.setOnClickListener(){
                //Dialog mevzusu
                if(isSelected == true)
                   showToDoDialog()
                else Toast.makeText(this@CalendarFragment.context,"You should select a day first",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemClick(text: String, day: String) {
        isSelected = true
        selectedDay = day
        selectedDate = text

        Toast.makeText(requireContext(), "onItemClick tıklandı: $selectedDate:, $selectedDay:", Toast.LENGTH_SHORT).show()
    }

    private fun setUpClickListener(){
        ivCalendarNext.setOnClickListener()
        {
            cal.add(Calendar.MONTH,1)
                setUpCalendar()
        }
        ivCalendarPrevious.setOnClickListener()
        {
            cal.add(Calendar.MONTH, -1)
            if(cal == currentDate)
                setUpCalendar()
            else
                setUpCalendar()
        }
    }

    private fun setUpAdapter()
    {
        val snapHelper : SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        adapter = CalendarAdapter { calendarDateModel: CalendarDateModel, position: Int ->
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


        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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
            Toast.makeText(requireContext(), " save button tıklandı", Toast.LENGTH_SHORT).show()
            rootView.removeView(overlay)
            dialog.dismiss()
        }
    }



}