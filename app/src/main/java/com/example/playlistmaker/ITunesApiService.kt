package com.example.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("/search?entity=song")
    fun searchSongs(@Query("term") text: String): Call<TrackResponseBody>
}

class TrackResponseBody(
    val resultCount: Int,
    var results: ArrayList<Track>,
) {
}