package com.boycodebyte.welderaks.ui.instruments.detailsinstrument

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.NavArgument
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.boycodebyte.welderaks.R
import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.models.getEmptyProfile
import com.boycodebyte.welderaks.databinding.FragmentInstrumentDetailsBinding


class InstrumentDetailsFragment : Fragment() {

    private var _binding: FragmentInstrumentDetailsBinding? = null
    private lateinit var layout: LayoutManager
    private lateinit var instrumentDetailsViewModel: InstrumentDetailsViewModel

    private val binding get() = _binding!!
    val args: InstrumentDetailsFragmentArgs by navArgs()
    var currentInstrument: Instrument? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instrumentDetailsViewModel =
            ViewModelProvider(this).get(InstrumentDetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstrumentDetailsBinding.inflate(inflater, container, false)

        val amount = args.identification

        instrumentDetailsViewModel.instrumentDetails.observe(viewLifecycleOwner, Observer {
            currentInstrument = it
            binding.nameInstrument.text = it.name
            binding.instrumentDetails.setText(it.description)
        })
        instrumentDetailsViewModel.loadInstrument(amount)

        val adapter = ArrayAdapter<Profile>(requireContext(), android.R.layout.simple_spinner_item)
        binding.spinner.adapter = adapter
        instrumentDetailsViewModel.profile.observe(viewLifecycleOwner) { list ->
            adapter.clear()
            adapter.addAll(list)
            val profile = list.find { it.id == currentInstrument?.idOfProfile ?: getEmptyProfile() }
            binding.spinner.setSelection(list.indexOf(profile))
        }

        binding.photoImage.setImageResource(R.drawable.ic_instruments)

        binding.upDate.setOnClickListener {
            instrumentDetailsViewModel.saveChange(
                binding.spinner.selectedItemPosition,
                binding.instrumentDetails.text.toString()
            )
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layout = LinearLayoutManager(activity)
    }
}