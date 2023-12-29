package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.SearchHistoryRepository
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.ISearchHistoryManager
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.SearchHistoryManager
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private lateinit var searchHistoryManager: ISearchHistoryManager

    fun init(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val searchHistoryRepository = SearchHistoryRepository(sharedPreferences)
        searchHistoryManager = SearchHistoryManager(searchHistoryRepository)
    }

    fun provideSearchHistoryManager(): ISearchHistoryManager = searchHistoryManager

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}