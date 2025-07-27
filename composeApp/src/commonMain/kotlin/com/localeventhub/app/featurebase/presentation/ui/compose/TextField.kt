package com.localeventhub.app.featurebase.presentation.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.localeventhub.app.featurebase.common.Colors
import localeventhub.composeapp.generated.resources.Res
import localeventhub.composeapp.generated.resources.eye
import localeventhub.composeapp.generated.resources.hidden
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    hint: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Words,
        keyboardType = KeyboardType.Text
    ),
    isError: () -> Boolean,
    errorString: @Composable () -> String,
    textFieldValue: () -> String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit = {},
    enabled: () -> Boolean = { true },
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth().clickable {
                    onClick()
                },
            enabled = enabled(),
            value = textFieldValue(),
            onValueChange = {
                onValueChange(if (keyboardOptions.keyboardType == KeyboardType.Phone) it.take(10) else it)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Colors.primary,
                disabledBorderColor = Color.Black,
                disabledTextColor = Color.Black
            ),
            label = {
                Text(
                    text = hint,
                    color = Color.DarkGray
                )

            },
            singleLine = true,
            maxLines = 1,
            isError = isError(),
            keyboardOptions = keyboardOptions,
            visualTransformation = if (keyboardOptions.keyboardType == KeyboardType.Password && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (keyboardOptions.keyboardType == KeyboardType.Password)
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = if (passwordVisible) painterResource(Res.drawable.hidden) else painterResource(Res.drawable.eye),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )
        if (isError() && errorString().isNotEmpty()) {
            Text(
                modifier = Modifier.padding(start = 35.dp),
                text = errorString(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun TextFieldWithLeadingIcon(
    modifier: Modifier = Modifier,
    hint: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Words,
        keyboardType = KeyboardType.Text
    ),
    isError: () -> Boolean,
    errorString: () -> StringResource,
    textFieldValue: () -> String,
    onValueChange: (String) -> Unit,
    leadingIcon: () -> DrawableResource,
) {
    Column {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp, end = 20.dp)
                .background(
                    Color.Gray,
                    shape = RoundedCornerShape(30.dp)
                ),
            value = textFieldValue(),
            onValueChange = {
                onValueChange(if (keyboardOptions.keyboardType == KeyboardType.Phone) it.take(10) else it)
            },
            label = {
                Text(
                    text = hint,
                    color = Color.Gray
                )
            },
            singleLine = true,
            maxLines = 1,
            isError = isError(),
            keyboardOptions = keyboardOptions,
            visualTransformation = if (keyboardOptions.keyboardType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None,
            leadingIcon = {
                Image(painter = painterResource(resource = leadingIcon()), contentDescription = "")
            }
        )
        if (isError()) {
            Text(
                modifier = Modifier.padding(start = 35.dp),
                text = stringResource(resource = errorString()),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
