package com.evanadwyer.simplevisitortracker.navigation

import android.Manifest
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.evanadwyer.simplevisitortracker.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalGetImage
@Composable
fun SimpleVisitorTrackerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)


    NavHost(
        navController = navController,
        startDestination = if (cameraPermissionState.status.isGranted) {
            Home.route
        } else {
            Permissions.route
        },
        modifier = modifier
    ) {
        composable(route = Home.route) {
            TimeStampMemberButton { navController.navigateSingleTopTo(Scanner.route) }
        }
        composable(route = Permissions.route) {
            PermissionScreen { navController.navigateSingleTopTo(Home.route) }
        }
        composable(route = Scanner.route) {
            SimpleCameraPreview()
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }