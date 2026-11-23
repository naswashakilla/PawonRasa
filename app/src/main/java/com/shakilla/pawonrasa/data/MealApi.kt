package com.shakilla.pawonrasa.data

import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): MealListResponse

    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): MealDetailResponse

    @GET("lookup.php")
    suspend fun getMealDetail(@Query("i") id: String): MealDetailResponse

    @GET("random.php")
    suspend fun getRandomMeal(): MealDetailResponse
}