package com.example.vaccinibiologicibrunosvezia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vaccinibiologicibrunosvezia.model.PatientInput
import com.example.vaccinibiologicibrunosvezia.model.Vaccine
import com.example.vaccinibiologicibrunosvezia.ui.theme.VacciniBiologiciBrunoSveziaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VacciniBiologiciBrunoSveziaTheme {
                SchermataPrincipale()
/*
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "Terapia biologica")
                    Text(text = "Età")
                    Text(text = "Condizioni cliniche")
                }
*/
                }
            }
        }
    }



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchermataPrincipale() {

    var expanded by remember { mutableStateOf(false) }

    val terapie = listOf(
        "anti-TNF",
        "anti-IL17",
        "anti-IL23",
        "altri immunosoppressori"
    )

    var terapiaSelezionata by remember { mutableStateOf("") }
    var etaText by remember { mutableStateOf("") }
    var condizioniText by remember { mutableStateOf("") }
   // var vacciniText by remember { mutableStateOf("") }

    var risultato by remember { mutableStateOf("") }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "VACCINI", fontSize = 40.sp)
        Spacer(modifier = Modifier.height(20.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            OutlinedTextField(
                value = terapiaSelezionata,
                onValueChange = {},
                readOnly = true,
                label = { Text("Terapia biologica") },
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
            label = { Text("Età") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = condizioniText ,
            onValueChange = { condizioniText  = it },
            label = { Text("Condizione clinica") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {

            val paziente = PatientInput(
                terapiaBiologica = terapiaSelezionata,
                eta = etaText.toIntOrNull() ?: 0,
                condizioni = condizioniText.split(",").map { it.trim() },
                storiaVaccinale = condizioniText.split(",").map { Vaccine(it.trim()) } //da modificare appena capiamo cosa fare con storia vaccinale
            )

            risultato =
                "Paziente:\n" +
                        "Terapia: ${paziente.terapiaBiologica}\n" +
                        "Età: ${paziente.eta}\n" +
                        "Condizioni: ${paziente.condizioni}\n" +
                        "Vaccini: ${paziente.storiaVaccinale}"
        }) {
            Text("Calcola")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = risultato)
    }
    Button(onClick = {

    }) { }
}

@Composable
fun SchermataCalcola(){

}

//prova non funziona niente Push da Sebastiano Svezia aka PROVAFINALE aka VERAPROVAFINALE