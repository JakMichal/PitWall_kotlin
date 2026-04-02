package com.example.pitwall

data class Race (
    val name: String,
    val country: String,
    val date: String,
    val time: String
)

val raceList = listOf(
    Race("Australian Grand Prix", "🇦🇺 Australia", "8. mar 2026", "15:00"),
    Race("Chinese Grand Prix", "🇨🇳 China", "15. mar 2026", "15:00"),
    Race("Japanese Grand Prix", "🇯🇵 Japan", "29. mar 2026", "14:00"),
    Race("Miami Grand Prix", "🇺🇸 Miami", "3. máj 2026", "21:00"),
    Race("Canadian Grand Prix", "🇨🇦 Canada", "24. máj 2026", "20:00"),
    Race("Monaco Grand Prix", "🇲🇨 Monaco", "7. jún 2026", "15:00"),
    Race("Spanish Grand Prix", "🇪🇸 Spain", "14. jún 2026", "15:00"),
    Race("Austrian Grand Prix", "🇦🇹 Austria", "28. jún 2026", "15:00"),
    Race("British Grand Prix", "🇬🇧 Britain", "5. júl 2026", "16:00"),
    Race("Belgian Grand Prix", "🇧🇪 Belgium", "19. júl 2026", "15:00"),
    Race("Hungarian Grand Prix", "🇭🇺 Hungary", "26. júl 2026", "15:00"),
    Race("Dutch Grand Prix", "🇳🇱 Netherlands", "23. aug 2026", "15:00"),
    Race("Italian Grand Prix", "🇮🇹 Italy", "6. sep 2026", "15:00"),
    Race("Madrid Grand Prix", "🇪🇸 Madrid", "13. sep 2026", "15:00"),
    Race("Azerbaijan Grand Prix", "🇦🇿 Azerbaijan", "26. sep 2026", "14:00"),
    Race("Singapore Grand Prix", "🇸🇬 Singapore", "11. okt 2026", "14:00"),
    Race("United States Grand Prix", "🇺🇸 USA", "25. okt 2026", "21:00"),
    Race("Mexico City Grand Prix", "🇲🇽 Mexico", "1. nov 2026", "21:00"),
    Race("São Paulo Grand Prix", "🇧🇷 Brazil", "8. nov 2026", "18:00"),
    Race("Las Vegas Grand Prix", "🇺🇸 Las Vegas", "21. nov 2026", "07:00"),
    Race("Qatar Grand Prix", "🇶🇦 Qatar", "29. nov 2026", "20:00"),
    Race("Abu Dhabi Grand Prix", "🇦🇪 Abu Dhabi", "6. dec 2026", "14:00")
)

data class Driver(
    val name: String,
    val team: String,
    val points: Int
)

data class Constructor(
    val name: String,
    val points: Int
)

val driverTop3 = listOf(
    Driver("G. Russell", "Mercedes", 135),
    Driver("C. Leclerc", "Ferrari", 90),
    Driver("L. Norris", "McLaren", 46)
)

val constructorTop3 = listOf(
    Constructor("Mercedes", 203),
    Constructor("Ferrari", 136),
    Constructor("McLaren", 86)
)
