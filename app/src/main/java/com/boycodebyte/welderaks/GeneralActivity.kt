package com.boycodebyte.welderaks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.boycodebyte.welderaks.databinding.ActivityGeneralBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class GeneralActivity : AppCompatActivity(){

    private lateinit var binding: ActivityGeneralBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityGeneralBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView
        navView.itemIconTintList = null

        val navController = findNavController(R.id.nav_host_fragment_activity_general)
        navView.setupWithNavController(navController)

    }
}