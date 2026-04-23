package com.example.pitwall.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pitwall.ChangeScreen
import com.example.pitwall.R
import com.example.pitwall.data.Race
import com.example.pitwall.data.RaceStatus
import com.example.pitwall.ui.theme.PitWallTheme
import com.example.pitwall.viewmodel.F1ViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun ScheduleScreen(viewModel: F1ViewModel, onRaceClick: (Race) -> Unit) {
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
        item { HeaderLogo() }
        item { Text(stringResource(R.string.season, LocalDate.now().year), color = Color.Red, fontSize = 13.sp, modifier = Modifier.padding(start = 16.dp, top = 8.dp))}
        items(races) { race -> RaceCard(race, nextRace, onRaceClick)}
    }
}

@Composable
fun RaceCard(currentRace: Race, nextRace : Race?, onRaceClick: (Race) -> Unit) {

    val status = ChronoUnit.SECONDS.between(
        LocalDateTime.now(),
        currentRace.getRaceDateTime()
    )
    val raceStatus: RaceStatus = when {
        status < 0 -> RaceStatus.FINISHED
        currentRace == nextRace -> RaceStatus.NEXT
        else -> RaceStatus.UPCOMING
    }
    val borderColor = when(raceStatus) {
        RaceStatus.FINISHED -> Color.DarkGray
        RaceStatus.NEXT -> Color.Red
        RaceStatus.UPCOMING -> Color.Gray
    }
    Box(
        modifier = Modifier
            .clickable { onRaceClick(currentRace) }
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
fun CurrentRaceInfo(currentRace: Race, raceStatus: RaceStatus) {
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
            RaceStatus.NEXT -> Text(stringResource(R.string.next_race), color = Color.Red, fontWeight = Bold)
            RaceStatus.UPCOMING -> Text(stringResource(R.string.upcoming), color = Color.LightGray, fontStyle = FontStyle.Italic)
            RaceStatus.FINISHED -> Text(stringResource(R.string.finished), color = Color.Gray)
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SchedulePrevieww() {
    PitWallTheme {
        val viewModel: F1ViewModel = viewModel()
        ChangeScreen(
            viewModel = viewModel
        )
    }
}