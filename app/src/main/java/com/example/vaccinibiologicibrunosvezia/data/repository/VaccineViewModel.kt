package com.example.vaccinibiologicibrunosvezia.data.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaccinibiologicibrunosvezia.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class VaccineUiState(
    val recommendations: List<Recommendation> = emptyList(),
    val loading: Boolean = false
)

class VaccineViewModel(
    private val repository: VaccineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VaccineUiState())
    val uiState: StateFlow<VaccineUiState> = _uiState.asStateFlow()

    fun calculateRecommendations(input: PatientInput) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)

            val rules = repository.getRules()
            val vaccines = repository.getVaccines()

            val result = mutableListOf<Recommendation>()

            for (rule in rules) {
                val therapyOk = rule.therapy == null || rule.therapy == input.terapiaBiologica
                val ageOk = (rule.minAge == null || input.eta >= rule.minAge) &&
                            (rule.maxAge == null || input.eta <= rule.maxAge)
                val conditionsOk = rule.requiredConditions.isEmpty() ||
                                   rule.requiredConditions.all { it in input.condizioni }

                if (therapyOk && ageOk && conditionsOk) {
                    val vaccine = vaccines.find { it.id == rule.vaccineId }
                    if (vaccine != null) {
                        val alreadyDone = vaccine.id in input.vacciniEffettuati
                        val type = if (alreadyDone) RecommendationType.POSSIBILE else rule.result
                        result.add(Recommendation(vaccine = vaccine, type = type))
                    }
                }
            }

            _uiState.value = VaccineUiState(recommendations = result, loading = false)
        }
    }
}
