package ba.etf.rma22.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Istrazivanje

@Dao
interface IstrazivanjeDao {
    @Query("SELECT * FROM Istrazivanje")
    fun getAll() : List<Istrazivanje>

    @Insert
    fun insertIstrazivanje(vararg istrazivanje: Istrazivanje)

    @Query("DELETE FROM Istrazivanje")
    fun deleteAll()
}