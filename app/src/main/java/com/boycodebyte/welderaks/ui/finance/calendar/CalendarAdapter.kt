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

    val mMinDate = Calendar.getInstance().apply { set(2022, Calendar.DECEMBER, 1) }
    val mMaxDate = Calendar.getInstance().apply { set(2032, Calendar.DECEMBER, 31) }
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
                    onDayClickListener?.onDayClick(view, day)
                }
            }
        }

    init {
        count = getDiffMonths(mMaxDate,mMinDate)
        notifyDataSetChanged()
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
        view.setMonthAndYear(
            getMonthForPosition(position),
            getYearForPosition(position),
            getMonthData(position)
        )
        view.monthData = calendarData.getYear(getYearForPosition(position))
            .getMonth(getMonthForPosition(position) + 1)
        view.onDayClickListener = listener
    }

    private fun getMonthData(position: Int): CalendarData.Month {
        val calendar = getCurrentMonth(position)
        val year = calendarData.getYear(calendar.get(Calendar.YEAR))
        return year.getMonth(calendar.get(Calendar.MONTH) + 1)
    }

    override fun getItemCount(): Int {
        return count
    }

    private fun getMonthForPosition(position: Int): Int {
        return (position + mMinDate[Calendar.MONTH]) % 12
    }

    private fun getYearForPosition(position: Int): Int {
        val yearOffset: Int =
            (position + mMinDate[Calendar.MONTH]) / 12
        return yearOffset + mMinDate[Calendar.YEAR]
    }

    fun getDiffMonths(maxDate: Calendar, minDate:Calendar): Int {
        val diffYear: Int = maxDate.get(Calendar.YEAR) - minDate.get(Calendar.YEAR)
        val diffMonth: Int = maxDate.get(Calendar.MONTH) - minDate.get(Calendar.MONTH)
        return diffMonth + 12 * diffYear
    }

    fun getCurrentMonth(position: Int): Calendar {
        val date = Calendar.getInstance()
        date[Calendar.YEAR] = getYearForPosition(position)
        date[Calendar.MONTH] = getMonthForPosition(position)
        date[Calendar.DAY_OF_MONTH] = 1
        return date
    }

    fun setOnClickDayListener(listener: OnDayClickListener) {
        onDayClickListener = listener
    }


    class DatePickerViewHolder(view: View) :
        RecyclerView.ViewHolder(view)
}