package com.example.vaccinibiologicibrunosvezia.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaccinibiologicibrunosvezia.data.local.entity.VaccineEntity
import com.example.vaccinibiologicibrunosvezia.model.*
import kotlinx.coroutines.launch

/**
 * VaccineUiState: Classe che contiene tutti i dati necessari alla UI.
 */
data class VaccineUiState(
    val recommendations: List<Recommendation> = emptyList(),
    val allVaccines: List<VaccineEntity> = emptyList(),
    val selectedVaccineIds: Set<Int> = emptySet(),
    val selectedConditions: Set<String> = emptySet(),
)

/**
 * VaccineViewModel: Gestisce lo stato e la logica dell'applicazione.
 * Utilizza "mutableStateOf" di Compose per una gestione semplice e diretta dello stato.
 */
class VaccineViewModel(
    private val repository: VaccineRepository
) : ViewModel() {

    // Stato della UI esposto direttamente come variabile osservabile da Compose
    var uiState by mutableStateOf(VaccineUiState())
        private set

    init {
        loadVaccines()
    }

    // Carica i vaccini dal database
    private fun loadVaccines() {
        viewModelScope.launch {
            // Raccogliamo i dati dal database e aggiorniamo lo stato
            repository.getVaccines().collect { vaccines ->
                uiState = uiState.copy(allVaccines = vaccines)
            }
        }
    }

    // Funzione per selezionare/deselezionare un vaccino fatto
    fun toggleVaccineSelection(vaccineId: Int) {
        val currentIds = uiState.selectedVaccineIds

        val updatedIds = if (vaccineId in currentIds) {
            currentIds - vaccineId
        } else {
            currentIds + vaccineId
        }

        uiState = uiState.copy(selectedVaccineIds = updatedIds)
    }

    // Funzione per selezionare/deselezionare una condizione clinica
    fun toggleConditionSelection(condition: String) {
        val currentConditions = uiState.selectedConditions

        val updatedConditions = if (condition in currentConditions) {
            currentConditions - condition
        } else {
            currentConditions + condition
        }

        uiState = uiState.copy(selectedConditions = updatedConditions)
    }

    fun calculateRecommendations(input: PatientInput) {
        viewModelScope.launch {

            val rules = repository.getRules()
            val vaccines = uiState.allVaccines
            val results = mutableListOf<Recommendation>()

            for (rule in rules) {
                // therapyOk verifica se la regola si applica al paziente attuale
                val therapyOk = rule.therapy == null || rule.therapy == input.terapiaBiologica
                val ageOk = (rule.minAge == null || input.eta >= rule.minAge) &&
                            (rule.maxAge == null || input.eta <= rule.maxAge)
                val conditionsOk = rule.requiredConditions.isEmpty() ||
                                   rule.requiredConditions.any { it in input.condizioni }

                if (therapyOk && ageOk && conditionsOk) {
                    val vaccine = vaccines.find { it.id == rule.vaccineId }
                    
                    if (vaccine != null && vaccine.id !in input.vacciniEffettuati) {

                        // Controlliamo le controindicazioni
                        val isContraindicated = vaccine.isLive && (
                            input.terapiaBiologica == "anti-TNF" ||
                            input.terapiaBiologica == "immunosoppressori"
                        )

                        val finalType = if (isContraindicated) RecommendationType.CONTROINDICATO else rule.result
                        results.add(Recommendation(vaccine, finalType))
                    }
                }
            }

            // Aggiorniamo
            uiState = uiState.copy(
                recommendations = results.distinctBy { it.vaccine.id },
            )
        }
    }
}
