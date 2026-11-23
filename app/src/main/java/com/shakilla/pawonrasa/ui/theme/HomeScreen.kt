package com.shakilla.pawonrasa.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.shakilla.pawonrasa.R
import com.shakilla.pawonrasa.data.Category
import com.shakilla.pawonrasa.data.Meal
import com.shakilla.pawonrasa.viewmodel.HomeViewModel
import com.shakilla.pawonrasa.viewmodel.UiState

private val fullWidth: LazyGridItemSpanScope.() -> GridItemSpan = { GridItemSpan(maxLineSpan) }

@Composable
fun HomeScreen(
    onMealClick: (String) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    vm: HomeViewModel = viewModel()
) {
    val categories by vm.categories.collectAsState()
    val featured by vm.featured.collectAsState()
    val meals by vm.meals.collectAsState()
    val focus = LocalFocusManager.current
    val dir = LocalLayoutDirection.current

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = contentPadding.calculateStartPadding(dir) + 16.dp,
            end = contentPadding.calculateEndPadding(dir) + 16.dp,
            top = contentPadding.calculateTopPadding() + 16.dp,
            bottom = contentPadding.calculateBottomPadding() + 16.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Sapaan
        item(span = fullWidth) {
            Text(
                text = stringResource(R.string.greeting),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Kolom cari
        item(span = fullWidth) {
            OutlinedTextField(
                value = vm.query,
                onValueChange = { vm.query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.search_hint)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    vm.onSearch()
                    focus.clearFocus()
                })
            )
        }

        // Banner resep acak
        item(span = fullWidth) {
            when (val f = featured) {
                is UiState.Success -> f.data?.let { meal ->
                    FeaturedBanner(meal) { onMealClick(meal.id) }
                }
                UiState.Loading -> LoadingBox()
                UiState.Error -> {}
            }
        }

        // Kategori
        item(span = fullWidth) {
            Text(
                text = stringResource(R.string.categories),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        item(span = fullWidth) {
            when (val c = categories) {
                is UiState.Success -> LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(c.data, key = { it.idCategory }) { cat ->
                        CategoryItem(
                            category = cat,
                            selected = vm.query.isBlank() && cat.strCategory == vm.selectedCategory,
                            onClick = { vm.selectCategory(cat.strCategory) }
                        )
                    }
                }
                UiState.Loading -> LoadingBox()
                UiState.Error -> ErrorBox(onRetry = vm::loadCategories)
            }
        }

        // Resep populer
        item(span = fullWidth) {
            Text(
                text = stringResource(R.string.popular_recipes),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        when (val m = meals) {
            UiState.Loading -> item(span = fullWidth) { LoadingBox() }
            UiState.Error -> item(span = fullWidth) { ErrorBox(onRetry = vm::loadMeals) }
            is UiState.Success -> {
                if (m.data.isEmpty()) {
                    item(span = fullWidth) {
                        Text(
                            text = stringResource(R.string.no_results),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } else {
                    items(m.data, key = { it.idMeal }) { meal ->
                        MealCard(meal = meal, onClick = { onMealClick(meal.idMeal) })
                    }
                }
            }
        }
    }
}

@Composable
private fun FeaturedBanner(meal: Meal, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.weekly_banner_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = meal.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) { Text(stringResource(R.string.view_recipe)) }
            }
            Spacer(Modifier.width(12.dp))
            AsyncImage(
                model = meal.thumb,
                contentDescription = meal.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
private fun CategoryItem(category: Category, selected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(78.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = if (selected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(64.dp)
        ) {
            AsyncImage(
                model = category.strCategoryThumb,
                contentDescription = category.strCategory,
                modifier = Modifier.padding(8.dp)
            )
        }
        Text(
            text = category.strCategory,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}