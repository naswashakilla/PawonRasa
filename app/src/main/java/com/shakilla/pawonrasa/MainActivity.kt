package com.shakilla.pawonrasa

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shakilla.pawonrasa.ui.AppNavigation
import com.shakilla.pawonrasa.ui.SplashScreen
import com.shakilla.pawonrasa.ui.theme.PawonRasaTheme
import com.shakilla.pawonrasa.viewmodel.SettingsViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings: SettingsViewModel = viewModel()
            val dark = settings.darkMode ?: isSystemInDarkTheme()
            var showSplash by rememberSaveable { mutableStateOf(true) }

            PawonRasaTheme(darkTheme = dark) {
                if (showSplash) {
                    SplashScreen(onFinished = { showSplash = false })
                } else {
                    AppNavigation(settings)
                }
            }
        }
    }
}