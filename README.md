# Equipment Scanner - Application Android

Application Android d'identification d'equipements thermiques (chaudiere, VMC, poele a granules) via OCR de plaque signaletique.

## Fonctionnalites

### Page 1 : Accueil / Capture
- Bouton photo camera ou upload galerie
- Checkbox "Plaque signaletique visible ?"
  - **Si oui** : "Photographiez la plaque signaletique de l'element a expertiser" + cadre de visee + flash auto
  - **Si non** : "Photographiez l'element a expertiser de maniere globale pour en determiner ses caracteristiques"

### Page 2 : Traitement (Chargement)
- Spinner + message : "Analyse en cours... OCR + identification (5s max)"
- Apercu photo floutee en fond
- Barre de progression : 0-100%
- Messages d'etapes : Detection OCR, Extraction, Identification, Verification

### Page 3 : Resultats Extraction
Donnees OCR en champs lisibles, organisees par type d'equipement :

#### Chaudiere
| Champ | Exemple |
|-------|--------|
| Fabricant | Viessmann |
| Modele | Vitodens 100 |
| Annee | 2018 |
| Type | Chaudiere gaz condensation |
| Puissance | 24 kW |

#### VMC
| Champ | Exemple |
|-------|--------|
| Fabricant | Atlantic |
| Modele | VMI 50F / A |
| Annee | 2021 |
| Type | VMC simple flux Hygro A |

#### Poele a granules
| Champ | Exemple |
|-------|--------|
| Fabricant | Invicta |
| Modele | Lodi 10 |
| Annee | 2019 |
| Type | Poele a granules |
| Puissance | 10 kW |

Bouton "Nouveau scan" pour relancer une capture.

## Stack technique
- **Langage** : Kotlin
- **UI** : Jetpack Compose + Material 3
- **Camera** : CameraX
- **OCR** : Google ML Kit Text Recognition
- **Navigation** : Navigation Compose
- **Images** : Coil
- **Min SDK** : 26 (Android 8.0)
- **Target SDK** : 34

## Structure du projet
```
app/src/main/java/com/vikingproduction/equipmentscanner/
|-- MainActivity.kt           # Navigation principale
|-- model/
|   |-- EquipmentData.kt      # Modeles de donnees
|-- screens/
|   |-- CaptureScreen.kt      # Page 1 - Capture
|   |-- ProcessingScreen.kt   # Page 2 - Traitement
|   |-- ResultsScreen.kt      # Page 3 - Resultats
|-- ocr/
|   |-- OcrProcessor.kt       # Logique OCR ML Kit
|-- ui/theme/
    |-- Theme.kt              # Theme Material 3
```

## Installation
1. Cloner le repo
2. Ouvrir dans Android Studio
3. Sync Gradle
4. Run sur device/emulateur

## Auteur
Viking Production - 2026
