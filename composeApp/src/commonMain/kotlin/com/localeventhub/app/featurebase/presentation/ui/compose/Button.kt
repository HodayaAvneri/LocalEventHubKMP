package com.localeventhub.app.featurebase.presentation.ui.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String = "",
    color: ButtonColors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
    textColor: Color = Color.White,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.defaultMinSize(minWidth = 220.dp, minHeight = 45.dp).padding(start = 10.dp, end = 10.dp),
        onClick = { onClick() },
        colors = if (enabled)
            color
         else
            ButtonDefaults.buttonColors(
                disabledContainerColor = Color.DarkGray
            )
        ,
        contentPadding = PaddingValues(10.dp),
        enabled = enabled,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 15.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(text = text, color = textColor)
    }
}

@Preview
@Composable
fun ButtonPreview() {

}
