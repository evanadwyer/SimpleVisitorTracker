package com.evanadwyer.simplevisitortracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.evanadwyer.simplevisitortracker.navigation.SimpleVisitorTrackerNavHost
import com.evanadwyer.simplevisitortracker.ui.theme.SimpleVisitorTrackerTheme

@ExperimentalGetImage
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

@ExperimentalGetImage
@Composable
fun SimpleVisitorTrackerApp() {
    val navController = rememberNavController()
    SimpleVisitorTrackerNavHost(
        navController = navController,
    )
}