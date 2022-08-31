package ba.etf.rma22.projekat.data.repositories

import android.content.Context
import android.util.Log
import ba.etf.rma22.projekat.data.AppDatabase
import ba.etf.rma22.projekat.data.models.AnketaTaken
import ba.etf.rma22.projekat.data.models.AnketaTakenNovo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

class TakeAnketaRepository {
    companion object{

        private lateinit var context: Context
        private val formater = SimpleDateFormat("yyyy-MM-dd")


        fun setContext(context: Context){
            this.context = context
        }

        suspend fun zapocniAnketu(idAnkete:Int): AnketaTaken? {
            return  withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.zapocniAnketu(idAnkete, AccountRepository.acHash)

                if(response.toString().contains("Anketa not found") || response.toString().contains("nije upisan")) return@withContext null
                else {
                    var zapoceteAnkete = getPoceteAnkete() // poziva metodu koja u sebi poziva upisivanje pokusaja u bazu
                }
                return@withContext response
            }
        }

        suspend fun getPoceteAnkete():List<AnketaTaken>?{
            val pokusajiKvizova: List<AnketaTaken>? = withContext(Dispatchers.IO){
                Log.d("baza", "////////////////getPoceteAnkete")
                var pokusaji = ApiAdapter.retrofit.getPoceteAnkete(AccountRepository.acHash)
                upisiPoceteUBazu(pokusaji)
                return@withContext pokusaji
            }
            if(pokusajiKvizova!!.size==0) return null
            return pokusajiKvizova
        }

        fun upisiPoceteUBazu(lista : List<AnketaTaken>){
            var pokusaji = lista
            var pokusajiubazi = AppDatabase.getInstance(context).anketaTakenDao().getAll()
            for(pokusaj in pokusaji){
                var pronadjen = false
                for(pokusajBaza in pokusajiubazi){
                    if(pokusaj.id == pokusajBaza.id){
                        Log.d("baza", "pronadjen u getPoceteAnkete")
                        pronadjen = true
                    }
                }
                if(!pronadjen){
                    var anketaTakenNovo = AnketaTakenNovo(pokusaj.id, pokusaj.student, pokusaj.progres, formater.format(pokusaj.datumRada), pokusaj.AnketumId)
                    AppDatabase.getInstance(context).anketaTakenDao().insertAnketaTaken(anketaTakenNovo)
                }
            }
        }


    }
}