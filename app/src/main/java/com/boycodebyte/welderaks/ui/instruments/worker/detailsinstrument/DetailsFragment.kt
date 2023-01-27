package com.boycodebyte.welderaks.ui.instruments.worker.detailsinstrument

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.boycodebyte.welderaks.R
import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.databinding.FragmentInstrumentDetailsWorkerBinding


class DetailsFragment : Fragment() {

    private var _binding: FragmentInstrumentDetailsWorkerBinding? = null
    private lateinit var layout: LayoutManager
    private lateinit var instrumentDetailsViewModel: InstrumentDetailsViewModel

    private val binding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()
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
        _binding = FragmentInstrumentDetailsWorkerBinding.inflate(inflater, container, false)

        val idOfInstrument = args.identification

        binding.photoImage.setImageResource(R.drawable.ic_instruments)
        instrumentDetailsViewModel.instrumentDetails.observe(viewLifecycleOwner, Observer {
            currentInstrument = it
            binding.nameInstrument.text = it.name
            binding.instrumentDetails.text = it.description
        })
        instrumentDetailsViewModel.profile.observe(viewLifecycleOwner) {
            binding.nameProfile.text = it.toString()
        }

        instrumentDetailsViewModel.loadInstrument(idOfInstrument)

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