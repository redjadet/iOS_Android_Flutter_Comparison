package com.example.kmpsamples

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime

@Composable
actual fun rememberReminderPickerHandler(): (LocalDateTime, (LocalDateTime) -> Unit) -> Unit {
    // Hold MutableState itself so the returned lambda stays valid across recompositions.
    val pendingState = remember {
        mutableStateOf<Pair<LocalDateTime, (LocalDateTime) -> Unit>?>(null)
    }
    val pending by pendingState

    pending?.let { (current, onSelected) ->
        AlertDialog(
            onDismissRequest = { pendingState.value = null },
            title = { Text("Adjust reminder") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Current: ${formatReminder(current)}")
                    Text("Compose Multiplatform has no system date picker here; use a quick offset for the demo.")
                }
            },
            confirmButton = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextButton(
                        onClick = {
                            onSelected(current.plusHours(1))
                            pendingState.value = null
                        },
                    ) {
                        Text("+1 hour")
                    }
                    TextButton(
                        onClick = {
                            onSelected(current.plusDays(1))
                            pendingState.value = null
                        },
                    ) {
                        Text("+1 day")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingState.value = null }) {
                    Text("Cancel")
                }
            },
        )
    }

    return remember(pendingState) {
        { current, onSelected ->
            pendingState.value = current to onSelected
        }
    }
}
