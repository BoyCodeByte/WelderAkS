package com.boycodebyte.welderaks.ui.finance

import android.app.AlertDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.repositories.CalendarDataRepository
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.databinding.FragmentPayBinding
import com.boycodebyte.welderaks.domain.usecase.SetAwardUseCase
import com.boycodebyte.welderaks.domain.usecase.SetPrepaymentUseCase
import com.boycodebyte.welderaks.domain.usecase.SetSalaryUseCase


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
            setPrepaymentUseCase.execute(
                state.id, state.calendar, binding.editPrepayment.text.toString().toInt()
            )
        }
        binding.buttonSalary.setOnClickListener {
            setSalaryUseCase.execute(
                state.id, state.calendar, binding.editSalary.text.toString().toInt()
            )
        }
        binding.buttonAward.setOnClickListener {
            setAwardUseCase.execute(
                state.id, state.calendar, binding.editAward.text.toString().toInt()
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