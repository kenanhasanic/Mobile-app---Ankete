package ba.etf.rma22.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.models.Pitanje

@Dao
interface PitanjeDao {
    @Query("SELECT * FROM Pitanje")
    fun getAll() : List<Pitanje>

    @Insert
    fun insertPitanje(vararg pitanje: Pitanje)

    @Query("DELETE FROM Pitanje")
    fun deleteAll()
}