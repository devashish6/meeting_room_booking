package com.booking.login

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

const val TAG = "LOGIN_SCREEN_DEBUG"
@Composable
fun LoginRoute(
    loginViewModel : LoginViewModel = hiltViewModel(),
    onSuccessfulLogin : () -> Unit
) {
    val loginUiState = loginViewModel.loginUiState.collectAsStateWithLifecycle()
    LoginScreen(
        loginUiState = loginUiState.value,
        onSuccessfulLogin = onSuccessfulLogin,
        loginValidation = { email, password ->
            loginViewModel.validateLoginCredentials(email = email, password = password)
        })
}

@Composable
fun LoginScreen(
    loginUiState: LoginUiState,
    loginValidation: (String, String) -> Unit,
    onSuccessfulLogin: () -> Unit
) {

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FormFields(
            headerText = stringResource(id = R.string.login_enter_your_email),
            hintText = email,
            onValueChange = { email = it },
            modifier = Modifier.padding(10.dp)
        )
        FormFields(
            headerText = stringResource(id = R.string.login_enter_your_password),
            hintText = password,
            onValueChange = { password = it },
            modifier = Modifier.padding(10.dp)
        )
        ComponentButton(
            onClick =
            {
                loginValidation(email, password)
            },
            text = stringResource(id = R.string.login_login),
            modifier = Modifier.padding(top = 40.dp)
        )
        when(loginUiState) {
            is LoginUiState.Loading -> Loading()
            is LoginUiState.Success -> onSuccessfulLogin.invoke()
            is LoginUiState.InvalidEmailID -> TODO() //show a toast
            is LoginUiState.InvalidCredentials -> TODO() //show a toast
            is LoginUiState.None -> Log.e(TAG, "LoginScreen: ")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    LoginScreen(
        loginUiState = LoginUiState.None ,
        onSuccessfulLogin = {},
        loginValidation = { _, _ -> })
}