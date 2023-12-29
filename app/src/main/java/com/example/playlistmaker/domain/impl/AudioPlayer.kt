package com.example.playlistmaker.domain.impl

import android.media.MediaPlayer

class AudioPlayer {
    enum class State {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }
    var onPrepared: (() -> Unit)? = null
    var onCompletion: (() -> Unit)? = null
    private val mediaPlayer = MediaPlayer()
    var state = State.DEFAULT
        private set

    init {
        mediaPlayer.setOnPreparedListener {
            state = State.PREPARED
            onPrepared?.invoke()
        }
            mediaPlayer.setOnCompletionListener {
                state = State.PREPARED
                onCompletion?.invoke()
            }
    }

    fun prepare(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }

    fun play() {
        if (state == State.PREPARED || state == State.PAUSED) {
            mediaPlayer.start()
            state = State.PLAYING
        }
    }

    fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            state = State.PAUSED
        }
    }

    fun stop() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
    }

    fun release() {
        mediaPlayer.release()
    }

    val currentPosition: Int
        get() = mediaPlayer.currentPosition
}