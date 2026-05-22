package dev.canem.nexusmoderandi.ui.pause

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun PauseBar(
    pauseUntil: Long,
    onPause: (hours: Int) -> Unit,
    onResume: () -> Unit,
    modifier: Modifier = Modifier
) {
    val now = System.currentTimeMillis()
    val isPaused = pauseUntil > now

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = if (isPaused) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        if (isPaused) {
            PausedContent(pauseUntil = pauseUntil, onResume = onResume)
        } else {
            ActiveContent(onPause = onPause)
        }
    }
}

@Composable
private fun ActiveContent(onPause: (hours: Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Pause screening:",
            style = MaterialTheme.typography.bodyMedium
        )
        OutlinedButton(onClick = { onPause(1) }) {
            Text("1 hour")
        }
        OutlinedButton(onClick = { onPause(4) }) {
            Text("4 hours")
        }
    }
}

@Composable
private fun PausedContent(pauseUntil: Long, onResume: () -> Unit) {
    var remaining by remember { mutableLongStateOf(pauseUntil - System.currentTimeMillis()) }

    LaunchedEffect(pauseUntil) {
        while (true) {
            remaining = pauseUntil - System.currentTimeMillis()
            if (remaining <= 0) {
                onResume()
                break
            }
            delay(1000)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Screening paused \u2022 ${formatRemaining(remaining)}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        FilledTonalButton(onClick = onResume) {
            Text("Reactivate now")
        }
    }
}

private fun formatRemaining(millis: Long): String {
    if (millis <= 0) return "resuming..."
    val totalSeconds = millis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        "%dh %02dm left".format(hours, minutes)
    } else {
        "%dm %02ds left".format(minutes, seconds)
    }
}
