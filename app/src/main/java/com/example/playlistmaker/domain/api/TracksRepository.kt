package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TrackSearchResult

interface TracksRepository {
    fun searchTracks(expression: String): TrackSearchResult<List<Track>>
}
