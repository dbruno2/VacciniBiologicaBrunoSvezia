package com.example.vaccinibiologicibrunosvezia

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vaccinibiologicibrunosvezia.ui.theme.VacciniBiologiciBrunoSveziaTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VacciniBiologiciBrunoSveziaTheme {
                val navController = rememberNavController()
                
                NavHost(navController = navController, startDestination = "principale") {
                    composable("principale") {
                        SchermataPrincipale(navController)
                    }
                    composable("secondaria") {
                        SchermataVaccini()
                    }
                }
            }
        }
    }
}
