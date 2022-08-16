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
        imageProxy: ImageProxy
    ) {
        BarcodeScannerProcessor().processImageProxy(
            imageProxy,
            onBarCodeValueChanged = this::updateBarCode
        )
    }

    private fun updateBarCode(newValue: String) {
        barcodeValue = newValue
    }

    override fun onCleared() {
        barcodeScannerProcessor.stop()
        super.onCleared()
    }
}

