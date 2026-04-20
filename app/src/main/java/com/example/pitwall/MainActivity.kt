package com.example.pitwall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pitwall.ui.theme.PitWallBackground
import com.example.pitwall.ui.theme.PitWallTheme
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PitWallTheme {
                val viewModel: F1ViewModel = viewModel()
                ChangeScreen(
                    viewModel = viewModel
                )
            }

        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeScreen(
    modifier: Modifier = Modifier,
    viewModel: F1ViewModel
) {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    var showBottomSheet by remember { mutableStateOf(false)}
    var selectedRace by remember { mutableStateOf<Race?>(null)}
    val raceResult by viewModel.raceResult.collectAsState()

    if  (showBottomSheet) {
        selectedRace?.let { race ->
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                containerColor = PitWallBackground
            ) {
                RaceDetailSheet(race = race, raceResult = raceResult)
            }
        }
    }
    //material3 layout stara sa o spravne rozmiesntenie topBar BottomBar a content
    Scaffold(
        containerColor = PitWallBackground,
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .padding(start = 15.dp, top = 2.dp, end = 15.dp, bottom = 17.dp)
                    .border(1.dp, Color.DarkGray, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .height(70.dp),
                containerColor = MaterialTheme.colorScheme.surface,

            ) {
                NavigationBarItem(
                    modifier = Modifier.padding(top = 10.dp),
                    selected = currentDestination?.route == "Home",
                    onClick = { navController.navigate("Home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(20.dp)) },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors (
                        indicatorColor = Color.Red,
                    ),
                )
                NavigationBarItem(
                    modifier = Modifier.padding(top = 10.dp),
                    selected = currentDestination?.route == "Schedule",
                    onClick = { navController.navigate("Schedule") },
                    icon = { Icon(painter = painterResource(R.drawable.calendar), contentDescription = "Schedule", modifier = Modifier.size(20.dp)) },
                    label = { Text("Schedule") },
                    colors = NavigationBarItemDefaults.colors (
                        indicatorColor = Color.Red
                    ),
                )
                NavigationBarItem(
                    modifier = Modifier.padding(top = 10.dp),
                    selected = currentDestination?.route == "Stats",
                    onClick = { navController.navigate("Stats") },
                    icon = { Icon(painter = painterResource(R.drawable.stats), contentDescription = "Stats", modifier = Modifier.size(20.dp)) },
                    label = { Text("Stats") },
                    colors = NavigationBarItemDefaults.colors (
                        indicatorColor = Color.Red
                    ),
                )
                NavigationBarItem(
                    modifier = Modifier.padding(top = 10.dp),
                    selected = currentDestination?.route == "Favourite",
                    onClick = { navController.navigate("Favourite") },
                    icon = { Icon(painter = painterResource(R.drawable.favorite), contentDescription = "Favourite", modifier = Modifier.size(20.dp)) },
                    label = { Text("Favourite") },
                    colors = NavigationBarItemDefaults.colors (
                        indicatorColor = Color.Red
                    ),
                )
            }
        }
    ) { innerPadding -> //
            Box(
                modifier
                    .fillMaxSize()
                    .background(PitWallBackground)
                    .padding(top = innerPadding.calculateTopPadding()),
            ) {
                NavHost(navController, startDestination = "Home")
                {
                    composable("Home") {
                        HomeScreen(
                            onRaceClick = { race ->
                                selectedRace = race
                                showBottomSheet = true
                            },
                            modifier,
                            onNavigateToStats = { navController.navigate("Stats")},
                            onNavigateToSchedule = { navController.navigate("Schedule")},
                            viewModel = viewModel
                        )
                    }
                    composable("Stats") {
                        StatsScreen(
                            viewModel = viewModel,
                            onDriverClick = { driverId -> navController.navigate("driver_detail/$driverId")},
                            onConstructorClick = { constructorId -> navController.navigate("constructor_detail/$constructorId")}
                        )
                    }

                    composable("Schedule")
                    {
                        ScheduleScreen(
                            viewModel = viewModel,
                            onRaceClick = { race ->
                                selectedRace = race
                                showBottomSheet = true
                            }
                        )
                    }
                    composable("Favourite") {
                        FavouriteScreen()
                    }
                    composable("driver_detail/{driverId}") { backStackEntry ->
                        val driverId = backStackEntry.arguments?.getString("driverId")
                        DriverDetailScreen(
                            driverId = driverId ?: "",
                            viewModel = viewModel,
                            onBack = { navController.navigateUp()}
                        )
                    }
                    composable("constructor_detail/{constructorId}") { backStackEntry ->
                        val constructorId = backStackEntry.arguments?.getString("constructorId")
                        Text(constructorId ?: "unknown")
                    }
                }
                Box(//vizualny efekt pre spod baru aby content za navigation bar nebol tvrdo odrezany
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, PitWallBackground)
                            )
                        )
                ) {}
            }


    }
}

@Composable
fun RaceDetailSheet(
    race: Race,
    raceResult: List<RaceResult>,
) {
    val results = raceResult.find { it.round == race.round }
    val isFinished = results != null && results.driverResults.isNotEmpty()
    var selectedTab by remember { mutableStateOf(0)}
    val tabs = if (isFinished) {
            listOf("Results", "Circuit", "Schedule")
         } else {
            listOf("Circuit", "Schedule")
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
        when (tabs[selectedTab]) {
            "Results" -> ResultDetailSheet(results!!)
            "Circuit" -> CircuitDetailSheet(race)
            "Schedule" -> ScheduleDetailSheet(race)
        }
    }
}

@Composable
fun ResultDetailSheet(result: RaceResult) {
    val maxPoints = result.driverResults.maxOfOrNull { it.points } ?: 0f
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
                Text("${driverResult.points} pts", color = Color.Gray)
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
        Text(race.country, fontSize = 20.sp, )
        Text(race.circuitName, fontSize = 18.sp, color = Color.LightGray)
        Image(
            painter = painterResource(race.circuitImage),
            contentDescription = race.circuitName,
            modifier = Modifier.fillMaxWidth().height(200.dp).padding(top = 16.dp),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color.White),
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                border = BorderStroke(1.dp, Color.DarkGray),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(race.laps.toString(), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Laps", color = Color.Gray, fontSize = 12.sp)
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                border = BorderStroke(1.dp, Color.DarkGray),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("${race.length} km", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Lap length", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}
@Composable
fun ScheduleDetailSheet(race: Race) {
    Column(modifier = Modifier.padding(top = 8.dp)) {
        race.firstPractice?.let { SessionRow("Practice 1", it.first, it.second) }
        race.secondPractice?.let { SessionRow("Practice 2", it.first, it.second) }
        race.thirdPractice?.let { SessionRow("Practice 3", it.first, it.second) }
        race.sprintQualifying?.let { SessionRow("Sprint Qualifying", it.first, it.second) }
        race.sprint?.let { SessionRow("Sprint", it.first, it.second) }
        race.qualifying?.let { SessionRow("Qualifying", it.first, it.second) }
        SessionRow("Race", race.date, race.time)
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
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(name, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        Column(horizontalAlignment = Alignment.End) {
            Text(formattedDate, color = Color.White)
            Text("$formattedTime local", color = Color.Gray, fontSize = 15.sp)
        }
    }
    HorizontalDivider(color = Color.DarkGray)
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    PitWallTheme {
        val viewModel: F1ViewModel = viewModel()
        ChangeScreen(
            viewModel = viewModel
        )
    }
}