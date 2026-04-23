package com.example.pitwall.data

import com.example.pitwall.R
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class Driver(
    val driverId: String,
    val number: String,
    val code: String,
    val firstName: String,
    val lastName: String,
    val nationality: String,
    val team: String,
    val points: Int,
    val wins: Int,
    val position: Int,
    val isFavourite: Boolean = false,
    val image: Int
) {
    val fullName get() = "$firstName $lastName"
}

data class Constructor(
    val constructorId: String,
    val name: String,
    val nationality: String,
    val points: Int,
    val wins: Int,
    val position: Int,
    val image: Int
)

data class StandingItem(
    val id: String,
    val name: String,
    val points: Int,
    val subTitle: String? = null
)

data class Race(
    val round: Int,
    val raceName: String,
    val circuitId: String,
    val circuitName: String,
    val country: String,
    val date: String,
    val time: String,
    val laps: Int,
    val length: Double,
    val backgroundImage: Int,
    val circuitImage: Int,
    val firstPractice: Pair<String, String>? = null,
    val secondPractice: Pair<String, String>? = null,
    val thirdPractice: Pair<String, String>? = null,
    val qualifying: Pair<String, String>? = null,
    val sprint: Pair<String, String>? = null,
    val sprintQualifying: Pair<String, String>? = null,
) {
    fun getRaceDateTime() : LocalDateTime {
        val dateTime = "${date}T${time}:00" //T - lebo je to len format ako standard pre ISO_LOCAL_DATE_TIME
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .atZone(ZoneOffset.UTC) //14:00 UTC
            .withZoneSameInstant(ZoneId.systemDefault()) //UTC so zariadenia 14:00 -> 16:00 UTC+2
            .toLocalDateTime() // aby to vratile localDateTime a nie ZonedDateTime pre pracu s casom
    }

}


data class DriverResult(
    val position: Int,
    val driverCode: String,
    val driverName: String,
    val team: String,
    val points: Int,
    val status: String
)

data class RaceResult(
    val round: Int,
    val raceName: String,
    val circuitName: String,
    val country: String,
    val date: String,
    val driverResults: List<DriverResult>
)

// Statické dáta okruhov — počet kôl
val circuitLaps = mapOf(
    "albert_park"        to 58,
    "shanghai"           to 56,
    "suzuka"             to 53,
    "bahrain"            to 57,
    "jeddah"             to 50,
    "miami"              to 57,
    "villeneuve"         to 70,
    "monaco"             to 78,
    "catalunya"          to 66,
    "red_bull_ring"      to 71,
    "silverstone"        to 52,
    "spa"                to 44,
    "hungaroring"        to 70,
    "zandvoort"          to 72,
    "monza"              to 53,
    "madring"            to 56,
    "baku"               to 51,
    "marina_bay"         to 62,
    "americas"           to 56,
    "rodriguez"          to 71,
    "interlagos"         to 71,
    "vegas"              to 50,
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
    "villeneuve"         to 4.361,
    "monaco"             to 3.337,
    "catalunya"          to 4.657,
    "red_bull_ring"      to 4.318,
    "silverstone"        to 5.891,
    "spa"                to 7.004,
    "hungaroring"        to 4.381,
    "zandvoort"          to 4.259,
    "monza"              to 5.793,
    "madring"            to 5.474,
    "baku"               to 6.003,
    "marina_bay"         to 4.940,
    "americas"           to 5.513,
    "rodriguez"          to 4.304,
    "interlagos"         to 4.309,
    "vegas"              to 6.201,
    "losail"             to 5.380,
    "yas_marina"         to 5.281
)

val circuitBackgrounds = mapOf(
    "albert_park"        to R.drawable.albertparkbackground,
    "shanghai"           to R.drawable.shanghaibackground,
    "suzuka"             to R.drawable.suzukabackground,
    "bahrain"            to R.drawable.bahrainbackground,
    "jeddah"             to R.drawable.jeddahbackground,
    "miami"              to R.drawable.miamibackground,
    "villeneuve"         to R.drawable.villenuevebackground,
    "monaco"             to R.drawable.monaco_background,
    "catalunya"          to R.drawable.catalyunabackground,
    "red_bull_ring"      to R.drawable.redbullringbackground,
    "silverstone"        to R.drawable.silverstonebackground,
    "spa"                to R.drawable.spabackground,
    "hungaroring"        to R.drawable.hungaroringbackground,
    "zandvoort"          to R.drawable.zandvoortbackground,
    "monza"              to R.drawable.monzabackground,
    "madring"            to R.drawable.madringbackground,
    "baku"               to R.drawable.bakubackground,
    "marina_bay"         to R.drawable.marinabaybackground,
    "americas"           to R.drawable.americasbackground,
    "rodriguez"          to R.drawable.rodriguezbackground,
    "interlagos"         to R.drawable.interlagosbackground,
    "vegas"              to R.drawable.lasvegasbackground,
    "losail"             to R.drawable.losailbackground,
    "yas_marina"         to R.drawable.yasmarinabackground
)

val circuitTrackImages = mapOf(
    "albert_park"        to R.drawable.albertparkcircuit,
    "shanghai"           to R.drawable.shanghaicircuit,
    "suzuka"             to R.drawable.suzukacircuit,
    "bahrain"            to R.drawable.bahraincircuit,
    "jeddah"             to R.drawable.jeddahcircuit,
    "miami"              to R.drawable.miamicircuit,
    "villeneuve"         to R.drawable.villenuevecircuit,
    "monaco"             to R.drawable.monaco_circuit,
    "catalunya"          to R.drawable.catalyunacircuit,
    "red_bull_ring"      to R.drawable.redbullringcircuit,
    "silverstone"        to R.drawable.silverstonecircuit,
    "spa"                to R.drawable.spacircuit,
    "hungaroring"        to R.drawable.hungaroringcircuit,
    "zandvoort"          to R.drawable.zandvoortcircuit,
    "monza"              to R.drawable.monzacircuit,
    "madring"            to R.drawable.madringcircuit,
    "baku"               to R.drawable.bakucircuit,
    "marina_bay"         to R.drawable.marinabaycircuit,
    "americas"           to R.drawable.americacircuit,
    "rodriguez"          to R.drawable.rodriguezcircuit,
    "interlagos"         to R.drawable.interlagoscircuit,
    "vegas"              to R.drawable.lasvegascircuit,
    "losail"             to R.drawable.losailcircuit,
    "yas_marina"         to R.drawable.yasmarinacircuit
)

val driversPictures = mapOf(
    "albon"             to R.drawable.albon,
    "alonso"            to R.drawable.fernandoalonso,
    "antonelli"         to R.drawable.kimiantonelli,
    "bearman"           to R.drawable.oliverbearman,
    "bortoleto"         to R.drawable.gabrielbortoleto,
    "bottas"            to R.drawable.valteribottas,
    "colapinto"         to R.drawable.francocollapinto,
    "gasly"             to R.drawable.pierregasly,
    "hadjar"            to R.drawable.isaachadjar,
    "hamilton"          to R.drawable.lewishamilton,
    "hulkenberg"        to R.drawable.nicohulkenberg,
    "lawson"            to R.drawable.liamlawson,
    "leclerc"           to R.drawable.charlesleclerc,
    "arvid_lindblad"    to R.drawable.arvinlindblad,
    "norris"            to R.drawable.landonorris,
    "ocon"              to R.drawable.estebanocon,
    "piastri"           to R.drawable.oscarpiastri,
    "perez"             to R.drawable.sergioperez,
    "sainz"             to R.drawable.carlossainz,
    "stroll"            to R.drawable.lancestroll,
    "max_verstappen"    to R.drawable.maxverstappen,
    "russell"            to R.drawable.georgerussel,
)

val constructorsPictures = mapOf(
    "mercedes"       to R.drawable.mercedes,
    "ferrari"        to R.drawable.ferrari,
    "mclaren"        to R.drawable.mclaren,
    "red_bull"       to R.drawable.redbull,
    "aston_martin"   to R.drawable.astonmartin,
    "alpine"         to R.drawable.alpine,
    "williams"       to R.drawable.williams,
    "haas"           to R.drawable.haas,
    "audi"           to R.drawable.audi,
    "cadillac"       to R.drawable.cadillac,
    "audi"           to R.drawable.audi,
    "rb"             to R.drawable.racingbulls
)

enum class RaceStatus {
    FINISHED, NEXT, UPCOMING
}