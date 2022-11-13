package com.boycodebyte.welderaks.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.boycodebyte.welderaks.MainActivity
import com.boycodebyte.welderaks.data.models.LoginParam
import com.boycodebyte.welderaks.data.repositories.LoginParamRepository
import com.boycodebyte.welderaks.data.storage.PrefStorage
import com.boycodebyte.welderaks.databinding.ActivitiLoginBinding
import com.boycodebyte.welderaks.domain.usecase.SetLoginParamUseCase


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivitiLoginBinding
    private val setLoginParamRepository = SetLoginParamUseCase(LoginParamRepository((PrefStorage(this))))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitiLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginButton.setOnClickListener{
            setLoginParamRepository.execute(LoginParam(login =binding.loginEdit.text.toString(),
            password = binding.passwordEdit.text.toString()))
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}