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
            (response as TrackResponseBody).results.map { dto ->
                Track(
                    dto.trackId.toInt(),
                    dto.trackName,
                    dto.artistName,
                    dto.trackTimeMillis,
                    dto.artworkUrl100,
                    dto.collectionName,
                    dto.releaseDate,
                    dto.primaryGenreName,
                    dto.country,
                    dto.previewUrl
                )
            }
        } else {
            emptyList()
        }

        return TrackSearchResult(data = tracks, isSuccess = isSuccess)
    }
}

