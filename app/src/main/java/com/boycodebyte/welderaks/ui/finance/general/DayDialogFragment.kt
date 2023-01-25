package com.boycodebyte.welderaks.ui.finance.general

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.boycodebyte.welderaks.databinding.CalendarDialogBinding

import com.boycodebyte.welderaks.ui.finance.DayDialogState

typealias DialogListener = (state: DayDialogState) -> Unit
typealias DeleteListener = () -> Unit

class DayDialogFragment : DialogFragment() {

    private var state: DayDialogState = DayDialogState()
    var acceptListener: DialogListener = { }
    var deleteListener: DeleteListener = { }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBinding = CalendarDialogBinding.inflate(layoutInflater)
        dialogBinding.textHours.setText(state.hours)
        dialogBinding.textRate.setText(state.rate)
        dialogBinding.textCoefficient.setText(state.coefficient)
        dialogBinding.textDescription.setText(state.description)
        return AlertDialog.Builder(requireContext())
            .setTitle(state.title)
            .setView(dialogBinding.root)
            .setPositiveButton("Ок") { _, _ ->
                var hours = dialogBinding.textHours.text.toString()
                if(hours.isEmpty()){
                    hours = "0"
                }else{
                    if(hours.toInt() >24){
                        hours = "24"
                    }
                }
                var rate = dialogBinding.textRate.text.toString()
                if(rate.isEmpty()){
                    rate = "0"
                }
                var coefficient = dialogBinding.textCoefficient.text.toString()
                if(coefficient.isEmpty()){
                    coefficient = "0"
                }
                var description = dialogBinding.textDescription.text.toString()
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