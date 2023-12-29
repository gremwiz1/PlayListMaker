package com.example.playlistmaker.domain.api

import android.content.Context
import com.example.playlistmaker.domain.models.Track

interface ISearchHistoryManager {
    fun addTrackInHistoryAndNavigate(track: Track)
    fun getListHistoryTracks(): ArrayList<Track>
    fun clearListHistory()
}
