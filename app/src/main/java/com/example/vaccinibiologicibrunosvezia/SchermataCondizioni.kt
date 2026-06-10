package com.example.vaccinibiologicibrunosvezia

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import com.example.vaccinibiologicibrunosvezia.data.repository.VaccineViewModel
import com.example.vaccinibiologicibrunosvezia.ui.theme.Verdino

/**
 * SchermataCondizioni: permette all'utente di selezionare le proprie patologie.
 */
@Composable
fun SchermataCondizioni(navController: NavController, viewModel: VaccineViewModel) {
    val state = viewModel.uiState
    val configuration = LocalConfiguration.current

    val nomiCondizioni = stringArrayResource(R.array.condizioni)
    val chiaviDatabase = listOf("DIABETE", "BPCO", "CARDIOPATIA", "IMMUNODEPRESSIONE", "MALATTIA_RENALE", "OBESITA", "ASMA", "EPATOPATIA")

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.condizione), fontSize = 30.sp)

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(nomiCondizioni.toList()) { index, nome ->
                    val chiave = chiaviDatabase.getOrElse(index) { "" }
                    if (chiave.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = state.selectedConditions.contains(chiave),
                                onCheckedChange = { viewModel.toggleConditionSelection(chiave) }
                            )
                            Text(text = nome, fontSize = 18.sp)
                        }
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
