package com.shakilla.pawonrasa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.shakilla.pawonrasa.R
import com.shakilla.pawonrasa.data.Meal
import com.shakilla.pawonrasa.viewmodel.DetailViewModel
import com.shakilla.pawonrasa.viewmodel.SavedViewModel
import com.shakilla.pawonrasa.viewmodel.UiState

@Composable
fun DetailScreen(
    id: String,
    onBack: () -> Unit,
    saved: SavedViewModel,
    contentPadding: PaddingValues = PaddingValues(),
    vm: DetailViewModel = viewModel()
) {
    LaunchedEffect(id) { vm.load(id) }
    val state by vm.meal.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val s = state) {
            UiState.Loading -> LoadingBox(Modifier.align(Alignment.Center))
            UiState.Error -> ErrorBox(
                onRetry = { vm.load(id, force = true) },
                modifier = Modifier.align(Alignment.Center)
            )
            is UiState.Success -> {
                val meal = s.data
                if (meal == null) {
                    Text(
                        text = stringResource(R.string.no_results),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    MealContent(meal, contentPadding)

                    // Tombol favorit dan koleksi
                    val isFav = saved.isFavorite(meal.id)
                    val inCol = saved.inCollection(meal.id)
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .statusBarsPadding()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { saved.toggleFavorite(meal) },
                            modifier = Modifier.background(
                                MaterialTheme.colorScheme.background.copy(alpha = 0.75f),
                                CircleShape
                            )
                        ) {
                            Icon(
                                imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = stringResource(
                                    if (isFav) R.string.remove_favorite else R.string.add_favorite
                                ),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            onClick = { saved.toggleCollection(meal) },
                            modifier = Modifier.background(
                                MaterialTheme.colorScheme.background.copy(alpha = 0.75f),
                                CircleShape
                            )
                        ) {
                            Icon(
                                imageVector = if (inCol) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = stringResource(
                                    if (inCol) R.string.remove_from_collection else R.string.save_to_collection
                                ),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        // Tombol kembali
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .statusBarsPadding()
                .padding(8.dp)
                .background(
                    MaterialTheme.colorScheme.background.copy(alpha = 0.75f),
                    CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun MealContent(meal: Meal, contentPadding: PaddingValues) {
    val steps = meal.instructions
        .split(Regex("\r?\n"))
        .map { it.trim() }
        .filter { it.isNotBlank() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = contentPadding.calculateBottomPadding() + 24.dp)
    ) {
        item {
            AsyncImage(
                model = meal.thumb,
                contentDescription = meal.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.6f)
            )
        }
        item {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Text(
                    text = translated(meal.name),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (meal.category.isNotBlank()) {
                        AssistChip(onClick = {}, label = { Text(translated(meal.category)) })
                    }
                    if (meal.area.isNotBlank()) {
                        AssistChip(onClick = {}, label = { Text(translated(meal.area)) })
                    }
                }
            }
        }
        item {
            Text(
                text = stringResource(R.string.ingredients),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        items(meal.ingredients) { ing ->
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .size(8.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = translated("${ing.measure} ${ing.name}".trim()),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        item {
            Text(
                text = stringResource(R.string.instructions),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 8.dp)
            )
        }
        itemsIndexed(steps) { index, step ->
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${index + 1}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = translated(step),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}