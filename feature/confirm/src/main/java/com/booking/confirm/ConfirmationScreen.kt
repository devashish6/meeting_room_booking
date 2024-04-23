package com.booking.confirm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.booking.designsystem.component.ComponentButton

@Composable
fun ConfirmationScreen() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            modifier = Modifier
                .padding(top = 50.dp)
                .size(50.dp))
        Text(
            text = stringResource(R.string.booking_confirmed),
            style = MaterialTheme.typography.titleLarge)
        Text(text = stringResource(R.string.booking_on_date_at_time),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 20.dp))
        Text(text = stringResource(R.string.invitees_are),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 20.dp))
        ComponentButton(onClick = { /*TODO*/ }, text = stringResource(R.string.go_home))
    }

}

@Preview(showBackground = true)
@Composable
fun Preview() {
    ConfirmationScreen()
}