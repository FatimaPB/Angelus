package com.example.angelus.presentation

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.angelus.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import androidx.core.app.NotificationCompat


class OracionReceiver : BroadcastReceiver() {
    @SuppressLint("WearRecents")
    override fun onReceive(context: Context, intent: Intent) {
        // Crear el intent hacia la pantalla de oración
        val openIntent = Intent(context, OracionActivity::class.java)
        openIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Crear canal de notificación (necesario para Android 8+)
        val channelId = "angelus_channel"
        val channelName = "Ángelus"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(channel)

        // Crear notificación
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.outline_add_24)
            .setContentTitle("Hora del Ángelus")
            .setContentText("Toca para rezar la oración")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(1001, notification)
    }
}
