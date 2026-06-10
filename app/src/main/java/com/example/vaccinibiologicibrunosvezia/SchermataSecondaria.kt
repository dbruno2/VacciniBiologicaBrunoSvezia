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

            if (state.loading) {
                CircularProgressIndicator()
            } else {
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
 * Funzione helper per mappare i nomi dal DB alle stringhe localizzate.
 * Duplicata qui per semplicità espositiva.
 */
@Composable
private fun getTraduzioneVaccino(nomeDB: String): String {
    return when (nomeDB) {
        "Antinfluenzale" -> stringResource(R.string.vaccine_antinfluenzale)
        "Pneumococcico" -> stringResource(R.string.vaccine_pneumococcico)
        "HPV" -> stringResource(R.string.vaccine_hpv)
        "Epatite A" -> stringResource(R.string.vaccine_epatite_a)
        "Epatite B" -> stringResource(R.string.vaccine_epatite_b)
        "Herpes Zoster" -> stringResource(R.string.vaccine_herpes_zoster)
        "COVID-19" -> stringResource(R.string.vaccine_covid_19)
        "Tetano" -> stringResource(R.string.vaccine_tetano)
        "Difterite" -> stringResource(R.string.vaccine_difterite)
        "Pertosse" -> stringResource(R.string.vaccine_pertosse)
        "Meningococco" -> stringResource(R.string.vaccine_meningococco)
        "Hib" -> stringResource(R.string.vaccine_hib)
        "Rabbia" -> stringResource(R.string.vaccine_rabbia)
        "Tifo" -> stringResource(R.string.vaccine_tifo)
        "Colera" -> stringResource(R.string.vaccine_colera)
        "Encefalite da zecca" -> stringResource(R.string.vaccine_tbe)
        "Poliomielite" -> stringResource(R.string.vaccine_polio)
        "MPR" -> stringResource(R.string.vaccine_mpr)
        "Varicella" -> stringResource(R.string.vaccine_varicella)
        "Rotavirus" -> stringResource(R.string.vaccine_rotavirus)
        "Febbre Gialla" -> stringResource(R.string.vaccine_febbre_gialla)
        else -> nomeDB
    }
}
