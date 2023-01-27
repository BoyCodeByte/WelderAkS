package com.boycodebyte.welderaks.ui.instruments.general.additioninstrument

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.models.getEmptyProfile
import com.boycodebyte.welderaks.databinding.FragmentAdditionInstrumentBinding

class AdditionInstrumentFragment:Fragment() {

    private var _binding:FragmentAdditionInstrumentBinding?=null
    private lateinit var additionInstrumentViewModel: AdditionInstrumentViewModel
    private lateinit var layout: RecyclerView.LayoutManager

    var currentInstrument: Instrument?=null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        additionInstrumentViewModel=ViewModelProvider(this).get(AdditionInstrumentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentAdditionInstrumentBinding.inflate(inflater,container,false)

        val adapter = ArrayAdapter<Profile>(requireContext(), R.layout.simple_spinner_item)
        binding.spinnerAddInstrument.adapter=adapter
        additionInstrumentViewModel.profile.observe(viewLifecycleOwner){ list ->
            adapter.clear()
            adapter.addAll(list)
            val profile=list.find { it.id== currentInstrument?.idOfProfile ?: getEmptyProfile() }
            binding.spinnerAddInstrument.setSelection(list.indexOf(profile))
        }

        binding.addingInstrument.setOnClickListener {
            additionInstrumentViewModel.addition(binding.spinnerAddInstrument.selectedItemPosition,
            binding.textDescription.text.toString(),binding.editNameInstrument.text.toString())
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layout=LinearLayoutManager(activity)
    }
}