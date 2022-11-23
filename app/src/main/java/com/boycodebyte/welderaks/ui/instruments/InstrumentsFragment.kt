package com.boycodebyte.welderaks.ui.instruments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.databinding.FragmentInstrumentsBinding


class InstrumentsFragment : Fragment() {

    private var _binding: FragmentInstrumentsBinding? = null//old
    private lateinit var adapter: InstrumentRecyclerAdapter
    private lateinit var layoutManager: LayoutManager
    private lateinit var instrumentsViewModel:InstrumentsViewModel


    private val binding get() = _binding!!//old

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val instrumentsViewModel =
//            ViewModelProvider(this).get(InstrumentsViewModel::class.java)
//
//        _binding = FragmentInstrumentsBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.textNotifications
//        instrumentsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        return root
//    }

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
                TODO("Not yet implemented")
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