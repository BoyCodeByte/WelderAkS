package com.boycodebyte.welderaks.ui.finance

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.boycodebyte.welderaks.databinding.CalendarDialogBinding

typealias DialogListener = (state: DayDialogState) -> Unit
typealias DeleteListener = () -> Unit

class DayDialogFragment : DialogFragment() {

    private var state: DayDialogState = DayDialogState()
    var acceptListener: DialogListener = { }
    var deleteListener: DeleteListener = { }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBinding = CalendarDialogBinding.inflate(layoutInflater)
        dialogBinding.editTextHours.setText(state.hours)
        dialogBinding.editTextRate.setText(state.rate)
        dialogBinding.editTextCoefficient.setText(state.coefficient)
        dialogBinding.editTextDescription.setText(state.description)
        return AlertDialog.Builder(requireContext())
            .setTitle(state.title)
            .setView(dialogBinding.root)
            .setPositiveButton("Ок") { _, _ ->
                var hours = dialogBinding.editTextHours.text.toString()
                if(hours.isEmpty()){
                    hours = "0"
                }else{
                    if(hours.toInt() >24){
                        hours = "24"
                    }
                }
                var rate = dialogBinding.editTextRate.text.toString()
                if(rate.isEmpty()){
                    rate = "0"
                }
                var coefficient = dialogBinding.editTextCoefficient.text.toString()
                if(coefficient.isEmpty()){
                    coefficient = "0"
                }
                var description = dialogBinding.editTextDescription.text.toString()
                acceptListener.invoke(
                    DayDialogState(
                        hours = hours,
                        rate = rate,
                        coefficient = coefficient,
                        description = description
                    )
                )
                dismiss()
            }
            .setNegativeButton("Отмена") { _, _ -> }
            .setNeutralButton("Удалить") { _, _ ->
                deleteListener.invoke()
            }
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