package com.example.pitwall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.pitwall.ui.theme.PitWallBackground
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
                .background(color = PitWallBackground)
                .padding(innerPadding),
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
                modifier.fillMaxWidth()
                    .background(color = Color(0xff212121)),
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