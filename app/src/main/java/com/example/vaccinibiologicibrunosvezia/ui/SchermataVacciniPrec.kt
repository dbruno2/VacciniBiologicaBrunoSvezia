package com.example.vaccinibiologicibrunosvezia.ui

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
import com.example.vaccinibiologicibrunosvezia.R
import com.example.vaccinibiologicibrunosvezia.data.repository.VaccineViewModel

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
                colors = ButtonDefaults.buttonColors(containerColor = _root_ide_package_.com.example.vaccinibiologicibrunosvezia.ui.theme.Verdino)
            ) {
                Text(text = stringResource(R.string.confirm_button))
            }
        }


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
