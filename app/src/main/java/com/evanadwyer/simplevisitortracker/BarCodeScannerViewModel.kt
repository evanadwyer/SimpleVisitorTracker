package com.evanadwyer.simplevisitortracker

import android.content.Context
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evanadwyer.simplevisitortracker.sheets.appendValues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BarCodeScannerViewModel : ViewModel() {

    var barcodeValue by mutableStateOf("hello")
        private set

    // TODO: configure scanner type to improve latency
    //  https://developers.google.com/ml-kit/vision/barcode-scanning/android#1.-configure-the-barcode-scanner

    private var barcodeScannerProcessor: BarcodeScannerProcessor = BarcodeScannerProcessor()

    @ExperimentalGetImage
    fun scanBarcode(
        imageProxy: ImageProxy,
        onBarcodeScanned: () -> Unit
    ) {
        BarcodeScannerProcessor().processImageProxy(
            imageProxy,
            onBarCodeValueChanged = { barcodeValue = it },
            onBarcodeScanned = onBarcodeScanned
        )
    }

    override fun onCleared() {
        barcodeScannerProcessor.stop()
        super.onCleared()
    }

    fun appendValuesVM(
        context: Context,
        simpleDateFormat: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val values = listOf(listOf(simpleDateFormat, barcodeValue))
            appendValues(
                context = context,
                viewModelScope,
                spreadsheetId = "1p2KlwvUreu2UoK0Sw563PYzpNUsB3d0sdZnnktcNpGk",
                range = "A1",
                valueInputOption = "USER_ENTERED",
                values = values
            )
        }
    }
}

