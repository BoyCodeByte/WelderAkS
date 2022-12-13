package com.boycodebyte.welderaks.ui.finance

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.models.CalendarData
import com.boycodebyte.welderaks.data.models.CalendarData.*
import com.boycodebyte.welderaks.data.repositories.CalendarDataRepository
import com.boycodebyte.welderaks.data.repositories.ProfileRepository
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.domain.usecase.GetCalendarDataByIDUseCase
import com.boycodebyte.welderaks.domain.usecase.GetProfilesUseCase
import kotlin.math.ceil

class FinanceViewModel : ViewModel() {

    private var currentProfile: Profile? = null

    private val getCalendarDataByIDUseCase = GetCalendarDataByIDUseCase(
        CalendarDataRepository(
            FirebaseStorage()
        )
    )
    private val getProfilesUseCase = GetProfilesUseCase(
        ProfileRepository(
            FirebaseStorage()
        )
    )

    private val _profiles = MutableLiveData<List<Profile>>().apply {
        value = getProfilesUseCase.execute()
    }
    val profiles: LiveData<List<Profile>> = _profiles

    private val _calendarData = MutableLiveData<CalendarData>().apply {
        value = getCalendarDataByIDUseCase.execute(1)
    }
    val calendarData: LiveData<CalendarData> = _calendarData

    private val _calendarDialogState = MutableLiveData<CalendarDialogState>()
    val calendarDialogState: LiveData<CalendarDialogState> = _calendarDialogState

    private val _monthlySummaryState = MutableLiveData<MonthlySummaryState>()
    val monthlySummaryState: LiveData<MonthlySummaryState> = _monthlySummaryState

    private val _paymentState = MutableLiveData<PaymentState>()
    val paymentState: LiveData<PaymentState> = _paymentState

    private fun updateProfiles() {
        _profiles.value = getProfilesUseCase.execute()
    }

    private fun updateCalendarData(id: Int) {
        _calendarData.value = getCalendarDataByIDUseCase.execute(id)
    }

    fun clickDay(selectedDate: Calendar) {
        val dataOfDay = _calendarData.value?.getDataOfDay(selectedDate)
        _calendarDialogState.value = CalendarDialogState(
            title = SimpleDateFormat("yyyy-MM-dd").format(selectedDate.time),
            hours = dataOfDay?.hours.toString(),
            coefficient = dataOfDay?.coefficient.toString(),
            //Баг
            description = dataOfDay?.description.toString()
        )
    }
    
    //Обновляет сводку за месяц
    fun updateMonthlySummary(date: Calendar) {
        val data = calendarData.value
        val year = data?.getYear(date.get(Calendar.YEAR)) ?: Year(date.get(Calendar.YEAR))
        val month = year.getMonth(date.get(Calendar.MONTH) + 1)
        var days = 0;
        var hours = 0;
        var pay = 0.0;
        month.days.forEach {
            days++
            hours += it.hours
            //add profile pay of hour
            pay += it.hours * 100 * it.coefficient
        }
        _monthlySummaryState.value = MonthlySummaryState(
            days.toString(),
            hours.toString(),
            ceil(pay).toInt().toString()
        )
    }

    //Возвращет пробный экземпляр данных за год
    private fun initProbe(): CalendarData {
        var calData = CalendarData()
        var yearData = Year(2023)
        var monthData = Month(2)
        monthData.days.add(Day(1, 1))
        monthData.days.add(Day(2, 5))
        monthData.days.add(Day(3, 8))
        monthData.days.add(Day(7, 12))
        yearData.months.add(monthData)
        calData.years.add(yearData)
        return calData
    }
}