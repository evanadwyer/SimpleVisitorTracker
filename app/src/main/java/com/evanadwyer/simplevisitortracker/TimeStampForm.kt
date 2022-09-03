package com.evanadwyer.simplevisitortracker

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalGetImage
@Composable
fun TimeStampForm(viewModel: BarCodeScannerViewModel = viewModel()) {
    var scanner by remember {
        mutableStateOf(false)
    }
    val current = LocalContext.current
    val currentTime = SimpleDateFormat("dd/MM/yyy HH:mm:ss", Locale.US).format(System.currentTimeMillis())
    if (scanner) {
        SimpleCameraPreview(
            onBarcodeScanned = { scanner = false },
            onBack = { scanner = false }
        )
    } else {
        Column {
            BarcodeCTA(modifier = Modifier.clickable { scanner = true })
            TimeStamp(currentTime)
            Button(onClick = {
                viewModel.appendValuesVM(current, currentTime)
            }) {
                Text(text = "Submit")
            }
        }
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
fun TimeStamp(dateTime: String) {
    Text(text = "Current time: $dateTime")
}