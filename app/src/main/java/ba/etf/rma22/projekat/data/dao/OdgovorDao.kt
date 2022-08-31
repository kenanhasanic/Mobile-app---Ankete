package ba.etf.rma22.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.models.Odgovor
import ba.etf.rma22.projekat.data.models.Pitanje

@Dao
interface OdgovorDao {
    @Query("SELECT * FROM Odgovor")
    fun getAll() : List<Odgovor>

    @Insert
    fun insertOdgovor(vararg odgovor: Odgovor)

    @Query("DELETE FROM Odgovor")
    fun deleteAll()
}