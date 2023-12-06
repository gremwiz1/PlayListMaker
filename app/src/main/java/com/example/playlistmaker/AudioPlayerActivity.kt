package com.example.playlistmaker

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*


class AudioPlayerActivity : AppCompatActivity() {
    enum class State {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }
    private lateinit var track: Track
    private val gson = Gson()
    val mediaPlayer = MediaPlayer()
    private var mediaPlayerState  = State.DEFAULT
    private lateinit var buttonPlay: ImageView
    private lateinit var timeToPlay: TextView
    private var mainThreadHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        mainThreadHandler =  Handler(Looper.getMainLooper())
        val trackJson = intent.getStringExtra("TrackExtra")
        track = gson.fromJson(trackJson, Track::class.java)
        buttonPlay = findViewById<ImageView>(R.id.button_play)
        timeToPlay = findViewById<TextView>(R.id.time_to_play)
        buttonPlay.isEnabled = false
        setupUIWithTrack(track)
        buttonPlay.setOnClickListener {
            mediaPlayerControl()
            if(mediaPlayerState == State.PLAYING) {
                refreshText()
            } else if(mediaPlayerState == State.PREPARED) {
                timeToPlay.setText("00:00")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(AudioPlayerActivity.TRACK, gson.toJson(track))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedTrack = savedInstanceState.getString(AudioPlayerActivity.TRACK)
        setupUIWithTrack(gson.fromJson(savedTrack, Track::class.java))

    }

    private fun setupUIWithTrack(track: Track) {
        val imageTrack = findViewById<ImageView>(R.id.imageTrack)
        val nameTrack = findViewById<TextView>(R.id.nameTrack)
        val nameArtist = findViewById<TextView>(R.id.nameArtist)
        val durationTrack = findViewById<TextView>(R.id.durationTrack)
        val collectionName = findViewById<TextView>(R.id.collection_name)
        val releaseDate = findViewById<TextView>(R.id.release_date)
        val primaryGenreName = findViewById<TextView>(R.id.primary_genre_name)
        val country = findViewById<TextView>(R.id.country)
        val buttonAddInPlayList = findViewById<ImageView>(R.id.button_add_in_playList)
        val buttonLike = findViewById<ImageView>(R.id.button_like)
        val arrowBackButton = findViewById<ImageView>(R.id.backArrow)

        arrowBackButton.setOnClickListener {
            finish()
        }
        nameTrack.text = track.trackName
        nameArtist.text = track.artistName
        durationTrack.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toInt())

        collectionName.text = track.collectionName ?: ""

        val year = track.releaseDate?.takeIf { it.length >= 4 }?.substring(0, 4) ?: "Год не указан"
        releaseDate.text = year

        primaryGenreName.text = track.primaryGenreName
        country.text = track.country

        val urlImage = track.artworkUrl100
        if (urlImage != null) {
            Glide.with(this)
                .load(urlImage.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .transform(RoundedCorners(10))
                .into(imageTrack)
        } else {
            Glide.with(this)
                .load(R.drawable.placeholder)
                .fitCenter()
                .transform(RoundedCorners(10))
                .into(imageTrack)
        }
        preparePlayer(track.previewUrl)
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            buttonPlay.isEnabled = true
            mediaPlayerState = State.PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            // тут нужно поменять картинку кнопки
            mediaPlayerState = State.PREPARED
        }
    }

    fun startMediaPlayer() {
        mediaPlayer.start()
        mediaPlayerState = State.PLAYING
    }

    fun pauseMediaPlayer() {
        mediaPlayer.pause()
        mediaPlayerState = State.PAUSED
    }

    fun mediaPlayerControl() {
        when (mediaPlayerState) {
            State.PREPARED, State.PAUSED -> {
                startMediaPlayer()
            }
            State.PLAYING -> {
                Log.i("MediaPlayerState", "Пауза")
                pauseMediaPlayer()
            }
            else -> {
                // Обработка других состояний
            }
    }}

    override fun onPause() {
        super.onPause()
        pauseMediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    fun refreshText() {
        val newThread = Thread {
            mainThreadHandler?.postDelayed(
                object: Runnable {
                    override fun run() {
                        val time = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                        timeToPlay.setText(time)
                        mainThreadHandler?.postDelayed(this, REFRESH_LIST_DELAY_MILLIS)
                    }
                }, REFRESH_LIST_DELAY_MILLIS
            )
        }
        newThread.start()
    }
    companion object {
        private val TRACK = "TRACK"
        private const val REFRESH_LIST_DELAY_MILLIS = 1_000L
    }
}