package ba.etf.rma22.projekat.data.repositories

import android.content.Context
import android.util.Log
import ba.etf.rma22.projekat.data.AppDatabase
import ba.etf.rma22.projekat.data.models.Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AccountRepository {
    companion object{
        var acHash = "e5182ef3-9d79-44eb-a4b1-84e054e4998b"
        private lateinit var context: Context
        fun setContext(context: Context){
            this.context = context
        }

        suspend fun postaviHash(payload:String):Boolean{
            return withContext(Dispatchers.IO) {
                try{
                    Log.d("baza", "payload u accountRepo je: " + payload)
                    acHash = payload
                    var db = AppDatabase.getInstance(context)

                    var trenutniAccount = db.accountDao().getAll()
                    if(trenutniAccount.isEmpty()){
                        AppDatabase.getInstance(context).accountDao().insertAccount(Account(0,"studentovEmail", payload))
                    }else if(trenutniAccount[0].acHash == acHash){
                        return@withContext true
                    }else{
                        AppDatabase.getInstance(context).anketaDao().deleteAll()
                        AppDatabase.getInstance(context).istrazivanjeDao().deleteAll()
                        AppDatabase.getInstance(context).grupaDao().deleteAll()
                        AppDatabase.getInstance(context).pitanjeDao().deleteAll()
                        AppDatabase.getInstance(context).odgovorDao().deleteAll()
                        AppDatabase.getInstance(context).accountDao().deleteAll()
                        AppDatabase.getInstance(context).anketaTakenDao().deleteAll()
                        AppDatabase.getInstance(context).upisanaAnketaDao().deleteAll()
                        AppDatabase.getInstance(context).accountDao().insertAccount(Account(0,"studentovEmail", payload))
                    }
                    return@withContext true
                } catch(error:Exception){
                    return@withContext false
                }
            }
        }

        fun getHash(): String{
            return acHash
        }

        suspend fun getAccount(idStudenta: String): Account {
            return withContext(Dispatchers.IO) {
                return@withContext ApiAdapter.retrofit.getAccount(idStudenta)
            }
        }
    }
}