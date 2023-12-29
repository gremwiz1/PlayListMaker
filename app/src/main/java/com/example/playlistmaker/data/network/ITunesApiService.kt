package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TrackResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("/search?entity=song")
    fun searchSongs(@Query("term") text: String): Call<TrackResponseBody>
}
