package com.example.pitwall

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pitwall.ui.theme.PitWallTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item { NextRace() }
        item { SeasonCalendar() }
        item { DriverChampionship() }
        item { ContructorChampionship() }
    }
}

@Composable
fun NextRace(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "NEXT RACE", fontSize = 13.sp, color = Color.Red)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, bottom = 10.dp),
                    ) {
                        Text(text = "Monaco Grand Prix", fontSize = 30.sp, color = Color.White)
                        Row() {
                            Text(text = "Circuit de Monaco", fontSize = 15.sp, color = Color.DarkGray)
                            Text(text = " • ", fontSize = 15.sp, color = Color.DarkGray)
                            Text(text = "Round 8", fontSize = 15.sp, color = Color.DarkGray)
                        }
                    }
                    Text(
                        text = "Remaining",
                        fontSize = 30.sp,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                    Row() {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "02", fontSize = 30.sp, color = Color.Red)
                            Text(text = "days", fontSize = 15.sp, color = Color.DarkGray)
                        }
                        Text(" : ", fontSize = 30.sp, color = Color.Red)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "15", fontSize = 30.sp, color = Color.Red)
                            Text(text = "hours", fontSize = 15.sp, color = Color.DarkGray)
                        }
                        Text(" : ", fontSize = 30.sp, color = Color.Red)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "38", fontSize = 30.sp, color = Color.Red)
                            Text(text = "min", fontSize = 15.sp, color = Color.DarkGray)
                        }
                        Text(" : ", fontSize = 30.sp, color = Color.Red)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "22", fontSize = 30.sp, color = Color.Red)
                            Text(text = "sec", fontSize = 15.sp, color = Color.DarkGray)
                        }

                    }

                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 16.dp),
                        contentScale = ContentScale.Fit,
                        painter = painterResource(R.drawable.monaco_circuit),
                        contentDescription = "Monaco circuit",
                        colorFilter = ColorFilter.tint(Color.White),

                        )
                }
            }
        }
}

@Composable
fun SeasonCalendar(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Text(text = "SEASON CALENDAR", fontSize = 13.sp, color = Color.Red)
        LazyRow(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(raceList) {
                currentRace -> RaceCard(race = currentRace, modifier = Modifier.fillParentMaxWidth())
            }
        }
    }
}

@Composable
fun RaceCard(race: Race, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
        ) {
            Text(text = race.country, fontSize = 13.sp, color = Color.Gray)
            Text(text = race.name, fontSize = 16.sp, color = Color.LightGray)
            Row() {
                Text(text = race.date, fontSize = 14.sp, color = Color.Gray)
                Text(text = " • ", fontSize = 15.sp, color = Color.DarkGray)
                Text(text = race.time, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}
@Composable
fun DriverChampionship(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Text(text = "DRIVER CHAMPIONSHIP", fontSize = 13.sp, color = Color.Red)
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val maxPoints = driverTop3.maxOf { it.points }

                driverTop3.forEachIndexed { index, driver ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "${index + 1}",
                            fontSize = 16.sp,
                            color = Color.Red,
                            modifier = Modifier.width(24.dp),
                            lineHeight = 10.sp
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = driver.name, fontSize = 14.sp, color = Color.White, lineHeight = 10.sp)
                            Text(text = driver.team, fontSize = 12.sp, color = Color.Gray, lineHeight = 10.sp)
                        }
                        Text(text = "${driver.points} pts", fontSize = 14.sp, color = Color.Gray)
                    }
                    LinearProgressIndicator(
                        progress = {driver.points.toFloat() / maxPoints.toFloat()},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 3.dp ,bottom = 2.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = Color.DarkGray,
                        drawStopIndicator = {}
                    )
                }
            }
        }
    }
}
@Composable
fun ContructorChampionship(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
    ) {
        Text(text = "CONSTRUCTOR CHAMPIONSHIP", fontSize = 13.sp, color = Color.Red)
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val maxPoints = constructorTop3.maxOf { it.points }

                constructorTop3.forEachIndexed { index, constructor ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "${index + 1}",
                            fontSize = 16.sp,
                            color = Color.Red,
                            modifier = Modifier.width(24.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = constructor.name, fontSize = 14.sp, color = Color.White)
                        }
                        Text(text = "${constructor.points} pts", fontSize = 14.sp, color = Color.Gray)
                    }
                    LinearProgressIndicator(
                        progress = {constructor.points.toFloat() / maxPoints.toFloat()},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 2.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = Color.DarkGray,
                        drawStopIndicator = {}
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPrevieww() {
    PitWallTheme {
        var activeScreen by remember { mutableStateOf("Home") }
        ChangeScreen(
            activeScreen = activeScreen,
            onScreenChange = { activeScreen = it }
        )
    }
}