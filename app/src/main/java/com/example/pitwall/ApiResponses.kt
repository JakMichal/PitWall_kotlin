package com.example.pitwall

import com.google.gson.annotations.SerializedName

data class DriverResponse(
    @SerializedName("driverId") val driverId: String,
    @SerializedName("permanentNumber") val number: String,
    @SerializedName("code") val code: String,
    @SerializedName("givenName") val firstName: String,
    @SerializedName("familyName") val lastName: String,
    @SerializedName("nationality") val nationality: String
)

data class ConstructorResponse(
    @SerializedName("constructorId") val constructorId: String,
    @SerializedName("name") val name: String,
    @SerializedName("nationality") val nationality: String,
)

data class DriverStandingResponse(
    @SerializedName("position") val position: String,
    @SerializedName("points") val points: String,
    @SerializedName("wins") val wins: String,
    @SerializedName("Driver") val driver: DriverResponse,
    @SerializedName("Constructors") val constructors: List<ConstructorResponse>,
)

data class StandingsListResponse(
    @SerializedName("DriverStandings")
    val driverStandings: List<DriverStandingResponse>
)

data class StandingsTableResponse(
    @SerializedName("StandingsLists")
    val standingsLists: List<StandingsListResponse>
)

data class MRDataResponse(
    @SerializedName("StandingsTable")
    val standingsTable: StandingsTableResponse
)

data class DriverStandingsApiResponse(
    @SerializedName("MRData")
    val mrData: MRDataResponse
)

data class ConstructorStandingsApiResponse(
    @SerializedName("MRData") val mrData: ConstructorMRDataResponse  // ← správny typ
)

data class RaceScheduleApiResponse(
    @SerializedName("MRData") val mrData: MRDataResponse
)

data class RaceResultsApiResponse(
    @SerializedName("MRData") val mrData: MRDataResponse
)

data class ConstructorStandingResponse(
    @SerializedName("position") val position: String,
    @SerializedName("points") val points: String,
    @SerializedName("wins") val wins: String,
    @SerializedName("Constructor") val constructor: ConstructorResponse
)

data class ConstructorStandingsListResponse(
    @SerializedName("ConstructorStandings")
    val constructorStandings: List<ConstructorStandingResponse>
)

data class ConstructorStandingsTableResponse(
    @SerializedName("StandingsLists")
    val standingsLists: List<ConstructorStandingsListResponse>
)

data class ConstructorMRDataResponse(
    @SerializedName("StandingsTable")
    val standingsTable: ConstructorStandingsTableResponse
)
