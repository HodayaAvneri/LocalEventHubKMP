package com.localeventhub.app.dashboard.presentation.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.localeventhub.app.expect.GoogleMap
import com.localeventhub.app.expect.LatLng

@Composable
fun MapScreen(paddingValues: () -> PaddingValues){

    Column(modifier = Modifier.fillMaxSize().padding(paddingValues()), verticalArrangement = Arrangement.Center) {
        GoogleMap(currentLocationPosition = LatLng(51.509865,-0.118092), markerPosition = LatLng(51.509865,-0.118092))
    }
}
