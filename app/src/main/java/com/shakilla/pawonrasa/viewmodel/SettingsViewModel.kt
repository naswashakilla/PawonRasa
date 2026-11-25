package com.shakilla.pawonrasa.viewmodel

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.AndroidViewModel

class SettingsViewModel(app: Application) : AndroidViewModel(app) {

    private val prefs = app.getSharedPreferences("settings", Context.MODE_PRIVATE)

    // null = ikuti sistem, true = gelap, false = terang
    var darkMode by mutableStateOf<Boolean?>(
        if (prefs.contains("dark")) prefs.getBoolean("dark", false) else null
    )
        private set

    fun setThemeMode(value: Boolean?) {
        darkMode = value
        prefs.edit().apply {
            if (value == null) remove("dark") else putBoolean("dark", value)
        }.apply()
    }

    // tag: "en" atau "id"
    fun setLanguage(tag: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
    }
}