package com.vikingproduction.equipmentscanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vikingproduction.equipmentscanner.ui.theme.EquipmentScannerTheme
import com.vikingproduction.equipmentscanner.screens.CaptureScreen
import com.vikingproduction.equipmentscanner.screens.ProcessingScreen
import com.vikingproduction.equipmentscanner.screens.ResultsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EquipmentScannerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EquipmentScannerApp()
                }
            }
        }
    }
}

@Composable
fun EquipmentScannerApp() {
    val navController = rememberNavController()
    var capturedImageUri by remember { mutableStateOf<String?>(null) }
    var isPlateVisible by remember { mutableStateOf(true) }

    NavHost(navController = navController, startDestination = "capture") {
        composable("capture") {
            CaptureScreen(
                onImageCaptured = { uri, plateVisible ->
                    capturedImageUri = uri
                    isPlateVisible = plateVisible
                    navController.navigate("processing")
                }
            )
        }
        composable("processing") {
            ProcessingScreen(
                imageUri = capturedImageUri,
                onProcessingComplete = { equipmentData ->
                    navController.navigate("results") {
                        popUpTo("processing") { inclusive = true }
                    }
                },
                onNavigateToResults = {
                    navController.navigate("results") {
                        popUpTo("processing") { inclusive = true }
                    }
                }
            )
        }
        composable("results") {
            ResultsScreen(
                imageUri = capturedImageUri,
                onNewScan = {
                    capturedImageUri = null
                    navController.navigate("capture") {
                        popUpTo("capture") { inclusive = true }
                    }
                }
            )
        }
    }
}
