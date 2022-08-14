package com.evanadwyer.simplevisitortracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.evanadwyer.simplevisitortracker.ui.theme.SimpleVisitorTrackerTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleVisitorTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SimpleVisitorTrackerApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SimpleVisitorTrackerApp() {
    val cameraPermissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)

    if (cameraPermissionState.status.isGranted) {
        CameraPreview(modifier = Modifier.fillMaxSize())
    } else {
        Column() {
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SimpleVisitorTrackerTheme {
        SimpleVisitorTrackerApp()
    }
}