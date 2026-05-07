package com.example.pitwall.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.pitwall.R

/**
 * BroadcastReceiver responsible for displaying a race reminder notification.
 *
 * Triggered by the system at the time scheduled via AlarmManager in F1ViewModel.
 * The notification is shown one hour before the start of the next race.
 */
class RaceAlarmReceiver : BroadcastReceiver() {

    companion object {
        /** ID of the notification channel used for race reminders. */
        const val CHANNEL_ID = "race_reminder"

        /** Intent extra key carrying the name of the upcoming race. */
        const val EXTRA_RACE_NAME = "race_name"
    }

    /**
     * Called by the system when the alarm fires.
     *
     * Creates (or updates) the notification channel and displays a notification
     * containing the name of the upcoming race.
     *
     * @param context Application context.
     * @param intent Intent containing the race name under the key [EXTRA_RACE_NAME].
     */
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