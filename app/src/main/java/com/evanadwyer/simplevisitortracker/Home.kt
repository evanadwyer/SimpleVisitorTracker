package com.evanadwyer.simplevisitortracker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TimeStampMemberButton(
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick.invoke()
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.app_padding))
    ) {
        Text(text = stringResource(R.string.timestamp_visit_button))
    }
}

@Composable
fun BarCodeValue(
    viewModel: BarCodeScannerViewModel = viewModel()
) {
    val barcode = viewModel.barcodeValue
    Text(text = "barcode: $barcode")
}