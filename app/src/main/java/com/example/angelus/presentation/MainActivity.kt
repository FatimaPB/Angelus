/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.angelus.presentation

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.angelus.R
import com.example.angelus.presentation.theme.AngelusTheme

import android.app.AlarmManager
import java.util.Calendar
import android.content.Intent
import android.app.PendingIntent
import android.content.Context
import androidx.annotation.RequiresPermission

class MainActivity : ComponentActivity() {
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        // Programar las alarmas
        programarAlarmas()

        setContent {
            WearApp("Android")
        }
    }
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    private fun programarAlarmas() {
        val horarios = listOf(
            Pair(6, 43),   // 6:00 AM

            Pair(23,13),
            Pair(23,14),
            Pair(23,15),
            Pair(23,16),
            Pair(23,17),
            Pair(23,18),
            Pair(23,19),
            Pair(23,20),
            Pair(23,21),
            Pair(23,22),
            Pair(23,23),
            Pair(23,24),
            Pair(23,25),
            Pair(23,26),
            Pair(23,27),
            Pair(23,28),
            Pair(23,29),
            Pair(23,30),
            Pair(23,31),
            Pair(23,32),


            Pair(18, 23)  // 6:20 PM
        )


        for ((hora, minuto) in horarios) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hora)
                set(Calendar.MINUTE, minuto)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                if (before(Calendar.getInstance())) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }

            val intent = Intent(this, OracionReceiver::class.java)
            val requestCode = hora * 100 + minuto // Código único por horario
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}


@Composable
fun WearApp(greetingName: String) {
    AngelusTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            Greeting(greetingName = greetingName)
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName)
    )
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}