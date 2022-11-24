package com.boycodebyte.welderaks.ui.finance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.databinding.FragmentFinanceBinding


class FinanceFragment : Fragment() {

    val storage = FirebaseStorage()

    private var _binding: FragmentFinanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val financeViewModel =
            ViewModelProvider(this).get(FinanceViewModel::class.java)

        _binding = FragmentFinanceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        financeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}