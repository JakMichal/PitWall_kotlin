package com.example.pitwall

import retrofit2.http.GET

interface F1ApiService {
    @GET("current/driverStandings.json")
    suspend fun getDriverStandings(): DriverStandingsApiResponse

    @GET("current/constructorStandings.json")
    suspend fun getConstructorStandings(): ConstructorStandingsApiResponse

    @GET("current.json")
    suspend fun getRaceSchedule(): RaceScheduleApiResponse

    @GET("current/results.json?limit=1000")
    suspend fun getRaceResults(): RaceResultsApiResponse


}