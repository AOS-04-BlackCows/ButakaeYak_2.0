package com.example.yactong.ui.take

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.yactong.R
import com.example.yactong.databinding.ActivityTakeAddBinding

class TakeAddActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTakeAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTakeAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            vp2Add.adapter = TakeViewPagerAdapter(this@TakeAddActivity)
            vp2Add.isUserInputEnabled = false
        }
    }
}