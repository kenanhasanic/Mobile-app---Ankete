package ba.etf.rma22.projekat.data.models

import com.google.gson.annotations.SerializedName

data class PitanjeAnketa (
    @SerializedName("AnketumId")var AnketumId : Int,
    @SerializedName("PitanjeId")var PitanjeId: Int
        )