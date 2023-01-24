package com.boycodebyte.welderaks.ui.finance.worker

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.boycodebyte.welderaks.databinding.CalendarDialogBinding
import com.boycodebyte.welderaks.databinding.CalendarDialogWorkerBinding
import com.boycodebyte.welderaks.ui.finance.DayDialogState

class DayDialogFragment : DialogFragment() {

    private var state: DayDialogState = DayDialogState()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBinding = CalendarDialogWorkerBinding.inflate(layoutInflater)
        dialogBinding.editTextHours.text = state.hours
        dialogBinding.editTextRate.text = state.rate
        dialogBinding.editTextCoefficient.text = state.coefficient
        dialogBinding.editTextDescription.text = state.description
        return AlertDialog.Builder(requireContext())
            .setTitle(state.title)
            .setView(dialogBinding.root)
            .setPositiveButton("Ок") { _, _ -> }
            .create()
    }

    fun show(manager: FragmentManager, tag: String, state: DayDialogState) {
        super.show(manager, tag)
        this.state = state
    }

    companion object {
        const val TAG = "CalendarDialogFragment"
    }
}