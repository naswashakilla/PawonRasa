package com.shakilla.pawonrasa.data

import com.google.android.gms.tasks.Task
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object Translator {

    private val translator by lazy {
        Translation.getClient(
            TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.INDONESIAN)
                .build()
        )
    }

    private val cache = HashMap<String, String>()
    private val mutex = Mutex()
    private var modelReady = false

    fun cached(text: String): String? = cache[text]

    suspend fun translate(text: String): String {
        cache[text]?.let { return it }
        return try {
            mutex.withLock {
                cache[text]?.let { return it }
                if (!modelReady) {
                    translator.downloadModelIfNeeded(DownloadConditions.Builder().build()).await()
                    modelReady = true
                }
                val result = translator.translate(text).await()
                cache[text] = result
                result
            }
        } catch (e: Exception) {
            text // kalau gagal (misal belum ada internet), tampilkan teks asli
        }
    }

    private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { cont ->
        addOnSuccessListener { cont.resume(it) }
        addOnFailureListener { cont.resumeWithException(it) }
    }
}