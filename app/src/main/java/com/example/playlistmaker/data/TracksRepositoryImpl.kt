package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackResponseBody
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TrackSearchResult

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): TrackSearchResult<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        val isSuccess = response.resultCode == 200

        val tracks = if (isSuccess) {
            (response as TrackResponseBody).results.map {
                Track(it.trackId.toInt(),
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl)
            }
        } else {
            emptyList()
        }

        return TrackSearchResult(data = tracks, isSuccess = isSuccess)
    }
}

