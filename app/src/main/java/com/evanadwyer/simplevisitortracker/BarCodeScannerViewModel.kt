package com.evanadwyer.simplevisitortracker

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

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
}

