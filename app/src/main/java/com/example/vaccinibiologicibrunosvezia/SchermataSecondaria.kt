package com.example.vaccinibiologicibrunosvezia

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

import androidx.compose.ui.platform.LocalContext

/**
 * SchermataVaccini: Mostra i risultati finali del calcolo vaccinale.
 */
@Composable
fun SchermataVaccini(viewModel: VaccineViewModel, navController: NavController) {
    val state = viewModel.uiState
    val configuration = LocalConfiguration.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(35.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.recommended_vaccines), fontSize = 30.sp)

            Spacer(modifier = Modifier.height(40.dp))


                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.recommendations) { rec ->
                        // Traduzione dinamica basata sul nome del database
                        val nomeTradotto = getTraduzioneVaccino(rec.vaccine.name)
                        val statoTradotto = when (rec.type) {
                            RecommendationType.RACCOMANDATO -> stringResource(R.string.type_raccomandato)
                            RecommendationType.POSSIBILE -> stringResource(R.string.type_possibile)
                            RecommendationType.CONTROINDICATO -> stringResource(R.string.type_controindicato)
                        }

                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = nomeTradotto, fontSize = 20.sp, style = MaterialTheme.typography.titleLarge)
                                Text(text = stringResource(R.string.status_label, statoTradotto), fontSize = 16.sp)
                            }
                        }
                    }
                }


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Verdino)
            ) {
                Text(text = stringResource(R.string.back_button))
            }
        }

        // Tasto lingua
        TextButton(
            onClick = {
                val next = if (configuration.locales[0].language.startsWith("en")) "it" else "en"
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(next))
            },
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 65.dp, end = 16.dp)
        ) {
            Text("ITA/ENG", fontSize = 16.sp)
        }
    }
}

/**
 * Risoluzione dinamica della traduzione basata sul nome della risorsa (es. "vaccine_antinfluenzale").
 * Se non trovata, ritorna il nome originale.
 */
@Composable
fun getTraduzioneVaccino(nomeDB: String): String {
    val context = LocalContext.current
    val resId = context.resources.getIdentifier(nomeDB, "string", context.packageName)
    return if (resId != 0) stringResource(resId) else nomeDB
}
