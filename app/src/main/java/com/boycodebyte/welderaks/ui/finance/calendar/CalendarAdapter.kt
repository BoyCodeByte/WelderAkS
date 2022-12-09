package com.boycodebyte.welderaks.ui.finance.calendar

import android.content.Context
import android.icu.util.Calendar
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.boycodebyte.welderaks.data.models.CalendarData
import com.boycodebyte.welderaks.ui.finance.calendar.DatePickerView.*

class CalendarAdapter(
    private val context: Context
) : RecyclerView.Adapter<CalendarAdapter.DatePickerViewHolder>() {

    private val selectedDay = Calendar.getInstance()
    private val mMinDate = Calendar.getInstance()
    private val mMaxDate = Calendar.getInstance()
    private var onDayClickListener: OnDayClickListener? = null
    private var count = 0

    var calendarData: CalendarData = CalendarData()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val listener: OnDayClickListener =
        object : OnDayClickListener {
            override fun onDayClick(view: DatePickerView?, day: Calendar) {
                if (day != null) {
//                    setSelectedDay(day)
                    onDayClickListener?.onDayClick(view, day)
                }
            }
        }

    init {
        initDateRange()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatePickerViewHolder {
        val view = DatePickerView(context)
        view.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return DatePickerViewHolder(view)
    }

    override fun onBindViewHolder(holder: DatePickerViewHolder, position: Int) {
        val view = holder.itemView as DatePickerView
        view.setMonthAndYear(getMonthForPosition(position), getYearForPosition(position) , getMonthData(position))
        view.monthData = calendarData.getYear(getYearForPosition(position)).getMonth(getMonthForPosition(position)+1)
        view.onDayClickListener = listener
    }

    private fun getMonthData(position: Int): CalendarData.Month {
        val calendar = getCurrentDate(position)
        val year = calendarData.getYear(calendar.get(Calendar.YEAR))
        return year.getMonth(calendar.get(Calendar.MONTH) + 1)
    }

    override fun getItemCount(): Int {
        return count
    }

    private fun initDateRange() {
        mMinDate.set(2022, Calendar.DECEMBER, 1)
        mMaxDate.set(2032, Calendar.DECEMBER, 31)
        val diffYear: Int = mMaxDate.get(Calendar.YEAR) - mMinDate.get(Calendar.YEAR)
        val diffMonth: Int = mMaxDate.get(Calendar.MONTH) - mMinDate.get(Calendar.MONTH)
        count = diffMonth + 12 * diffYear + 1
        notifyDataSetChanged()
    }


    private fun getMonthForPosition(position: Int): Int {
        return (position + mMinDate[Calendar.MONTH]) % 12
    }

    private fun getYearForPosition(position: Int): Int {
        val yearOffset: Int =
            (position + mMinDate[Calendar.MONTH]) / 12
        return yearOffset + mMinDate[Calendar.YEAR]
    }

    fun getCurrentDate(position: Int): Calendar {
        val date = Calendar.getInstance()
        date[Calendar.MONTH] = getMonthForPosition(position)
        date[Calendar.YEAR] = getYearForPosition(position)
        date[Calendar.DAY_OF_MONTH] = 1
        return date
    }

    fun setOnClickDayListener(listener: OnDayClickListener) {
        onDayClickListener = listener
    }


    class DatePickerViewHolder(view: View) :
        RecyclerView.ViewHolder(view)
}