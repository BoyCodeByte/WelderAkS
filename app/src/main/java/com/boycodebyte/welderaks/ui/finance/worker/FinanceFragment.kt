package com.boycodebyte.welderaks.ui.finance.worker

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.boycodebyte.welderaks.databinding.FragmentFinanceWorkerBinding
import com.boycodebyte.welderaks.ui.finance.general.DayDialogFragment
import com.boycodebyte.welderaks.ui.finance.DayDialogState
import com.boycodebyte.welderaks.ui.finance.MonthlySummaryState
import com.boycodebyte.welderaks.ui.finance.PaymentState
import com.boycodebyte.welderaks.ui.finance.calendar.CalendarAdapter
import com.boycodebyte.welderaks.ui.finance.calendar.DatePickerView
import com.boycodebyte.welderaks.ui.finance.calendar.DatePickerView.OnDayClickListener


class FinanceFragment : Fragment() {

    private var _binding: FragmentFinanceWorkerBinding? = null
    private val binding get() = _binding!!
    private lateinit var financeViewModel: FinanceViewModel
    private lateinit var calendarAdapter: CalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        financeViewModel = ViewModelProvider(this)[FinanceViewModel::class.java]

        _binding = FragmentFinanceWorkerBinding.inflate(inflater, container, false)
        calendarAdapter = CalendarAdapter(requireContext())
        calendarAdapter.setOnClickDayListener(object : OnDayClickListener {
            override fun onDayClick(view: DatePickerView?, day: Calendar) {
                val state = financeViewModel.clickDay(day)
                showCalendarDialog(state)
            }
        })

        binding.pager.adapter = calendarAdapter
        binding.pager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                financeViewModel.selectMonth(calendarAdapter.getCurrentMonth(position))
            }
        })

        financeViewModel.profiles.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                calendarVisible(false)
            } else {
                calendarVisible(true)
            }
        }
        financeViewModel.calendarData.observe(viewLifecycleOwner) {
            calendarAdapter.calendarData = it
        }

        financeViewModel.monthlySummaryState.observe(viewLifecycleOwner) {
            updateMonthlySummary(it)
        }

        financeViewModel.paymentState.observe(viewLifecycleOwner) {
            updatePayment(it)
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        financeViewModel.update()
        binding.pager.setCurrentItem(
            calendarAdapter.getDiffMonths(
                Calendar.getInstance(),
                calendarAdapter.mMinDate
            ), false
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    private fun showCalendarDialog(state: DayDialogState) {
        val dialog = DayDialogFragment()
        dialog.acceptListener = { financeViewModel.setDayData(it) }
        dialog.deleteListener = {financeViewModel.removeDayData()}
        dialog.show(childFragmentManager, DayDialogFragment.TAG, state)
    }

    private fun updateMonthlySummary(state: MonthlySummaryState) {
        binding.days.text = state.days
        binding.hours.text = state.hours
        binding.hourlyPayment.text = state.hourlyPayment
    }

    private fun updatePayment(state: PaymentState) {
        binding.prepayment.text = state.prepayment
        binding.salary.text = state.salary
        binding.award.text = state.award
    }

    private fun calendarVisible(isVisible: Boolean) {
        if (isVisible) {
            binding.pager.visibility = View.VISIBLE
            binding.hours.visibility = View.VISIBLE
            binding.hoursLabel.visibility = View.VISIBLE
            binding.days.visibility = View.VISIBLE
            binding.daysLabel.visibility = View.VISIBLE
            binding.hourlyPayment.visibility = View.VISIBLE
            binding.hourlyPaymentLabel.visibility = View.VISIBLE
            binding.prepayment.visibility = View.VISIBLE
            binding.prepaymentLabel.visibility = View.VISIBLE
            binding.salary.visibility = View.VISIBLE
            binding.salaryLabel.visibility = View.VISIBLE
            binding.award.visibility = View.VISIBLE
            binding.awardLabel.visibility = View.VISIBLE
            binding.noDataLabel.visibility = View.GONE
        } else {
            binding.pager.visibility = View.GONE
            binding.hours.visibility = View.GONE
            binding.hoursLabel.visibility = View.GONE
            binding.days.visibility = View.GONE
            binding.daysLabel.visibility = View.GONE
            binding.hourlyPayment.visibility = View.GONE
            binding.hourlyPaymentLabel.visibility = View.GONE
            binding.prepayment.visibility = View.GONE
            binding.prepaymentLabel.visibility = View.GONE
            binding.salary.visibility = View.GONE
            binding.salaryLabel.visibility = View.GONE
            binding.award.visibility = View.GONE
            binding.awardLabel.visibility = View.GONE
            binding.noDataLabel.visibility = View.VISIBLE
        }
    }
}