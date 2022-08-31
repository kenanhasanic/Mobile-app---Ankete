package ba.etf.rma22.projekat.data.repositories

import android.content.Context
import android.util.Log
import ba.etf.rma22.projekat.data.AppDatabase
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.OdgPitanja
import ba.etf.rma22.projekat.data.models.Odgovor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.RoundingMode
import java.text.SimpleDateFormat

class OdgovorRepository {
    companion object{
        private lateinit var context: Context
        fun setContext(context: Context){
            this.context = context
        }

        fun upisiOdgovoreUBazu(odgovori : List<Odgovor>){
            Log.d("grupa1", "//////////////////////upisiOdgovorUBazu")
            var odgovoriUBazi = AppDatabase.getInstance(context).odgovorDao().getAll()
            var dostupniOdogovri = odgovori
            for(odgovor in dostupniOdogovri){
                var pronadjen = false
                for(odgovorBaza in odgovoriUBazi){
                    if( odgovor.PitanjeId == odgovorBaza.PitanjeId && odgovor.AnketaTakenId == odgovorBaza.AnketaTakenId){
                        pronadjen = true
                    }
                }
                if(!pronadjen){
                    AppDatabase.getInstance(context).odgovorDao().insertOdgovor(odgovor)
                }
            }
        }

        suspend fun getOdgovoriAnketa(idAnkete:Int):List<Odgovor> {

            var pocetaAnketaId = 0
            var zapoceteAnkete = TakeAnketaRepository.getPoceteAnkete()
            if (zapoceteAnkete != null) {
                for(anketaTaken in zapoceteAnkete){
                    if(anketaTaken.AnketumId== idAnkete){
                        pocetaAnketaId = anketaTaken.id
                    }
                }
            }
            return withContext(Dispatchers.IO){
                var odgovori = ApiAdapter.retrofit.getOdgovoriAnketa(AccountRepository.acHash, pocetaAnketaId)
                upisiOdgovoreUBazu(odgovori)
                return@withContext odgovori
            }
        }

        suspend fun postaviOdgovorAnketa(idAnketaTaken:Int,idPitanje:Int,odgovor:Int):Int{
            var idAnkete = 0
            var zapoceteAnkete = TakeAnketaRepository.getPoceteAnkete()
            if (zapoceteAnkete != null) {
                for(anketaTaken in zapoceteAnkete){
                    if(anketaTaken.id== idAnketaTaken){
                        idAnkete = anketaTaken.AnketumId
                    }
                }
            }
            var pitanja = PitanjeAnketaRepository.getPitanja(idAnkete)
            var odgovori = getOdgovoriAnketa(idAnkete)
            var progres : Float= ((odgovori.size+1)/pitanja.size.toFloat())
            var rounded = (progres*10).toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble()
            var finalProgress = rounded.toInt()
            if(rounded.toInt() % 2 != 0){
                finalProgress += 1
            }
            finalProgress*=10;
            var odgPitanje = OdgPitanja(odgovor,idPitanje,finalProgress)
            return withContext(Dispatchers.IO){

                //kreiranje i dodavanje odgovora u bazu
                var odgovorZaDodatUBazu = Odgovor(AppDatabase.getInstance(context).odgovorDao().getAll().size+1,odgovor,idAnketaTaken,idPitanje)
                AppDatabase.getInstance(context).odgovorDao().insertOdgovor(odgovorZaDodatUBazu)
                AppDatabase.getInstance(context).anketaTakenDao().updateProgres(finalProgress, idAnketaTaken)

                var response = ApiAdapter.retrofit.postaviOdgovorAnketa(AccountRepository.acHash, idAnketaTaken,odgPitanje)

                if(response.toString().contains("već odgovoreno na ovo pitanje") || response.toString().contains("Ne postoji account") || response.toString().contains("AnketaTaken not found")) return@withContext -1
                else return@withContext finalProgress


            }

        }

        ////////////////////////////baza
        suspend fun upisiOdgovorUBazu(odgovor: Odgovor) : String?{
            return withContext(Dispatchers.IO) {
                try{
                    var db = AppDatabase.getInstance(context)
                    db!!.odgovorDao().insertOdgovor(odgovor)
                    return@withContext "Upisan odgovor u bazu na anketaTaken: " + odgovor.AnketaTakenId
                }
                catch(error:Exception){
                    return@withContext null
                }
            }
        }

        suspend fun getOdgovoreIzBaze() : List<Odgovor>?{
            return withContext(Dispatchers.IO) {
                try{
                    var db = AppDatabase.getInstance(context)
                    return@withContext db!!.odgovorDao().getAll()
                }
                catch(error:Exception){
                    return@withContext null
                }
            }
        }

    }
}