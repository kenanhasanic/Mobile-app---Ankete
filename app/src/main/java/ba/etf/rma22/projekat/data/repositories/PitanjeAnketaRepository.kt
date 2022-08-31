package ba.etf.rma22.projekat.data.repositories


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import ba.etf.rma22.projekat.data.AppDatabase
import ba.etf.rma22.projekat.data.models.Odgovor
import ba.etf.rma22.projekat.data.models.Pitanje
import ba.etf.rma22.projekat.data.models.PitanjeBaza
import ba.etf.rma22.projekat.data.models.PitanjeNovo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

@SuppressLint("StaticFieldLeak")
object PitanjeAnketaRepository {

    private lateinit var context: Context
    fun setContext(context: Context){
        this.context = context
    }

    /*fun getPitanja(nazivAnkete: String, nazivIstrazivanja: String): List<Pitanje>{
        var pitanjaPoAnketi = mutableListOf<Pitanje>()
        for(jednaPitanjeAnketa in pitanjaAnketa()){
            if(nazivAnkete==jednaPitanjeAnketa.anketa && nazivIstrazivanja == jednaPitanjeAnketa.nazivIstrazivanje){
                for(jednoPitanje in pitanja()){
                    if(jednoPitanje.naziv==jednaPitanjeAnketa.naziv){
                        pitanjaPoAnketi.add(jednoPitanje)
                    }
                }
            }
        }
        return pitanjaPoAnketi
    }*/

    suspend fun getPitanja(idAnkete:Int):List<Pitanje>{
        return withContext(Dispatchers.IO){
            var response = ApiAdapter.retrofit.getPitanja(idAnkete)
            var listaPitanja: List<Pitanje> = mutableListOf()
            var listaPitanjaBaza = mutableListOf<PitanjeBaza>()
            if(response != null)
                for(pitanje in response){
                    var opcije = ""
                    for(i in 0 until pitanje.opcije.size){
                        if(i == pitanje.opcije.size -1) opcije += pitanje.opcije[i]
                        else opcije += pitanje.opcije[i] + ","
                    }
                    listaPitanja += Pitanje(pitanje.id, pitanje.naziv, pitanje.tekstPitanja, opcije)
                    listaPitanjaBaza += PitanjeBaza(pitanje.id, pitanje.naziv, pitanje.tekstPitanja, opcije,pitanje.PitanjeAnketa.AnketumId)
                }
            Log.d("baza", "uslo u getPitanja")

            //insertanje pitanja duplikata sa razlicitim anketaId
            for(pitanje in listaPitanjaBaza) {
                AppDatabase.getInstance(context).pitanjeBazaDao().insertPitanje(pitanje)
            }

            //insertanje normalnih pitanja
            var pitanjaUBazi = AppDatabase.getInstance(context).pitanjeDao().getAll()
            for(pitanje in listaPitanja) {
                var pronadjen = false
                for(pitanjeUbazi in pitanjaUBazi){
                    if(pitanjeUbazi.naziv == pitanje.naziv){
                        pronadjen = true
                    }
                }
                if(!pronadjen){
                    AppDatabase.getInstance(context).pitanjeDao().insertPitanje(pitanje)
                }
            }
            return@withContext listaPitanja
        }
    }


    ////////////////////////////baza
    suspend fun upisiPitanjeUBazu(pitanje: Pitanje) : String?{
        return withContext(Dispatchers.IO) {
            try{
                var db = AppDatabase.getInstance(context)
                db!!.pitanjeDao().insertPitanje(pitanje)
                return@withContext "Upisano pitanje u bazu: " + pitanje.naziv
            }
            catch(error:Exception){
                return@withContext null
            }
        }
    }

    suspend fun getPitanjaIzBaze() : List<Pitanje>?{
        return withContext(Dispatchers.IO) {
            try{
                var db = AppDatabase.getInstance(context)
                return@withContext db!!.pitanjeDao().getAll()
            }
            catch(error:Exception){
                return@withContext null
            }
        }
    }
}