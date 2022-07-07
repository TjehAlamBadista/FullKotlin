package com.example.authfirebase.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.authfirebase.databinding.ActivityUpdateEmailBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class UpdateEmailActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var updateEmailBinding: ActivityUpdateEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        updateEmailBinding = ActivityUpdateEmailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(updateEmailBinding.root)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        updateEmailBinding.layoutVerificationPassword.visibility = View.VISIBLE
        updateEmailBinding.layoutUpdateEmail.visibility = View.GONE
        updateEmailBinding.btnAuth.setOnClickListener {
            val pass = updateEmailBinding.etPassword.text.toString().trim()
            if (pass.isEmpty()){
                updateEmailBinding.etPassword.error = "Password Tidak Boleh Kosong !"
                updateEmailBinding.etPassword.requestFocus()
                return@setOnClickListener
            }

            user.let {
                val userCredential = EmailAuthProvider.getCredential(it?.email!!, pass)
                it.reauthenticate(userCredential).addOnCompleteListener { Task ->
                    when {
                        Task.isSuccessful -> {
                            updateEmailBinding.layoutVerificationPassword.visibility = View.GONE
                            updateEmailBinding.layoutUpdateEmail.visibility = View.VISIBLE
                        }
                                Task.exception is FirebaseAuthInvalidCredentialsException -> {
                            updateEmailBinding.etPassword.error = "Password Salah"
                            updateEmailBinding.etPassword.requestFocus()
                        }
                        else -> {
                            Toast.makeText(applicationContext, "${Task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            updateEmailBinding.btnUpdate.setOnClickListener updateEmail@ { _ ->
                val email = updateEmailBinding.etEmail.text.toString().trim()
                if (email.isEmpty()){
                    updateEmailBinding.etEmail.error = "Email Tidak Boleh Kosong"
                    updateEmailBinding.etEmail.requestFocus()
                    return@updateEmail
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    updateEmailBinding.etEmail.error = "Email Tidak valid"
                    updateEmailBinding.etEmail.requestFocus()
                    return@updateEmail
                }
                user?.let {
                    user.updateEmail(email).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(applicationContext, "Email Berhasil Diupdate", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(applicationContext, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}