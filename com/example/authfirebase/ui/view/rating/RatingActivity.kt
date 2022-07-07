package com.example.authfirebase.ui.view.rating

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.example.authfirebase.R

class RatingActivity : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var btnRating: Button
    private lateinit var tvRating: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        ratingBar = findViewById(R.id.ratingBar)
        btnRating = findViewById(R.id.btnRating)
        tvRating = findViewById(R.id.tvRating)

        ratingBar.numStars = 5
        ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener{ _, rating, _ ->
            Toast.makeText(this@RatingActivity, "Stars : "+rating.toInt(), Toast.LENGTH_SHORT).show()
        }
        btnRating.setOnClickListener {
            tvRating.text = "Kamu mendapatkan "+ratingBar.rating.toInt()+" Bintang"
        }
    }
}