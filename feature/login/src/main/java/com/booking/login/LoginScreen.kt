package com.booking.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    loginViewModel: LoginViewModel = hiltViewModel(),
    onSuccessfulLogin: () -> Unit,
    onBackClicked: () -> Unit
) {
    val loginUiState = loginViewModel.loginUiState.collectAsStateWithLifecycle()
    LoginScreen(
        onBackClicked = onBackClicked,
        loginUiState = loginUiState.value,
        onSuccessfulLogin = onSuccessfulLogin,
        loginValidation = { email, password ->
            loginViewModel.validateLoginCredentials(email = email, password = password)
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBackClicked: () -> Unit = {},
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
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(R.string.login_details),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
            },
                navigationIcon = {
                    Image(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            onBackClicked()
                        })
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FormFields(
                headerText = stringResource(id = R.string.login_enter_your_email),
                hintText = email,
                onValueChange = { email = it },
                trailingIcon = Icons.Default.Email,
                modifier = Modifier.padding(10.dp)
            )
            FormFields(
                headerText = stringResource(id = R.string.login_enter_your_password),
                hintText = password,
                onValueChange = { password = it },
                trailingIcon = Icons.Default.Lock,
                modifier = Modifier.padding(10.dp),
                isPasswordField = true
            )
            ComponentButton(
                onClick =
                {
                    loginValidation(email, password)
                },
                text = stringResource(id = R.string.login_login),
                modifier = Modifier.padding(top = 40.dp)
            )
            when (loginUiState) {
                is LoginUiState.Loading -> Loading()
                is LoginUiState.Success -> onSuccessfulLogin.invoke()
                is LoginUiState.InvalidEmailID -> {
                    Toast.makeText(
                        LocalContext.current,
                        stringResource(R.string.please_enter_a_valid_email_id), Toast.LENGTH_SHORT
                    ).show()
                }

                is LoginUiState.InvalidCredentials -> {
                    Toast.makeText(
                        LocalContext.current,
                        stringResource(R.string.email_id_and_password_doesn_t_match),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is LoginUiState.None -> Log.e(TAG, "LoginScreen: ")
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun Preview() {
    LoginScreen(
        loginUiState = LoginUiState.None,
        onSuccessfulLogin = {},
        loginValidation = { _, _ -> })
}