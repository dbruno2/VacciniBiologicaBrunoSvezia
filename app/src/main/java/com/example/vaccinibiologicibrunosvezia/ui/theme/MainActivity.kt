package com.example.vaccinibiologicibrunosvezia.ui.theme

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vaccinibiologicibrunosvezia.data.local.database.AppDatabase
import com.example.vaccinibiologicibrunosvezia.data.local.database.DatabaseSeeder
import com.example.vaccinibiologicibrunosvezia.data.repository.VaccineRepository
import com.example.vaccinibiologicibrunosvezia.data.repository.VaccineViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = AppDatabase.getDatabase(applicationContext)

        // Seeding database
        lifecycleScope.launch {
            DatabaseSeeder.seed(database)
        }

        val repository = VaccineRepository(database.vaccineDao(), database.ruleDao())
        
        // Factory
        val factory = viewModelFactory {
            initializer {
                VaccineViewModel(repository)
            }
        }

        setContent {
            VacciniBiologiciBrunoSveziaTheme {
                val navController = rememberNavController()
                
                val vaccineViewModel: VaccineViewModel = viewModel(factory = factory)
                
                // Navigazione tra pagine
                NavHost(navController = navController, startDestination = "principale") {
                    composable("principale") {
                        SchermataPrincipale(navController, vaccineViewModel)
                    }
                    composable("vaccini_prec") {
                        SchermataVacciniPrec(navController, vaccineViewModel)
                    }
                    composable("secondaria") {
                        SchermataVaccini(vaccineViewModel, navController)
                    }
                    composable("schermata_condizioni"){
                        SchermataCondizioni(navController, vaccineViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun getTraduzioneVaccino(nomeDB: String): String {
    val context = LocalContext.current
    val resId = context.resources.getIdentifier(nomeDB, "string", context.packageName)
    return if (resId != 0) stringResource(resId) else nomeDB
}
