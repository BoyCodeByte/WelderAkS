package com.boycodebyte.welderaks.ui.finance.worker

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
import com.boycodebyte.welderaks.domain.usecase.RemoveDayDataByIDUseCase
import com.boycodebyte.welderaks.domain.usecase.SetDayDataByIDUseCase
import com.boycodebyte.welderaks.ui.finance.DayDialogState
import com.boycodebyte.welderaks.ui.finance.MonthlySummaryState
import com.boycodebyte.welderaks.ui.finance.PayDialogState
import com.boycodebyte.welderaks.ui.finance.PaymentState
import kotlin.math.ceil

class FinanceViewModel : ViewModel() {

    private var selectedMonth: Calendar = Calendar.getInstance()

    private val getCalendarDataByIDUseCase = GetCalendarDataByIDUseCase(
        CalendarDataRepository(
            FirebaseStorage()
        )
    )

    private val _calendarData = MutableLiveData<CalendarData>()
    val calendarData: LiveData<CalendarData> = _calendarData

    private val _monthlySummaryState = MutableLiveData<MonthlySummaryState>()
    val monthlySummaryState: LiveData<MonthlySummaryState> = _monthlySummaryState

    private val _paymentState = MutableLiveData<PaymentState>()
    val paymentState: LiveData<PaymentState> = _paymentState

    fun selectMonth(month: Calendar) {
        selectedMonth = month
        updateMonth()
    }
    fun clickDay(selectedDate: Calendar): DayDialogState {
        val dataOfDay = _calendarData.value?.getDataOfDay(selectedDate)
        return DayDialogState(
            title = SimpleDateFormat("dd-MM-yyyy").format(selectedDate.time),
            hours = dataOfDay?.hours.toString(),
            rate = dataOfDay?.rate.toString(),
            coefficient = dataOfDay?.coefficient.toString(),
            description = dataOfDay?.description.toString()
        )
    }

    fun updateCalendarData(profile: Profile?) {
            _calendarData.value = profile?.id?.let { getCalendarDataByIDUseCase.execute(it) }
            updateMonth()
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

    private fun updateMonthSalary(month: Month) {
        _paymentState.value = PaymentState(
            prepayment = month.prepayment.toString(),
            salary = month.salary.toString(),
            award = month.award.toString(),
            amount = (month.prepayment + month.salary + month.award).toString()
        )
    }
}