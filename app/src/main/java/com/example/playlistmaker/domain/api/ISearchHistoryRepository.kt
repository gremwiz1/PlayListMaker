package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface ISearchHistoryRepository {
    fun addTrackInHistory(track: Track)
    fun getListHistoryTracks(): ArrayList<Track>
    fun clearListHistory()
}
