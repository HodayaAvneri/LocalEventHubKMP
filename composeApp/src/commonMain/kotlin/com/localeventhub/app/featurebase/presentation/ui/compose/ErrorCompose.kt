package com.localeventhub.app.featurebase.presentation.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import localeventhub.composeapp.generated.resources.Res
import localeventhub.composeapp.generated.resources.ic_error
import localeventhub.composeapp.generated.resources.ic_no_records
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

sealed class ErrorType(val image: DrawableResource, val description: String, val buttonText: String) {
    data class ApiError(
        val errorImage: DrawableResource = Res.drawable.ic_error,
        val errorDescription: String = "Something went wrong please try again!!!",
        val errorButtonText: String = "Retry"
    ) : ErrorType(errorImage, errorDescription, errorButtonText)

    data class NoRecords(
        val noRecordsImage: DrawableResource = Res.drawable.ic_no_records,
        val noRecordsDescription: String = "No Records Found",
        val noRecordsButtonText: String = "Refresh"
    ) : ErrorType(noRecordsImage, noRecordsDescription, noRecordsButtonText)

    data class NoInternet(
        val noInternetImage: DrawableResource = Res.drawable.ic_error,
        val noInternetDescription: String = "Internet Connection Not Available",
        val noInternetButtonText: String = "Retry"
    ) : ErrorType(noInternetImage, noInternetDescription, noInternetButtonText)
}

@Composable
fun ErrorCompose(
    type: ErrorType = ErrorType.ApiError(),
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(resource = type.image),
            contentDescription = "Error Image"
        )
        Text(modifier = Modifier.padding(20.dp, 20.dp),text = type.description,textAlign = TextAlign.Center,style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
        CustomButton(text = type.buttonText) {
            onClick()
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
