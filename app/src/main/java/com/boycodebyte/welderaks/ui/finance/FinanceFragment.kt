package com.boycodebyte.welderaks.ui.finance

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.boycodebyte.welderaks.R
import com.boycodebyte.welderaks.data.models.CalendarData
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.databinding.FragmentFinanceBinding
import com.boycodebyte.welderaks.ui.finance.calendar.CalendarAdapter
import com.boycodebyte.welderaks.ui.finance.calendar.DatePickerView
import com.boycodebyte.welderaks.ui.finance.calendar.DatePickerView.OnDayClickListener
import java.time.Month


class FinanceFragment : Fragment() {

    private var _binding: FragmentFinanceBinding? = null
    private val binding get() = _binding!!
    private lateinit var financeViewModel: FinanceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        financeViewModel = ViewModelProvider(this)[FinanceViewModel::class.java]

        _binding = FragmentFinanceBinding.inflate(inflater, container, false)

        val profilesAdapter = ArrayAdapter<Profile>(requireContext(), R.layout.spinner_item)
        binding.spinner.adapter = profilesAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val profile = parent.getItemAtPosition(pos) as Profile
                financeViewModel.selectProfile(profile)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        val calendarAdapter = CalendarAdapter(requireContext())
        calendarAdapter.setOnClickDayListener(object : OnDayClickListener {
            override fun onDayClick(view: DatePickerView?, day: Calendar) {
                financeViewModel.clickDay(day)
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
            profilesAdapter.clear()
            profilesAdapter.addAll(it)
        }
        financeViewModel.calendarData.observe(viewLifecycleOwner) {
            calendarAdapter.calendarData = it
        }

        financeViewModel.dayDialogState.observe(viewLifecycleOwner) {
            showCalendarDialog(it)
        }

        financeViewModel.payDialogState.observe(viewLifecycleOwner) {
            showPayDialog(it)
        }

        financeViewModel.monthlySummaryState.observe(viewLifecycleOwner) {
            updateMonthlySummary(it)
        }

        financeViewModel.paymentState.observe(viewLifecycleOwner) {
            updatePayment(it)
        }

        binding.payButton.setOnClickListener {
            financeViewModel.clickPay()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showCalendarDialog(state: DayDialogState) {
        val dialog = DayDialogFragment()
        dialog.listener = { financeViewModel.setDayData(it) }
        dialog.show(childFragmentManager, DayDialogFragment.TAG, state)
    }

    private fun showPayDialog(state: PayDialogState) {
        val dialog = PayDialogFragment(state)
        dialog.show(childFragmentManager, DayDialogFragment.TAG)
    }

    private fun updateMonthlySummary(state: MonthlySummaryState) {
        binding.days.text = state.days
        binding.hours.text = state.hours
        binding.hourlyPayment.text = state.salary
    }

    private fun updatePayment(state: PaymentState) {
        binding.prepayment.text = state.prepayment
        binding.salary.text = state.salary
        binding.award.text = state.award
    }
}