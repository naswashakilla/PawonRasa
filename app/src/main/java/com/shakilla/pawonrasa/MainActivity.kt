package com.shakilla.pawonrasa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shakilla.pawonrasa.ui.theme.PawonRasaTheme
import androidx.compose.ui.res.stringResource
import com.shakilla.pawonrasa.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var gelap by remember { mutableStateOf(false) }
            PawonRasaTheme(darkTheme = gelap) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Pawon Rasa", style = MaterialTheme.typography.headlineLarge)
                        Text("Sajian Lezat, Setiap Hari", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(16.dp))
                        Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineLarge)
                        Text(stringResource(R.string.tagline), style = MaterialTheme.typography.bodyLarge)

                        Button(onClick = { gelap = !gelap }) {
                            Text("Ganti Mode Terang/Gelap")
                        }
                    }
                }
            }
        }
    }
}