package com.boycodebyte.welderaks.ui.finance.general

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.boycodebyte.welderaks.data.repositories.CalendarDataRepository
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.databinding.FragmentPayBinding

import com.boycodebyte.welderaks.domain.usecase.SetAwardUseCase
import com.boycodebyte.welderaks.domain.usecase.SetPrepaymentUseCase
import com.boycodebyte.welderaks.domain.usecase.SetSalaryUseCase
import com.boycodebyte.welderaks.ui.finance.PayDialogState


class PayDialogFragment(
    private val state: PayDialogState
) : DialogFragment() {

    private var _binding: FragmentPayBinding? = null
    private val binding get() = _binding!!
    var listener = { }

    private val setPrepaymentUseCase = SetPrepaymentUseCase(CalendarDataRepository(FirebaseStorage()))
    private val setSalaryUseCase = SetSalaryUseCase(CalendarDataRepository(FirebaseStorage()))
    private val setAwardUseCase = SetAwardUseCase(CalendarDataRepository(FirebaseStorage()))

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentPayBinding.inflate(layoutInflater)
        binding.editPrepayment.setText(state.paymentState.prepayment)
        binding.editSalary.setText(state.paymentState.salary)
        val currentSalary = state.hourlyPayment- binding.editPrepayment.text.toString().toInt()
        binding.textSalaryHint.text = "По часам: ${state.hourlyPayment} минус аванс: $currentSalary"
        binding.editAward.setText(state.paymentState.award)
        binding.buttonPrepayment.setOnClickListener {
            var prepayment = binding.editPrepayment.text.toString()
            if(prepayment.isEmpty()){
                prepayment = "0"
                binding.editPrepayment.setText(prepayment)
            }
            setPrepaymentUseCase.execute(
                state.id, state.calendar,prepayment.toInt()
            )
        }
        binding.buttonSalary.setOnClickListener {
            var salary = binding.editSalary.text.toString()
            if(salary.isEmpty()){
                salary = "0"
                binding.editSalary.setText(salary)
            }
            setSalaryUseCase.execute(
                state.id, state.calendar, salary.toInt()
            )
        }
        binding.buttonAward.setOnClickListener {
            var award = binding.editAward.text.toString()
            if(award.isEmpty()){
                award = "0"
                binding.editAward.setText(award)
            }
            setAwardUseCase.execute(
                state.id, state.calendar, award.toInt()
            )
        }
        return AlertDialog.Builder(requireContext())
            .setTitle("\n")
            .setView(binding.root)
            .setPositiveButton("Ок") { _, _ ->
                listener.invoke()
            }
            .create()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}