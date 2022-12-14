package com.boycodebyte.welderaks.ui.employers.detailsprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        profileDetailsViewModel.profile.observe(viewLifecycleOwner,{
            binding.profileNameEdit.setText(it.name)
            binding.profileSurnameEdit.setText(it.surname)
            binding.profileBirthdateEdit.setText(it.dateOfBirth)
            binding.profileJobTitleEdit.setText(it.jobTitle)
            binding.profilePhoneNumberEdit.setText(it.phoneNumber)
        })

        profileDetailsViewModel.loadProfile(amount)

        binding.updateProfile.setOnClickListener {
            profileDetailsViewModel.saveChange(binding.profileNameEdit.text.toString(),
            binding.profileSurnameEdit.text.toString(),
            binding.profileBirthdateEdit.text.toString(),
            binding.profileJobTitleEdit.text.toString(),
            binding.profilePhoneNumberEdit.text.toString())
            findNavController().popBackStack()
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