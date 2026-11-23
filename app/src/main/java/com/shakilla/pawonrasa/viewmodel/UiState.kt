package com.shakilla.pawonrasa.viewmodel

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data object Error : UiState<Nothing>
}

fun <T> Result<T>.toUiState(): UiState<T> =
    fold(onSuccess = { UiState.Success(it) }, onFailure = { UiState.Error })
