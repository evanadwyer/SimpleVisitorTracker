package com.evanadwyer.simplevisitortracker

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.services.sheets.v4.SheetsScopes


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    onPermissionGranted: () -> Unit
) {
    var cameraGranted by remember {
        mutableStateOf(false)
    }
    var googleGranted by remember {
        mutableStateOf(false)
    }
    if (cameraGranted && googleGranted) {
        onPermissionGranted.invoke()
    }
    Column(modifier = Modifier.fillMaxSize()) {
        if (!cameraGranted) {
            CameraPermission { cameraGranted = true }
        }
        if (!googleGranted) {
            GoogleSignInPermission { googleGranted = true }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermission(onPermissionGranted: () -> Unit) {
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

@Composable
fun GoogleSignInPermission(onPermissionGranted: () -> Unit) {
    val current = LocalContext.current

    val account = GoogleSignIn.getLastSignedInAccount(current)
    if (account != null) {
        onPermissionGranted.invoke()
    } else {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(SheetsScopes.SPREADSHEETS))
            .requestServerAuthCode(current.getString(R.string.server_client_id))
            .build()
        val googleSignInClient = GoogleSignIn.getClient(current, gso)
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = {
                if (it.resultCode == Activity.RESULT_OK) {
                    val intent = it.data
                    if (intent != null) {
                        val task: Task<GoogleSignInAccount> =
                            GoogleSignIn.getSignedInAccountFromIntent(intent)
                        handleSignInResult(task, onPermissionGranted)
                    }
                }
            })
        Button(onClick = {
            launcher.launch(googleSignInClient.signInIntent)
        }) {
            Text(text = "Sign in with Google")
        }
    }
}

private fun handleSignInResult(
    task: Task<GoogleSignInAccount>,
    onPermissionGranted: () -> Unit
) {
    try {
        val account = task.getResult(ApiException::class.java)
        if (account != null) {
            onPermissionGranted.invoke()
        }
    } catch (_: ApiException) {

    }
}