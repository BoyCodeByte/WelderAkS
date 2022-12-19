package com.boycodebyte.welderaks.ui.finance

import android.app.AlertDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.boycodebyte.welderaks.databinding.FragmentPayBinding


class PayDialogFragment : DialogFragment() {

    private lateinit var viewModel: PayViewModel
    private var _binding: FragmentPayBinding? = null
    private val binding get() = _binding!!

    private var state: PaymentState? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = ViewModelProvider(this)[PayViewModel::class.java]
        _binding = FragmentPayBinding.inflate(layoutInflater)
        binding.editPrepayment.setText(state?.prepayment)
        binding.editSalary.setText(state?.salary)
        binding.editAward.setText(state?.award)
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