package ba.etf.rma22.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "Anketa")
data class Anketa(
    @PrimaryKey @SerializedName("id")var id: Int,
    @ColumnInfo(name = "naziv")@SerializedName("naziv")var naziv: String,
    @ColumnInfo(name = "nazivIstrazivanja")@SerializedName("nazivIstrazivanja")var nazivIstrazivanja : String?,
    @ColumnInfo(name = "datumPocetak")@SerializedName("datumPocetak")var datumPocetak: String?,
    @ColumnInfo(name = "datumKraj")@SerializedName("datumKraj")var datumKraj: String?,
    @ColumnInfo(name = "datumRada")@SerializedName("datumRada")var datumRada: String?,
    @ColumnInfo(name = "trajanje")@SerializedName("trajanje")var trajanje : Int,
    @ColumnInfo(name = "nazivGrupe")@SerializedName("nazivGrupe")var nazivGrupe : String?,
    @ColumnInfo(name = "progres")@SerializedName("progres")var progres : Float?
){

    override fun equals(other: Any?): Boolean{
        if(other is Anketa)
            return other?.id == this.id
        return false
    }

    override fun hashCode() = Objects.hash(id)
}