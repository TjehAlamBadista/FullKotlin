package com.example.authfirebase.ui.view.bottomsheet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.authfirebase.R
import com.example.authfirebase.databinding.ActivityDialogAlertBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogAlertActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDialogAlertBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogAlertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnShow.setOnClickListener {
            showCurvedAlertDialog()
        }
    }

    private fun showCurvedAlertDialog() {
        val dialog: AlertDialog = MaterialAlertDialogBuilder(
            this,
            R.style.roundedMaterialDialog)
            .setView(R.layout.item_dialog_alert)
            .show()

        dialog.findViewById<View>(R.id.imgCloseDialog)!!.setOnClickListener {
            dialog.dismiss()
        }
    }
}