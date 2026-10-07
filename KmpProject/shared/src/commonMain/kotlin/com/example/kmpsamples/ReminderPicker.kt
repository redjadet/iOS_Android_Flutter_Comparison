package com.example.kmpsamples

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDateTime

@Composable
expect fun rememberReminderPickerHandler(): (LocalDateTime, (LocalDateTime) -> Unit) -> Unit
