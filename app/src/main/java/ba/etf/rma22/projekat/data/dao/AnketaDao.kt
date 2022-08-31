package ba.etf.rma22.projekat.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.Anketa

@Dao
interface AnketaDao {
    @Query("SELECT * FROM Anketa")
    fun getAll() : List<Anketa>

    @Insert
    fun insertAnketa(vararg anketa: Anketa)

    @Query("DELETE FROM Anketa")
    fun deleteAll()
}