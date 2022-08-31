package ba.etf.rma22.projekat.viewmodel

import ba.etf.rma22.projekat.data.models.Odgovor
import ba.etf.rma22.projekat.data.repositories.OdgovorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

class OdgovorViewModel {

    fun getOdgovoriAnketa(
        idKviza:Int, onSuccess: KFunction1<List<Odgovor>, Unit>,
        onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = OdgovorRepository.getOdgovoriAnketa(idKviza)
            when (result) {
                is List<Odgovor>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun postaviOdgovorAnketa(idKvizTaken: Int, idPitanje: Int, odgovor: Int, onSuccess: KFunction1<Int?, Unit>,
                           onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = OdgovorRepository.postaviOdgovorAnketa(idKvizTaken, idPitanje, odgovor)
            when (result) {
                is Int? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }
}