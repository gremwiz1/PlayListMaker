package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val imdbBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val imdbService = retrofit.create(ITunesApiService::class.java)

    override fun doRequest(dto: Any): Response {
        try {
            if (dto is TrackSearchRequest) {
                val response = imdbService.searchSongs(dto.expression).execute()
                val body = response.body() ?: Response()
                return body.apply { resultCode = response.code() }
            } else {
                return Response().apply { resultCode = 400 }
            }
        } catch (e: Exception) {
            return Response().apply { resultCode = 400 }
        }
    }
}