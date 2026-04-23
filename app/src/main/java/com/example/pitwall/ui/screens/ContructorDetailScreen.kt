package com.example.pitwall.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pitwall.viewmodel.F1ViewModel
import com.example.pitwall.R
import com.example.pitwall.data.Constructor
import com.example.pitwall.data.Driver

@Composable
fun ConstructorDetailScreen(
    constructorId: String,
    viewModel: F1ViewModel,
    onBack: () -> Unit,
    onDriverClick: (String) -> Unit
) {
    val constructors by viewModel.constructorStandings.collectAsState()
    val drivers by viewModel.driverStandings.collectAsState()
    val constructor = constructors.find { it.constructorId == constructorId}
    val teamDrivers = drivers.filter { it.team == constructor?.name }
    val favouriteConstructors by viewModel.favouriteConstructors.collectAsState()
    val isFavourite = favouriteConstructors.any { it.constructorId == constructorId }


    if (constructor == null) {
        Text(stringResource(R.string.constructor_not_found))
        return
    }

    LazyColumn (
        modifier = Modifier.padding(bottom = 100.dp)
    ) {
        item {
            DetailHeader(
                headerText = stringResource(R.string.constructor_detail),
                onBack = onBack,
                onFavouriteClick = {
                    if (isFavourite) viewModel.removeFavouriteConstructor(constructorId)
                    else viewModel.addFavouriteConstructor(constructorId)
                },
                isFavourite = isFavourite,
            )
        }
        item { ConstructorDetailCard(constructor) }
        item { ConstructorDetailGrid(constructor) }
        item { ConstructorDetailDrivers(teamDrivers, onDriverClick) }
    }
}

@Composable
fun ConstructorDetailCard(constructor: Constructor) {
    DetailEntityCard {
        if (constructor.image != 0) {
            Image(
                painter = painterResource(constructor.image),
                contentDescription = stringResource(R.string.constructor_picture),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            )
        }
        Text(constructor.position.toString(), color = Color.Red, fontSize = 35.sp)
        Text(
            constructor.name,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 10.dp)
        )
        NationalityBadge(constructor.nationality)
    }
}
@Composable
fun ConstructorDetailGrid(constructor: Constructor) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            constructor.points.toString(),
            stringResource(R.string.points),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            constructor.wins.toString(),
            stringResource(R.string.wins),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ConstructorDetailDrivers(teamDrivers: List<Driver>, onDriverClick: (String) -> Unit) {
    Text(stringResource(R.string.drivers), fontSize = 15.sp, color = Color.Red, modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp))

    teamDrivers.forEach { driver ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 5.dp)
                .border(1.dp, Color.DarkGray, RoundedCornerShape(10.dp))
                .clickable { onDriverClick(driver.driverId) },

            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
            if (driver.image != 0) {
                Image(
                    painter = painterResource(driver.image),
                    contentDescription = stringResource(R.string.driver_image),
                    modifier = Modifier.size(80.dp))
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(driver.fullName, fontSize = 18.sp)
                    Text(stringResource(R.string.points_format, driver.points), color = Color.Gray)
                }
                Text(">", color = Color.Red, modifier = Modifier.padding(end = 8.dp))
            }
        }
    }
}


