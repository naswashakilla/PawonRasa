package com.shakilla.pawonrasa

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import com.shakilla.pawonrasa.ui.HomeScreen
import com.shakilla.pawonrasa.ui.theme.PawonRasaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PawonRasaTheme {
                Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
                    HomeScreen(
                        onMealClick = { id -> Log.d("NAV", "Klik resep $id") },
                        contentPadding = innerPadding
                    )
                }
            }
        }
    }
}