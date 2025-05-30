package com.example.angelus.presentation


import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.angelus.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh


import androidx.compose.material3.IconButton
import androidx.wear.compose.material.Icon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import androidx.wear.tooling.preview.devices.WearDevices



class OracionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                OracionScreen()
            }
        }
    }


}

@Composable
fun OracionScreen() {
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var hasStarted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Actualiza el progreso mientras suena el audio
    LaunchedEffect(isPlaying) {
        while (isPlaying && mediaPlayer != null) {
            val current = mediaPlayer?.currentPosition ?: 0
            val duration = mediaPlayer?.duration ?: 1
            progress = current / duration.toFloat()
            delay(100L)
        }
    }

    // Libera mediaPlayer cuando el Composable se destruye
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ángelus",
            style = MaterialTheme.typography.body1
        )

        Box(contentAlignment = Alignment.Center) {
            if (hasStarted) {
                CircularProgressIndicator(
                    progress = progress,
                    modifier = Modifier.size(60.dp),
                    strokeWidth = 3.dp
                )
            }

            IconButton(
                onClick = {
                    if (!hasStarted) {
                        // Primera vez: crear y reproducir
                        mediaPlayer = MediaPlayer.create(context, R.raw.oracion).apply {
                            setOnCompletionListener {
                                isPlaying = false
                                hasStarted = false
                                progress = 0f
                                release()
                                mediaPlayer = null
                            }
                            start()
                        }
                        isPlaying = true
                        hasStarted = true
                    } else {
                        if (isPlaying) {
                            // Pausar sin destruir
                            mediaPlayer?.pause()
                            isPlaying = false
                        } else {
                            // Reanudar desde donde se pausó
                            mediaPlayer?.start()
                            isPlaying = true
                        }
                    }
                },
                modifier = Modifier.size(38.dp)
            ) {
                if (hasStarted && isPlaying) {
                    PauseIcon(
                        modifier = Modifier.size(28.dp),
                        tint = MaterialTheme.colors.primary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Reproducir",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        IconButton(
            onClick = {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
                isPlaying = false
                hasStarted = false
                progress = 0f
            },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Detener",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}


@Composable
fun PauseIcon(
    modifier: Modifier = Modifier,
    tint: Color = Color.White
) {
    Row(
        modifier = modifier.fillMaxWidth(), // ocupar todo el ancho disponible
        horizontalArrangement = Arrangement.Center, // centrar contenido
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Box(
                modifier = Modifier
                    .size(width = 3.dp, height = 15.dp) // más delgado
                    .background(tint)
            )
            Box(
                modifier = Modifier
                    .size(width = 3.dp, height = 15.dp) // más delgado
                    .background(tint)
            )
        }
    }
}



@Preview(name = "Small Round", device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun PreviewOracionScreenSmall() {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            OracionScreen()
        }
    }
}

@Preview(name = "Large Round", device = WearDevices.LARGE_ROUND, showSystemUi = true)
@Composable
fun PreviewOracionScreenLarge() {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            OracionScreen()
        }
    }
}

