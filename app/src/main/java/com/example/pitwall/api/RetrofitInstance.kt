package com.example.pitwall.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object providing a single Retrofit client instance throughout the app.
 *
 * Design patterns used: Singleton (Kotlin `object`) + Lazy initialization.
 * Retrofit is not created at app startup — it is instantiated only when first used,
 * saving resources during initialization.
 *
 * Base URL: Jolpica mirror of the Ergast F1 API.
 */
object RetrofitInstance {

    /** Base URL for all API calls. All endpoints are relative to this address. */
    private const val BASE_URL = "https://api.jolpi.ca/ergast/f1/"

    /**
     * Lazily initialized implementation of [F1ApiService].
     *
     * Retrofit dynamically generates an implementation of the [F1ApiService] interface
     * based on its annotations. GsonConverterFactory automatically deserializes
     * JSON responses into the corresponding DTO classes.
     */
    val api: F1ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(F1ApiService::class.java)
    }
}