package ba.etf.rma22.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*
@Entity(tableName = "Istrazivanje")
data class Istrazivanje(
    @PrimaryKey @SerializedName("id")var id: Int,
    @ColumnInfo(name = "naziv") @SerializedName("naziv")var naziv: String,
    @ColumnInfo(name = "godina") @SerializedName("godina")var godina : Int
)