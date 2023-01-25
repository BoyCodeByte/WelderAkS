package com.boycodebyte.welderaks.ui.finance.worker

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.boycodebyte.welderaks.R
import com.boycodebyte.welderaks.databinding.CalendarDialogBinding
import com.boycodebyte.welderaks.databinding.CalendarDialogWorkerBinding
import com.boycodebyte.welderaks.ui.finance.DayDialogState

class DayDialogFragment : DialogFragment() {

    private var state: DayDialogState = DayDialogState()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBinding = CalendarDialogWorkerBinding.inflate(layoutInflater)
        dialogBinding.textHours.text = getString(R.string.hours, state.hours)
        dialogBinding.textRate.text = getString(R.string.rate, state.rate)
        dialogBinding.textCoefficient.text = getString(R.string.coefficient, state.coefficient)
        dialogBinding.textDescription.text = getString(R.string.description, state.description)
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