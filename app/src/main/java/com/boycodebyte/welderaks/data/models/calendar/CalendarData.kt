package com.boycodebyte.welderaks.data.models.calendar

import com.boycodebyte.welderaks.data.exceptions.DateNotFoundException

class CalendarData {

    var years: ArrayList<Year> = ArrayList()

    fun getYear(year: Int): Year{
        years.forEach{
            if(it.number == year){
                return it
            }
        }
        throw DateNotFoundException()
    }

    data class Day(
        var number: Int = 0,
        var hours: Int = 0,
        var coefficient: Int = 1,
        var description: String = ""
    )

    data class Month(
        var number: Int,
        var days: ArrayList<Day> = ArrayList(),
        var prepayment: Int = 0,
        var salary: Int = 0
    ){
        fun getDay(day: Int): Day{
            days.forEach{
                if(it.number == day){
                    return it
                }
            }
            throw DateNotFoundException()
        }
    }

    data class Year(
        var number: Int,
        var months: ArrayList<Month> = ArrayList()
    ){
        fun getMonth(month: Int): Month{
            months.forEach{
                if(it.number == month){
                    return it
                }
            }
            throw DateNotFoundException()
        }
    }
}