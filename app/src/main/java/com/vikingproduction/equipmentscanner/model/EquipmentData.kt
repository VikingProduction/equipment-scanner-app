package com.vikingproduction.equipmentscanner.model

/**
 * Types d'equipements supportes par l'application
 */
enum class EquipmentType(val label: String) {
    CHAUDIERE("Chaudiere"),
    VMC("VMC"),
    POELE_GRANULES("Poele a granules"),
    UNKNOWN("Non identifie")
}

/**
 * Donnees extraites d'un equipement via OCR
 */
data class EquipmentData(
    val equipmentType: EquipmentType = EquipmentType.UNKNOWN,
    val fabricant: String = "",
    val modele: String = "",
    val anneeFabrication: String = "",
    val type: String = "",
    val puissanceNominale: String = "",
    val rawOcrText: String = "",
    val confidence: Float = 0f
) {
    fun hasPuissance(): Boolean {
        return equipmentType == EquipmentType.CHAUDIERE ||
               equipmentType == EquipmentType.POELE_GRANULES
    }

    companion object {
        fun sampleChaudiere() = EquipmentData(
            equipmentType = EquipmentType.CHAUDIERE,
            fabricant = "Viessmann",
            modele = "Vitodens 100",
            anneeFabrication = "2018",
            type = "Chaudiere gaz condensation",
            puissanceNominale = "24 kW",
            confidence = 0.95f
        )

        fun sampleVMC() = EquipmentData(
            equipmentType = EquipmentType.VMC,
            fabricant = "Atlantic",
            modele = "VMI 50F / A",
            anneeFabrication = "2021",
            type = "VMC simple flux Hygro A",
            confidence = 0.90f
        )

        fun samplePoele() = EquipmentData(
            equipmentType = EquipmentType.POELE_GRANULES,
            fabricant = "Invicta",
            modele = "Lodi 10",
            anneeFabrication = "2019",
            type = "Poele a granules",
            puissanceNominale = "10 kW",
            confidence = 0.92f
        )
    }
}
