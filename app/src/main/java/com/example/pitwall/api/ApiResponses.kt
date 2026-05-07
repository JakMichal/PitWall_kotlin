package com.example.pitwall.api

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) for deserializing JSON responses from Jolpica F1 API using GSON.
 *
 * Each class corresponds to a single level of the nested JSON structure
 * The [@serializedName] annotation maps JSON keys to Kotlin properties -
 * without it, GSON would look for keys based on Kotlin property names that do not exist in the JSON
 */

/**
 * DTO for a single driver from API.
 * @property driverId Unique driver identifier.
 * @property number Driver number.
 * @property code Three-letter driver code.
 * @property firstName First name of driver.
 * @property lastName Last name of driver.
 * @property nationality Driver nationality.
 */
data class DriverResponse(
    @SerializedName("driverId") val driverId: String,
    @SerializedName("permanentNumber") val number: String,
    @SerializedName("code") val code: String,
    @SerializedName("givenName") val firstName: String,
    @SerializedName("familyName") val lastName: String,
    @SerializedName("nationality") val nationality: String
)

/**
 * DTO for a single entry in the driver standings.
 * @property position Position in the championship.
 * @property points Number of points.
 * @property wins Number of wins.
 * @property driver Embedded driver data.
 * @property constructors List of the driver's teams (in API it is list, but typically there is only one team).
 */
data class DriverStandingResponse(
    @SerializedName("position") val position: String,
    @SerializedName("points") val points: String,
    @SerializedName("wins") val wins: String,
    @SerializedName("Driver") val driver: DriverResponse,
    @SerializedName("Constructors") val constructors: List<ConstructorResponse>,
)

/** DTO for a list of racers' positions within a single season. */
data class DriverStandingsListResponse(
    @SerializedName("DriverStandings")
    val driverStandings: List<DriverStandingResponse>
)

/** DTO for the standings table — contains a list [DriverStandingsListResponse]. */
data class DriverStandingsTableResponse(
    @SerializedName("StandingsLists")
    val standingsLists: List<DriverStandingsListResponse>
)

/** DTO for the MRData root object in the racer rankings' response. */
data class DriverMRDataResponse(
    @SerializedName("StandingsTable")
    val standingsTable: DriverStandingsTableResponse
)

/** DTO for the entire response from the racer rankings API endpoint. */
data class DriverStandingsApiResponse(
    @SerializedName("MRData")
    val mrData: DriverMRDataResponse
)

/**
 * DTO for a constructor (team) from the API.
 * @property constructorId Unique identifier for the team.
 * @property name Display name of the team.
 * @property nationality Nationality of the team.
 */
data class ConstructorResponse(
    @SerializedName("constructorId") val constructorId: String,
    @SerializedName("name") val name: String,
    @SerializedName("nationality") val nationality: String,
)

/** DTO for a single entry in the Constructors' Championship. */
data class ConstructorStandingResponse(
    @SerializedName("position") val position: String,
    @SerializedName("points") val points: String,
    @SerializedName("wins") val wins: String,
    @SerializedName("Constructor") val constructor: ConstructorResponse
)

/** DTO for a list of constructors' standings within a single season. */
data class ConstructorStandingsListResponse(
    @SerializedName("ConstructorStandings")
    val constructorStandings: List<ConstructorStandingResponse>
)

/** DTO for the constructors' standings table. */
data class ConstructorStandingsTableResponse(
    @SerializedName("StandingsLists")
    val standingsLists: List<ConstructorStandingsListResponse>
)

/** DTO for the root MRData object in the constructors' standings response. */
data class ConstructorMRDataResponse(
    @SerializedName("StandingsTable")
    val standingsTable: ConstructorStandingsTableResponse
)

/** DTO for the entire response of the constructors' standings API endpoint. */
data class ConstructorStandingsApiResponse(
    @SerializedName("MRData") val mrData: ConstructorMRDataResponse  // ← správny typ
)

/**
 * DTO for a circuit (track).
 * @property circuitId Unique identifier for the circuit.
 * @property circuitName Name of the circuit.
 * @property location Location of the circuit, including the country.
 */
data class CircuitResponse(
    @SerializedName("circuitId") val circuitId: String,
    @SerializedName("circuitName") val circuitName: String,
    @SerializedName("Location") val location: LocationResponse,
)

/**
 * DTO for a circuit location.
 * @property country The country where the circuit is located.
 */
data class LocationResponse(
    @SerializedName("country") val country: String,
)

/**
 * DTO for the date and time of a single race session.
 * @property date Date in "yyyy-MM-dd" format.
 * @property time Time in "HH:mm:ssZ" format, UTC.
 */
data class DateTimeResponse(
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String,
)

/**
 * DTO for a single race from the season schedule.
 * Optional sessions (practices, qualifying, sprint) are null
 * if they are not held on that race weekend.
 */
data class RacesResponse(
    @SerializedName("round") val round: String,
    @SerializedName("raceName") val raceName: String,
    @SerializedName("Circuit") val circuit: CircuitResponse,
    @SerializedName("date") val date: String,
    @SerializedName("time") val time: String,
    @SerializedName("Results") val results: List<RaceResultItemResponse> = emptyList(),
    @SerializedName("FirstPractice") val firstPractice: DateTimeResponse? = null,
    @SerializedName("SecondPractice") val secondPractice: DateTimeResponse? = null,
    @SerializedName("ThirdPractice") val thirdPractice: DateTimeResponse? = null,
    @SerializedName("Qualifying") val qualifying: DateTimeResponse? = null,
    @SerializedName("Sprint") val sprint: DateTimeResponse? = null,
    @SerializedName("SprintQualifying") val sprintQualifying: DateTimeResponse? = null,
)

/** DTO for the race table within MRData. */
data class RaceTableResponse(
    @SerializedName("Races")
    val races: List<RacesResponse>
)

/** DTO for the root MRData object in the race schedule API response. */
data class RacesMRDataResponse(
    @SerializedName("RaceTable")
    val raceTable: RaceTableResponse
)

/** DTO for the complete API response of the race schedule endpoint. */
data class RaceScheduleApiResponse(
    @SerializedName("MRData") val mrData: RacesMRDataResponse
)

/**
 * DTO for a driver in the context of race results.
 * Contains only the fields required to display results.
 */
data class ResultDriverResponse(
    @SerializedName("givenName") val firstName: String,
    @SerializedName("familyName") val lastName: String,
    @SerializedName("code") val code: String,
)

/**
 * DTO for the result of a single driver in a race.
 * @property position Finishing position.
 * @property points Points scored.
 * @property status Finishing status (e.g. "Finished", "DNF").
 * @property driver Driver data.
 * @property constructor Driver's constructor data.
 */
data class RaceResultItemResponse(
    @SerializedName("position") val position: String,
    @SerializedName("points") val points: String,
    @SerializedName("status") val status: String,
    @SerializedName("Driver") val driver: ResultDriverResponse,
    @SerializedName("Constructor") val constructor: ConstructorResponse,
)

/** DTO for the root MRData object in the race results API response. */
data class RaceResultMRDataResponse(
    @SerializedName("RaceTable") val raceTable: RaceTableResponse
)

/** DTO for the complete API response of the race results' endpoint. */
data class RaceResultsApiResponse(
    @SerializedName("MRData") val mrData: RaceResultMRDataResponse
)




