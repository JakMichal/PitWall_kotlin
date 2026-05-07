package com.example.pitwall.data

import com.example.pitwall.R
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Domain model representing an F1 driver.
 *
 * @property driverId The driver's unique identifier from the API (e.g., "max_verstappen").
 * @property number The driver's race number.
 * @property code The driver's three-letter code (e.g., "VER").
 * @property firstName The driver's first name.
 * @property lastName The driver's last name.
 * @property nationality The driver's nationality.
 * @property team The name of the team the driver races for (as a string — intentional simplification).
 * @property points Total points in the current season.
 * @property wins Number of wins in the current season.
 * @property position Current position in the championship.
 * @property image Resource ID of the racer's image, or 0 if no image is available.
 */
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
/**
 * Domain model representing an F1 constructor (team).
 *
 * @property constructorId Unique team identifier from the API (e.g., "red_bull").
 * @property name Display name of the team.
 * @property nationality Team nationality.
 * @property points Total number of points in the current season.
 * @property wins Number of wins in the current season.
 * @property position Current position in the constructors championship.
 * @property image Resource ID of the team logo, or 0 if the logo is not available.
 */
data class Constructor(
    val constructorId: String,
    val name: String,
    val nationality: String,
    val points: Int,
    val wins: Int,
    val position: Int,
    val image: Int
)
/**
 * A helper model used in UI-level ranking lists.
 * It is used to display both drivers and constructors consistently within the same composable.
 *
 * @property id Item identifier (driverId or constructorId).
 * @property name Display name of the item.
 * @property points Number of points.
 * @property subTitle Optional subtitle (e.g., team name for drivers).
 */
data class StandingItem(
    val id: String,
    val name: String,
    val points: Int,
    val subTitle: String? = null
)

/**
 * A domain model representing a single F1 race.
 *
 * Static circuit data (number of laps, length, images) is loaded from local maps
 * [circuitLaps], [circuitLength], [circuitBackgrounds], and [circuitTrackImages], because
 * the Jolpica API does not provide this data (paid endpoints).
 *
 * @property round The lap number in the season.
 * @property raceName The race name.
 * @property circuitId The unique circuit identifier from the API.
 * @property circuitName The displayed circuit name.
 * @property country The country where the race takes place.
 * @property date Race date in "yyyy-MM-dd" format.
 * @property time Start time in "HH:mm" format in UTC.
 * @property laps Number of race laps (static data).
 * @property length Circuit length in km (static data).
 * @property backgroundImage Resource ID of the race card background.
 * @property circuitImage Resource ID of the circuit map image.
 * @property firstPractice Date and time of the first practice session, or null if not taking place.
 * @property secondPractice Date and time of the second practice session, or null if not taking place.
 * @property thirdPractice Date and time of the third practice session, or null if not taking place.
 * @property qualifying Date and time of qualifying, or null if not taking place.
 * @property sprint Date and time of the sprint, or null if it is not a sprint weekend.
 * @property sprintQualifying Date and time of sprint qualifying, or null.
 */
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
    /**
     * Converts the race date and time from UTC to the device's local time.
     *
     * The API returns times in UTC. This function converts them to [LocalDateTime]
     * based on the device's time zone so that the countdown timer displays the correct time.
     *
     * @return The race date and time in the device's local time zone.
     */
    fun getRaceDateTime() : LocalDateTime {
        val dateTime = "${date}T${time}:00" //T - is format for ISO_LOCAL_DATE_TIME
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .atZone(ZoneOffset.UTC) //14:00 UTC
            .withZoneSameInstant(ZoneId.systemDefault()) //UTC from device 14:00 -> 16:00 UTC+2
            .toLocalDateTime() // return localDateTime instead of ZonedDateTime
    }

}

/**
 * Reusult of single drive in a single race.
 *
 * @property position Finished position in a single race.
 * @property driverCode Three-letter driver code.
 * @property driverName Driver's full name.
 * @property team Driver's tema name.
 * @property points Number of points earned.
 * @property status Finish status (e.g. 'Finished', '+1 Lap', 'DNF').
 */
data class DriverResult(
    val position: Int,
    val driverCode: String,
    val driverName: String,
    val team: String,
    val points: Int,
    val status: String
)

/**
 * Results for the entire race, including the standings for all drivers.
 *
 * @property round The round number in the season.
 * @property raceName The race name.
 * @property circuitName The circuit name.
 * @property country The host country.
 * @property date The race date.
 * @property driverResults A sorted list of driver results.
 */
data class RaceResult(
    val round: Int,
    val raceName: String,
    val circuitName: String,
    val country: String,
    val date: String,
    val driverResults: List<DriverResult>
)

/**
 * Static data: the number of laps for each circuit indentified by circuidId.
 * This data is hard-coded because tje Jolpica API does not provide it for free.
 */
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

/** Static data: circuit length in km for each circuit identified by circuidId. */
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

/** Static data: resource ID of the race card background for each circuit. */
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

/** Static data: resource ID of the circuit map image for each circuit. */
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

/** Mapping of driverId to the resource ID of the driver's photo. */
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

/** Mapping of constructorId to the resource ID of the constructor's logo. */
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

/**
 * Race status in the season calendar.
 *
 * - FINISHED - the race has already taken place.
 * - NEXT - the next upcoming race.
 * - UPCOMING - a race in the future but not the next one.
 */
enum class RaceStatus {
    FINISHED, NEXT, UPCOMING
}