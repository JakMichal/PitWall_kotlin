package com.example.pitwall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.String

class F1ViewModel : ViewModel() {

    private val _driverStandings = MutableStateFlow<List<Driver>>(emptyList())
    val driverStandings: StateFlow<List<Driver>> = _driverStandings

    private val _constructorStandings = MutableStateFlow<List<Constructor>>(emptyList())
    val constructorStandings: StateFlow<List<Constructor>> = _constructorStandings

    init {
        loadDriverStandings()
        loadConstructorStandings()
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
                        points = standing.points.toFloat(),
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
                        points = standing.points.toFloat(),
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
}
