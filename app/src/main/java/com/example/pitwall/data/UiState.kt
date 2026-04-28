package com.example.pitwall.data
sealed class UiState {
    object Success : UiState()
    data class Error(val messageRes: Int) : UiState()
}