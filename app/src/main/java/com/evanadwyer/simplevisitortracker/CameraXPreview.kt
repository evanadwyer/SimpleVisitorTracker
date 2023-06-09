package com.evanadwyer.simplevisitortracker

import androidx.activity.compose.BackHandler
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.evanadwyer.simplevisitortracker.ui.theme.LightYellow

@ExperimentalGetImage
@Composable
fun SimpleCameraPreview(
    onBarcodeScanned: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)
    CameraXLivePreview(
        onBarcodeScanned = onBarcodeScanned,
        modifier = modifier.fillMaxSize()
    )
}

@ExperimentalGetImage
@Composable
fun CameraXLivePreview(
    modifier: Modifier = Modifier,
    onBarcodeScanned: () -> Unit,
    viewModel: BarCodeScannerViewModel = viewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    Box(modifier = modifier.fillMaxWidth()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val executor = ContextCompat.getMainExecutor(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                        .build()

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        ImageAnalysis
                            .Builder()
                            .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .apply {
                                setAnalyzer(ContextCompat.getMainExecutor(ctx)) {
                                    viewModel.scanBarcode(
                                        it,
                                        onBarcodeScanned
                                    )
                                }
                            },
                        preview
                    )
                }, executor)
                previewView
            },
            modifier = modifier,
        )
//        Box(
//            modifier = Modifier
////                .background(color = LightYellow)
//                .size(180.dp, 300.dp)
//                .align(Alignment.TopEnd)
//                .padding(24.dp)
//                .border(5.dp, LightGreen, RectangleShape)
//        )
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.99f)
        ) {
            drawRect(
                color = LightYellow,
                size = Size(width = 250.dp.toPx(), height = 180.dp.toPx()),
                topLeft = Offset(x = 70.dp.toPx(), y = 100.dp.toPx()),
                style = Stroke(width = 6.dp.toPx()),
                blendMode = BlendMode.SrcOut
            )
        }
    }

}