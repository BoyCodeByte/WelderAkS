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
import com.boycodebyte.welderaks.getProfile
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
        financeViewModel.updateCalendarData(requireActivity().getProfile())
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
}