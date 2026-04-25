package com.example.pitwall.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.pitwall.R

class RaceAlarmReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "race_reminder"
        const val EXTRA_RACE_NAME = "race_name"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val raceName = intent.getStringExtra(EXTRA_RACE_NAME) ?: "Race"

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.pitwall_logo)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_text, raceName))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(1, notification)
    }
}