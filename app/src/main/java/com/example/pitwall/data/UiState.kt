package com.example.pitwall.data

/**
 * Represents the current UI state of the application.
 *
 * Used by F1ViewModel to communicate the result of API calls to the UI layer.
 * The error message is stored as a string resource ID (not a raw string) so that
 * language switching works correctly — the resource is resolved in the UI layer.
 */
sealed class UiState {

    /**
     * Data was loaded successfully and the screen content is ready to be displayed.
     */
    object Success : UiState()

    /**
     * An error occurred during data loading.
     *
     * @property messageRes String resource ID of the error message to display
     * (e.g. R.string.no_internet or R.string.server_error).
     */
    data class Error(val messageRes: Int) : UiState()
}