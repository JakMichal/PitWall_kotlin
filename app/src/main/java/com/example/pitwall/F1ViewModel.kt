package com.example.pitwall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.Int
import kotlin.String

class F1ViewModel : ViewModel() {

    private val _driverStandings = MutableStateFlow<List<Driver>>(emptyList())
    val driverStandings: StateFlow<List<Driver>> = _driverStandings


    private val _constructorStandings = MutableStateFlow<List<Constructor>>(emptyList())
    val constructorStandings: StateFlow<List<Constructor>> = _constructorStandings

    private val _races = MutableStateFlow<List<Race>>(emptyList())
    val races: StateFlow<List<Race>> = _races

    private val _nextRace = MutableStateFlow<Race?>(null)
    val nextRace: StateFlow<Race?> = _nextRace

    init {
        loadDriverStandings()
        loadConstructorStandings()
        loadRaces()
    }
    private fun loadDriverStandings() {
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
                    println("circuitId: ${race.circuit.circuitId}")
                    Race(
                        round = race.round.toInt(),
                        raceName = race.raceName,
                        circuitId = race.circuit.circuitId,
                        circuitName = race.circuit.circuitName,
                        country = race.circuit.location.country,
                        date = race.date,
                        time = race.time.substring(0, 5)

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
}
