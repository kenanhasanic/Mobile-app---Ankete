package ba.etf.rma22.projekat.data.models

import com.google.gson.annotations.SerializedName

class PitanjeNovo (
    @SerializedName("id") val id: Int,
    @SerializedName("naziv") val naziv: String,
    @SerializedName("tekstPitanja") val tekstPitanja: String,
    @SerializedName("opcije") val opcije: List<String>,
    @SerializedName("PitanjeAnketa") val PitanjeAnketa: PitanjeAnketa,
        ){
}