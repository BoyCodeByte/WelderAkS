package com.boycodebyte.welderaks.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.boycodebyte.welderaks.MainActivity
import com.boycodebyte.welderaks.data.models.LoginParam
import com.boycodebyte.welderaks.data.repositories.LoginParamRepository
import com.boycodebyte.welderaks.data.repositories.ProfileRepository
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.data.storage.PrefStorage
import com.boycodebyte.welderaks.databinding.ActivitiLoginBinding
import com.boycodebyte.welderaks.domain.usecase.LoginUseCase
import com.boycodebyte.welderaks.domain.usecase.SetLoginParamUseCase
import com.boycodebyte.welderaks.setProfile


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivitiLoginBinding
    private val setLoginParamRepository =
        SetLoginParamUseCase(LoginParamRepository((PrefStorage(this))))
    private val loginUseCase = LoginUseCase(ProfileRepository(FirebaseStorage()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitiLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginButton.setOnClickListener {
            val login = binding.loginEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
            val param = LoginParam(login, password)
            try {
                val profile = loginUseCase.execute(param)
                setLoginParamRepository.execute(LoginParam(login, password))
                setProfile(profile)
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                val toast = Toast.makeText(
                    this@LoginActivity,
                    "Неверно введен логин или пароль",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }
    }
}