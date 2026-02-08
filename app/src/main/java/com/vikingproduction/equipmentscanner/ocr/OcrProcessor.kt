package com.vikingproduction.equipmentscanner.ocr

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.vikingproduction.equipmentscanner.model.EquipmentData
import com.vikingproduction.equipmentscanner.model.EquipmentType
import kotlinx.coroutines.tasks.await

class OcrProcessor(private val context: Context) {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun processImage(imageUri: Uri): EquipmentData {
        return try {
            val image = InputImage.fromFilePath(context, imageUri)
            val result = recognizer.process(image).await()
            parseEquipmentData(result.text)
        } catch (e: Exception) {
            EquipmentData(rawOcrText = "Erreur: ${e.message}")
        }
    }

    private fun parseEquipmentData(rawText: String): EquipmentData {
        val textLower = rawText.lowercase()
        val type = detectEquipmentType(textLower)
        return EquipmentData(
            equipmentType = type,
            fabricant = extractField(rawText, listOf("fabricant", "manufacturer", "marque")),
            modele = extractField(rawText, listOf("modele", "model", "ref")),
            anneeFabrication = extractYear(rawText),
            type = extractTypeLabel(textLower, type),
            puissanceNominale = extractPower(rawText),
            rawOcrText = rawText,
            confidence = calcConfidence(rawText, type)
        )
    }

    private fun detectEquipmentType(t: String): EquipmentType = when {
        t.contains("chaudiere") || t.contains("gaz") || t.contains("condensation") -> EquipmentType.CHAUDIERE
        t.contains("vmc") || t.contains("ventilation") || t.contains("hygro") -> EquipmentType.VMC
        t.contains("poele") || t.contains("granule") || t.contains("pellet") -> EquipmentType.POELE_GRANULES
        else -> EquipmentType.UNKNOWN
    }

    private fun extractField(text: String, keys: List<String>): String {
        for (k in keys) {
            val r = Regex("(?i)$k[:\\s]*([^\\n]+)")
            r.find(text)?.let { return it.groupValues[1].trim() }
        }
        return ""
    }

    private fun extractYear(t: String) = Regex("(19|20)\\d{2}").find(t)?.value ?: ""

    private fun extractPower(t: String): String {
        Regex("(\\d+[.,]?\\d*)\\s*(kw|kW|KW)").find(t)?.let { return "${it.groupValues[1]} kW" }
        return ""
    }

    private fun extractTypeLabel(t: String, type: EquipmentType) = when (type) {
        EquipmentType.CHAUDIERE -> when {
            t.contains("condensation") && t.contains("gaz") -> "Chaudiere gaz condensation"
            t.contains("gaz") -> "Chaudiere gaz"
            else -> "Chaudiere"
        }
        EquipmentType.VMC -> when {
            t.contains("double flux") -> "VMC double flux"
            t.contains("hygro b") -> "VMC simple flux Hygro B"
            t.contains("hygro a") -> "VMC simple flux Hygro A"
            else -> "VMC simple flux"
        }
        EquipmentType.POELE_GRANULES -> "Poele a granules"
        EquipmentType.UNKNOWN -> "Non identifie"
    }

    private fun calcConfidence(t: String, type: EquipmentType): Float {
        if (type == EquipmentType.UNKNOWN) return 0.3f
        var c = 0.5f
        if (t.length > 50) c += 0.1f
        if (t.length > 100) c += 0.1f
        if (extractYear(t).isNotEmpty()) c += 0.1f
        if (extractPower(t).isNotEmpty()) c += 0.1f
        return c.coerceAtMost(0.98f)
    }
}
