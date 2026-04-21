package com.example.pitwall

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pitwall.ui.theme.PitWallTheme
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Composable
fun HomeScreen(onRaceClick: (Race) -> Unit,modifier: Modifier = Modifier, onNavigateToStats: () -> Unit, onNavigateToSchedule: () -> Unit, viewModel: F1ViewModel) {
    val drivers by viewModel.driverStandings.collectAsState()
    val constructors by viewModel.constructorStandings.collectAsState()
    val nextRace by viewModel.nextRace.collectAsState() //collectAsState compose funkcia ktora sleduje stateflow a prekresli composeable
    //zakazdym ked sa zmeni hodnota. By je kotlin delegat skratka pre .value

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item { HeaderLogo()}
        item { nextRace?.let { race -> NextRace(race, onRaceClick) } }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ToScreenButton(stringResource(R.string.nav_schedule), onNavigateToSchedule, R.drawable.calendar, modifier.weight(1f))
                ToScreenButton(stringResource(R.string.nav_stats), onNavigateToStats, R.drawable.result, modifier.weight(1f))
            }
        }
        item { ChampionshipCard(
            stringResource(R.string.driver_championship),
            drivers.take(3).map { StandingItem(it.driverId, it.fullName, it.points, it.team)
            })
        }
        item { ChampionshipCard(
            stringResource(R.string.constructor_championship),
            constructors.take(3).map { StandingItem(it.constructorId, it.name, it.points)
            })
        }
    }
}
@Composable
fun HeaderLogo() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.pitwall_logo),
            contentDescription = stringResource(R.string.pitwall_logo),
            modifier = Modifier
                .height(28.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun NextRace(nextRace: Race, onRaceClick: (Race) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp, 16.dp, 16.dp)
    ) {
        Text(text = stringResource(R.string.next_race), fontSize = 13.sp, color = Color.Red)
        Box(
            modifier = Modifier
                .clickable { onRaceClick(nextRace) }
                .fillMaxWidth()
                .height(305.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color.DarkGray, RoundedCornerShape(10.dp))
        ) {
            NextRaceCard(nextRace)
            BlackOverlay()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                NextRaceName(nextRace)
                NextRaceCircuit(nextRace)
                NextRaceTimer(nextRace)
             }
        }
    }
}

@Composable
fun NextRaceCard(nextRace: Race) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .size(305.dp)
            .paint(
                painterResource(nextRace.backgroundImage),
                contentScale = ContentScale.Crop,
            )

    ) { }
}

@Composable
fun BlackOverlay() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(305.dp)
            .background(Color.Black.copy(alpha = 0.7f))
    ) { }
}

@Composable
fun NextRaceName(nextRace: Race) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, bottom = 10.dp),
    ) {
        Text(
            text = nextRace.raceName,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        Row() {
            Text(
                text = nextRace.circuitName,
                fontSize = 15.sp,
                color = Color.LightGray
            )
            Text(text = " • ", fontSize = 15.sp, color = Color.LightGray)
            Text(stringResource(R.string.round, nextRace.round), fontSize = 15.sp, color = Color.LightGray)
        }
    }
}

@Composable
fun NextRaceTimer(nextRace: Race) {
    var totalSeconds by remember { mutableStateOf(0L) }
    val days = totalSeconds / 86400
    val hours = (totalSeconds % 86400) / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    //korutina na zivotny cyklus composable unit ako kluc znamenza spusti sa raz apri prvom zobrazeni a bezi az composable zmizne z ui
    LaunchedEffect(Unit) {
        while (true) {
            totalSeconds = ChronoUnit.SECONDS.between(
                LocalDateTime.now(),
                nextRace.getRaceDateTime()
            )
            delay(1000L)
        }
    }
    Box(modifier = Modifier
        .background(
        brush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.Black)
        )
    )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
        ) {
            Text(
                stringResource(R.string.remaining),
                fontSize = 30.sp,
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = days.toString().padStart(2, '0'),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    Text(stringResource(R.string.days), fontSize = 15.sp, color = Color.LightGray)
                }
                Text(" : ", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = hours.toString().padStart(2, '0'),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    Text(stringResource(R.string.hours), fontSize = 15.sp, color = Color.LightGray)
                }
                Text(" : ", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = minutes.toString().padStart(2, '0'),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    Text(stringResource(R.string.minutes), fontSize = 15.sp, color = Color.LightGray)
                }
                Text(
                    text = " : ",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = seconds.toString().padStart(2, '0'),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    Text(stringResource(R.string.seconds), fontSize = 15.sp, color = Color.LightGray)
                }

            }
        }
    }
}

@Composable
fun NextRaceCircuit(nextRace: Race) {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .padding(27.dp, 27.dp),
        contentScale = ContentScale.Fit,
        painter = painterResource(nextRace.circuitImage),
        contentDescription = nextRace.circuitName,
        colorFilter = ColorFilter.tint(Color.White),
    )
}

@Composable
fun ToScreenButton(change:String, onNavigateToScreen: () -> Unit, drawable: Int,modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = { onNavigateToScreen() },
        modifier = modifier.height(70.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.DarkGray),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.LightGray,
            containerColor = MaterialTheme.colorScheme.surface
        )

    ) {
        Icon(
            painter = painterResource(drawable),
            contentDescription = stringResource(R.string.red_calendar_icon),
            tint = Color.Red,
            modifier = Modifier.size(50.dp).padding(end = 15.dp),
        )
        Text(change, fontSize =  17.5.sp)
        Text(">", fontSize =  15.sp, color = Color.Gray, modifier = Modifier.padding(start = 10.dp))
    }
}

@Composable
fun ChampionshipCard(title: String, items: List<StandingItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Text(text = title, fontSize = 13.sp, color = Color.Red)
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, color = Color.DarkGray)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val maxPoints = items.maxOfOrNull { it.points } ?: 0f
                items.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "${index + 1}.",
                            fontSize = 16.sp,
                            color = Color.Red,
                            modifier = Modifier.width(24.dp),
                            lineHeight = 10.sp
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = item.name, fontSize = 14.sp, color = Color.White, lineHeight = 10.sp)
                            item.subTitle?.let {
                                Text(text = it, fontSize = 12.sp, color = Color.Gray, lineHeight = 10.sp)
                            }
                        }
                        Text(stringResource(R.string.points_format, item.points), fontSize = 14.sp, color = Color.Gray)
                    }
                    LinearProgressIndicator(
                        progress = {item.points.toFloat() / maxPoints.toFloat()},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 3.dp, bottom = 2.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = Color.DarkGray,
                        drawStopIndicator = {}
                    )
                }
            }
        }
    }
}