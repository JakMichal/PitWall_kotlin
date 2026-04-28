package com.example.pitwall.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.pitwall.api.RetrofitInstance
import com.example.pitwall.data.Constructor
import com.example.pitwall.data.Driver
import com.example.pitwall.data.DriverResult
import com.example.pitwall.data.FavouriteConstructor
import com.example.pitwall.data.FavouriteDriver
import com.example.pitwall.data.PitWallDatabase
import com.example.pitwall.data.Race
import com.example.pitwall.data.RaceResult
import com.example.pitwall.data.circuitBackgrounds
import com.example.pitwall.data.circuitLaps
import com.example.pitwall.data.circuitLength
import com.example.pitwall.data.circuitTrackImages
import com.example.pitwall.data.constructorsPictures
import com.example.pitwall.data.driversPictures
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import androidx.core.content.edit
import com.example.pitwall.R
import com.example.pitwall.data.UiState
import com.example.pitwall.notification.RaceAlarmReceiver
import com.example.pitwall.widget.WidgetConstants
import kotlin.jvm.java

//ViewModel je trieda, ktorá prežije rotáciu obrazovky.
//keby data boli v composable pri otoceni mobilu by sa vsetko stratilo
//viewmodel zije dlhsie ako UI
class F1ViewModel(application: Application) : AndroidViewModel(application) {
    private val _currentScreen = MutableStateFlow<UiState>(UiState.Success)
    val currentScreen: StateFlow<UiState> = _currentScreen
    private val db = PitWallDatabase.getDatabase(application)
    private val driverDao = db.favouriteDriverDao()
    private val constructorDao = db.favouriteConstructorDao()

    private val _favouriteDrivers = MutableStateFlow<List<FavouriteDriver>>(emptyList())
    val favouriteDrivers: StateFlow<List<FavouriteDriver>> = _favouriteDrivers

    private val _favouriteConstructors = MutableStateFlow<List<FavouriteConstructor>>(emptyList())
    val favouriteConstructors: StateFlow<List<FavouriteConstructor>> = _favouriteConstructors

    //_driverStandings - konvencia oznacenie aby sme vedeli ze je sukromna a iba viewModel
    //don moze zapisovat
    private val _driverStandings = MutableStateFlow<List<Driver>>(emptyList())
    val driverStandings: StateFlow<List<Driver>> = _driverStandings
    //verejny iba na citanie pre UI
    //StateFlow je reaktívny stream, ked sa zmeni hodnota vsetky composable ktore ho sleduju
    //sa automaticky prekresli

    private val _constructorStandings = MutableStateFlow<List<Constructor>>(emptyList())
    val constructorStandings: StateFlow<List<Constructor>> = _constructorStandings

    private val _races = MutableStateFlow<List<Race>>(emptyList())
    val races: StateFlow<List<Race>> = _races

    private val _nextRace = MutableStateFlow<Race?>(null)
    val nextRace: StateFlow<Race?> = _nextRace

    private val _raceResult = MutableStateFlow<List<RaceResult>>(emptyList())
    val raceResult: StateFlow<List<RaceResult>> = _raceResult

    //init blok sa spusti hned pri vytvoreni viewModelu. ihned zacne stahovat data z api
    init {
        loadDriverStandings()
        loadConstructorStandings()
        loadRaces()
        loadRaceResults()

        viewModelScope.launch {
            driverDao.getAll().collect { list ->
                _favouriteDrivers.value = list
            }
        }

        viewModelScope.launch {
            constructorDao.getAll().collect { list ->
                _favouriteConstructors.value = list
            }
        }
    }

    private fun isNetworkAvalaible(): Boolean {
            val connectivityManager = getApplication<Application>()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun loadDriverStandings() {
        //viewmodelscope spusti korutinu naviazanu na zivotny cyklus viewmodelu
        //ked viewModel zanikne korutina sa automaticky zrusi - ziadne memory leaks
        viewModelScope.launch {
            if (!isNetworkAvalaible()) {
                _currentScreen.value = UiState.Error(R.string.no_internet)
                return@launch
            }
            try {
                val response = RetrofitInstance.api.getDriverStandings()
                val standings = response.mrData.standingsTable.standingsLists[0].driverStandings

                _driverStandings.value = standings.map { standing ->
                    Driver(
                        driverId = standing.driver.driverId,
                        number = standing.driver.number,
                        code = standing.driver.code,
                        firstName = standing.driver.firstName,
                        lastName = standing.driver.lastName,
                        nationality = standing.driver.nationality,
                        team = standing.constructors[0].name,
                        points = standing.points.toInt(),
                        wins = standing.wins.toInt(),
                        position = standing.position.toInt(),
                        image = driversPictures[standing.driver.driverId] ?: 0
                    )
                }

                val prefs = application.getSharedPreferences(WidgetConstants.PREFS_NAME, Context.MODE_PRIVATE)
                val top3 = _driverStandings.value.take(3)

                prefs.edit {
                    putString(WidgetConstants.KEY_DRIVER1_NAME, top3[0].fullName)
                        .putInt(WidgetConstants.KEY_DRIVER1_POINTS, top3[0].points)
                        .putString(WidgetConstants.KEY_DRIVER2_NAME, top3[1].fullName)
                        .putInt(WidgetConstants.KEY_DRIVER2_POINTS, top3[1].points)
                        .putString(WidgetConstants.KEY_DRIVER3_NAME, top3[2].fullName)
                        .putInt(WidgetConstants.KEY_DRIVER3_POINTS, top3[2].points)
                }
                _currentScreen.value = UiState.Success
            } catch (e: Exception) {
                _currentScreen.value = UiState.Error(R.string.server_error)
                e.printStackTrace()
            }
        }
    }

    private fun loadConstructorStandings() {
        viewModelScope.launch {
            if (!isNetworkAvalaible()) {
                _currentScreen.value = UiState.Error(R.string.no_internet)
                return@launch
            }
            try {
                val response = RetrofitInstance.api.getConstructorStandings()
                val standings = response.mrData.standingsTable.standingsLists[0].constructorStandings

                _constructorStandings.value = standings.map { standing ->
                    Constructor(
                        constructorId = standing.constructor.constructorId,
                        name = standing.constructor.name,
                        nationality = standing.constructor.nationality,
                        points = standing.points.toInt(),
                        wins = standing.wins.toInt(),
                        position = standing.position.toInt(),
                        image = constructorsPictures[standing.constructor.constructorId] ?: 0
                    )
                }
                val prefs = application.getSharedPreferences(WidgetConstants.PREFS_NAME, Context.MODE_PRIVATE)
                val top3 = _constructorStandings.value.take(3)

                prefs.edit {
                    putString(WidgetConstants.KEY_CONSTRUCTOR1_NAME, top3[0].name)
                        .putInt(WidgetConstants.KEY_CONSTRUCTOR1_POINTS, top3[0].points)
                        .putString(WidgetConstants.KEY_CONSTRUCTOR2_NAME, top3[1].name)
                        .putInt(WidgetConstants.KEY_CONSTRUCTOR2_POINTS, top3[1].points)
                        .putString(WidgetConstants.KEY_CONSTRUCTOR3_NAME, top3[2].name)
                        .putInt(WidgetConstants.KEY_CONSTRUCTOR3_POINTS, top3[2].points)
                }
                _currentScreen.value = UiState.Success
            } catch (e: Exception) {
                _currentScreen.value = UiState.Error(R.string.server_error)
                e.printStackTrace()
            }
        }
    }

    private fun loadRaces() {
        viewModelScope.launch {
            if (!isNetworkAvalaible()) {
                _currentScreen.value = UiState.Error(R.string.no_internet)
                return@launch
            }
            try {
                val response = RetrofitInstance.api.getRaceSchedule()
                val races = response.mrData.raceTable.races
                _races.value = races.map { race ->
                    Race(
                        round = race.round.toInt(),
                        raceName = race.raceName,
                        circuitId = race.circuit.circuitId,
                        circuitName = race.circuit.circuitName,
                        country = race.circuit.location.country,
                        date = race.date,
                        time = race.time.substring(0, 5),
                        laps = circuitLaps[race.circuit.circuitId] ?: 0,
                        length = circuitLength[race.circuit.circuitId] ?: 0.0,
                        backgroundImage = circuitBackgrounds[race.circuit.circuitId] ?: 0,
                        circuitImage = circuitTrackImages[race.circuit.circuitId] ?: 0,
                        firstPractice = race.firstPractice?.let { Pair(it.date, it.time) },
                        secondPractice = race.secondPractice?.let { Pair(it.date, it.time) },
                        thirdPractice = race.thirdPractice?.let { Pair(it.date, it.time) },
                        qualifying = race.qualifying?.let { Pair(it.date, it.time) },
                        sprint = race.sprint?.let { Pair(it.date, it.time) },
                        sprintQualifying = race.sprintQualifying?.let { Pair(it.date, it.time) },
                    )
                }
                loadNextRace()
                _currentScreen.value = UiState.Success
            } catch (e: Exception) {
                _currentScreen.value = UiState.Error(R.string.server_error)
                e.printStackTrace()
            }
        }
    }

    private fun loadNextRace() {
        _nextRace.value = _races.value
            .filter { race -> race.getRaceDateTime() > LocalDateTime.now() }
            .minByOrNull { race -> ChronoUnit.SECONDS.between(
                LocalDateTime.now(),
                race.getRaceDateTime()) }

        _nextRace.value?.let { scheduleRaceReminder(it) }
    }

    private fun loadRaceResults() {
        viewModelScope.launch {
            if (!isNetworkAvalaible()) {
                _currentScreen.value = UiState.Error(R.string.no_internet)
                return@launch
            }
            try {
                val response = RetrofitInstance.api.getRaceResults()
                val races = response.mrData.raceTable.races
                _raceResult.value = races.map { raceResult ->
                    RaceResult(
                        round = raceResult.round.toInt(),
                        raceName = raceResult.raceName,
                        circuitName = raceResult.circuit.circuitName,
                        country = raceResult.circuit.location.country,
                        date = raceResult.date,
                        driverResults = raceResult.results.map { result ->
                            DriverResult(
                                position = result.position.toInt(),
                                driverCode = result.driver.code,
                                driverName = "${result.driver.firstName} ${result.driver.lastName}",
                                team = result.constructor.name,
                                points = result.points.toInt(),
                                status = result.status
                            )
                        }
                    )
                }
                _currentScreen.value = UiState.Success
            } catch (e: Exception) {
                _currentScreen.value = UiState.Error(R.string.server_error)
                e.printStackTrace()
            }
        }
    }

    fun addFavouriteDriver(driverId: String) {
        //spusti korutinu bez brzdenia UI
        viewModelScope.launch {
            driverDao.insert(FavouriteDriver(driverId = driverId))
        }
    }

    fun removeFavouriteDriver(driverId: String) {
        viewModelScope.launch {
            driverDao.delete(FavouriteDriver(driverId = driverId))
        }
    }

    fun addFavouriteConstructor(constructorId: String) {
        //spusti korutinu bez brzdenia UI
        viewModelScope.launch {
            constructorDao.insert(FavouriteConstructor(constructorId = constructorId))
        }
    }

    fun removeFavouriteConstructor(constructorId: String) {
        viewModelScope.launch {
            constructorDao.delete(FavouriteConstructor(constructorId = constructorId))
        }
    }

    private fun scheduleRaceReminder(race: Race) {
        val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) return
        }

        val intent = Intent(application, RaceAlarmReceiver::class.java).apply {
            putExtra(RaceAlarmReceiver.EXTRA_RACE_NAME, race.raceName)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            application,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val raceTime = race.getRaceDateTime()
            .minusHours(1)
            .atZone(java.time.ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            raceTime,
            pendingIntent
        )
    }
    fun refresh() {
        _currentScreen.value = UiState.Success
        loadDriverStandings()
        loadConstructorStandings()
        loadRaces()
        loadRaceResults()
    }
}