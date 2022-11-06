package com.boycodebyte.welderaks.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.boycodebyte.welderaks.MainActivity
import com.boycodebyte.welderaks.data.models.LoginParam
import com.boycodebyte.welderaks.data.repositories.LoginUserRepository
import com.boycodebyte.welderaks.data.storage.FirebaseLoginUserStorage
import com.boycodebyte.welderaks.databinding.ActivitiRegisterBinding
import java.util.*
import kotlin.math.abs


class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivitiRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitiRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.register.setOnClickListener {
            val param = LoginParam(
                login = binding.login.text.toString(),
                password = binding.password.text.toString(),
                id = abs(Random().nextInt())
            )
            val loginUserRepository = LoginUserRepository(FirebaseLoginUserStorage())
            loginUserRepository.addLoginUser(param)
            val toast = Toast.makeText(
                applicationContext,
                "Пользователь добавлен", Toast.LENGTH_SHORT
            )
            toast.show()
            startActivity(Intent(this,MainActivity::class.java))
        }

    }

}