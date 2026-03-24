package com.example.pitwall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pitwall.ui.theme.PitWallTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PitWallTheme {
                var activeScreen by remember { mutableStateOf("Home") }
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
    Scaffold { innerPadding ->
        Column(
            modifier.fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier.weight(1f)
            ) {
                when (activeScreen) {
                    "Home" -> HomeScreen()
                    "Result" -> ResultScreen()
                    "Stats" -> StatsScreen()
                    "Favorite" -> FavoriteScreen()
                }
            }
            Row(
                modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        onScreenChange("Home")
                    }
                ) { Text("Home") }
                Button(
                    onClick = {
                        onScreenChange("Result")
                    }
                ) { Text("Result") }
                Button(
                    onClick = {
                        onScreenChange("Stats")
                    }
                ) { Text("Stats") }
                Button(
                    onClick = {
                        onScreenChange("Favorite")
                    }
                ) { Text("Favorite") }
            }
        }
    }
}
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Home - closest race")
    }
}
@Composable
fun ResultScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Result - race result")
    }
}
@Composable
fun StatsScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Stats - drivers and teams stats")
    }
}
@Composable
fun FavoriteScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
        Text(text = "Favorite")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(modifier: Modifier = Modifier) {
    PitWallTheme {
        var activeScreen by remember { mutableStateOf("Home") }
        ChangeScreen(
            activeScreen = activeScreen,
            onScreenChange = { activeScreen = it }
        )
    }
}