package ba.etf.rma22.projekat.viewmodel

import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.*
import ba.etf.rma22.projekat.data.repositories.AccountRepository
import ba.etf.rma22.projekat.data.repositories.AnketaRepository
import ba.etf.rma22.projekat.data.repositories.IstrazivanjeIGrupaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

class IstrazivanjeIGrupaViewModel {

    fun getIstrazivanja(offset: Int, onSuccess: KFunction1<List<Istrazivanje>, Unit>,
                   onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = IstrazivanjeIGrupaRepository.getIstrazivanja(offset)
            when (result) {
                is List<Istrazivanje>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getIstrazivanja(onSuccess: KFunction1<List<Istrazivanje>, Unit>,
                   onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = IstrazivanjeIGrupaRepository.getIstrazivanja()
            when (result) {
                is List<Istrazivanje>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getGrupe(onSuccess : KFunction1<List<Grupa>, Unit>,
                 onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = IstrazivanjeIGrupaRepository.getGrupe()
            when (result) {
                is List<Grupa>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getGrupeZaIstrazivanje(idIstrazivanja:Int, onSuccess : KFunction1<List<Grupa>, Unit>,
                 onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = IstrazivanjeIGrupaRepository.getGrupeZaIstrazivanje(idIstrazivanja)
            when (result) {
                is List<Grupa>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun upisiUGrupu(idGrupe:Int, onSuccess : KFunction1<Boolean?, Unit>,
                               onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = IstrazivanjeIGrupaRepository.upisiUGrupu(idGrupe)
            when (result) {
                is Boolean? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getUpisaneGrupe( onSuccess : KFunction1<List<Grupa>?, Unit>,
                               onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = IstrazivanjeIGrupaRepository.getUpisaneGrupe()
            when (result) {
                is List<Grupa>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getUpisanaIstrazivanjaId( onSuccess : KFunction1<List<Int>?, Unit>,
                         onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = IstrazivanjeIGrupaRepository.getUpisanaIstrazivanjaId()
            when (result) {
                is List<Int>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }
    //baza

    fun upisiIstrazivanjeUBazu(istrazivanje: Istrazivanje, onSuccess: KFunction1<String?, Unit>,
                      onError: () -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val result = IstrazivanjeIGrupaRepository.upisiIstrazivanjeUBazu(istrazivanje)
            when (result) {
                is String? -> onSuccess?.invoke(result)
                else-> onError.invoke()
            }
        }
    }

    fun getIstrazivanjaIzBaze(onSuccess: KFunction1<List<Istrazivanje>?, Unit>,
                              onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = IstrazivanjeIGrupaRepository.getIstrazivanjaIzBaze()
            when (result) {
                is List<Istrazivanje>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }
}