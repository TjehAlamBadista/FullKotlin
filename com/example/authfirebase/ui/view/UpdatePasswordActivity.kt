package com.example.authfirebase.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.authfirebase.databinding.ActivityUpdatePasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var updatePasswordBinding: ActivityUpdatePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        updatePasswordBinding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(updatePasswordBinding.root)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        updatePasswordBinding.layoutVerificationPassword.visibility = View.VISIBLE
        updatePasswordBinding.layoutUpdatePassword.visibility = View.GONE
        updatePasswordBinding.btnAuth.setOnClickListener {
            val pass = updatePasswordBinding.etPassword.text.toString().trim()
            if (pass.isEmpty()){
                updatePasswordBinding.etPassword.error = "Password Tidak Boleh Kosong"
                updatePasswordBinding.etPassword.requestFocus()
            }
            user.let {
                val userCredential = EmailAuthProvider.getCredential(it?.email!!, pass)
                it.reauthenticate(userCredential).addOnCompleteListener { Task ->
                    when {
                        Task.isSuccessful -> {
                            updatePasswordBinding.layoutVerificationPassword.visibility = View.GONE
                            updatePasswordBinding.layoutUpdatePassword.visibility = View.VISIBLE
                        }
                        Task.exception is FirebaseAuthInvalidCredentialsException -> {
                            updatePasswordBinding.etPassword.error = "Password Salah"
                            updatePasswordBinding.etPassword.requestFocus()
                        }
                        else -> {
                            Toast.makeText(applicationContext, "${Task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            updatePasswordBinding.btnUpdate.setOnClickListener updatePassword@{
                val newPass = updatePasswordBinding.etNewPass.text.toString().trim()
                val newPassConfirm = updatePasswordBinding.etNewPassConfirm.text.toString().trim()
                if (newPass.isEmpty()){
                    updatePasswordBinding.etNewPass.error = "Password Tidak Boleh Kosong"
                    updatePasswordBinding.etNewPass.requestFocus()
                    return@updatePassword
                }
                if (newPass.length < 8){
                    updatePasswordBinding.etNewPass.error = "Password Harus Lebih Dari 8 Karakter"
                    updatePasswordBinding.etNewPass.requestFocus()
                }
                if (newPass != newPassConfirm){
                    updatePasswordBinding.etNewPassConfirm.error = "Password Tidak Sama"
                    updatePasswordBinding.etNewPassConfirm.requestFocus()
                    return@updatePassword
                }
                user?.let {
                    user.updatePassword(newPass).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(applicationContext, "Password Berhasil Diupdate", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(applicationContext, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}