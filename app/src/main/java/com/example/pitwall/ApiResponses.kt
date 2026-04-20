package com.example.pitwall

import com.google.gson.annotations.SerializedName

//serializedName je anotacie pre metadata pre gson a hovori ze
// v JSON hladaj kluc givenName a uloz do vlastnosti firstName napr
// bez toho by gson hladal kluc firtsName ktory v json neexistuje
data class DriverResponse(
    @SerializedName("driverId") val driverId: String,
    @SerializedName("permanentNumber") val number: String,
    @SerializedName("code") val code: String,
    @SerializedName("givenName") val firstName: String,
    @SerializedName("familyName") val lastName: String,
    @SerializedName("nationality") val nationality: String
)

data class DriverStandingResponse(
    @SerializedName("position") val position: String,
    @SerializedName("points") val points: String,
    @SerializedName("wins") val wins: String,
    @SerializedName("Driver") val driver: DriverResponse,
    @SerializedName("Constructors") val constructors: List<ConstructorResponse>,
)

data class DriverStandingsListResponse(
    @SerializedName("DriverStandings")
    val driverStandings: List<DriverStandingResponse>
)

data class DriverStandingsTableResponse(
    @SerializedName("StandingsLists")
    val standingsLists: List<DriverStandingsListResponse>
)

data class DriverMRDataResponse(
    @SerializedName("StandingsTable")
    val standingsTable: DriverStandingsTableResponse
)

data class DriverStandingsApiResponse(
    @SerializedName("MRData")
    val mrData: DriverMRDataResponse
)

data class ConstructorResponse(
    @SerializedName("constructorId") val constructorId: String,
    @SerializedName("name") val name: String,
    @SerializedName("nationality") val nationality: String,
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

data class ConstructorStandingsApiResponse(
    @SerializedName("MRData") val mrData: ConstructorMRDataResponse  // ← správny typ
)

data class CircuitResponse(
    @SerializedName("circuitId") val circuitId: String,
    @SerializedName("circuitName") val circuitName: String,
    @SerializedName("Location") val location: LocationResponse,
)

data class LocationResponse(
    @SerializedName("country") val country: String,
)

data class dateTimeResponse(
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String,
)

data class RacesResponse(
    @SerializedName("round") val round: String,
    @SerializedName("raceName") val raceName: String,
    @SerializedName("Circuit") val circuit: CircuitResponse,
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String,
    @SerializedName("Results") val results: List<RaceResultItemResponse> = emptyList(),
    @SerializedName("FirstPractice") val firstPractice: dateTimeResponse? = null,
    @SerializedName("SecondPractice") val secondPractice: dateTimeResponse? = null,
    @SerializedName("ThirdPractice") val thirdPractice: dateTimeResponse? = null,
    @SerializedName("Qualifying") val qualifying: dateTimeResponse? = null,
    @SerializedName("Sprint") val sprint: dateTimeResponse? = null,
    @SerializedName("SprintQualifying") val sprintQualifying: dateTimeResponse? = null,
)

data class RaceTableResponse(
    @SerializedName("Races")
    val Races: List<RacesResponse>
)

data class RacesMRDataResponse(
    @SerializedName("RaceTable")
    val raceTable: RaceTableResponse
)
data class RaceScheduleApiResponse(
    @SerializedName("MRData") val mrData: RacesMRDataResponse
)

data class ResultDriverResponse(
    @SerializedName("givenName") val firstName: String,
    @SerializedName("familyName") val lastName: String,
    @SerializedName("code") val code: String,
)

data class RaceResultItemResponse(
    @SerializedName("position") val position: String,
    @SerializedName("points") val points: String,
    @SerializedName("status") val status: String,
    @SerializedName("Driver") val driver: ResultDriverResponse,
    @SerializedName("Constructor") val constructor: ConstructorResponse,
)

data class RaceResultMRDataResponse(
    @SerializedName("RaceTable") val raceTable: RaceTableResponse
)

data class RaceResultsApiResponse(
    @SerializedName("MRData") val mrData: RaceResultMRDataResponse
)




