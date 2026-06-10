package com.example.vaccinibiologicibrunosvezia

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vaccinibiologicibrunosvezia.data.local.database.AppDatabase
import com.example.vaccinibiologicibrunosvezia.data.repository.VaccineRepository
import com.example.vaccinibiologicibrunosvezia.data.repository.VaccineViewModel
import com.example.vaccinibiologicibrunosvezia.ui.theme.VacciniBiologiciBrunoSveziaTheme

/**
 * MainActivity: Punto di ingresso dell'app.
 * Qui inizializziamo il database e configuriamo il sistema di navigazione.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Inizializzazione del Database e del Repository
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = VaccineRepository(database.vaccineDao(), database.ruleDao())
        
        // Factory necessaria per passare il Repository al ViewModel
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return VaccineViewModel(repository) as T
            }
        }

        setContent {
            VacciniBiologiciBrunoSveziaTheme {
                val navController = rememberNavController()
                
                // Otteniamo l'istanza del ViewModel usando la factory definita sopra
                val vaccineViewModel: VaccineViewModel = viewModel(factory = factory)
                
                // Configurazione delle rotte dell'applicazione
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
