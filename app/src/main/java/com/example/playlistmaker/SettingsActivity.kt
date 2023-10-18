package com.example.playlistmaker

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrowBackButton = findViewById<ImageView>(R.id.backArrow)

        arrowBackButton.setOnClickListener {
            finish()
        }
        val writeSupportButton = findViewById<ImageView>(R.id.write_support)
        writeSupportButton.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse(getString(R.string.url_mail))
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.eamil_rasrab)))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.message_for_rasrab))
            emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.thanks_for_rasrab))

            try {
                startActivity(emailIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, getString(R.string.not_apps_for_sent_email), Toast.LENGTH_SHORT).show()
            }
        }



        val switchDarkMode = findViewById<Switch>(R.id.switchDarkMode)

        // Функция для определения текущего режима темы устройства
        fun isNightModeEnabled(context: Context): Boolean {
            val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            val currentMode = uiModeManager.nightMode
            return currentMode == 2
        }

        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val isFirstRun: Boolean = sharedPreferences.getBoolean(KEY_IS_FIRST_RUN, true)

        if (isFirstRun) {
            // Экран запускается впервые
            // Получаем текущий режим темы устройства
            val isNightMode = isNightModeEnabled(this)
            // Устанавливаем начальное состояние свитча в зависимости от текущего режима темы
            switchDarkMode.isChecked = isNightMode
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean(KEY_IS_FIRST_RUN, false)
            editor.apply()
        } else {
            // Экран уже запускался ранее
            // Получаем текущий режим темы
            val currentNightMode = AppCompatDelegate.getDefaultNightMode()

            switchDarkMode.isChecked = currentNightMode == AppCompatDelegate.MODE_NIGHT_YES

        }


        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val agreementButton = findViewById<LinearLayout>(R.id.user_agreement)
        agreementButton.setOnClickListener {
            val url = Uri.parse(getString(R.string.agreement_link))
            val agreementIntent = Intent(Intent.ACTION_VIEW, url)
            startActivity(agreementIntent)
        }

        val shareAppButton = findViewById<LinearLayout>(R.id.share_app)
        shareAppButton.setOnClickListener() {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
            // Проверка на наличие доступных Activity
            if (shareIntent.resolveActivity(packageManager) != null) {
                startActivity(shareIntent)
            } else {
                Toast.makeText(this, getString(R.string.not_apps_for_share_link), Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object {
        const val KEY_IS_FIRST_RUN = "isFirstRun"
    }
}