package ba.etf.rma22.projekat.viewmodel

import ba.etf.rma22.projekat.data.models.Pitanje
import ba.etf.rma22.projekat.data.models.PitanjeNovo
import ba.etf.rma22.projekat.data.repositories.PitanjeAnketaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

class PitanjeAnketaViewModel {

    /*fun getPitanja(nazivAnkete: String, nazivIstrazivanja: String): List<Pitanje>{
        return PitanjeAnketaRepository.getPitanja(nazivAnkete, nazivIstrazivanja)
    }*/


    fun getPitanjaApi(
        idKviza: Int, onSuccess: KFunction1<List<Pitanje>, Unit>,
        onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = PitanjeAnketaRepository.getPitanja(idKviza)
            when (result) {
                is List<Pitanje>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }
}