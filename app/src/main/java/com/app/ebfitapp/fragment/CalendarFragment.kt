package com.app.ebfitapp.fragment

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.app.ebfitapp.adapter.CalendarAdapter
import com.app.ebfitapp.databinding.FragmentCalendarBinding
import com.app.ebfitapp.model.CalendarDateModel
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment(), CalendarAdapter.onItemClickListener{

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvDateMonth: TextView
    private lateinit var ivCalendarNext: ImageView
    private lateinit var ivCalendarPrevious: ImageView

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
        }
    }

    override fun onItemClick(text: String, date: String, day: String) {
        with(fragmentCalenderBinding)
        {
            selectedDate.text = "Selected date: $text"
            selectedDD.text = "Selected date: $date"
            selectedDay.text = "Selected date: $day"
        }
    }

    private fun setUpClickListener(){
        ivCalendarNext.setOnClickListener()
        {
            cal.add(Calendar.MONTH,-1)
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

}