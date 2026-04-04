package com.example.pitwall

data class Driver(
    val driverId: String,
    val number: String,
    val code: String,
    val firstName: String,
    val lastName: String,
    val nationality: String,
    val team: String,
    val points: Float,
    val wins: Int,
    val position: Int,
    val isFavourite: Boolean = false
) {
    val fullName get() = "$firstName $lastName"
}

data class Constructor(
    val constructorId: String,
    val name: String,
    val nationality: String,
    val points: Float,
    val wins: Int,
    val position: Int,
    val isFavourite: Boolean = false
)

data class Race(
    val round: Int,
    val raceName: String,
    val circuitId: String,
    val circuitName: String,
    val country: String,
    val date: String,
    val time: String,
    val laps: Int = circuitLaps[circuitId] ?: 0,
    val length: Double = circuitLength[circuitId] ?: 0.0
)

data class RaceResult(
    val round: Int,
    val raceName: String,
    val circuitName: String,
    val country: String,
    val date: String,
    val results: List<DriverResult>
)

data class DriverResult(
    val position: Int,
    val driverCode: String,
    val driverName: String,
    val team: String,
    val points: Float,
    val status: String   // "Finished", "+1 Lap", "DNF"...
)

// Statické dáta okruhov — počet kôl
val circuitLaps = mapOf(
    "albert_park"        to 58,
    "shanghai"           to 56,
    "suzuka"             to 53,
    "bahrain"            to 57,
    "jeddah"             to 50,
    "miami"              to 57,
    "imola"              to 63,
    "monaco"             to 78,
    "villeneuve"         to 70,
    "catalunya"          to 66,
    "red_bull_ring"      to 71,
    "silverstone"        to 52,
    "hungaroring"        to 70,
    "spa"                to 44,
    "zandvoort"          to 72,
    "monza"              to 53,
    "baku"               to 51,
    "marina_bay"         to 62,
    "americas"           to 56,
    "rodriguez"          to 71,
    "interlagos"         to 71,
    "las_vegas"          to 50,
    "losail"             to 57,
    "yas_marina"         to 58
)

// Statické dáta okruhov — dĺžka v km
val circuitLength = mapOf(
    "albert_park"        to 5.278,
    "shanghai"           to 5.451,
    "suzuka"             to 5.807,
    "bahrain"            to 5.412,
    "jeddah"             to 6.174,
    "miami"              to 5.412,
    "imola"              to 4.909,
    "monaco"             to 3.337,
    "villeneuve"         to 4.361,
    "catalunya"          to 4.657,
    "red_bull_ring"      to 4.318,
    "silverstone"        to 5.891,
    "hungaroring"        to 4.381,
    "spa"                to 7.004,
    "zandvoort"          to 4.259,
    "monza"              to 5.793,
    "baku"               to 6.003,
    "marina_bay"         to 4.940,
    "americas"           to 5.513,
    "rodriguez"          to 4.304,
    "interlagos"         to 4.309,
    "las_vegas"          to 6.201,
    "losail"             to 5.380,
    "yas_marina"         to 5.281
)