package ba.etf.rma22.projekat.data.models

data class AnketaInfo(
    var anketa: Anketa?,
    var predana : Boolean,
    var zaustavljena : Boolean,
    var listaOdgovora : MutableList<Pair<String, Int>>
)