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
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.databinding.FragmentFinanceBinding
import com.boycodebyte.welderaks.ui.finance.calendar.CalendarAdapter
import com.boycodebyte.welderaks.ui.finance.calendar.DatePickerView
import com.boycodebyte.welderaks.ui.finance.calendar.DatePickerView.OnDayClickListener


class FinanceFragment : Fragment() {

    private var _binding: FragmentFinanceBinding? = null
    private val binding get() = _binding!!
    private lateinit var financeViewModel: FinanceViewModel
    private lateinit var calendarAdapter: CalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        financeViewModel = ViewModelProvider(this)[FinanceViewModel::class.java]

        _binding = FragmentFinanceBinding.inflate(inflater, container, false)
        calendarVisible(false)
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
                profilesAdapter.clear()
                profilesAdapter.addAll(it)
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

        binding.payButton.setOnClickListener {

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
        dialog.listener = { financeViewModel.setDayData(it) }
        dialog.show(childFragmentManager, DayDialogFragment.TAG, state)
    }

    private fun showPayDialog(state: PayDialogState) {
        val dialog = PayDialogFragment(state)
        dialog.listener = { financeViewModel.updateCalendarData() }
        dialog.show(childFragmentManager, DayDialogFragment.TAG)
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
            binding.spinner.visibility = View.VISIBLE
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
            binding.payButton.visibility = View.VISIBLE
            binding.noDataLabel.visibility = View.GONE
        } else {
            binding.spinner.visibility = View.GONE
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
            binding.payButton.visibility = View.GONE
            binding.noDataLabel.visibility = View.VISIBLE
        }
    }
}