package com.shakilla.pawonrasa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shakilla.pawonrasa.data.Meal
import com.shakilla.pawonrasa.data.MealRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val _meal = MutableStateFlow<UiState<Meal?>>(UiState.Loading)
    val meal: StateFlow<UiState<Meal?>> = _meal.asStateFlow()

    private var currentId: String? = null

    fun load(id: String, force: Boolean = false) {
        // Jangan muat ulang kalau data resep ini sudah ada (misal setelah rotasi)
        if (!force && id == currentId && _meal.value is UiState.Success) return
        currentId = id
        viewModelScope.launch {
            _meal.value = UiState.Loading
            _meal.value = MealRepository.getMealDetail(id).toUiState()
        }
    }
}