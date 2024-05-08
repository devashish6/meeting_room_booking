package com.booking.registration

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
import androidx.compose.material.icons.filled.AccountBox
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

private val TAG = "REGISTRATION_SCREEN_DEBUG"

@Composable
fun RegistrationRoute(
    registrationViewModel: RegistrationViewModel = hiltViewModel(),
    onSuccessfulRegistration: () -> Unit,
    onBackClicked: () -> Unit
) {
    val registrationUiState =
        registrationViewModel.registrationUiState.collectAsStateWithLifecycle()
    RegistrationScreen(
        onBackClicked = onBackClicked,
        registrationUiState = registrationUiState.value,
        onSuccessfulRegistration = onSuccessfulRegistration,
        onRegistrationClick = { name, email, password, confirmedPassword ->
            registrationViewModel.registerUser(name, email, password, confirmedPassword)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    onBackClicked : () -> Unit = {},
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
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(R.string.enter_your_details),
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
                .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FormFields(
                headerText = stringResource(id = R.string.register_enter_your_name),
                hintText = name,
                onValueChange = { name = it },
                trailingIcon = Icons.Default.AccountBox,
                modifier = Modifier.padding(10.dp)
            )
            FormFields(
                headerText = stringResource(id = R.string.register_enter_your_email),
                hintText = email,
                onValueChange = { email = it },
                trailingIcon = Icons.Default.Email,
                modifier = Modifier.padding(10.dp)
            )
            FormFields(
                headerText = stringResource(id = R.string.register_enter_your_password),
                hintText = password,
                onValueChange = { password = it },
                trailingIcon = Icons.Default.Lock,
                modifier = Modifier.padding(10.dp)
            )
            FormFields(
                headerText = stringResource(id = R.string.register_confirm_your_password),
                hintText = confirmPassword,
                onValueChange = { confirmPassword = it },
                trailingIcon = Icons.Default.Lock,
                modifier = Modifier.padding(10.dp)
            )
            ComponentButton(
                onClick =
                {
                    onRegistrationClick(name, email, password, confirmPassword)
                },
                text = stringResource(id = R.string.register_confirm),
                modifier = Modifier.padding(top = 40.dp)
            )
            Log.d(TAG, "RegistrationScreen: $registrationUiState")
            when (registrationUiState) {
                is RegistrationUiState.Loading -> {
                    Loading()
                }

                is RegistrationUiState.Success -> onSuccessfulRegistration.invoke()
                is RegistrationUiState.InvalidEmailID -> {
                    Toast.makeText(LocalContext.current,
                        stringResource(R.string.please_enter_a_valid_email_id), Toast.LENGTH_SHORT).show()
                }
                is RegistrationUiState.AccountAlreadyExists -> {
                    Toast.makeText(LocalContext.current,
                        stringResource(R.string.account_already_exists), Toast.LENGTH_SHORT).show()

                }
                is RegistrationUiState.PasswordMismatch -> {
                    Toast.makeText(LocalContext.current,
                        stringResource(R.string.passwords_doesn_t_match), Toast.LENGTH_SHORT).show()
                }
                is RegistrationUiState.None -> {}
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    RegistrationScreen(
        onRegistrationClick = { _, _, _, _ -> },
        onSuccessfulRegistration = {},
        registrationUiState = RegistrationUiState.None
    )
}