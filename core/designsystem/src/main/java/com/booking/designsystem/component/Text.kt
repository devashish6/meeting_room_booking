package com.booking.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FormFields(
    headerText : String,
    hintText: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier) {
    Column {
        Text(
            text = headerText,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(start = 12.dp))
        TextField(
            value = hintText,
            onValueChange = onValueChange,
            modifier = modifier)

    }
}