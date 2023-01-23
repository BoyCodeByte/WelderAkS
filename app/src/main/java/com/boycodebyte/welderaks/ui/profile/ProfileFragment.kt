package com.boycodebyte.welderaks.ui.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import com.boycodebyte.welderaks.databinding.FragmentProfileBinding
import com.boycodebyte.welderaks.getProfile
import com.boycodebyte.welderaks.removeProfile
import com.boycodebyte.welderaks.ui.login.LoginActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        viewModel.profileLiveData.observe(viewLifecycleOwner) {
            binding.textViewName.text = "Имя:  ${it.name}"
            binding.textViewSurname.text = "Фамилия:  ${it.surname}"
            binding.textViewDateOfBirth.text = "Дата рождения:  ${it.dateOfBirth}"
            binding.textViewJobTitle.text = "Профессия:  ${it.jobTitle}"
            binding.textViewRate.text = "Почасовая ставка:  ${it.rate}"
        }
        viewModel.profile = requireActivity().getProfile()
        binding.buttonLogout.setOnClickListener {
            val listener = DialogInterface.OnClickListener{_, which ->
                when(which){
                    DialogInterface.BUTTON_POSITIVE -> logout()
                }
            }
            val dialog = AlertDialog.Builder(requireContext())
                .setMessage("Выйти из профиля?")
                .setPositiveButton("Да", listener)
                .setNegativeButton("Нет") { _, _ -> }
                .create()
            dialog.show()
        }
        return binding.root
    }

    private fun logout() {
        viewModel.logout(requireContext())
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        activity?.removeProfile()
        activity?.finish()
    }

}