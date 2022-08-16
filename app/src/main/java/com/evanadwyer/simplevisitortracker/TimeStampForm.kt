package com.evanadwyer.simplevisitortracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TimeStampForm(onClickScanner: () -> Unit) {
    Column {
        BarcodeCTA(modifier = Modifier.clickable { onClickScanner.invoke() })
        TimeStamp()
    }
}

@Composable
fun BarcodeCTA(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BarCodeValue()
        Icon(painter = painterResource(id = R.drawable.camera), contentDescription = "Scan Barcode")
    }
}

@Composable
fun TimeStamp() {
    val currentTime by remember {
        mutableStateOf(System.currentTimeMillis())
    }
    val dateTime = SimpleDateFormat("dd/MM/yyy HH:mm:ss", Locale.US)
    Text(text = "Current time: ${dateTime.format(currentTime)}")
}