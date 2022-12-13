package com.boycodebyte.welderaks.ui.employers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.databinding.FragmentEmployersBinding

class EmployersFragment : Fragment() {

    private var _binding: FragmentEmployersBinding? = null
    private val binding get() = _binding!!
    private lateinit var employersViewModel: EmployersViewModel
    private lateinit var layoutManager: LayoutManager
    private lateinit var adapter: ProfileRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        employersViewModel =
            ViewModelProvider(this).get(EmployersViewModel::class.java)

        _binding = FragmentEmployersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter= ProfileRecyclerAdapter(object : ProfileActionListeners{

            override fun onProfileDetails(profile: Profile) {
                val action=EmployersFragmentDirections.actionNavigationEmployersToProfileDetailsFragment(profile.id)
                view!!.findNavController().navigate(action)
            }

            override fun onProfileDelete(profile: Profile) {
                employersViewModel.deleteProfile(profile)
            }
        })

        binding.recyclerView.adapter=adapter

        employersViewModel.profiles.observe(viewLifecycleOwner) {
            adapter.users=it
        }

        binding.addProfile.setOnClickListener {
            val action=EmployersFragmentDirections.actionNavigationEmployersToAdditionProfileFragment()
            it.findNavController().navigate(action)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        employersViewModel.updateProfileList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager=LinearLayoutManager(activity)
        binding.recyclerView.layoutManager=layoutManager
    }
}