package com.example.pitwall.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pitwall.R
import com.example.pitwall.data.StandingItem
import com.example.pitwall.viewmodel.F1ViewModel

@Composable
fun StatsScreen(viewModel: F1ViewModel, onDriverClick: (String) -> Unit, onConstructorClick: (String) -> Unit) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val driversStats by viewModel.driverStandings.collectAsState()
    val constructorsStats by viewModel.constructorStandings.collectAsState()
    val showLanguageMenu = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HeaderLogo(onLanguageClick = { showLanguageMenu.value = true }, showLanguageMenu)
        TabRow(
            selectedTab,
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0},
                text = { Text(stringResource(R.string.stats_drivers_tab), color = Color.Red)}
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1},
                text = { Text(stringResource(R.string.stats_constructors_tab), color = Color.Red)}
            )
        }
        when(selectedTab) {
            0 -> StandingsList(
                driversStats.map { StandingItem(it.driverId, it.fullName, it.points) },
                    onItemClick = { driverId -> onDriverClick(driverId)}
                )
            1 -> StandingsList(
                constructorsStats.map { StandingItem(it.constructorId, it.name, it.points) },
                onItemClick = { constructorId -> onConstructorClick(constructorId)}
            )
        }
    }
}

@Composable
fun StandingsList(items: List<StandingItem>, onItemClick: (String) -> Unit) {
    val maxPoints =items.maxOfOrNull { it.points } ?: 0
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        itemsIndexed(items) { index, item ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clickable { onItemClick(item.id)},
                border = BorderStroke(1.dp, color = Color.DarkGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "${index + 1}.",
                        fontSize = 16.sp,
                        color = Color.Red,
                        modifier = Modifier.width(24.dp),
                        lineHeight = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.name,
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp,
                        color = Color.White,
                        lineHeight = 10.sp)
                    Text(stringResource(R.string.points_format, item.points), fontSize = 14.sp, color = Color.Gray)
                }
                LinearProgressIndicator(
                    progress = {item.points.toFloat() / maxPoints.toFloat()},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp, 0.dp, 12.dp, 8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color.DarkGray,
                    drawStopIndicator = {}
                )
            }
        }
    }
}


