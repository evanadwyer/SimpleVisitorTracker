package com.evanadwyer.simplevisitortracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
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
        navController = navController
    )
}

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