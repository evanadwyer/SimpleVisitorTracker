package com.evanadwyer.simplevisitortracker

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    onPermissionGranted: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)


    if (cameraPermissionState.status.isGranted) {
        onPermissionGranted.invoke()
    } else {
        Column {
            val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                "We gots to use the camera to scan, guy"
            } else {
                "Need camera permission"
            }
            Text(text = textToShow)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.app_padding)))
            Button(onClick = {
                cameraPermissionState.launchPermissionRequest()
            }) {
                Text(text = "Request Permission")
            }
        }
    }
}