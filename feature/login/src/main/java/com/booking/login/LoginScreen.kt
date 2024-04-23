package com.booking.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.booking.designsystem.component.ComponentButton
import com.booking.designsystem.component.FormFields

@Composable
fun LoginScreen() {
    Column (
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FormFields(
            text = stringResource(id = R.string.login_enter_your_email),
            onValueChange = {},
            modifier = Modifier.padding(10.dp))
        FormFields(
            text = stringResource(id = R.string.login_enter_your_password),
            onValueChange = {},
            modifier = Modifier.padding(10.dp))
        ComponentButton(
            onClick = { /*TODO*/ },
            text = stringResource(id = R.string.login_login),
            modifier = Modifier.padding(top = 40.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    LoginScreen()
}