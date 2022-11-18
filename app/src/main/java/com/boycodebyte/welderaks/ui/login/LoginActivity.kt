package com.boycodebyte.welderaks.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.boycodebyte.welderaks.MainActivity
import com.boycodebyte.welderaks.domain.models.ErrorResult
import com.boycodebyte.welderaks.data.models.LoginParam
import com.boycodebyte.welderaks.domain.models.PendingResult
import com.boycodebyte.welderaks.domain.models.SuccessResult
import com.boycodebyte.welderaks.data.repositories.LoginParamRepository
import com.boycodebyte.welderaks.data.repositories.LoginUsersRepository
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.data.storage.PrefStorage
import com.boycodebyte.welderaks.databinding.ActivitiLoginBinding
import com.boycodebyte.welderaks.domain.usecase.LoginUseCase
import com.boycodebyte.welderaks.domain.usecase.SetLoginParamUseCase


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivitiLoginBinding
    private val setLoginParamRepository =
        SetLoginParamUseCase(LoginParamRepository((PrefStorage(this))))
    private val loginUseCase = LoginUseCase(LoginUsersRepository(FirebaseStorage()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitiLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginButton.setOnClickListener {
            val login = binding.loginEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
            val param = LoginParam(login, password)
            loginUseCase.execute(param) {
                when (it) {
                    is SuccessResult -> {
                        setLoginParamRepository.execute(LoginParam(login, password))
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is ErrorResult -> {
                        val toast = Toast.makeText(
                            this@LoginActivity,
                            "Неверно введен логин или пароль",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                    is PendingResult -> {

                    }
                }
            }
        }
    }
}