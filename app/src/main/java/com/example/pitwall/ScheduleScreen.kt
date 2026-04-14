package com.example.pitwall

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pitwall.ui.theme.PitWallTheme
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun ScheduleScreen(viewModel: F1ViewModel) {
    val races by viewModel.races.collectAsState()
    val nextRace by viewModel.nextRace.collectAsState()
    val nextRaceIndex = races.indexOfFirst{ it == nextRace }
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = if (nextRaceIndex > 0) nextRaceIndex + 1 else 0
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item { HeaderLogo()}
        items(races) { race -> RaceCard(race, nextRace)}
    }
}

@Composable
fun RaceCard(currentRace: Race, nextRace : Race?) {

    val status = ChronoUnit.SECONDS.between(
        LocalDateTime.now(),
        currentRace.getRaceDateTime()
    )
    val raceStatus = when {
        status < 0 -> "Finished"
        currentRace == nextRace -> "Next race"
        else -> "Upcoming"
    }
    val borderColor = when {
        status < 0 -> Color.DarkGray
        currentRace == nextRace -> Color.Red
        else -> Color.Gray
    }
    Box(
        modifier = Modifier
            .padding(16.dp, 15.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
    ) {
        CurrentRaceBackground(currentRace)
        BlackOverlay()
        Image(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterEnd)
                .padding(8.dp),
            painter = painterResource(currentRace.circuitImage),
            contentDescription = currentRace.circuitName,
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White)
        )
        CurrentRaceInfo( currentRace, raceStatus)
    }

}

@Composable
fun CurrentRaceBackground(currentRace: Race) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .size(200.dp)
            .paint(
                painterResource(currentRace.backgroundImage),
                contentScale = ContentScale.Crop,
                alignment = Alignment.BottomCenter
            )

    ) { }
}

@Composable
fun CurrentRaceInfo(currentRace: Race, raceStatus: String) {
    val formattedDate = LocalDate.parse(currentRace.date)
        .format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    Column(
        modifier = Modifier
            .padding(start = 20.dp, top = 40.dp),
        ) {
        Text(currentRace.country, color = Color.Red)
        Text(currentRace.raceName, fontSize = 18.sp, fontWeight = Bold)
        Text(currentRace.circuitName, color = Color.Gray ,fontSize = 13.sp)
        Text(formattedDate)
        when(raceStatus) {
            "Next race" -> Text(raceStatus, color = Color.Red, fontWeight = Bold)
            "Upcoming" -> Text(raceStatus, color = Color.LightGray, fontStyle = FontStyle.Italic)
            else -> Text(raceStatus, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SchedulePrevieww() {
    PitWallTheme {
        val viewModel: F1ViewModel = viewModel()
        var activeScreen by remember { mutableStateOf("Schedule") }
        ChangeScreen(
            activeScreen = activeScreen,
            onScreenChange = { activeScreen = it },
            viewModel = viewModel
        )
    }
}