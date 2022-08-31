package ba.etf.rma22.projekat.viewmodel

import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.repositories.AnketaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

class AnketaViewModel {


    fun getAll(offset: Int, onSuccess: KFunction1<List<Anketa>, Unit>,
               onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = AnketaRepository.getAll(offset)
            when (result) {
                is List<Anketa>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getAll(onSuccess: KFunction1<List<Anketa>, Unit>,
               onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = AnketaRepository.getAll()
            when (result) {
                is List<Anketa>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getAllSaUmnozenim(offset: Int, onSuccess: KFunction1<List<Anketa>, Unit>,
               onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = AnketaRepository.getAllSaUmnozenim(offset)
            when (result) {
                is List<Anketa>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getAllSaUmnozenim(onSuccess: KFunction1<List<Anketa>, Unit>,
               onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = AnketaRepository.getAllSaUmnozenim()
            when (result) {
                is List<Anketa>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getMyAnkete(onSuccess: KFunction1<List<Anketa>, Unit>,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = AnketaRepository.getMyAnkete()
            when (result) {
                is List<Anketa>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getDone(onSuccess: KFunction1<List<Anketa>, Unit>,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = AnketaRepository.getDone()
            when (result) {
                is List<Anketa>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getFuture(onSuccess: KFunction1<List<Anketa>, Unit>,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = AnketaRepository.getFuture()
            when (result) {
                is List<Anketa>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getNotTaken(onSuccess: KFunction1<List<Anketa>, Unit>,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = AnketaRepository.getNotTaken()
            when (result) {
                is List<Anketa>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getById(
        id: Int, onSuccess: KFunction1<Anketa, Unit>,
        onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val result = AnketaRepository.getById(id)) {
                is Anketa? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getUpisane(onSuccess: KFunction1<List<Anketa>, Unit>,
                   onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = AnketaRepository.getUpisane()
            when (result) {
                is List<Anketa>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun predavanjeNeodgovorenih(idAnkete : Int) {
        CoroutineScope(Dispatchers.IO).launch {
            AnketaRepository.predavanjeNeodgovorenih(idAnkete)

        }
    }

    fun upisiAnketuUBazu(anketa: Anketa, onSuccess: KFunction1<String?, Unit>,
                         onError: () -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val result = AnketaRepository.upisiAnketuUBazu(anketa)
            when (result) {
                is String? -> onSuccess?.invoke(result)
                else-> onError.invoke()
            }
        }
    }

    fun getAnketeIzBaze(onSuccess: KFunction1<List<Anketa>?, Unit>,
                        onError: () -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val result = AnketaRepository.getAnketeIzBaze()
            when (result) {
                is List<Anketa>? -> onSuccess?.invoke(result)
                else-> onError.invoke()
            }
        }
    }


}