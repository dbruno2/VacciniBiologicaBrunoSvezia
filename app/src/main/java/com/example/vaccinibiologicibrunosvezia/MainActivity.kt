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
import kotlinx.coroutines.MainScope

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Manual DI since VaccineApplication was rolled back
        val database = AppDatabase.getDatabase(applicationContext, MainScope())
        val repository = VaccineRepository(database.vaccineDao(), database.ruleDao())
        
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return VaccineViewModel(repository) as T
            }
        }

        setContent {
            VacciniBiologiciBrunoSveziaTheme {
                val navController = rememberNavController()
                val vaccineViewModel: VaccineViewModel = viewModel(factory = viewModelFactory)
                
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
                }
            }
        }
    }
}
