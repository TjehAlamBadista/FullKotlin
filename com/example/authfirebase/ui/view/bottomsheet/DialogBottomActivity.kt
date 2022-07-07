package com.example.authfirebase.ui.view.bottomsheet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.authfirebase.R
import com.example.authfirebase.databinding.ActivityDialogBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class DialogBottomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDialogBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBottomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnShow.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this@DialogBottomActivity, R.style.bottomSheetDialogTheme)

            val bottomSheetView = LayoutInflater.from(applicationContext)
                .inflate(R.layout.bottom_sheet, findViewById(R.id.bottom_sheet))

            bottomSheetView.findViewById<View>(R.id.btnShare).setOnClickListener {
                Toast.makeText(this@DialogBottomActivity, "...SHARE CHARACTER...", Toast.LENGTH_LONG).show()
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }
    }
}