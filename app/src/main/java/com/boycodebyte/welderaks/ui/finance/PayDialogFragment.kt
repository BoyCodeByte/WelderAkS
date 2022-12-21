package com.boycodebyte.welderaks.ui.finance

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.boycodebyte.welderaks.databinding.FragmentPayBinding


class PayDialogFragment(private val state: PayDialogState) : DialogFragment() {

    private lateinit var viewModel: PayViewModel
    private var _binding: FragmentPayBinding? = null
    private val binding get() = _binding!!


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = ViewModelProvider(this)[PayViewModel::class.java]
        _binding = FragmentPayBinding.inflate(layoutInflater)
        binding.editPrepayment.setText(state.paymentState.prepayment)
        binding.editSalary.setText(state.paymentState.salary)
        binding.editAward.setText(state.paymentState.award)
        binding.buttonPrepayment.setOnClickListener{

        }
        binding.buttonSalary.setOnClickListener{

        }
        binding.buttonAward.setOnClickListener{

        }
        return AlertDialog.Builder(requireContext())
            .setTitle("\n")
            .setView(binding.root)
            .setPositiveButton("Ок"){_,_->}
            .create()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}