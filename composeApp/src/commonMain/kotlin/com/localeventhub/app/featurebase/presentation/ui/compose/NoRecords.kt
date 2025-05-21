package com.localeventhub.app.featurebase.presentation.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import localeventhub.composeapp.generated.resources.Res
import localeventhub.composeapp.generated.resources.ic_no_records
import org.jetbrains.compose.resources.painterResource

@Composable
fun NoRecords(message: String){
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(Res.drawable.ic_no_records), contentDescription = "")
        TextTitleMedium(modifier = Modifier.padding(10.dp), text = message)
    }
}