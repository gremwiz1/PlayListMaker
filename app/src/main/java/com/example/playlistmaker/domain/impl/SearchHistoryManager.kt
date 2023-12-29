package com.example.playlistmaker.domain.impl

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.domain.api.ISearchHistoryManager
import com.example.playlistmaker.domain.api.ISearchHistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.ui.AudioPlayerActivity
import com.google.gson.Gson

class SearchHistoryManager(private val repository: ISearchHistoryRepository) :
    ISearchHistoryManager {

    private val gson = Gson()

    override fun addTrackInHistoryAndNavigate(track: Track) {
        repository.addTrackInHistory(track)
    }

    override fun getListHistoryTracks(): ArrayList<Track> {
        return repository.getListHistoryTracks()
    }

    override fun clearListHistory() {
        repository.clearListHistory()
    }
}