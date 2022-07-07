package com.example.authfirebase.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.authfirebase.databinding.FragmentServiceBinding
import com.example.authfirebase.ui.view.musicplayer.MusicActivity
import com.google.firebase.auth.FirebaseAuth

class ServiceFragment : Fragment(){

    private lateinit var _binding: FragmentServiceBinding
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.btnMusicPlayer.setOnClickListener {
            Intent(context, MusicActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}