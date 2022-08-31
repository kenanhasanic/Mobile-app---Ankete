package ba.etf.rma22.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.models.Istrazivanje

@Dao
interface GrupaDao {
    @Query("SELECT * FROM Grupa")
    fun getAll(): List<Grupa>

    @Insert
    fun insertGrupa(vararg grupa: Grupa)

    @Query("DELETE FROM Grupa")
    fun deleteAll()
}