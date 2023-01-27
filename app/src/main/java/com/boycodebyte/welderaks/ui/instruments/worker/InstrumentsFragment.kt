package com.boycodebyte.welderaks.ui.instruments.worker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.databinding.FragmentInstrumentsWorkerBinding
import com.boycodebyte.welderaks.getProfile


class InstrumentsFragment : Fragment() {

    private var _binding: FragmentInstrumentsWorkerBinding? = null
    private lateinit var adapter: InstrumentRecyclerAdapter
    private lateinit var currentProfile: Profile
    private lateinit var layoutManager: LayoutManager
    private lateinit var instrumentsViewModel: InstrumentsViewModel


    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        instrumentsViewModel =
            ViewModelProvider(this).get(InstrumentsViewModel::class.java)

        _binding = FragmentInstrumentsWorkerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        currentProfile = requireActivity().getProfile()!!


        adapter = InstrumentRecyclerAdapter(currentProfile, object : InstrumentActionListeners {
            override fun onInstrumentDetails(instrument: Instrument) {
                val action = InstrumentsFragmentDirections.actionInstrumentDetailsFragment(instrument.id)
                view!!.findNavController().navigate(action)
            }
        })

        binding.recyclerView.adapter = adapter

        instrumentsViewModel.instrumentLiveData.observe(viewLifecycleOwner) {
            adapter.instruments = it
            if(it.isEmpty()){
                showEmptyListInfo()
            } else{
                hideEmptyListInfo()
            }
        }
        return root
    }

    override fun onStart() {
        super.onStart()
        instrumentsViewModel.updateInstrumentsList(currentProfile.id)
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

    private fun showEmptyListInfo(){
        binding.textEmptyInstruments.visibility = View.VISIBLE
    }

    private fun hideEmptyListInfo(){
        binding.textEmptyInstruments.visibility = View.GONE
    }
}