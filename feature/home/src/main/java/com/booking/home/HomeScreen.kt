package com.booking.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.booking.designsystem.component.ComponentButton

@Composable
fun HomeScreen(
    navigateToLogin : () -> Unit,
    navigateToRegister : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ComponentButton(
            onClick = {navigateToLogin.invoke() },
            modifier = Modifier.padding(25.dp),
            text = stringResource(id = R.string.home_login))
        ComponentButton(
            onClick = {navigateToRegister.invoke() },
            modifier = Modifier.padding(25.dp),
            text = stringResource(id = R.string.home_register))
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    HomeScreen({}, {})
}