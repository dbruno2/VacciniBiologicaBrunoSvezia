package com.example.vaccinibiologicibrunosvezia.ui.theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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

@Composable
fun SchermataCondizioni(navController: NavController, viewModel: VaccineViewModel) {
    val state = viewModel.uiState
    val configuration = LocalConfiguration.current

    // recupera le condizioni dalle risorse
    val condizioni = stringArrayResource(R.array.condizioni)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.condizione), fontSize = 30.sp)

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(condizioni.toList()) { condizione ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = state.selectedConditions.contains(condizione),
                            onCheckedChange = { viewModel.toggleConditionSelection(condizione) }
                        )
                        Text(text = condizione, fontSize = 18.sp)
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
