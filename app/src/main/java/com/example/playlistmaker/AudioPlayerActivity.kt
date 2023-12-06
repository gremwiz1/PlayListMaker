package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*


class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var track: Track
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val trackJson = intent.getStringExtra("TrackExtra")
        track = gson.fromJson(trackJson, Track::class.java)
        setupUIWithTrack(track)

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
        val buttonPlay = findViewById<ImageView>(R.id.button_play)
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
    }

    companion object {
        private val TRACK = "TRACK"
    }
}