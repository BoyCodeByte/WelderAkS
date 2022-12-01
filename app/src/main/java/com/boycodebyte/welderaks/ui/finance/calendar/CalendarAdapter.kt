package com.boycodebyte.welderaks.ui.finance.calendar

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(val context: Context): RecyclerView.Adapter<CalendarAdapter.DatePickerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatePickerViewHolder {
        val view = DatePickerView(context)
        view.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return DatePickerViewHolder(view)
    }

    override fun onBindViewHolder(holder: DatePickerViewHolder, position: Int) {
        val view = holder.itemView as DatePickerView


    }

    override fun getItemCount(): Int {
        return 5
    }

    class DatePickerViewHolder(view: View) :
        RecyclerView.ViewHolder(view)
}