package ba.etf.rma22.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.Account
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.models.Pitanje

@Dao
interface AccountDao {
    @Query("SELECT * FROM Account")
    fun getAll() : List<Account>

    @Insert
    fun insertAccount(vararg account: Account)

    @Query("DELETE FROM Account")
    fun deleteAll()

}