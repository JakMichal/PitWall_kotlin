package com.example.pitwall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pitwall.ui.theme.PitWallBackground
import com.example.pitwall.ui.theme.PitWallTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PitWallTheme {
                var activeScreen by rememberSaveable { mutableStateOf("Home") }
                ChangeScreen(
                    activeScreen = activeScreen,
                    onScreenChange = { activeScreen = it }
                )

            }

        }
    }
}
@Composable
fun ChangeScreen(
    activeScreen: String,
    modifier: Modifier = Modifier,
    onScreenChange: (String) -> Unit,
) {
    Scaffold(
        containerColor = PitWallBackground,
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .padding(start = 15.dp, top = 2.dp, end = 15.dp, bottom = 10.dp)
                    .border(1.dp, Color.DarkGray, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .height(80.dp),
                containerColor = MaterialTheme.colorScheme.surface,

            ) {
                NavigationBarItem(
                    selected = activeScreen == "Home",
                    onClick = { onScreenChange("Home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(20.dp)) },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors (
                        indicatorColor = Color.Red,
                    ),
                )
                NavigationBarItem(
                    selected = activeScreen == "All Result",
                    onClick = { onScreenChange("All Result") },
                    icon = { Icon(painter = painterResource(R.drawable.result), contentDescription = "Result", modifier = Modifier.size(20.dp)) },
                    label = { Text("Result") },
                    colors = NavigationBarItemDefaults.colors (
                        indicatorColor = Color.Red
                    ),
                )
                NavigationBarItem(
                    selected = activeScreen == "Stats",
                    onClick = { onScreenChange("Stats") },
                    icon = { Icon(painter = painterResource(R.drawable.stats), contentDescription = "Stats", modifier = Modifier.size(20.dp)) },
                    label = { Text("Stats") },
                    colors = NavigationBarItemDefaults.colors (
                        indicatorColor = Color.Red
                    ),
                )
                NavigationBarItem(
                    selected = activeScreen == "Schedule",
                    onClick = { onScreenChange("Schedule") },
                    icon = { Icon(painter = painterResource(R.drawable.calendar), contentDescription = "Schedule", modifier = Modifier.size(20.dp)) },
                    label = { Text("Schedule") },
                    colors = NavigationBarItemDefaults.colors (
                        indicatorColor = Color.Red
                    ),
                )
                NavigationBarItem(
                    selected = activeScreen == "Favourite",
                    onClick = { onScreenChange("Favourite") },
                    icon = { Icon(painter = painterResource(R.drawable.favorite), contentDescription = "Favourite", modifier = Modifier.size(20.dp)) },
                    label = { Text("Favourite") },
                    colors = NavigationBarItemDefaults.colors (
                        indicatorColor = Color.Red
                    ),
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
                when (activeScreen) {
                    "Home" -> HomeScreen(modifier, onScreenChange = onScreenChange)
                    "All Result" -> ResultScreen()
                    "Stats" -> StatsScreen()
                    "Schedule" -> ScheduleScreen()
                    "Favourite" -> FavouriteScreen()
                }
                Box(
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

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    PitWallTheme {
        var activeScreen by remember { mutableStateOf("Home") }
        ChangeScreen(
            activeScreen = activeScreen,
            onScreenChange = { activeScreen = it }
        )
    }
}