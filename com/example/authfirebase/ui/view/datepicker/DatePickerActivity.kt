package com.example.authfirebase.ui.view.datepicker

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import com.example.authfirebase.R
import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DatePickerActivity : AppCompatActivity() {

    private lateinit var textClock: TextView
    private lateinit var textDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker)

        textClock = findViewById(R.id.tvClock)
        textDate = findViewById(R.id.tvDate)
        
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                @SuppressLint("SimpleDateFormat") val clockFormat: DateFormat =
                    SimpleDateFormat("HH:mm")
                @SuppressLint("SimpleDateFormat") var dateFormat: DateFormat? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy")
                }

                textClock.setText(clockFormat.format(Date()))
                textDate.setText(dateFormat!!.format(Date()))
                handler.postDelayed(this, 1000)
            }
        })
    }
}