package com.example.pitwall

import retrofit2.http.GET

// definicia api volani
interface F1ApiService {
    @GET("current/driverStandings.json") //base url z retrofitInstancie
    suspend fun getDriverStandings(): DriverStandingsApiResponse // korutina moze byt
    //pozastavena bez blokovania hlavneho vlakna, inak by UI zamrzlo kvoli sietovim volaniam
    //suspend funkcie sa smu volať len z korutinoveho kontextu napr viewModelScope.launch

    @GET("current/constructorStandings.json")
    suspend fun getConstructorStandings(): ConstructorStandingsApiResponse

    @GET("current.json")
    suspend fun getRaceSchedule(): RaceScheduleApiResponse

    @GET("current/results.json?limit=1000")
    suspend fun getRaceResults(): RaceResultsApiResponse


}