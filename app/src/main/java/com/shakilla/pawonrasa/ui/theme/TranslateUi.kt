package com.shakilla.pawonrasa.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalConfiguration
import com.shakilla.pawonrasa.data.Translator

@Composable
fun translated(text: String): String {
    val lang = LocalConfiguration.current.locales[0].language
    val isIndonesian = lang == "in" || lang == "id"
    if (!isIndonesian || text.isBlank()) return text

    val result by produceState(
        initialValue = Translator.cached(text) ?: text,
        key1 = text
    ) {
        value = Translator.translate(text)
    }
    return result
}