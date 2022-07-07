package com.example.authfirebase.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.authfirebase.databinding.FragmentHomeBinding
import com.example.authfirebase.ui.view.bottomsheet.BottomSheetActivity
import com.example.authfirebase.ui.view.cart.KeranjangActivity
import com.example.authfirebase.ui.view.datepicker.DatePickerActivity
import com.example.authfirebase.ui.view.drwamaps.MapssActivity
import com.example.authfirebase.ui.view.gesture.GestureActivity
import com.example.authfirebase.ui.view.livechat.ChatActivity
import com.example.authfirebase.ui.view.maps.DrawMapsActivity
import com.example.authfirebase.ui.view.maps.MapsActivity
import com.example.authfirebase.ui.view.movie.MovieActivity
import com.example.authfirebase.ui.view.rating.RatingActivity
import com.example.authfirebase.ui.view.tablayout.TabLayoutActivity
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.btnNotifikasi.setOnClickListener {
            Intent(context, NotifikasiActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnMaps.setOnClickListener {
            Intent(context, MapsActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnDrawMaps.setOnClickListener {
            Intent(context, MapssActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnLiveChat.setOnClickListener {
            Intent(context, ChatActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnBottomSheet.setOnClickListener {
            Intent(context, BottomSheetActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnDatePicker.setOnClickListener {
            Intent(context, DatePickerActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnMovie.setOnClickListener {
            Intent(context, MovieActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnKeranjang.setOnClickListener {
            Intent(context, KeranjangActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnRating.setOnClickListener {
            Intent(context, RatingActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnGesture.setOnClickListener {
            Intent(context, GestureActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnTabLayout.setOnClickListener {
            Intent(context, TabLayoutActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}