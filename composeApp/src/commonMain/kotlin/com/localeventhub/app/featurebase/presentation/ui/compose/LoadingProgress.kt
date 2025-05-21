package com.localeventhub.app.featurebase.presentation.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import localeventhub.composeapp.generated.resources.Res
import localeventhub.composeapp.generated.resources.loading
import localeventhub.composeapp.generated.resources.logo
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun FullScreenLoadingProgress(
    modifier: Modifier = Modifier,
    message: StringResource = Res.string.loading,
    paddingValues: PaddingValues = PaddingValues()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadingProgress()
        Text(modifier = Modifier.padding(top = 20.dp), text = stringResource (message), color = Color.Black)
    }
}

@Composable
fun LoadingProgress() {
    Box {
        CircularProgressIndicator(modifier = Modifier.width(80.dp).height(80.dp))
        Image(
            modifier = Modifier.width(60.dp).height(60.dp).align(Alignment.Center),
            painter = painterResource(Res.drawable.logo),
            contentDescription = "",
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
        )
    }
}
