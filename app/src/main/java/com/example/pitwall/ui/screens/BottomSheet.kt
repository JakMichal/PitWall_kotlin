package com.example.pitwall.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pitwall.R
import com.example.pitwall.data.Race
import com.example.pitwall.data.RaceResult
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun RaceDetailSheet(
    race: Race,
    raceResult: List<RaceResult>,
) {
    val results = raceResult.find { it.round == race.round }
    val isFinished = results != null && results.driverResults.isNotEmpty()
    var selectedTab by remember { mutableIntStateOf(0)}
    val tabs = if (isFinished) {
        listOf(
            stringResource(R.string.tab_results),
            stringResource(R.string.tab_circuit),
            stringResource(R.string.tab_schedule))
    } else {
        listOf(
            stringResource(R.string.tab_circuit),
            stringResource(R.string.tab_schedule))
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(race.raceName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        TabRow(
            selectedTab,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {selectedTab = index},
                    text = { Text(title)}
                )
            }
        }
        when (selectedTab) {
            0 -> if (isFinished) ResultDetailSheet(results) else CircuitDetailSheet(race)
            1 -> if (isFinished) CircuitDetailSheet(race) else ScheduleDetailSheet(race)
            2 -> ScheduleDetailSheet(race)
        }
    }
}

@Composable
fun ResultDetailSheet(result: RaceResult) {
    val maxPoints = result.driverResults.maxOfOrNull { it.points } ?: 0
    LazyColumn {
        itemsIndexed(result.driverResults) { index, driverResult ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp, 12.dp, 12.dp, 4.dp),
                verticalAlignment = Alignment.Top,

                ) {
                Text("${index + 1}.", fontWeight = FontWeight.Bold, color = Color.Red, modifier = Modifier.width(24.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(driverResult.driverName, color = Color.White)
                    Text(driverResult.team, color = Color.Gray, fontSize = 12.sp)
                }
                Text(stringResource(R.string.points_format, driverResult.points), color = Color.Gray)
            }
            LinearProgressIndicator(
                progress = { driverResult.points / maxPoints.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp, 0.dp, 12.dp, 6.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color.DarkGray,
                drawStopIndicator = {}
            )
            HorizontalDivider(color = Color.DarkGray, thickness = 1.dp)
        }
    }
}
@Composable
fun CircuitDetailSheet(race: Race) {
    Column(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Text(race.country, fontSize = 20.sp)
        Text(race.circuitName, fontSize = 18.sp, color = Color.LightGray)
        Image(
            painter = painterResource(race.circuitImage),
            contentDescription = race.circuitName,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(top = 16.dp),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(race.laps.toString(), stringResource(R.string.laps), modifier = Modifier.weight(1f))
            StatCard("${race.length} km", stringResource(R.string.lap_length), modifier = Modifier.weight(1f))
        }
    }
}
@Composable
fun ScheduleDetailSheet(race: Race) {
    Column(modifier = Modifier.padding(top = 8.dp)) {
        race.firstPractice?.let { SessionRow(stringResource(R.string.practice_1), it.first, it.second) }
        race.secondPractice?.let { SessionRow(stringResource(R.string.practice_2), it.first, it.second) }
        race.thirdPractice?.let { SessionRow(stringResource(R.string.practice_3), it.first, it.second) }
        race.sprintQualifying?.let { SessionRow(stringResource(R.string.sprint_qualifying), it.first, it.second) }
        race.sprint?.let { SessionRow(stringResource(R.string.sprint), it.first, it.second) }
        race.qualifying?.let { SessionRow(stringResource(R.string.qualifying), it.first, it.second) }
        SessionRow(stringResource(R.string.race), race.date, race.time)
    }
}
@Composable
fun SessionRow(name: String, date: String, time: String) {
    val fullTime = if (time.length < 8) "$time:00" else time
    val local = LocalDateTime.parse(
        "${date}T${fullTime.substring(0, 8)}",
        DateTimeFormatter.ISO_LOCAL_DATE_TIME
    )
        .atZone(ZoneOffset.UTC)
        .withZoneSameInstant(ZoneId.systemDefault())
        .toLocalDateTime()

    val formattedDate = local.toLocalDate()
        .format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    val formattedTime = local.toLocalTime()
        .format(DateTimeFormatter.ofPattern("HH:mm"))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(name, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Column(horizontalAlignment = Alignment.End) {
            Text(formattedDate, color = Color.White)
            Text(stringResource(R.string.local_time, formattedTime), color = Color.Gray, fontSize = 15.sp)
        }
    }
    HorizontalDivider(color = Color.DarkGray)
}
