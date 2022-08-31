package ba.etf.rma22.projekat.viewmodel

import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.repositories.DataBaseRepository
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeIGrupaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

class DataBaseViewModel {

    /*fun updateDataBase(onSuccess: KFunction1<String?, Unit>,
                               onError: () -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val result = DataBaseRepository.updateDataBase()
            when (result) {
                is String? -> onSuccess?.invoke(result)
                else-> onError.invoke()
            }
        }
    }*/

    fun ocistiBazu(onSuccess: KFunction1<String?, Unit>,
                       onError: () -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val result = DataBaseRepository.ocistiBazu()
            when (result) {
                is String? -> onSuccess?.invoke(result)
                else-> onError.invoke()
            }
        }
    }
}