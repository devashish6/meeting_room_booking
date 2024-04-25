package com.booking.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Loading() {
    CircularProgressIndicator(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    )
}

@Preview
@Composable
fun Preview() {
    Loading()
}