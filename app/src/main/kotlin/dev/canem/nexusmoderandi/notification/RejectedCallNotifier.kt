package dev.canem.nexusmoderandi.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.canem.nexusmoderandi.MainActivity
import dev.canem.nexusmoderandi.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RejectedCallNotifier @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val CHANNEL_ID = "rejected_calls"
        const val NOTIFICATION_ID = 1
    }

    init {
        createChannel()
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Rejected Calls",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Silent notifications for rejected call counts"
            setShowBadge(true)
        }
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    fun updateBadge(count: Int) {
        if (count <= 0) {
            dismiss()
            return
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("navigate_to", "rejected")
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val text = if (count == 1) "1 call rejected in the last 24 hours"
        else "$count calls rejected in the last 24 hours"

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Nexus Moderandi")
            .setContentText(text)
            .setNumber(count)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSilent(true)
            .setOnlyAlertOnce(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    fun dismiss() {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
    }
}
