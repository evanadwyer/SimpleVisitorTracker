package com.evanadwyer.simplevisitortracker

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.evanadwyer.simplevisitortracker.sheets.SheetsService
import com.evanadwyer.simplevisitortracker.sound.Sounder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class BarCodeScannerViewModel(application: Application) : AndroidViewModel(application) {

    //    first is ID
//    second is First Name
    var barcodeValue by mutableStateOf("" to "")
        private set

    fun setBarcodeValueForGuestSignIn(newPair: Pair<String, String>) {
        barcodeValue = newPair
    }

    fun clearBarcodeValue() {
        barcodeValue = "" to ""
    }

    private var sounder: Sounder = Sounder(application.assets)
    private val sheetsService = SheetsService(application)

    private val iDToName = mutableMapOf<String, String>()

    init {
        populateIDsToNames(application)
    }

    // TODO: configure scanner type to improve latency
    //  https://developers.google.com/ml-kit/vision/barcode-scanning/android#1.-configure-the-barcode-scanner

    private var barcodeScannerProcessor = BarcodeScannerProcessor(iDToName)

    @ExperimentalGetImage
    fun scanBarcode(
        imageProxy: ImageProxy,
        onBarcodeScanned: () -> Unit
    ) {
        barcodeScannerProcessor.processImageProxy(
            imageProxy,
            onBarCodeValueChanged = { barcodeValue = it },
            onBarcodeScanned = onBarcodeScanned
        )
    }

    override fun onCleared() {
        barcodeScannerProcessor.stop()
        sounder.release()
        super.onCleared()
    }

    fun appendValuesVM(
        context: Context,
        visitType: VisitType,
        simpleDateFormat: String = SimpleDateFormat(
            "MM/dd/yyy HH:mm:ss",
            Locale.US
        ).format(System.currentTimeMillis())
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val values = listOf(listOf(simpleDateFormat, barcodeValue.first, visitType.name))
            val response = sheetsService.appendValues(
//                gearhouse
//                spreadsheetId = "1J3e4GLJSXUAFHo7wIyWtxUlY8sVYklmoSCddETne078",
//                mine
                spreadsheetId = "1p2KlwvUreu2UoK0Sw563PYzpNUsB3d0sdZnnktcNpGk",
                range = "A1",
                valueInputOption = "USER_ENTERED",
                values = values
            )
            if (response != null) {
                sounder.play()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Thank you for checking in!", Toast.LENGTH_LONG).show()
                }
                barcodeValue = "" to ""
            }
        }
    }

    private fun populateIDsToNames(
        context: Context,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = sheetsService.readIDsToNames(
//                gearhouse
//                spreadsheetId = "",
//                mine
                spreadsheetId = "1HQ92hTMkvekekHd8akjH_d4SJH2jKM7BdNkiSNJS61M",
//                google quickstart
//                spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms",

//                mine
                range = "Sheet1!A2:D"
//                google quickstart
//                range = "Class Data!A2:E"
            )
            if (response != null) {
                val values = response.getValues()
                if (values != null && values.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Names loaded", Toast.LENGTH_LONG).show()
                    }
                    for (row in values) {
                        Log.d("Read Names", "${row[0]}, ${row[1]}")
                        iDToName.putIfAbsent(row[0].toString(), row[1].toString())
                    }
                }
            }
        }
    }
}

