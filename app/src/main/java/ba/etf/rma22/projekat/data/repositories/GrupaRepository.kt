package ba.etf.rma22.projekat.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import ba.etf.rma22.projekat.data.models.Grupa
import java.text.SimpleDateFormat

@SuppressLint("StaticFieldLeak")
object GrupaRepository {
    private lateinit var context: Context
    fun setContext(context: Context){
        this.context = context
    }

    fun getGroupsByIstrazivanje(nazivIstrazivanja: String): List<Grupa> {
        /*var grupe = dajGrupe()
        var grupePoIstrazivanju = mutableListOf<Grupa>()
        for (jednaGrupa in grupe) {
            if (jednaGrupa.nazivIstrazivanja == nazivIstrazivanja) {
                grupePoIstrazivanju.add(jednaGrupa)
            }
        }*/
        return mutableListOf()
    }

}