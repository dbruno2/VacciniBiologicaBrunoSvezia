package com.example.vaccinibiologicibrunosvezia

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import com.example.vaccinibiologicibrunosvezia.data.repository.VaccineViewModel
import com.example.vaccinibiologicibrunosvezia.model.RecommendationType
import com.example.vaccinibiologicibrunosvezia.ui.theme.Verdino

@Composable
fun SchermataVaccini(viewModel: VaccineViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current

    Box(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(40.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(35.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.recommended_vaccines),
                fontSize = 30.sp,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (uiState.loading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.recommendations) { recommendation ->
                        // Get translated vaccine name
                        val translatedName = when (recommendation.vaccine.name) {
                            "Antinfluenzale" -> stringResource(R.string.vaccine_antinfluenzale)
                            "Pneumococcico" -> stringResource(R.string.vaccine_pneumococcico)
                            "HPV" -> stringResource(R.string.vaccine_hpv)
                            "MPR" -> stringResource(R.string.vaccine_mpr)
                            "Varicella" -> stringResource(R.string.vaccine_varicella)
                            else -> recommendation.vaccine.name
                        }

                        // Get translated type
                        val translatedType = when (recommendation.type) {
                            RecommendationType.RACCOMANDATO -> stringResource(R.string.type_raccomandato)
                            RecommendationType.POSSIBILE -> stringResource(R.string.type_possibile)
                            RecommendationType.CONTROINDICATO -> stringResource(R.string.type_controindicato)
                        }

                        CardRecommendation(translatedName, translatedType)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottone per tornare all'inizio
            Button(
                onClick = {
                    navController.navigate("principale") {
                        popUpTo("principale") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Verdino)
            ) {
                Text(text = stringResource(R.string.back_button))
            }
        }

        // Bottone lingua in alto a destra
        TextButton(
            onClick = {
                val currentLocale = configuration.locales[0].language
                val newLocale = if (currentLocale.startsWith("en")) "it" else "en"
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(newLocale)
                AppCompatDelegate.setApplicationLocales(appLocale)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 65.dp, end = 16.dp)
        ) {
            Text("ITA/ENG", fontSize = 16.sp)
        }
    }
}

@Composable
fun CardRecommendation(name: String, type: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = name, fontSize = 20.sp, style = MaterialTheme.typography.titleLarge)
            Text(
                text = stringResource(R.string.status_label, type),
                fontSize = 16.sp,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
