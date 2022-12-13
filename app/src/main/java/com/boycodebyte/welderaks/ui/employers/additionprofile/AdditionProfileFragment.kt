package com.boycodebyte.welderaks.ui.employers.additionprofile

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.boycodebyte.welderaks.databinding.FragmentAdditionProfileBinding
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener

class AdditionProfileFragment: Fragment() {

    private var _binding:FragmentAdditionProfileBinding?=null
    private lateinit var additionProfileViewModel: AdditionProfileViewModel
    private lateinit var layoutManager: LayoutManager

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        additionProfileViewModel= AdditionProfileViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding=FragmentAdditionProfileBinding.inflate(inflater,container,false)

        val adapter = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item)
        binding.spinnerAccountType.adapter=adapter


        additionProfileViewModel.accountType.observe(viewLifecycleOwner){list->
            adapter.clear()
            adapter.addAll(list)
        }


        binding.addProfile.setOnClickListener {
            additionProfileViewModel.add(binding.profileNameEdit.text.toString(),binding.profileSurnameEdit.text.toString(),
            binding.spinnerAccountType.selectedItem.toString(),binding.profileBirthdateEdit.text.toString(),
            binding.profileJobTitleEdit.text.toString(),binding.profileLoginEdit.text.toString(),
                binding.profilePasswordEdit.text.toString(),binding.profilePhoneNumberEdit.text.toString())
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
        layoutManager=LinearLayoutManager(activity)
    }
}