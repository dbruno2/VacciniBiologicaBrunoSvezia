package com.example.vaccinibiologicibrunosvezia

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import com.example.vaccinibiologicibrunosvezia.model.PatientInput
import com.example.vaccinibiologicibrunosvezia.model.Vaccine
import com.example.vaccinibiologicibrunosvezia.ui.theme.VacciniBiologiciBrunoSveziaTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VacciniBiologiciBrunoSveziaTheme {
                SchermataPrincipale()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchermataPrincipale() {
    var expanded by remember { mutableStateOf(false) }

    val terapie = stringArrayResource(R.array.terapie)
    val context = LocalContext.current
    val activity = context as? Activity
    
    var terapiaSelezionata by rememberSaveable { mutableStateOf("") }
    var etaText by rememberSaveable { mutableStateOf("") }
    var condizioniText by rememberSaveable { mutableStateOf("") }
    var risultato by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp),
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
                modifier = Modifier.menuAnchor()
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

        Button(onClick = {
            val paziente = PatientInput(
                terapiaBiologica = terapiaSelezionata,
                eta = etaText.toIntOrNull() ?: 0,
                condizioni = condizioniText.split(",").map { it.trim() },
                storiaVaccinale = condizioniText.split(",").map { Vaccine(it.trim()) } 
            )
            // TODO: Implement logic for risultato
        }) {
            Text(stringResource(R.string.calcola))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = risultato)

        Spacer(modifier = Modifier.height(110.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Button(onClick = {
                    changeLanguage(activity, "it")
                }) {
                    Text(stringResource(R.string.ita))
                }

                Button(onClick = {
                    changeLanguage(activity, "en")
                }) {
                    Text(stringResource(R.string.eng))
                }
            }
        }
    }
}

fun changeLanguage(activity: Activity?, language: String) {
    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
    AppCompatDelegate.setApplicationLocales(appLocale)
    activity?.recreate()
}
