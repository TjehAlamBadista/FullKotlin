package com.example.authfirebase.ui.view.bottomsheet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.authfirebase.R
import com.example.authfirebase.databinding.ActivityPresistentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class PresistentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPresistentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPresistentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomSheetBehavior: BottomSheetBehavior<*>?
        val expand = binding.btnExpand
        val collapse = binding.btnCollapse
        val bottomSheet: View = findViewById(R.id.bottom_sheet_presistent)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        expand.setOnClickListener {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }

        collapse.setOnClickListener {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }

        bottomSheet.setOnClickListener {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
    }
}