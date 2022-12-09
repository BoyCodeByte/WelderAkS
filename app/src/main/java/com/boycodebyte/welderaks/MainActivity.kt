package com.boycodebyte.welderaks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.storage.PROFILES_CHILD
import com.boycodebyte.welderaks.databinding.ActivityMainBinding
import com.boycodebyte.welderaks.ui.instruments.detailsinstrument.InstrumentDetailsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView
        navView.itemIconTintList = null

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        Thread{
            val myRef = FirebaseDatabase.getInstance().reference
            val res = myRef.get()
            while (!res.isComplete){}
            if(res.isSuccessful){
                res.result.children.forEach{ println(it.key)}
            }
        }.start()

    }

}