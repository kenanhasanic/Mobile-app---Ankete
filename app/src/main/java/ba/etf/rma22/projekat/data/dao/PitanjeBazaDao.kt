package ba.etf.rma22.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.models.Pitanje
import ba.etf.rma22.projekat.data.models.PitanjeBaza

@Dao
interface PitanjeBazaDao {
    @Query("SELECT * FROM PitanjeBaza")
    fun getAll() : List<PitanjeBaza>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPitanje(vararg pitanje: PitanjeBaza)

    @Query("DELETE FROM PitanjeBaza")
    fun deleteAll()
}