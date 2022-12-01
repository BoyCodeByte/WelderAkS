package com.boycodebyte.welderaks.ui.instruments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.boycodebyte.welderaks.R
import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.databinding.FragmentInstrumentsBinding
import com.boycodebyte.welderaks.ui.instruments.detailsinstrument.INSTRUMENT_KEY
import com.boycodebyte.welderaks.ui.instruments.detailsinstrument.InstrumentDetailsViewModel
import kotlin.concurrent.fixedRateTimer


class InstrumentsFragment : Fragment() {

    private var _binding: FragmentInstrumentsBinding? = null//old
    private lateinit var adapter: InstrumentRecyclerAdapter
    private lateinit var layoutManager: LayoutManager
    private lateinit var instrumentsViewModel:InstrumentsViewModel


    private val binding get() = _binding!!//old


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        instrumentsViewModel =
            ViewModelProvider(this).get(InstrumentsViewModel::class.java)

        _binding = FragmentInstrumentsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        adapter = InstrumentRecyclerAdapter(object : InstrumentActionListeners {
            override fun onInstrumentDelete(instrument: Instrument) {
                instrumentsViewModel.deleteInstrument(instrument)
            }

            override fun onInstrumentDetails(instrument: Instrument) {
                println("tool:${instrument.id}")
                val action=InstrumentsFragmentDirections.actionNavigationInstrumentsToInstrumentDetailsFragment(instrument.id)
                view!!.findNavController().navigate(action)
            }
        })

        binding.recyclerView.adapter = adapter

        instrumentsViewModel.instrumentLiveData.observe(viewLifecycleOwner) {
            adapter.instruments = it
        }

        instrumentsViewModel.profileLiveData.observe(viewLifecycleOwner){
            adapter.users=it
        }

        return root
    }

    override fun onStart() {
        super.onStart()
        instrumentsViewModel.updateInstrumentsList()
        instrumentsViewModel.updateProfilesList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager=LinearLayoutManager(activity)
        binding.recyclerView.layoutManager=layoutManager
    }
}