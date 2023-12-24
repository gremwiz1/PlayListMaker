package com.example.playlistmaker.domain

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    var darkTheme = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(IS_DARK_THEME, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val IS_DARK_THEME = "IS_DARK_THEME"
    }
}
