package com.booking.registration

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.booking.designsystem.component.ComponentButton
import com.booking.designsystem.component.FormFields
import com.booking.ui.Loading

private val TAG = "REGISTRATION_SCREEN_DEBUG"
@Composable
fun RegistrationRoute (
    registrationViewModel: RegistrationViewModel = hiltViewModel(),
    onSuccessfulRegistration : () -> Unit
) {
    val registrationUiState = registrationViewModel.registrationUiState.collectAsStateWithLifecycle()
    RegistrationScreen(
        registrationUiState = registrationUiState.value,
        onSuccessfulRegistration = onSuccessfulRegistration,
        onRegistrationClick = {name, email, password, confirmedPassword ->
            registrationViewModel.registerUser(name, email, password, confirmedPassword)
        }
    )
}

@Composable
fun RegistrationScreen(
    registrationUiState: RegistrationUiState,
    onSuccessfulRegistration: () -> Unit,
    onRegistrationClick: (String, String, String, String) -> Unit
) {
    var name by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var confirmPassword by remember {
        mutableStateOf("")
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        FormFields(
            headerText = stringResource(id = R.string.register_enter_your_name),
            hintText = name,
            onValueChange = { name = it },
            modifier = Modifier.padding(10.dp))
        FormFields(
            headerText = stringResource(id = R.string.register_enter_your_email),
            hintText = email,
            onValueChange = {email = it},
            modifier = Modifier.padding(10.dp))
        FormFields(
            headerText = stringResource(id = R.string.register_enter_your_password),
            hintText = password,
            onValueChange = {password = it},
            modifier = Modifier.padding(10.dp))
        FormFields(
            headerText = stringResource(id = R.string.register_confirm_your_password),
            hintText = confirmPassword,
            onValueChange = {confirmPassword = it},
            modifier = Modifier.padding(10.dp))
        ComponentButton(
            onClick =
            {
                onRegistrationClick(name, email, password, confirmPassword)
            },
            text = stringResource(id = R.string.register_confirm),
            modifier = Modifier.padding(top = 40.dp))
        Log.d(TAG, "RegistrationScreen: $registrationUiState")
        when (registrationUiState) {
            is RegistrationUiState.Loading -> {Loading()}
            is RegistrationUiState.Success -> onSuccessfulRegistration.invoke()
            is RegistrationUiState.InvalidEmailID -> {}// TODO() //show toast
            is RegistrationUiState.AccountAlreadyExists -> {}// TODO() //show toast
            is RegistrationUiState.PasswordMismatch -> {}//TODO() //show toast
            is RegistrationUiState.None -> {}
        }
    }

}


@Preview(showBackground = true)
@Composable
fun Preview() {
    RegistrationScreen(
        onRegistrationClick = {_,_,_,_ ->},
        onSuccessfulRegistration = {},
        registrationUiState = RegistrationUiState.None)
}