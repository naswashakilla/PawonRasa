package com.shakilla.pawonrasa.data

data class CategoryResponse(val categories: List<Category>?)

data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String?
)

data class MealListResponse(val meals: List<MealSummary>?)

data class MealSummary(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String?
)

data class MealDetailResponse(val meals: List<Map<String, String?>>?)

data class Ingredient(val name: String, val measure: String)

data class Meal(
    val id: String,
    val name: String,
    val category: String,
    val area: String,
    val instructions: String,
    val thumb: String,
    val ingredients: List<Ingredient>
)

fun Map<String, String?>.toMeal(): Meal {
    val ingredients = (1..20).mapNotNull { i ->
        val name = this["strIngredient$i"]?.trim()
        val measure = this["strMeasure$i"]?.trim().orEmpty()
        if (name.isNullOrBlank()) null else Ingredient(name, measure)
    }
    return Meal(
        id = this["idMeal"].orEmpty(),
        name = this["strMeal"].orEmpty(),
        category = this["strCategory"].orEmpty(),
        area = this["strArea"].orEmpty(),
        instructions = this["strInstructions"].orEmpty(),
        thumb = this["strMealThumb"].orEmpty(),
        ingredients = ingredients
    )
}