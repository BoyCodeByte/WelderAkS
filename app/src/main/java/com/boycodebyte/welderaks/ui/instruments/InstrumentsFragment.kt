package com.boycodebyte.welderaks.ui.instruments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.boycodebyte.welderaks.databinding.FragmentInstrumentsBinding


class InstrumentsFragment : Fragment() {

    private var _binding: FragmentInstrumentsBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val instrumentsViewModel =
            ViewModelProvider(this).get(InstrumentsViewModel::class.java)

        _binding = FragmentInstrumentsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        instrumentsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}