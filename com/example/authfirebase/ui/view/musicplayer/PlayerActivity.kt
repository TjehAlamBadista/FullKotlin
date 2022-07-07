package com.example.authfirebase.ui.view.musicplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.authfirebase.R
import com.example.authfirebase.databinding.ActivityPlayerBinding
import com.example.authfirebase.service.MusicService
import com.example.authfirebase.ui.model.Music
import java.lang.Exception

class PlayerActivity : AppCompatActivity(), ServiceConnection {

    companion object{
        lateinit var musicListPA : ArrayList<Music>
        var songPosition : Int = 0
        var isPlaying: Boolean = false
        var musicService: MusicService? = null
        lateinit var binding: ActivityPlayerBinding
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // untuk service
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)

        initializeLayout()

        binding.btnPlayPausePA.setOnClickListener {
            if (isPlaying) pauseMusic()
            else playMusic()
        }

        binding.btnPreviousPA.setOnClickListener {
            prevNextSong(false)
        }
        binding.btnNextPA.setOnClickListener {
            prevNextSong(true)
        }
    }

    private fun setLayout(){
        Glide.with(this)
            .load(musicListPA[songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music).centerCrop())
            .into(binding.imgSongPA)

        binding.tvSongPA.text = musicListPA[songPosition].title
    }

    private fun createMediaPlayer(){
        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()

            isPlaying = true
            binding.btnPlayPausePA.setIconResource(R.drawable.ic_pause)

            musicService!!.showNotification(R.drawable.ic_pause)
        }
        catch (e: Exception) {return}
    }

    private fun initializeLayout(){
        songPosition = intent.getIntExtra("index", 0)
        when(intent.getStringExtra("class")){
            "MusicAdapter" -> {
                musicListPA = ArrayList()
                musicListPA.addAll(MusicActivity.musicListMA)
                setLayout()
//                createMediaPlayer()
            }
            "MusicActivity" -> {
                musicListPA = ArrayList()
                musicListPA.addAll(MusicActivity.musicListMA)
                musicListPA.shuffle()
                setLayout()
//                createMediaPlayer()
            }
        }
    }

    private fun playMusic(){
        binding.btnPlayPausePA.setIconResource(R.drawable.ic_pause)
        musicService!!.showNotification(R.drawable.ic_pause)
        isPlaying = true
        musicService!!.mediaPlayer!!.start()
    }


    private fun pauseMusic(){
        binding.btnPlayPausePA.setIconResource(R.drawable.ic_play)
        musicService!!.showNotification(R.drawable.ic_play)
        isPlaying = false
        musicService!!.mediaPlayer!!.pause()
    }

    private fun prevNextSong(increment: Boolean){
        if (increment){
            setSongPosition(increment = true)
            setLayout()
            createMediaPlayer()
        }
        else{
            setSongPosition(increment = false)
            setLayout()
            createMediaPlayer()
        }
    }

    private fun setSongPosition(increment: Boolean){
        if (increment){
            if (musicListPA.size-1 == songPosition)
                songPosition = 0
            else ++songPosition
        }
        else{
            if (0 == songPosition)
                songPosition = musicListPA.size-1
            else --songPosition
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        createMediaPlayer()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }

}