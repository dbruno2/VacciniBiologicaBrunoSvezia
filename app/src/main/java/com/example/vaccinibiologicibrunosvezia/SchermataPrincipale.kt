package com.example.vaccinibiologicibrunosvezia

import androidx.appcompat.app.AppCompatDelegate
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
import com.example.vaccinibiologicibrunosvezia.model.PatientInput
import com.example.vaccinibiologicibrunosvezia.model.Vaccine
import com.example.vaccinibiologicibrunosvezia.ui.theme.Verdino

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchermataPrincipale(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    val terapie = stringArrayResource(R.array.terapie)
    val configuration = LocalConfiguration.current

    var terapiaSelezionata by rememberSaveable { mutableStateOf("") }
    var etaText by rememberSaveable { mutableStateOf("") }
    var condizioniText by rememberSaveable { mutableStateOf("") }
    var risultato by rememberSaveable { mutableStateOf("") }

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

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = terapiaSelezionata,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.condizione)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, true)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
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
                label = { Text(stringResource(R.string.age)) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = condizioniText ,
                onValueChange = { condizioniText  = it },
                label = { Text(stringResource(R.string.condizione)) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    val paziente = PatientInput(
                        terapiaBiologica = terapiaSelezionata,
                        eta = etaText.toIntOrNull() ?: 0,
                        condizioni = condizioniText.split(",").map { it.trim() },
                        storiaVaccinale = condizioniText.split(",").map { Vaccine(it.trim()) }
                    )

                    // Navigate to the second screen
                    navController.navigate("secondaria")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Verdino)
            ) {
                Text(stringResource(R.string.calcola))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = risultato)
        }

        TextButton(
            onClick = {
                val currentLocale = configuration.locales[0].language
                val newLocale = if (currentLocale.startsWith("en")) "it" else "en"
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(newLocale)
                AppCompatDelegate.setApplicationLocales(appLocale)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 16.dp)
        ) {
            Text("ITA/ENG", fontSize = 16.sp)
        }
    }
}
