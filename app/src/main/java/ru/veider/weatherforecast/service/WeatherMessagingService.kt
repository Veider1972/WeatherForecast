package ru.veider.weatherforecast.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.ui.MainActivity
import ru.veider.weatherforecast.utils.*


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class WeatherMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification
        val messageData = message.data
        if (messageData.isNotEmpty()) handleMessageData(messageData.toMap())
    }

    private fun handleMessageData(value: Map<String, String>) {
        val title: String = value[TITLE].toString()
        val message: String = value[MESSAGE].toString()
        val latitude: Double? = value[LATITUDE]?.toDouble()
        val longitude: Double? = value[LONGITUDE]?.toDouble()
        if (latitude != null && longitude != null) {

            val intent = Intent(BROADCAST_ACTION)
            intent.putExtra(TITLE, title)
            intent.putExtra(LATITUDE, latitude)
            intent.putExtra(LONGITUDE, longitude)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
        showNotification(title, message)
    }

    private fun showNotification(title: String, message: String) {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setOngoing(false)
            setCategory(Notification.CATEGORY_ALARM)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            if (title.isNotEmpty()) setContentTitle(title)
            if (message.isNotEmpty()) setContentText(message)
            setStyle(NotificationCompat.BigTextStyle())
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            setVibrate(vibroPattern)
            setLights(Color.RED, 0, 1)
            setAutoCancel(true)
            color = Color.RED
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                priority = NotificationManager.IMPORTANCE_HIGH
                setSmallIcon(R.drawable.attention)
            } else {
                priority = NotificationCompat.PRIORITY_MAX
            }
        }
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
            createNotificationChannel(this)
            notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                resources.getString(R.string.alarm_channel_name),
                NotificationManager.IMPORTANCE_HIGH
                                                         ).apply {
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                enableVibration(true)
                vibrationPattern = vibroPattern
                enableLights(true)
                lightColor = Color.WHITE
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}