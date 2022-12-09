package com.boycodebyte.welderaks.ui.finance

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.boycodebyte.welderaks.R
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.databinding.FragmentFinanceBinding
import com.boycodebyte.welderaks.ui.finance.calendar.CalendarAdapter
import com.boycodebyte.welderaks.ui.finance.calendar.DatePickerView
import com.boycodebyte.welderaks.ui.finance.calendar.DatePickerView.OnDayClickListener


class FinanceFragment : Fragment() {

    val storage = FirebaseStorage()

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

        val calendarAdapter = CalendarAdapter(requireContext())
        calendarAdapter.setOnClickDayListener(object : OnDayClickListener{
            override fun onDayClick(view: DatePickerView?, day: Calendar) {
               financeViewModel.clickDay(day)
            }
        })

        binding.pager.adapter = calendarAdapter
        binding.pager.registerOnPageChangeCallback(object : OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                financeViewModel.updateMonthlySummary(calendarAdapter.getCurrentDate(position))
            }
        })

        financeViewModel.profiles.observe(viewLifecycleOwner){
            profilesAdapter.clear()
            profilesAdapter.addAll(it)
        }
        financeViewModel.calendarData.observe(viewLifecycleOwner){
            calendarAdapter.calendarData = it
        }

        financeViewModel.calendarDialogState.observe(viewLifecycleOwner){
            showCalendarDialog(it)
        }

        financeViewModel.monthlySummaryState.observe(viewLifecycleOwner){
            updateMonthlySummary(it)
        }

        financeViewModel.paymentState.observe(viewLifecycleOwner){
            updatePayment(it)
        }

        binding.payButton.setOnClickListener{

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

    private fun showCalendarDialog(state: CalendarDialogState){
        CalendarDialogFragment().show(childFragmentManager,CalendarDialogFragment.TAG,state)
    }

    private fun updateMonthlySummary(state: MonthlySummaryState){
        binding.days.text = state.days
        binding.hours.text = state.hours
        binding.hourlyPayment.text = state.salary
    }

    private fun updatePayment(state: PaymentState){
        binding.prepayment.text = state.prepayment
        binding.salary.text = state.salary
        binding.award.text = state.award
    }
}