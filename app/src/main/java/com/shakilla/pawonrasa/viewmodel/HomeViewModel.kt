package com.shakilla.pawonrasa.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shakilla.pawonrasa.data.Category
import com.shakilla.pawonrasa.data.Meal
import com.shakilla.pawonrasa.data.MealRepository
import com.shakilla.pawonrasa.data.MealSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _categories = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val categories: StateFlow<UiState<List<Category>>> = _categories.asStateFlow()

    private val _featured = MutableStateFlow<UiState<Meal?>>(UiState.Loading)
    val featured: StateFlow<UiState<Meal?>> = _featured.asStateFlow()

    private val _meals = MutableStateFlow<UiState<List<MealSummary>>>(UiState.Loading)
    val meals: StateFlow<UiState<List<MealSummary>>> = _meals.asStateFlow()

    var query by mutableStateOf("")
    var selectedCategory by mutableStateOf("Chicken")

    init {
        loadCategories()
        loadFeatured()
        loadMeals()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _categories.value = UiState.Loading
            _categories.value = MealRepository.getCategories().toUiState()
        }
    }

    fun loadFeatured() {
        viewModelScope.launch {
            _featured.value = UiState.Loading
            _featured.value = MealRepository.getRandomMeal().toUiState()
        }
    }

    fun loadMeals() {
        viewModelScope.launch {
            _meals.value = UiState.Loading
            _meals.value = if (query.isBlank()) {
                MealRepository.getMealsByCategory(selectedCategory).toUiState()
            } else {
                MealRepository.searchMeals(query.trim())
                    .map { list -> list.map { MealSummary(it.id, it.name, it.thumb) } }
                    .toUiState()
            }
        }
    }

    fun selectCategory(name: String) {
        selectedCategory = name
        query = ""
        loadMeals()
    }

    fun onSearch() = loadMeals()
}