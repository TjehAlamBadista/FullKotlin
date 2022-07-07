package com.example.authfirebase.ui.view.bottomsheet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.authfirebase.R
import com.example.authfirebase.databinding.ActivityBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPresistent.setOnClickListener {
            Intent(this@BottomSheetActivity, PresistentActivity::class.java).also { startActivity(it) }
        }

        binding.btnModal.setOnClickListener {
//            Intent(this@MainActivity, ModalActivity::class.java).also { startActivity(it) }
            val view = layoutInflater.inflate(R.layout.item_bottom_sheet, null)
            val dialog = BottomSheetDialog(this)
            dialog.setContentView(view)
            dialog.show()
        }

        binding.btnDialog.setOnClickListener {
            Intent(this@BottomSheetActivity, DialogAlertActivity::class.java).also { startActivity(it) }
        }

        binding.btnBottomDialog.setOnClickListener {
            Intent(this@BottomSheetActivity, DialogBottomActivity::class.java).also { startActivity(it) }
        }
    }
}