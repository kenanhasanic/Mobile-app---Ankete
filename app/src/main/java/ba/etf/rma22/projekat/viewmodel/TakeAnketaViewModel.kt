package ba.etf.rma22.projekat.viewmodel

import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.AnketaTaken
import ba.etf.rma22.projekat.data.repositories.AnketaRepository
import ba.etf.rma22.projekat.data.repositories.TakeAnketaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

class TakeAnketaViewModel {

    fun zapocniAnketu(idAnkete:Int,onSuccess: KFunction1<AnketaTaken?, Unit>,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = TakeAnketaRepository.zapocniAnketu(idAnkete)
            when (result) {
                is AnketaTaken? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getPoceteAnkete(onSuccess: KFunction1<List<AnketaTaken>?, Unit>,
                        onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = TakeAnketaRepository.getPoceteAnkete()
            when (result) {
                is List<AnketaTaken>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }
}