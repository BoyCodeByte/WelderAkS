package com.boycodebyte.welderaks.data.models

import android.icu.util.Calendar

class CalendarData {

    var years: ArrayList<Year> = ArrayList()

    fun getYear(year: Int): Year {
        years.forEach{
            if(it.number == year){
                return it
            }
        }
        return Year(year)
    }

    data class Day(
        var number: Int = 0,
        var hours: Int = 0,
        var coefficient: Double = 1.0,
        var description: String = ""
    )

    data class Month(
        var number: Int,
        var days: ArrayList<Day> = ArrayList(),
        var prepayment: Int = 0,
        var salary: Int = 0,
        var award: Int = 0
    ){
        fun getDay(day: Int): Day {
            days.forEach{
                if(it.number == day){
                    return it
                }
            }
            return Day(day)
        }
    }

    data class Year(
        var number: Int,
        var months: ArrayList<Month> = ArrayList(12)
    ){
        fun getMonth(month: Int): Month {
            months.forEach{
                if(it.number == month){
                    return it
                }
            }
            return Month(month)
        }
    }

    //Получаем данные за день
    fun getDataOfDay(date: Calendar): Day {
        return getYear(date.get(Calendar.YEAR))
            .getMonth(date.get(Calendar.MONTH) + 1)
            .getDay(date.get(Calendar.DATE))
    }

    //Получаем данные за месяц
    fun getDataOfMonth(date: Calendar): Month {
        return getYear(date.get(Calendar.YEAR))
            .getMonth(date.get(Calendar.MONTH) + 1)
    }
}