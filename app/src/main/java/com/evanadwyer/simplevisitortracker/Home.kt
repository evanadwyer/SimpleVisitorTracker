package com.evanadwyer.simplevisitortracker

import androidx.activity.compose.BackHandler
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.evanadwyer.simplevisitortracker.ui.theme.LightGreen
import com.evanadwyer.simplevisitortracker.ui.theme.LightOrange
import com.evanadwyer.simplevisitortracker.ui.theme.LightYellow

@ExperimentalGetImage
@Composable
fun HomeScreen(viewModel: BarCodeScannerViewModel = viewModel()) {
    var scanning by remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current
    val memberScanned = viewModel.barcodeValue.isNotBlank()
    val memberStatus = if (memberScanned) {
        "Member: ${viewModel.barcodeValue}"
    } else {
        "Please scan your member tag"
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .background(LightOrange)
    ) {
        Image(
            painter = painterResource(id = R.drawable.gearhouse_logo),
            contentDescription = "Gearhouse logo",
            modifier = Modifier.size(128.dp)
        )
//        Text(
//            text = "Welcome to Gearhouse!",
//            fontSize = 32.sp,
//            color = LightGreen,
//            maxLines = 1
//        )
        Text(
            text = memberStatus,
            fontSize = 24.sp,
            color = LightGreen,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(if (memberScanned) Color.Transparent else LightYellow)
                .clickable {
                viewModel.clearBarcodeValue()
                scanning = true
            }
        )
        if (scanning) {
            SimpleCameraPreview(
                onBack = { scanning = false },
                onBarcodeScanned = { scanning = false }
            )
        } else {
            Text(
                text = "Whatcha here for?",
                fontSize = 32.sp,
                color = LightGreen,
            )
            VisitTypeSelectionCTAs(
                enabled = viewModel.barcodeValue.isNotBlank(),
                onBack = { scanning = true },
                onClick = {
                    viewModel.appendValuesVM(context, it)
                    scanning = true
                }
            )
        }
    }
}

@Composable
fun VisitTypeSelectionCTAs(
    enabled: Boolean,
    onBack: () -> Unit,
    onClick: (VisitType) -> Unit
) {
    BackHandler(onBack = onBack)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.background(LightOrange)
    ) {
        CTAButton(
            type = VisitType.GEAR_PICKUP,
            enabled = enabled,
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        CTAButton(
            type = VisitType.GEAR_DROP_OFF,
            enabled = enabled,
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        CTAButton(
            type = VisitType.EVENT,
            enabled = enabled,
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
fun CTAButton(
    type: VisitType,
    enabled: Boolean,
    onClick: (VisitType) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClick.invoke(type) },
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(backgroundColor = LightYellow),
        modifier = modifier.padding(8.dp)
    ) {
        Text(text = type.value, color = LightGreen, fontSize = 40.sp)
    }
}