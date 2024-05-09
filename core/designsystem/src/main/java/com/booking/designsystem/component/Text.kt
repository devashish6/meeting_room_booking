package com.booking.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun FormFields(
    modifier: Modifier = Modifier,
    headerText : String,
    hintText: String,
    onValueChange: (String) -> Unit,
    trailingIcon: ImageVector,
    isPasswordField: Boolean = false,
) {
    var passwordVisibility: Boolean by remember { mutableStateOf(!isPasswordField) }

    var text by remember {
        mutableStateOf(hintText)
    }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(text)
        },
        label = {
            Text(
                text = headerText,
                style = MaterialTheme.typography.titleSmall)
        },
        trailingIcon = {
            Image(
                imageVector = trailingIcon,
                contentDescription = "",
                modifier = Modifier.clickable {
                    if (isPasswordField) {
                        passwordVisibility = !passwordVisibility
                    }
                })
        },
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = modifier
    )
}