package com.example.pitwall.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pitwall.R
import com.example.pitwall.viewmodel.F1ViewModel

@Composable
fun FavouriteScreen(
    viewModel: F1ViewModel,
    onDriverClick: (String) -> Unit,
    onConstructorClick: (String) -> Unit,
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val favouriteDrivers by viewModel.favouriteDrivers.collectAsState()
    val allDrivers by viewModel.driverStandings.collectAsState()
    val favouriteConstructors by viewModel.favouriteConstructors.collectAsState()
    val allConstructors by viewModel.constructorStandings.collectAsState()

    Column {
        HeaderLogo()
        TabRow(
            selectedTab,
                containerColor = MaterialTheme.colorScheme.background
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(stringResource(R.string.stats_drivers_tab)) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(stringResource(R.string.stats_constructors_tab)) }
            )
        }
        when(selectedTab) {
            0 -> LazyColumn(
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(favouriteDrivers) { favDriver ->
                    val driver = allDrivers.find { it.driverId == favDriver.driverId }
                    if (driver != null) {
                        FavouriteItem(
                            driver.image,
                            driver.fullName,
                            onClick = { onDriverClick(driver.driverId)},
                            onRemove = { viewModel.removeFavouriteDriver(driver.driverId) })
                    }
                }
            }
            1 -> LazyColumn (
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(favouriteConstructors) { favConstructor ->
                    val constructor =
                        allConstructors.find { it.constructorId == favConstructor.constructorId }
                    if (constructor != null) {
                        FavouriteItem(
                            constructor.image,
                            constructor.name,
                            onClick = { onConstructorClick(constructor.constructorId)},
                            onRemove = { viewModel.removeFavouriteConstructor(constructor.constructorId) })
                    }
                }
            }
        }
    }
}

@Composable
fun FavouriteItem(
    imageRes: Int,
    name: String,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 5.dp)
            .border(1.dp, Color.DarkGray, RoundedCornerShape(10.dp))
            .clickable { onClick() },

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (imageRes != 0) {
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = stringResource(R.string.driver_image),
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(80.dp))
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(name, fontSize = 18.sp, modifier = Modifier.padding(start = 10.dp))
            }
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = stringResource(R.string.remove_from_favourites),
                tint = Color.Red,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clickable { onRemove() },
            )
        }
    }
}