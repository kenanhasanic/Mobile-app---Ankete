package ba.etf.rma22.projekat.data.models

import java.util.*

data class Korisnik(
    var upisaneAnkete: MutableList<Anketa> = mutableListOf(),
    var upisanaIstrazivanja: MutableList<Istrazivanje> = mutableListOf(),
    var upisaneGrupe: MutableList<Grupa> = mutableListOf()
)