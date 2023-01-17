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
import com.boycodebyte.welderaks.domain.usecase.GetProfilesWithoutGeneralUseCase
import com.boycodebyte.welderaks.domain.usecase.SetDayDataByIDUseCase
import kotlin.math.ceil

class FinanceViewModel : ViewModel() {

    private var selectedProfile: Profile? = null
    private var selectedMonth: Calendar = Calendar.getInstance()
    private var selectedDay: Calendar = Calendar.getInstance()

    private val getCalendarDataByIDUseCase = GetCalendarDataByIDUseCase(
        CalendarDataRepository(
            FirebaseStorage()
        )
    )

    private val setDayDataByIDUseCase = SetDayDataByIDUseCase(
        CalendarDataRepository(
            FirebaseStorage()
        )
    )

    private val getProfilesUseCase = GetProfilesWithoutGeneralUseCase(
        ProfileRepository(
            FirebaseStorage()
        )
    )

    private val _profiles = MutableLiveData<List<Profile>>()
    val profiles: LiveData<List<Profile>> = _profiles

    private val _calendarData = MutableLiveData<CalendarData>()
    val calendarData: LiveData<CalendarData> = _calendarData

    private val _monthlySummaryState = MutableLiveData<MonthlySummaryState>()
    val monthlySummaryState: LiveData<MonthlySummaryState> = _monthlySummaryState

    private val _paymentState = MutableLiveData<PaymentState>()
    val paymentState: LiveData<PaymentState> = _paymentState

    fun selectProfile(profile: Profile) {
        selectedProfile = profile
        updateCalendarData()
        updateMonth()
    }

    fun selectMonth(month: Calendar) {
        selectedMonth = month
        updateMonth()
    }

    fun updateCalendarData() {
        if (selectedProfile != null) {
            _calendarData.value = getCalendarDataByIDUseCase.execute(selectedProfile!!.id)
            updateMonth()
        }
    }

    fun clickDay(selectedDate: Calendar): DayDialogState {
        selectedDay = selectedDate
        val dataOfDay = _calendarData.value?.getDataOfDay(selectedDate)

        var rate = dataOfDay?.rate.toString()
        if (rate == "0") {
            rate = selectedProfile?.rate.toString()
        }
        return DayDialogState(
            title = SimpleDateFormat("yyyy-MM-dd").format(selectedDate.time),
            hours = dataOfDay?.hours.toString(),
            rate = rate,
            coefficient = dataOfDay?.coefficient.toString(),
            description = dataOfDay?.description.toString()
        )
    }

    fun clickPay(): PayDialogState? {
        if (selectedProfile != null) {
            val month = _calendarData.value?.getDataOfMonth(selectedMonth)
            val hourlyPayment = monthlySummaryState.value?.hourlyPayment ?: "0"
            return PayDialogState(
                PaymentState(
                    prepayment = month?.prepayment.toString(),
                    salary = month?.salary.toString(),
                    award = month?.award.toString(),
                ),
                selectedMonth,
                selectedProfile!!.id,
                hourlyPayment.toInt()
            )
        }
        return null
    }

    fun setDayData(state: DayDialogState) {
        val day = Day(
            number = selectedDay.get(Calendar.DATE),
            hours = state.hours.toInt(),
            rate = state.rate.toInt(),
            coefficient = state.coefficient.toDouble(),
            description = state.description
        )
        if (selectedProfile != null) {
            setDayDataByIDUseCase.execute(selectedProfile!!.id, selectedDay, day)
            updateCalendarData()
        }
    }

    private fun updateMonth() {
        val data = calendarData.value
        val year = data?.getYear(selectedMonth.get(Calendar.YEAR)) ?: Year(
            selectedMonth.get(
                Calendar.YEAR
            )
        )
        val month = year.getMonth(selectedMonth.get(Calendar.MONTH) + 1)
        updateMonthlySummary(month)
        updateMonthSalary(month)
    }

    //Обновляет сводку за месяц
    private fun updateMonthlySummary(month: Month) {
        var days = 0;
        var hours = 0;
        var pay = 0.0;
        month.days.forEach {
            days++
            hours += it.hours
            pay += it.hours * it.rate * it.coefficient
        }
        _monthlySummaryState.value = MonthlySummaryState(
            days.toString(),
            hours.toString(),
            ceil(pay).toInt().toString()
        )
    }

    //Обновляет данные о зарплате
    private fun updateMonthSalary(month: Month) {
        _paymentState.value = PaymentState(
            prepayment = month.prepayment.toString(),
            salary = month.salary.toString(),
            award = month.award.toString()
        )
    }

    fun update() {
        _profiles.value = getProfilesUseCase.execute()
    }
}