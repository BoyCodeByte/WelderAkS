package com.boycodebyte.welderaks.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.boycodebyte.welderaks.databinding.ActivitiLoginBinding


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivitiLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitiLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}