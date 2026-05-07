package com.example.pitwall.api

import retrofit2.http.GET

/**
 * Retrofit interface defining HTTP calls to the Jolpica/Ergast F1 API.
 *
 * Each function is marked as [suspend], allowing it to be called from a coroutine
 * without blocking the main (UI) thread. All endpoints are relative to the base URL
 * defined in [RetrofitInstance].
 */
interface F1ApiService {

    /**
     * Fetches the drivers' standings for the current season.
     * @return [DriverStandingsApiResponse] containing the full API response structure.
     */
    @GET("current/driverStandings.json")
    suspend fun getDriverStandings(): DriverStandingsApiResponse

    /**
     * Fetches the Constructors' Championship standings for the current season.
     * @return [ConstructorStandingsApiResponse] containing the full API response structure.
     */
    @GET("current/constructorStandings.json")
    suspend fun getConstructorStandings(): ConstructorStandingsApiResponse

    /**
     * Fetches the race schedule for the current season.
     * @return [RaceScheduleApiResponse] with the list of all races.
     */
    @GET("current.json")
    suspend fun getRaceSchedule(): RaceScheduleApiResponse

    /**
     * Fetches the results of all completed races in the current season.
     * The `limit=1000` parameter ensures the API returns results for the full season in one request.
     * @return [RaceResultsApiResponse] with the list of race results.
     */
    @GET("current/results.json?limit=1000")
    suspend fun getRaceResults(): RaceResultsApiResponse


}