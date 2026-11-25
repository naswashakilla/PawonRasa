package com.shakilla.pawonrasa.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shakilla.pawonrasa.data.Meal
import com.shakilla.pawonrasa.data.SavedMeal

class SavedViewModel(app: Application) : AndroidViewModel(app) {

    private val prefs = app.getSharedPreferences("saved", Context.MODE_PRIVATE)
    private val gson = Gson()

    var items by mutableStateOf(load())
        private set

    private fun load(): List<SavedMeal> {
        val json = prefs.getString("items", null) ?: return emptyList()
        return try {
            gson.fromJson<List<SavedMeal>>(
                json, object : TypeToken<List<SavedMeal>>() {}.type
            ) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun update(meal: Meal, change: (SavedMeal) -> SavedMeal) {
        val existing = items.find { it.id == meal.id }
            ?: SavedMeal(meal.id, meal.name, meal.thumb, meal.category, false, false)
        val updated = change(existing)
        val others = items.filter { it.id != meal.id }
        val newList = if (updated.isFavorite || updated.inCollection) listOf(updated) + others else others
        items = newList
        prefs.edit().putString("items", gson.toJson(newList)).apply()
    }

    fun toggleFavorite(meal: Meal) = update(meal) { it.copy(isFavorite = !it.isFavorite) }
    fun toggleCollection(meal: Meal) = update(meal) { it.copy(inCollection = !it.inCollection) }

    fun isFavorite(id: String) = items.any { it.id == id && it.isFavorite }
    fun inCollection(id: String) = items.any { it.id == id && it.inCollection }
}