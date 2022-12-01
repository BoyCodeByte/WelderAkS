package com.boycodebyte.welderaks.ui.finance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.databinding.FragmentFinanceBinding
import com.boycodebyte.welderaks.ui.finance.calendar.CalendarAdapter


class FinanceFragment : Fragment() {

    val storage = FirebaseStorage()

    private var _binding: FragmentFinanceBinding? = null
    private val binding get() = _binding!!

    lateinit var viewPager: ViewPager2


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val financeViewModel = ViewModelProvider(this).get(FinanceViewModel::class.java)
        _binding = FragmentFinanceBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val mViewPager: ViewPager2 = binding.pager
        val adapter = CalendarAdapter(requireContext())
        mViewPager.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}