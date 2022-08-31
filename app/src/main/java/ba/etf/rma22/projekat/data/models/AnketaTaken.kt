package ba.etf.rma22.projekat.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*


data class AnketaTaken (
    @SerializedName("id") var id : Int,
    @SerializedName("student")var student : String,
    @SerializedName("progres")var progres : Int,
    @SerializedName("datumRada") var datumRada : Date,
    @SerializedName("AnketumId") var AnketumId : Int
        ){

}