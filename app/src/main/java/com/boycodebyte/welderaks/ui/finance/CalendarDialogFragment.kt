package com.boycodebyte.welderaks.ui.finance

import android.app.AlertDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.boycodebyte.welderaks.data.models.CalendarData.*
import com.boycodebyte.welderaks.databinding.CalendarDialogBinding

class CalendarDialogFragment: DialogFragment() {

    private var title: String = ""
    var hours: String = ""
    var coefficient: String = ""
    var description: String = ""


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBinding = CalendarDialogBinding.inflate(layoutInflater)
        dialogBinding.editTextHours.setText(hours)
        dialogBinding.editTextCoefficient.setText(coefficient)
        dialogBinding.editTextDescription.setText(description)
        println("create")
        return AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(dialogBinding.root)
            .setPositiveButton("Ок") {_,_ ->}
            .setNegativeButton("Отмена") {_,_ ->}
            .create()
    }

    fun show(manager: FragmentManager, tag: String, state: CalendarDialogState) {
        super.show(manager, tag)
        title = state.title
        hours = state.hours
        coefficient = state.coefficient
        description = state.description
    }

    companion object{
        const val TAG ="CalendarDialogFragment"
    }
}