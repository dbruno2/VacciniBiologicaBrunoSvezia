package com.example.vaccinibiologicibrunosvezia

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import com.example.vaccinibiologicibrunosvezia.data.repository.VaccineViewModel
import com.example.vaccinibiologicibrunosvezia.ui.theme.Verdino

/**
 * SchermataVacciniPrec: permette di selezionare i vaccini già effettuati.
 */
@Composable
fun SchermataVacciniPrec(navController: NavController, viewModel: VaccineViewModel) {
    val state = viewModel.uiState
    val configuration = LocalConfiguration.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.previous_vaccines_title), fontSize = 30.sp)

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.allVaccines) { vaccine ->
                    val nomeTradotto = getTraduzioneVaccino(vaccine.name)

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = state.selectedVaccineIds.contains(vaccine.id),
                            onCheckedChange = { viewModel.toggleVaccineSelection(vaccine.id) }
                        )
                        Text(text = nomeTradotto, fontSize = 18.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Verdino)
            ) {
                Text(text = stringResource(R.string.confirm_button))
            }
        }

        // Tasto lingua
        TextButton(
            onClick = {
                val next = if (configuration.locales[0].language.startsWith("en")) "it" else "en"
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(next))
            },
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 48.dp, end = 16.dp)
        ) {
            Text("ITA/ENG", fontSize = 16.sp)
        }
    }
}

/**
 * Helper per le traduzioni dei nomi dei vaccini.
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
