package ba.etf.rma22.projekat.viewmodel

import ba.etf.rma22.projekat.data.models.Account
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.repositories.AccountRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

class AccountViewModel {

    fun getHash(): String{
        return AccountRepository.acHash
    }

    fun getAccount(idStudenta: String, onSuccess: (account: Account) -> Unit,
                   onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = AccountRepository.getAccount(idStudenta)
            when (result) {
                is Account -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun postaviHash(payload : String, onSuccess: KFunction1<Boolean, Unit>,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = AccountRepository.postaviHash(payload)
            when (result) {
                is Boolean -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }
}