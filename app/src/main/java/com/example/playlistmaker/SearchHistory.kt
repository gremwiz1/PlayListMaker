package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPreferences: SharedPreferences) {
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

    fun addTrackInHistory(context: Context, track: Track) {
        val currentHistory = getHistoryTracks()

        // Удаляем трек, если он уже есть в истории
        currentHistory.removeAll { it.trackId == track.trackId }

        // Добавляем трек в начало списка
        currentHistory.add(0, track)

        // Ограничиваем размер списка до 10
        while (currentHistory.size > maxHistorySize) {
            currentHistory.removeAt(currentHistory.size - 1)
        }

        // Сохраняем измененный список в SharedPreferences
        sharedPreferences.edit()
            .putString(HISTORY_ARRAY_TRAKS, gson.toJson(currentHistory))
            .apply()

        val gson = Gson()
        val intent = Intent(context, AudioPlayerActivity::class.java)
        intent.putExtra("TrackExtra", gson.toJson(track))
        context.startActivity(intent)
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
