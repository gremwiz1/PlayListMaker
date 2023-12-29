package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit
import com.example.playlistmaker.domain.api.ISearchHistoryRepository

class SearchHistoryRepository(private val sharedPreferences: SharedPreferences) :
    ISearchHistoryRepository {

    private val gson = Gson()
    private val maxHistorySize = 10

    private fun getHistoryTracks(): ArrayList<Track> {
        val json = sharedPreferences.getString(HISTORY_ARRAY_TRACKS, "")
        return if (json.isNullOrEmpty()) {
            ArrayList()
        } else {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            gson.fromJson(json, type)
        }
    }

    override fun addTrackInHistory(track: Track) {
        val currentHistory = getHistoryTracks()
        currentHistory.removeAll { it.trackId == track.trackId }
        currentHistory.add(0, track)
        while (currentHistory.size > maxHistorySize) {
            currentHistory.removeAt(currentHistory.size - 1)
        }
        sharedPreferences.edit { putString(HISTORY_ARRAY_TRACKS, gson.toJson(currentHistory)) }
    }

    override fun getListHistoryTracks(): ArrayList<Track> {
        return getHistoryTracks()
    }

    override fun clearListHistory() {
        sharedPreferences.edit { remove(HISTORY_ARRAY_TRACKS) }
    }

    companion object {
        const val HISTORY_ARRAY_TRACKS = "HISTORY_ARRAY_TRACKS"
    }
}