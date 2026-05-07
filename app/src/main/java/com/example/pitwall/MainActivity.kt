package com.example.pitwall

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pitwall.data.Race
import com.example.pitwall.data.UiState
import com.example.pitwall.ui.screens.ConstructorDetailScreen
import com.example.pitwall.ui.screens.DriverDetailScreen
import com.example.pitwall.ui.screens.ErrorScreen
import com.example.pitwall.ui.screens.FavouriteScreen
import com.example.pitwall.ui.screens.HomeScreen
import com.example.pitwall.ui.screens.RaceDetailSheet
import com.example.pitwall.ui.screens.ScheduleScreen
import com.example.pitwall.ui.screens.StatsScreen
import com.example.pitwall.ui.theme.PitWallBackground
import com.example.pitwall.ui.theme.PitWallTheme
import com.example.pitwall.viewmodel.F1ViewModel


/**
 * The sole Activity of the PitWall application.
 *
 * Sets up the Compose UI and creates [F1ViewModel] using
 * [ViewModelProvider.AndroidViewModelFactory], which is required for
 * AndroidViewModel because it needs the Application context.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Called when the Activity is first created.
     * Enables edge-to-edge display and sets the root Compose content.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PitWallTheme {
                val viewModel: F1ViewModel by viewModels {
                    ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                }
                ChangeScreen(
                    viewModel = viewModel
                )
            }

        }
    }
}

/**
 * Data class representing a single item in the bottom navigation bar.
 *
 * @property route Navigation route string used by NavController.
 * @property labelRes Resource ID of the item's text label.
 * @property iconRes Resource ID of the item's icon.
 */
private data class NavDestination (
    val route: String,
    val labelRes: Int,
    val iconRes: Int
)

/**
 * Root composable responsible for navigation and global UI state.
 *
 * Responsible for:
 * - Creating the NavController and defining the navigation graph.
 * - Managing the ModalBottomSheet state (race detail display).
 * - Rendering the Scaffold with PitWallBottomBar.
 * - A gradient overlay for a smooth transition between content and the navigation bar.
 *
 * @param modifier Modifier for the root container.
 * @param viewModel Shared ViewModel for all screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeScreen(
    modifier: Modifier = Modifier,
    viewModel: F1ViewModel
) {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    val showBottomSheetState = remember { mutableStateOf(false) }
    val showBottomSheet = showBottomSheetState.value

    val selectedRaceState = remember { mutableStateOf<Race?>(null) }
    val selectedRace = selectedRaceState.value

    val raceResult by viewModel.raceResult.collectAsState()
    val uiState by viewModel.currentScreen.collectAsState()

    if  (showBottomSheet) {
        selectedRace?.let { race ->
            ModalBottomSheet(
                onDismissRequest = { showBottomSheetState.value = false },
                containerColor = PitWallBackground
            ) {
                RaceDetailSheet(race = race, raceResult = raceResult)
            }
        }
    }
    Scaffold(
        containerColor = PitWallBackground,
        bottomBar = {
            if (uiState == UiState.Success) {
                PitWallBottomBar(
                    currentRoute = currentDestination?.route,
                    onNavigate = { route -> navController.navigate(route) }
                )
            }
        }
    ) { innerPadding ->
            Box(
                modifier
                    .fillMaxSize()
                    .background(PitWallBackground)
                    .padding(top = innerPadding.calculateTopPadding()),
            ) {

                when (uiState) {
                    is UiState.Error -> ErrorScreen(
                        message = (uiState as UiState.Error).messageRes,
                        onRetry = { viewModel.refresh() }
                    )
                    UiState.Success ->
                        NavHost(navController, startDestination = "Home")
                        {
                            composable("Home") {
                                HomeScreen(
                                    onRaceClick = { race ->
                                        selectedRaceState.value = race
                                        showBottomSheetState.value = true
                                    },
                                    modifier,
                                    onNavigateToStats = { navController.navigate("Stats") },
                                    onNavigateToSchedule = { navController.navigate("Schedule") },
                                    viewModel = viewModel
                                )
                            }
                            composable("Stats") {
                                StatsScreen(
                                    viewModel = viewModel,
                                    onDriverClick = { driverId -> navController.navigate("driver_detail/$driverId") },
                                    onConstructorClick = { constructorId -> navController.navigate("constructor_detail/$constructorId") }
                                )
                            }

                            composable("Schedule")
                            {
                                ScheduleScreen(
                                    viewModel = viewModel,
                                    onRaceClick = { race ->
                                        selectedRaceState.value = race
                                        showBottomSheetState.value = true
                                    }
                                )
                            }
                            composable("Favourite") {
                                FavouriteScreen(
                                    viewModel = viewModel,
                                    onDriverClick = { driverId -> navController.navigate("driver_detail/$driverId") },
                                    onConstructorClick = { constructorId -> navController.navigate("constructor_detail/$constructorId") }
                                )
                            }
                            composable("driver_detail/{driverId}") { backStackEntry ->
                                val driverId = backStackEntry.arguments?.getString("driverId")
                                DriverDetailScreen(
                                    driverId = driverId ?: "",
                                    viewModel = viewModel,
                                    onBack = { navController.navigateUp() }
                                )
                            }
                            composable("constructor_detail/{constructorId}") { backStackEntry ->
                                val constructorId = backStackEntry.arguments?.getString("constructorId")
                                ConstructorDetailScreen(
                                    constructorId = constructorId ?: "",
                                    viewModel = viewModel,
                                    onBack = { navController.navigateUp() },
                                    onDriverClick = { driverId -> navController.navigate("driver_detail/$driverId") }
                                )
                            }
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
                )
            }


    }
}

/**
 * Composable for the app's bottom navigation bar.
 *
 * Displays four navigation items (Home, Schedule, Stats, Favourite)
 * inside a visually distinct card with rounded corners.
 * The active item is highlighted with a red indicator.
 *
 * @param currentRoute The current NavController route — determines the active item.
 * @param onNavigate Callback invoked when an item is tapped; receives the target route.
 */
@Composable
private fun PitWallBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        NavDestination("Home", R.string.nav_home, iconRes = R.drawable.home),
        NavDestination("Schedule", R.string.nav_schedule, iconRes = R.drawable.calendar),
        NavDestination("Stats", R.string.nav_stats, iconRes = R.drawable.stats),
        NavDestination("Favourite", R.string.nav_favourite, iconRes = R.drawable.favorite),
    )

    NavigationBar(
        modifier = Modifier
            .padding(start = 15.dp, top = 2.dp, end = 15.dp, bottom = 17.dp)
            .border(1.dp, Color.DarkGray, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .height(70.dp),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        items.forEach { dest ->
            NavigationBarItem(
                modifier = Modifier.padding(top = 10.dp),
                selected = currentRoute == dest.route,
                onClick = { onNavigate(dest.route) },
                icon = {
                    val iconModifier = Modifier.size(20.dp)
                    Icon(painterResource(dest.iconRes), contentDescription = stringResource(dest.labelRes), modifier = iconModifier)
                },
                label = { Text(stringResource(dest.labelRes))},
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Red)
            )
        }
    }
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