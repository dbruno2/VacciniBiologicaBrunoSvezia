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
    val loading: Boolean = false
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
        val current = uiState.selectedVaccineIds
        val next = if (current.contains(vaccineId)) current - vaccineId else current + vaccineId
        uiState = uiState.copy(selectedVaccineIds = next)
    }

    // Funzione per selezionare/deselezionare una condizione clinica
    fun toggleConditionSelection(condition: String) {
        val current = uiState.selectedConditions
        val next = if (current.contains(condition)) current - condition else current + condition
        uiState = uiState.copy(selectedConditions = next)
    }

    /**
     * Calcola le raccomandazioni basandosi sui dati inseriti.
     * Questa è la logica centrale dell'applicazione.
     */
    fun calculateRecommendations(input: PatientInput) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)

            val rules = repository.getRules()
            val vaccines = uiState.allVaccines
            val results = mutableListOf<Recommendation>()

            for (rule in rules) {
                // Verifichiamo se la regola si applica al paziente attuale
                val therapyOk = rule.therapy == null || rule.therapy == input.terapiaBiologica
                val ageOk = (rule.minAge == null || input.eta >= rule.minAge) &&
                            (rule.maxAge == null || input.eta <= rule.maxAge)
                val conditionsOk = rule.requiredConditions.isEmpty() ||
                                   rule.requiredConditions.any { it in input.condizioni }

                if (therapyOk && ageOk && conditionsOk) {
                    val vaccine = vaccines.find { it.id == rule.vaccineId }
                    
                    if (vaccine != null && vaccine.id !in input.vacciniEffettuati) {
                        
                        // Controlliamo se ci sono controindicazioni (vaccini vivi + certe terapie)
                        val isContraindicated = vaccine.isLive && (
                            input.terapiaBiologica == "anti-TNF" || 
                            input.terapiaBiologica == "immunosoppressori"
                        )

                        val finalType = if (isContraindicated) RecommendationType.CONTROINDICATO else rule.result
                        results.add(Recommendation(vaccine, finalType))
                    }
                }
            }

            // Aggiorniamo lo stato con i risultati (rimuovendo duplicati per sicurezza)
            uiState = uiState.copy(
                recommendations = results.distinctBy { it.vaccine.id },
                loading = false
            )
        }
    }
}
