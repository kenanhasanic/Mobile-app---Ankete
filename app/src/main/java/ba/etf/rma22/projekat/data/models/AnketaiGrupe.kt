package ba.etf.rma22.projekat.data.models

import com.google.gson.annotations.SerializedName

data class AnketaiGrupe (
    @SerializedName("GrupaId")var grupaId : Int,
    @SerializedName("AnketumId")var AnketumId : Int
        )
