package ba.etf.rma22.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "AnketaTaken")
data class AnketaTakenNovo (
    @PrimaryKey @SerializedName("id") var id : Int,
    @ColumnInfo(name = "student")@SerializedName("student")var student : String,
    @ColumnInfo(name = "progres")@SerializedName("progres")var progres : Int,
    @ColumnInfo(name = "datumRada")@SerializedName("datumRada") var datumRada : String,
    @ColumnInfo(name = "AnketumId")@SerializedName("AnketumId") var AnketumId : Int
){

}