package com.vikingproduction.equipmentscanner.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptureScreen(
    onImageCaptured: (String, Boolean) -> Unit
) {
    var isPlateVisible by remember { mutableStateOf(true) }
    val context = LocalContext.current
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempImageUri != null) {
            onImageCaptured(tempImageUri.toString(), isPlateVisible)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageCaptured(it.toString(), isPlateVisible) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Equipment Scanner", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Identification d'equipement",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Checkbox plaque signaletique
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = isPlateVisible, onCheckedChange = { isPlateVisible = it })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Plaque signaletique visible ?", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }

            // Message conditionnel
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isPlateVisible) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (isPlateVisible)
                            "Photographiez la plaque signaletique de l'element a expertiser."
                        else
                            "Photographiez l'element a expertiser de maniere globale pour en determiner ses caracteristiques.",
                        fontSize = 14.sp, textAlign = TextAlign.Center,
                        color = if (isPlateVisible) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }

            // Cadre de visee pour plaque
            if (isPlateVisible) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(3.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.05f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("[ Cadre de visee plaque ]", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        Text("Flash auto active", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Boutons Camera et Galerie
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = {
                        val file = File.createTempFile("equipment_", ".jpg", context.cacheDir)
                        tempImageUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                        tempImageUri?.let { cameraLauncher.launch(it) }
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Camera", modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Camera", fontSize = 16.sp)
                }

                OutlinedButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = "Galerie", modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Galerie", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
