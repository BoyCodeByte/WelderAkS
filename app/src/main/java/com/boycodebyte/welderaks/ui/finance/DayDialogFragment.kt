package com.boycodebyte.welderaks.ui.finance

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.boycodebyte.welderaks.databinding.CalendarDialogBinding

typealias DialogListener =(state: DayDialogState) -> Unit

class DayDialogFragment: DialogFragment() {

    private var state: DayDialogState = DayDialogState()
    var listener: DialogListener = { }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBinding = CalendarDialogBinding.inflate(layoutInflater)
        dialogBinding.editTextHours.setText(state.hours)
        dialogBinding.editTextRate.setText(state.rate)
        dialogBinding.editTextCoefficient.setText(state.coefficient)
        dialogBinding.editTextDescription.setText(state.description)
        return AlertDialog.Builder(requireContext())
            .setTitle(state.title)
            .setView(dialogBinding.root)
            .setPositiveButton("Ок") {_,_ ->
                listener.invoke(
                    DayDialogState(
                        hours = dialogBinding.editTextHours.text.toString(),
                        rate = dialogBinding.editTextRate.text.toString(),
                        coefficient = dialogBinding.editTextCoefficient.text.toString(),
                        description = dialogBinding.editTextDescription.text.toString()
                )
                )
                dismiss()
            }
            .setNegativeButton("Отмена") {_,_ ->}
            .create()
    }

    fun show(manager: FragmentManager, tag: String, state: DayDialogState) {
        super.show(manager, tag)
        this.state = state
    }

    companion object{
        const val TAG ="CalendarDialogFragment"
    }
}