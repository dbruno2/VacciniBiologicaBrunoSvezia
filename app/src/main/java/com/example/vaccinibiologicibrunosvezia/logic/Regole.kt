package com.example.vaccinibiologicibrunosvezia.logic

import com.example.vaccinibiologicibrunosvezia.model.StatusVaccino
import com.example.vaccinibiologicibrunosvezia.model.Vaccino

class Regole {

    private val regole = mutableListOf<Regola>()

    init {
        caricaRegoleBase()
    }

    private fun caricaRegoleBase() {

        // =====================================================
        // ETÀ (CDC)
        // =====================================================

        regole.add(
            Regola(
                vaccino = Vaccino("Influenza"),
                condizione = { it.eta >= 65 },
                stato = StatusVaccino.RACCOMANDATO,
                ragioni = "Raccomandato over 65 (CDC)"
            )
        )

        regole.add(
            Regola(
                vaccino = Vaccino("Pneumococco"),
                condizione = { it.eta >= 65 },
                stato = StatusVaccino.RACCOMANDATO,
                ragioni = "Prevenzione infezioni invasive negli anziani"
            )
        )

        regole.add(
            Regola(
                vaccino = Vaccino("HPV"),
                condizione = { it.eta in 11..26 },
                stato = StatusVaccino.RACCOMANDATO,
                ragioni = "Prevenzione infezione HPV (linee guida nazionali)"
            )
        )

        // =====================================================
        // TERAPIA BIOLOGICA (EULAR)
        // =====================================================

        regole.add(
            Regola(
                vaccino = Vaccino("MMR"),
                condizione = {
                    it.terapiaBiologica.contains("anti-TNF") ||
                            it.terapiaBiologica.contains("biologic") ||
                            it.condizioni.contains("immunosuppression")
                },
                stato = StatusVaccino.CONTROINDICATO,
                ragioni = "EULAR: vaccini vivi controindicati in immunosoppressione"
            )
        )

        regole.add(
            Regola(
                vaccino = Vaccino("Varicella"),
                condizione = { it.condizioni.contains("immunosuppression") },
                stato = StatusVaccino.CONTROINDICATO,
                ragioni = "Rischio infezione da vaccino vivo"
            )
        )

        regole.add(
            Regola(
                vaccino = Vaccino("Yellow Fever"),
                condizione = { it.condizioni.contains("immunosuppression") },
                stato = StatusVaccino.CONTROINDICATO,
                ragioni = "Vaccino vivo non sicuro in immunosoppressione"
            )
        )

        // =====================================================
        // COMORBIDITÀ
        // =====================================================

        regole.add(
            Regola(
                vaccino = Vaccino("Influenza"),
                condizione = { it.condizioni.contains("diabetes") },
                stato = StatusVaccino.RACCOMANDATO,
                ragioni = "Rischio aumentato complicanze infettive nel diabete"
            )
        )

        regole.add(
            Regola(
                vaccino = Vaccino("Epatite B"),
                condizione = {
                    it.condizioni.contains("chronic liver disease") ||
                            it.condizioni.contains("diabetes")
                },
                stato = StatusVaccino.RACCOMANDATO,
                ragioni = "Indicazione in patologie metaboliche o epatiche"
            )
        )

        // =====================================================
        // CONDIZIONI SPECIALI
        // =====================================================

        regole.add(
            Regola(
                vaccino = Vaccino("Meningococco"),
                condizione = { it.condizioni.contains("asplenia") },
                stato = StatusVaccino.RACCOMANDATO,
                ragioni = "Alto rischio infezioni invasive in asplenia"
            )
        )

        regole.add(
            Regola(
                vaccino = Vaccino("COVID-19"),
                condizione = {
                    it.eta >= 18 &&
                            (it.condizioni.isNotEmpty() || it.terapiaBiologica.isNotEmpty())
                },
                stato = StatusVaccino.RACCOMANDATO,
                ragioni = "Raccomandazione generale in pazienti a rischio"
            )
        )

        // =====================================================
        // CASI NEUTRI
        // =====================================================

        regole.add(
            Regola(
                vaccino = Vaccino("Influenza"),
                condizione = { it.eta in 18..64 },
                stato = StatusVaccino.CONSIGLIATO,
                ragioni = "Vaccinazione consigliata nella popolazione adulta"
            )
        )
    }

    // =====================================================
    // ESTENSIBILITÀ
    // =====================================================

    fun aggiungiRegola(regola: Regola) {
        regole.add(regola)
    }

    fun ottieniRegole(): List<Regola> {
        return regole
    }
}