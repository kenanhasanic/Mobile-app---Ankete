package ba.etf.rma22.projekat.data.models

import com.google.gson.annotations.SerializedName

data class OdgPitanja (
    @SerializedName("odgovor")var odgovor: Int,
    @SerializedName("pitanje")var idPitanje : Int,
    @SerializedName("progres")var progres: Int
)