package com.boycodebyte.welderaks.ui.employers.detailsprofile

import android.R
import android.content.res.Resources.Theme
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.boycodebyte.welderaks.databinding.FragmentProfileDetailsBinding

class ProfileDetailsFragment: Fragment() {

    private var _binding: FragmentProfileDetailsBinding?=null
    private lateinit var layoutManager: LayoutManager
    private lateinit var profileDetailsViewModel: ProfileDetailsViewModel
    private lateinit var currentLogin: String

    private val binding get() = _binding!!

    val args: ProfileDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileDetailsViewModel=ViewModelProvider(this).get(ProfileDetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding=FragmentProfileDetailsBinding.inflate(inflater,container,false)

        val amount=args.idProfile

        val adapter = ArrayAdapter<String>(requireContext(), R.layout.simple_spinner_item)
        binding.spinnerAccountType.adapter=adapter


        profileDetailsViewModel.accountType.observe(viewLifecycleOwner){list->
            adapter.clear()
            adapter.addAll(list)
        }

        profileDetailsViewModel.profile.observe(viewLifecycleOwner) {
            binding.profileNameEdit.setText(it.name)
            binding.profileSurnameEdit.setText(it.surname)
            binding.profileBirthdateEdit.setText(it.dateOfBirth)
            binding.profileJobTitleEdit.setText(it.jobTitle)
            binding.profilePhoneNumberEdit.setText(it.phoneNumber)

            var account: String = it.accountType.toString()
            var position: Int = 0
            when (account) {
                "GENERAL" -> position
                "MASTER" -> position = 1
                "WORKER" -> position = 2
            }
            binding.spinnerAccountType.setSelection(position)
            binding.rateEdit.setText(it.rate.toString())///????????????????????
            binding.profileLoginEdit.setText(it.login)
            currentLogin = it.login
            binding.profilePasswordEdit.setText(it.password)
        }

        profileDetailsViewModel.loadProfile(amount)

        binding.updateProfile.setOnClickListener {
            if(binding.rateEdit.text.isNotEmpty()){
                if(currentLogin != binding.profileLoginEdit.text.toString()) {
                    if (!profileDetailsViewModel.checkLogin(binding.profileLoginEdit.text.toString())) {
                        val toast = Toast.makeText(
                            requireContext(),
                            "Такой логин уже существует: ${binding.profileLoginEdit.text}",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                        return@setOnClickListener
                    }
                }
            profileDetailsViewModel.saveChange(binding.profileNameEdit.text.toString(),
            binding.profileSurnameEdit.text.toString(),
            binding.profileBirthdateEdit.text.toString(),
            binding.profileJobTitleEdit.text.toString(),
            binding.profilePhoneNumberEdit.text.toString(),
            binding.rateEdit.text.toString().toInt(),
            binding.profileLoginEdit.text.toString(),
            binding.profilePasswordEdit.text.toString(),
            binding.spinnerAccountType.selectedItem.toString())
            findNavController().popBackStack()
            }else {
                val toast=Toast.makeText(requireContext(),"Заполните поле: ${binding.rateText.text}",Toast.LENGTH_SHORT)
                toast.show()
                binding.rateEdit.background.setTint(resources.getColor(R.color.holo_red_light,resources.newTheme()))
                return@setOnClickListener}
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager=LinearLayoutManager(activity)
    }
}