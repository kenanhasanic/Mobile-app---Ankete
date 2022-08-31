package ba.etf.rma22.projekat.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.UpisanaAnketa

@Dao
interface UpisanaAnketaDao {
    @Query("SELECT * FROM UpisanaAnketa")
    fun getAll() : List<UpisanaAnketa>

    @Insert
    fun insertAnketa(vararg anketa: UpisanaAnketa)

    @Query("DELETE FROM UpisanaAnketa")
    fun deleteAll()

}