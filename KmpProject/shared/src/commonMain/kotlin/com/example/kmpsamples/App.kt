package com.example.kmpsamples

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun App() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        SamplesApp()
    }
}
