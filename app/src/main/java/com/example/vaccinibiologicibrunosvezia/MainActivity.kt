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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.vaccinibiologicibrunosvezia.data.local.database.AppDatabase
import com.example.vaccinibiologicibrunosvezia.data.local.database.DatabaseSeeder
import com.example.vaccinibiologicibrunosvezia.data.repository.VaccineRepository
import com.example.vaccinibiologicibrunosvezia.data.repository.VaccineViewModel
import com.example.vaccinibiologicibrunosvezia.ui.theme.VacciniBiologiciBrunoSveziaTheme
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
