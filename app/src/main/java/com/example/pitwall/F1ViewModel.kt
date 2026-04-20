package com.example.pitwall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

//ViewModel je trieda, ktorá prežije rotáciu obrazovky.
//keby data boli v composable pri otoceni mobilu by sa vsetko stratilo
//viewmodel zije dlhsie ako UI
class F1ViewModel : ViewModel() {

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
    }



    private fun loadDriverStandings() {
        //viewmodelscope spusti korutinu naviazanu na zivotny cyklus viewmodelu
        //ked viewModel zanikne korutina sa automaticky zrusi - ziadne memory leaks
        viewModelScope.launch {
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
                        position = standing.position.toInt()
                    )
                }
            } catch (e: Exception) {
                // zatiaľ len vypíšeme chybu
                e.printStackTrace()
            }
        }
    }

    private fun loadConstructorStandings() {
        viewModelScope.launch {
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
                        position = standing.position.toInt()
                    )
                }
            } catch (e: Exception) {
                // zatiaľ len vypíšeme chybu
                e.printStackTrace()
            }
        }
    }

    private fun loadRaces() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getRaceSchedule()
                val races = response.mrData.raceTable.Races
                _races.value = races.map { race ->
                    Race(
                        round = race.round.toInt(),
                        raceName = race.raceName,
                        circuitId = race.circuit.circuitId,
                        circuitName = race.circuit.circuitName,
                        country = race.circuit.location.country,
                        date = race.date,
                        time = race.time.substring(0, 5),
                        firstPractice = race.firstPractice?.let { Pair(it.date, it.time) },
                        secondPractice = race.secondPractice?.let { Pair(it.date, it.time) },
                        thirdPractice = race.thirdPractice?.let { Pair(it.date, it.time) },
                        qualifying = race.qualifying?.let { Pair(it.date, it.time) },
                        sprint = race.sprint?.let { Pair(it.date, it.time) },
                        sprintQualifying = race.sprintQualifying?.let { Pair(it.date, it.time) },
                    )
                }
                loadNextRace()
            } catch (e: Exception) {
                // zatiaľ len vypíšeme chybu
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
    }

    private fun loadRaceResults() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getRaceResults()
                val races = response.mrData.raceTable.Races
                _raceResult.value = races.map { raceResult ->
                    RaceResult(
                        round = raceResult.round.toInt(),
                        raceName = raceResult.raceName,
                        circuitName = raceResult.circuit.circuitName,
                        country = raceResult.circuit.location.country,
                        date = raceResult.date,
                        driverResults = raceResult.results.map { result ->
                            DriverResult (
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
            } catch (e: Exception) {
                // zatiaľ len vypíšeme chybu
                e.printStackTrace()
            }
        }
    }
}
