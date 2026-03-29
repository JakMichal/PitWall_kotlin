package com.example.pitwall

import android.os.CountDownTimer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var remainingTime by remember { mutableStateOf(3600000L) }

    //https://developer.android.com/reference/android/os/CountDownTimer#kotlin
    val timer = remember {
        object : CountDownTimer(remainingTime, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
            }

            override fun onFinish() {

            }
        }.start()
    }


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
            Text(text = formatTime(remainingTime), fontSize = 30.sp, color = Color.Red)
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

fun formatTime(timeInMillis: Long): String {
    val hours = timeInMillis / 3600000
    val minutes = (timeInMillis % 3600000) / 60000
    val seconds = (timeInMillis % 60000) / 1000
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}