package ru.lostgirl.mymapapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.lostgirl.mymapapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentMapBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentMapBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(fragmentMapBinding.root)
    }
}