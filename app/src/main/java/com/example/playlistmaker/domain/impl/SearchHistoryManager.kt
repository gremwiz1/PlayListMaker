package com.example.playlistmaker.domain.impl

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.data.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.ui.AudioPlayerActivity
import com.google.gson.Gson

class SearchHistoryManager(context: Context) {
    private val searchHistoryRepository = SearchHistoryRepository(
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    )
    private val gson = Gson()

    fun addTrackInHistoryAndNavigate(context: Context, track: Track) {
        searchHistoryRepository.addTrackInHistory(track)
        val intent = Intent(context, AudioPlayerActivity::class.java)
        intent.putExtra("TrackExtra", gson.toJson(track))
        context.startActivity(intent)
    }

    fun getListHistoryTracks(): ArrayList<Track> {
        return searchHistoryRepository.getListHistoryTracks()
    }

    fun clearListHistory() {
        searchHistoryRepository.clearListHistory()
    }

}