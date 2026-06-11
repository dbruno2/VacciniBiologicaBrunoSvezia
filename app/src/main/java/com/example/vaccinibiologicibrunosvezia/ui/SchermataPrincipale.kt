package com.example.vaccinibiologicibrunosvezia.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import com.example.vaccinibiologicibrunosvezia.R
import com.example.vaccinibiologicibrunosvezia.data.repository.VaccineViewModel
import com.example.vaccinibiologicibrunosvezia.model.PatientInput


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchermataPrincipale(navController: NavController, viewModel: VaccineViewModel) {
    val state = viewModel.uiState

    var expanded by remember { mutableStateOf(false) }

    // recupera le terapie dalle risorse
    val terapie = stringArrayResource(R.array.terapie)

    val configuration = LocalConfiguration.current

    var terapiaSelezionata by rememberSaveable { mutableStateOf("") }
    var etaText by rememberSaveable { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(stringResource(R.string.title), fontSize = 40.sp)
            Spacer(modifier = Modifier.height(20.dp))

            // Menu a tendina per terapia biologica
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = terapiaSelezionata,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.terapia_biologica)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, true).fillMaxWidth()
                )

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    terapie.forEach { terapia ->
                        DropdownMenuItem(
                            text = { Text(terapia) },
                            onClick = {
                                terapiaSelezionata = terapia
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))


            OutlinedTextField(
                value = etaText,
                onValueChange = { etaText = it },
                label = { Text(stringResource(R.string.age)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))


            Box(modifier = Modifier.fillMaxWidth().clickable { navController.navigate("schermata_condizioni") }) {
                OutlinedTextField(
                    value = if (state.selectedConditions.isNotEmpty()) "${state.selectedConditions.size} selezionate" else "",
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text(stringResource(R.string.condizione)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))


            Box(modifier = Modifier.fillMaxWidth().clickable { navController.navigate("vaccini_prec") }) {
                OutlinedTextField(
                    value = if (state.selectedVaccineIds.isNotEmpty()) "${state.selectedVaccineIds.size} selezionati" else "",
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text(stringResource(R.string.select_vaccines_button)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))


            Button(
                onClick = {
                    val input = PatientInput(
                        terapiaBiologica = terapiaSelezionata,
                        eta = etaText.toIntOrNull() ?: 0,
                        condizioni = state.selectedConditions.toList(),
                        vacciniEffettuati = state.selectedVaccineIds.toList()
                    )
                    viewModel.calculateRecommendations(input)
                    navController.navigate("secondaria")
                },
                colors = ButtonDefaults.buttonColors(containerColor = _root_ide_package_.com.example.vaccinibiologicibrunosvezia.ui.theme.Verdino),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.calcola))
            }
        }


        TextButton(
            onClick = {
                val current = configuration.locales[0].language
                val next = if (current.startsWith("en")) "it" else "en"
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(next))
            },
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 48.dp, end = 16.dp)
        ) {
            Text("ITA/ENG", fontSize = 16.sp)
        }
    }
}
