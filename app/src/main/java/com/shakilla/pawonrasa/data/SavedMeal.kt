package com.shakilla.pawonrasa.data

data class SavedMeal(
    val id: String,
    val name: String,
    val thumb: String,
    val category: String,
    val isFavorite: Boolean,
    val inCollection: Boolean
)