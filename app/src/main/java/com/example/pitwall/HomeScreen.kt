package com.example.pitwall

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Monaco Grand Prix", fontSize = 35.sp, color = Color.White)
            Text(text = "Remaining", fontSize = 30.sp, color = Color.Red)
            Text(text = "00:09:45:36", fontSize = 30.sp, color = Color.Red)
            Image(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(top = 40.dp)
                    .size(250.dp),
                painter = painterResource(R.drawable.monaco_circuit),
                contentDescription = "Monaco circuit",
                colorFilter = ColorFilter.tint(Color.White),

                )
        }
    }
}