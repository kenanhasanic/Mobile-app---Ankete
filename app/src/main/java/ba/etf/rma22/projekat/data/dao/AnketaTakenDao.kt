package ba.etf.rma22.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ba.etf.rma22.projekat.data.models.*

@Dao
interface AnketaTakenDao {
    @Query("SELECT * FROM AnketaTaken")
    fun getAll() : List<AnketaTakenNovo>

    @Insert
    fun insertAnketaTaken(vararg anketaTaken: AnketaTakenNovo)

    @Query("DELETE FROM AnketaTaken")
    fun deleteAll()

    @Query("UPDATE anketaTaken SET progres = :progres WHERE id = :id")
    fun updateProgres(progres : Int, id : Int)
}