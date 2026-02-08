package com.vikingproduction.equipmentscanner.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vikingproduction.equipmentscanner.model.EquipmentData
import kotlinx.coroutines.delay

@Composable
fun ProcessingScreen(
    imageUri: String?,
    onProcessingComplete: (EquipmentData) -> Unit,
    onNavigateToResults: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }
    var statusMessage by remember { mutableStateOf("Initialisation...") }

    LaunchedEffect(Unit) {
        val steps = listOf(
            0.15f to "Chargement de l'image...",
            0.35f to "Detection de texte OCR...",
            0.55f to "Extraction des donnees...",
            0.75f to "Identification de l'equipement...",
            0.90f to "Verification des resultats...",
            1.0f to "Analyse terminee !"
        )
        for ((targetProgress, message) in steps) {
            statusMessage = message
            while (progress < targetProgress) {
                delay(50)
                progress = (progress + 0.02f).coerceAtMost(targetProgress)
            }
            delay(300)
        }
        delay(500)
        onNavigateToResults()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (imageUri != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(Uri.parse(imageUri)).crossfade(true).build(),
                contentDescription = "Apercu",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().blur(20.dp)
            )
        }

        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background.copy(alpha = 0.85f)))

        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(80.dp), color = MaterialTheme.colorScheme.primary, strokeWidth = 6.dp)
            Spacer(modifier = Modifier.height(32.dp))
            Text("Analyse en cours...", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text("OCR + identification (5s max)", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(32.dp))
            Text(statusMessage, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.secondary, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(24.dp))
            Column(modifier = Modifier.fillMaxWidth(0.8f), horizontalAlignment = Alignment.CenterHorizontally) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(6.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("${(progress * 100).toInt()}%", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
