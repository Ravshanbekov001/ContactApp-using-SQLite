package com.example.contactappusingsqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.contactappusingsqlite.databinding.ActivityMainBinding
import com.example.contactappusingsqlite.fragments.MainFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}