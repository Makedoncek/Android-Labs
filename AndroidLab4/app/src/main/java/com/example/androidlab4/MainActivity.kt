package com.example.androidlab4

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View  // Додаємо імпорт класу View
import android.widget.Button
import android.widget.ImageView  // Додаємо імпорт класу ImageView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var videoView: VideoView
    private lateinit var audioImage: ImageView
    private var audioPaused: Boolean = false
    private var videoPaused: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonPlayAudio = findViewById<Button>(R.id.buttonPlayAudio)
        val buttonPauseAudio = findViewById<Button>(R.id.buttonPauseAudio)
        val buttonStopAudio = findViewById<Button>(R.id.buttonStopAudio)
        videoView = findViewById(R.id.videoView)
        audioImage = findViewById(R.id.audioImage)
        val buttonPlayVideo = findViewById<Button>(R.id.buttonPlayVideo)
        val buttonPauseVideo = findViewById<Button>(R.id.buttonPauseVideo)
        val buttonStopVideo = findViewById<Button>(R.id.buttonStopVideo)

        // Аудіо-файл з внутрішнього сховища
        val audioUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.shape_of_myheart)
        mediaPlayer = MediaPlayer.create(this, audioUri)

        buttonPlayAudio.setOnClickListener {
            audioImage.visibility = View.VISIBLE
            if (audioPaused) {
                mediaPlayer.start()
                audioPaused = false
            } else {
                mediaPlayer = MediaPlayer.create(this, audioUri)
                mediaPlayer.start()
            }
        }

        buttonPauseAudio.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                audioPaused = true
            }
        }

        buttonStopAudio.setOnClickListener {
            if (mediaPlayer.isPlaying || audioPaused) {
                mediaPlayer.stop()
                mediaPlayer.release()
                audioImage.visibility = View.GONE
                audioPaused = false
            }
        }

        // Відео-файл з внутрішнього сховища
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.pes_patron)
        videoView.setVideoURI(videoUri)

        buttonPlayVideo.setOnClickListener {
            audioImage.visibility = View.GONE
            if (videoPaused) {
                videoView.start()
                videoPaused = false
            } else {
                videoView.setVideoURI(videoUri)
                videoView.start()
            }
        }

        buttonPauseVideo.setOnClickListener {
            if (videoView.isPlaying) {
                videoView.pause()
                videoPaused = true
            }
        }

        buttonStopVideo.setOnClickListener {
            if (videoView.isPlaying || videoPaused) {
                videoView.stopPlayback()
                videoPaused = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        if (videoView.isPlaying) {
            videoView.stopPlayback()
        }
    }
}
