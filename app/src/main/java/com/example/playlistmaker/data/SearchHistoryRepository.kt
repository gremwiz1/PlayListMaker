package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepository(private val sharedPreferences: SharedPreferences) {
    private val gson = Gson()
    private val maxHistorySize = 10

    private fun getHistoryTracks(): ArrayList<Track> {
        val json = sharedPreferences.getString(HISTORY_ARRAY_TRAKS, "")
        return if (json.isNullOrEmpty()) {
            ArrayList()
        } else {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            gson.fromJson(json, type)
        }
    }

    fun addTrackInHistory(track: Track) {
        val currentHistory = getHistoryTracks()
        currentHistory.removeAll { it.trackId == track.trackId }
        currentHistory.add(0, track)
        while (currentHistory.size > maxHistorySize) {
            currentHistory.removeAt(currentHistory.size - 1)
        }
        sharedPreferences.edit().putString(HISTORY_ARRAY_TRAKS, gson.toJson(currentHistory)).apply()
    }

    fun getListHistoryTracks(): ArrayList<Track> {
        return getHistoryTracks()
    }

    fun clearListHistory() {
        sharedPreferences.edit().remove(HISTORY_ARRAY_TRAKS).apply()
    }

    companion object {
        const val HISTORY_ARRAY_TRAKS = "HISTORY_ARRAY_TRACKS"
    }
}
