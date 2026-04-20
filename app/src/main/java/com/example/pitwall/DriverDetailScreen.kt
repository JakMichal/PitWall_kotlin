package com.example.pitwall

import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DriverDetailScreen(
    driverId: String,
    viewModel: F1ViewModel,
    onBack: () -> Unit
) {
    val drivers by viewModel.driverStandings.collectAsState()
    val driver = drivers.find { it.driverId == driverId}

    if (driver == null) {
        Text("Driver not found")
        return
    }

    Column {
        DriverDetailHeader(onBack)
        DriverDetailCard(driver)
        DriverDetailGrid(driver)
    }
}
@Composable
fun DriverDetailHeader(onBack: () -> Unit) {
    Row (
        modifier = Modifier.padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back arrow",
            modifier = Modifier
                .size(25.dp)
                .clickable { onBack() },
            tint = Color.Red,

            )
        Text("Driver detail", fontSize = 15.sp, color = Color.Red, modifier = Modifier.padding(start = 5.dp))
    }
}

@Composable
fun DriverDetailCard(driver: Driver) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .size(305.dp)
            .border(1.dp, Color.DarkGray, RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Image("Probelm nemam fotky jazdcov")
            Text(driver.number, color = Color.Red, fontSize = 35.sp)
            Text(driver.fullName,  fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(driver.team, color = Color.Gray, fontSize = 13.sp)
            Text(
                driver.nationality,
                fontSize = 13.sp,
                modifier = Modifier
                    .background(
                        color = Color.Red,
                        shape = CircleShape
                    )
                    .padding(8.dp, 2.dp)
            )
        }

    }
}

@Composable
fun DriverDetailGrid(driver: Driver) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(driver.points.toString(), "Points", modifier = Modifier.weight(1f))
        StatCard(driver.wins.toString(), "Wins", modifier = Modifier.weight(1f))
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(driver.position.toString(), "Position", modifier = Modifier.weight(1f))
        StatCard(driver.code, "Driver code", modifier = Modifier.weight(1f))

    }

}

@Composable
fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        border = BorderStroke(1.dp, Color.DarkGray),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(label, color = Color.Gray, fontSize = 12.sp)
        }
    }
}



