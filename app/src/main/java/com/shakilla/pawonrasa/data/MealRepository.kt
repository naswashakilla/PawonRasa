package com.shakilla.pawonrasa.data

object MealRepository {
    private val api = RetrofitInstance.api

    suspend fun getCategories(): Result<List<Category>> = runCatching {
        api.getCategories().categories.orEmpty()
    }

    suspend fun getMealsByCategory(category: String): Result<List<MealSummary>> = runCatching {
        api.getMealsByCategory(category).meals.orEmpty()
    }

    suspend fun searchMeals(query: String): Result<List<Meal>> = runCatching {
        api.searchMeals(query).meals.orEmpty().map { it.toMeal() }
    }

    suspend fun getMealDetail(id: String): Result<Meal?> = runCatching {
        api.getMealDetail(id).meals?.firstOrNull()?.toMeal()
    }

    suspend fun getRandomMeal(): Result<Meal?> = runCatching {
        api.getRandomMeal().meals?.firstOrNull()?.toMeal()
    }
}