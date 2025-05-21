package com.localeventhub.app.featurebase.presentation.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialog(onDismissed: () -> Unit, onCustomButtonClick: () -> Unit) {
    BasicAlertDialog(modifier = Modifier.background(shape = RoundedCornerShape(20.dp), color = Color.White), properties = DialogProperties(), onDismissRequest = onDismissed, content = {
        Column(
            modifier = Modifier.padding(10.dp).
                background(color = Color.White).padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextTitleMedium("Permission Required", textAlign = TextAlign.Center)
            CustomButton(modifier = Modifier.padding(top = 15.dp),  text = "Open Settings", onClick = onCustomButtonClick);
        }

    })
}