package com.example.authfirebase.ui.view.tablayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.authfirebase.R
import com.example.authfirebase.databinding.ActivityTabLayoutBinding
import com.example.authfirebase.ui.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class TabLayoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTabLayoutBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTabLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        with(binding){
            viewPagerInformation.adapter = viewPagerAdapter

            TabLayoutMediator(tabLayoutInformation, viewPagerInformation){ tab, position ->
                when(position){
                    0 -> tab.text = "Tab Pertama"
                    1 -> tab.text = "Tab Kedua"
                }
            }.attach()
        }
    }
}