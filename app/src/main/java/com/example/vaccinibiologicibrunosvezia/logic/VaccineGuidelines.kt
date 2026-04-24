package com.example.vaccinibiologicibrunosvezia.logic

import com.example.vaccinibiologicibrunosvezia.model.VaccineStatus
import com.example.vaccinibiologicibrunosvezia.model.Vaccine
import com.example.vaccinibiologicibrunosvezia.R

class VaccineGuidelines {

    private val Vaccini = mutableListOf<VaccineCheck>()

    init {
        riempiListaVaccini()
    }


    private fun riempiListaVaccini() {

        Vaccini.add(
            VaccineCheck(
                vaccino = Vaccine("Influenza"),
                condizione = { it.eta >= 65 },
                stato = VaccineStatus.RACCOMANDATO,
                reasonResId = R.string.reason_age_over_65
            )
        )

        Vaccini.add(
            VaccineCheck(
                vaccino = Vaccine("Pneumococco"),
                condizione = { it.eta >= 65 },
                stato = VaccineStatus.RACCOMANDATO,
                reasonResId = R.string.reason_pneumococco_anziani
            )
        )

        Vaccini.add(
            VaccineCheck(
                vaccino = Vaccine("HPV"),
                condizione = { it.eta in 11..26 },
                stato = VaccineStatus.RACCOMANDATO,
                reasonResId = R.string.reason_hpv
            )
        )

        // =====================
        // TERAPIA BIOLOGICA (EULAR)
        // =====================

        Vaccini.add(
            VaccineCheck(
                vaccino = Vaccine("MMR"),
                condizione = {
                    it.terapiaBiologica.contains("anti-TNF") ||
                            it.terapiaBiologica.contains("biologic") ||
                            it.condizioni.contains("immunosuppression")
                },
                stato = VaccineStatus.CONTROINDICATO,
                reasonResId = R.string.reason_immunosuppression
            )
        )

        Vaccini.add(
            VaccineCheck(
                vaccino = Vaccine("Varicella"),
                condizione = { it.condizioni.contains("immunosuppression") },
                stato = VaccineStatus.CONTROINDICATO,
                reasonResId = R.string.reason_immunosuppression
            )
        )

        Vaccini.add(
            VaccineCheck(
                vaccino = Vaccine("Yellow Fever"),
                condizione = { it.condizioni.contains("immunosuppression") },
                stato = VaccineStatus.CONTROINDICATO,
                reasonResId = R.string.reason_immunosuppression
            )
        )

        // =====================
        // COMORBIDITÀ
        // =====================

        Vaccini.add(
            VaccineCheck(
                vaccino = Vaccine("Influenza"),
                condizione = { it.condizioni.contains("diabetes") },
                stato = VaccineStatus.RACCOMANDATO,
                reasonResId = R.string.reason_diabetes
            )
        )

        Vaccini.add(
            VaccineCheck(
                vaccino = Vaccine("Epatite B"),
                condizione = {
                    it.condizioni.contains("chronic liver disease") ||
                            it.condizioni.contains("diabetes")
                },
                stato = VaccineStatus.RACCOMANDATO,
                reasonResId = R.string.reason_liver_diabetes
            )
        )

        // =====================
        // CONDIZIONI SPECIALI
        // =====================

        Vaccini.add(
            VaccineCheck(
                vaccino = Vaccine("Meningococco"),
                condizione = { it.condizioni.contains("asplenia") },
                stato = VaccineStatus.RACCOMANDATO,
                reasonResId = R.string.reason_asplenia
            )
        )

        Vaccini.add(
            VaccineCheck(
                vaccino = Vaccine("COVID-19"),
                condizione = {
                    it.eta >= 18 &&
                            (it.condizioni.isNotEmpty() || it.terapiaBiologica.isNotEmpty())
                },
                stato = VaccineStatus.RACCOMANDATO,
                reasonResId = R.string.reason_covid_risk
            )
        )

        // =====================
        // CASO NEUTRO
        // =====================

        Vaccini.add(
            VaccineCheck(
                vaccino = Vaccine("Influenza"),
                condizione = { it.eta in 18..64 },
                stato = VaccineStatus.CONSIGLIATO,
                reasonResId = R.string.reason_adult_flu
            )
        )
    }

    // =====================================================
    // ESTENSIBILITÀ
    // =====================================================

    fun aggiungiVaccino(vaccineCheck: VaccineCheck) {
        Vaccini.add(vaccineCheck)
    }

    fun ottieniVaccini(): List<VaccineCheck> {
        return Vaccini
    }
}