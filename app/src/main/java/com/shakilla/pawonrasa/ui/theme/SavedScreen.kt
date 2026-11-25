package com.shakilla.pawonrasa.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shakilla.pawonrasa.R
import com.shakilla.pawonrasa.data.MealSummary
import com.shakilla.pawonrasa.data.SavedMeal

@Composable
fun SavedScreen(
    title: String,
    emptyText: String,
    items: List<SavedMeal>,
    showCategoryFilter: Boolean,
    onMealClick: (String) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    val dir = LocalLayoutDirection.current
    var selected by rememberSaveable { mutableStateOf<String?>(null) }

    val categories = items.map { it.category }.filter { it.isNotBlank() }.distinct()
    val active = if (selected in categories) selected else null
    val shown = if (active == null) items else items.filter { it.category == active }

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
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (showCategoryFilter && categories.isNotEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = active == null,
                            onClick = { selected = null },
                            label = { Text(stringResource(R.string.category_all)) }
                        )
                    }
                    items(categories) { cat ->
                        FilterChip(
                            selected = active == cat,
                            onClick = { selected = cat },
                            label = { Text(cat) }
                        )
                    }
                }
            }
        }

        if (shown.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = emptyText,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            }
        } else {
            items(shown, key = { it.id }) { meal ->
                MealCard(
                    meal = MealSummary(meal.id, meal.name, meal.thumb),
                    onClick = { onMealClick(meal.id) }
                )
            }
        }
    }
}