package com.example.vaccinibiologicibrunosvezia.data.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaccinibiologicibrunosvezia.data.local.entity.VaccineEntity
import com.example.vaccinibiologicibrunosvezia.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class VaccineUiState(
    val recommendations: List<Recommendation> = emptyList(),
    val allVaccines: List<VaccineEntity> = emptyList(),
    val selectedVaccineIds: Set<Int> = emptySet(),
    val selectedConditions: Set<String> = emptySet(),
    val loading: Boolean = false
)

class VaccineViewModel(
    private val repository: VaccineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VaccineUiState())
    val uiState: StateFlow<VaccineUiState> = _uiState.asStateFlow()

    init {
        loadVaccines()
    }

    private fun loadVaccines() {
        viewModelScope.launch {
            repository.getVaccines().collect { vaccines ->
                _uiState.value = _uiState.value.copy(allVaccines = vaccines)
            }
        }
    }

    fun toggleVaccineSelection(vaccineId: Int) {
        val currentSelected = _uiState.value.selectedVaccineIds
        val newSelected = if (currentSelected.contains(vaccineId)) {
            currentSelected - vaccineId
        } else {
            currentSelected + vaccineId
        }
        _uiState.value = _uiState.value.copy(selectedVaccineIds = newSelected)
    }

    fun toggleConditionSelection(condition: String) {
        val currentSelected = _uiState.value.selectedConditions
        val newSelected = if (currentSelected.contains(condition)) {
            currentSelected - condition
        } else {
            currentSelected + condition
        }
        _uiState.value = _uiState.value.copy(selectedConditions = newSelected)
    }

    fun calculateRecommendations(input: PatientInput) {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(loading = true)

            val rules = repository.getRules()
            val vaccines = _uiState.value.allVaccines

            val result = mutableListOf<Recommendation>()

            for (rule in rules) {

                val therapyOk =
                    rule.therapy == null ||
                            rule.therapy == input.terapiaBiologica

                val ageOk =
                    (rule.minAge == null || input.eta >= rule.minAge) &&
                            (rule.maxAge == null || input.eta <= rule.maxAge)

                val conditionsOk =
                    rule.requiredConditions.isEmpty() ||
                            rule.requiredConditions.any { it in input.condizioni }

                if (therapyOk && ageOk && conditionsOk) {

                    val vaccine =
                        vaccines.find { it.id == rule.vaccineId }

                    if (vaccine != null) {

                        val liveVaccineContraindicated =
                            vaccine.isLive &&
                                    (
                                            input.terapiaBiologica == "anti-TNF" ||
                                                    input.terapiaBiologica == "immunosoppressori"
                                            )

                        val alreadyDone =
                            vaccine.id in input.vacciniEffettuati

                        if (!alreadyDone) {

                            val type =
                                if (liveVaccineContraindicated) {
                                    RecommendationType.CONTROINDICATO
                                } else {
                                    rule.result
                                }

                            result.add(
                                Recommendation(
                                    vaccine = vaccine,
                                    type = type
                                )
                            )
                        }
                    }
                }
            }

            // 🔥 DEVE STARE QUI DENTRO
            _uiState.value = _uiState.value.copy(
                recommendations = result,
                loading = false
            )
        }
    }
}