package com.booking.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FormFields(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier) {
    Column {
        TextField(
            value = text,
            onValueChange = onValueChange,
            modifier = modifier)

    }
}