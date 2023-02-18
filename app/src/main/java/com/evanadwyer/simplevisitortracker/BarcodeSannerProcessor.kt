package com.evanadwyer.simplevisitortracker

import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarcodeScannerProcessor(private val iDToName: Map<String, String>) {

    private val executor = ScopedExecutor(TaskExecutors.MAIN_THREAD)
    private var isShutdown = false

    // Note that if you know which format of barcode your app is dealing with, detection will be
    // faster to specify the supported barcode formats one by one, e.g.
    // BarcodeScannerOptions.Builder()
    //     .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
    //     .build();
    private val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient()

    @ExperimentalGetImage
    fun processImageProxy(
        image: ImageProxy,
        onBarCodeValueChanged: (Pair<String, String>) -> Unit,
        onBarcodeScanned: () -> Unit
    ) {
        if (isShutdown) {
            return
        }

        requestDetectInImage(
            InputImage.fromMediaImage(image.image!!, image.imageInfo.rotationDegrees),
            onBarCodeValueChanged,
            onBarcodeScanned
        )
            // When the image is from CameraX analysis use case, must call image.close() on received
            // images when finished using them. Otherwise, new images may not be received or the camera
            // may stall.
            .addOnCompleteListener { image.close() }
    }

    private fun requestDetectInImage(
        image: InputImage,
        onBarCodeValueChanged: (Pair<String, String>) -> Unit,
        onBarcodeScanned: () -> Unit
    ): Task<List<Barcode>> {
        return setUpListener(
            detectInImage(image),
            onBarCodeValueChanged,
            onBarcodeScanned
        )
    }

    private fun setUpListener(
        task: Task<List<Barcode>>,
        onBarCodeValueChanged: (Pair<String, String>) -> Unit,
        onBarcodeScanned: () -> Unit
    ): Task<List<Barcode>> {
        return task
            .addOnSuccessListener(
                executor
            ) { results ->
                this@BarcodeScannerProcessor.onSuccess(results,
                    onBarCodeValueChanged,
                    onBarcodeScanned
                    )
            }
            .addOnFailureListener(
                executor
            ) { e: Exception ->
                this@BarcodeScannerProcessor.onFailure(e)
            }
    }

    fun stop() {
        executor.shutdown()
        isShutdown = true
        barcodeScanner.close()
    }

    private fun detectInImage(image: InputImage): Task<List<Barcode>> {
        return barcodeScanner.process(image)
    }

    private fun onSuccess(results: List<Barcode>,
                          onBarCodeValueChanged: (Pair<String, String>) -> Unit,
                          onBarcodeScanned: () -> Unit
                  ) {
        if (results.isEmpty()) {
            return
        }
        for (i in results.indices) {
            val barcode = results[i]
            barcode.displayValue?.let { Log.d("barcode read", it) }
        }
        val barcode = results[0].displayValue ?: "test"
        if (barcode in iDToName) {
            iDToName[barcode]?.let { onBarCodeValueChanged.invoke(barcode to it) }
            onBarcodeScanned.invoke()
        }
    }

    private fun onFailure(e: Exception) {
        Log.e("barcode scanner", "Barcode detection failed $e")
    }
}