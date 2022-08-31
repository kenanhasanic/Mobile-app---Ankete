package ba.etf.rma22.projekat.data.repositories

import android.content.Context
import android.util.Log
import ba.etf.rma22.projekat.data.AppDatabase
import ba.etf.rma22.projekat.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IstrazivanjeIGrupaRepository {


    companion object{
        private lateinit var context: Context
        fun setContext(context: Context){
            this.context = context
        }
        fun upisiIstrazivanjaUBazu(istrazivanja : List<Istrazivanje>){
            var istrazivanjaUBazi = AppDatabase.getInstance(context).istrazivanjeDao().getAll()
            var dostupnaIstrazivanja = istrazivanja
            for(istrazivanje in dostupnaIstrazivanja){
                var pronadjen = false
                for(istrazivanjeBaza in istrazivanjaUBazi){
                    if( istrazivanje.id == istrazivanjeBaza.id){
                        pronadjen = true
                    }
                }
                if(!pronadjen){
                    AppDatabase.getInstance(context).istrazivanjeDao().insertIstrazivanje(istrazivanje)
                }
            }
        }

        fun upisiGrupeUBazu(grupe : List<Grupa>){
            var grupeUBazi = AppDatabase.getInstance(context).grupaDao().getAll()
            var dostupneGrupe = grupe
            for(grupa in dostupneGrupe){
                var pronadjen = false
                for(grupaUBazi in grupeUBazi){
                    if( grupa.id == grupaUBazi.id){
                        pronadjen = true
                    }
                }
                if(!pronadjen){
                    AppDatabase.getInstance(context).grupaDao().insertGrupa(grupa)
                }
            }
        }

        suspend fun getIstrazivanja(offset:Int):List<Istrazivanje>{
            return withContext(Dispatchers.IO){
                var istrazivanja = ApiAdapter.retrofit.getIstrazivanja(offset)
                upisiIstrazivanjaUBazu(istrazivanja)
                return@withContext istrazivanja
            }
        }

        suspend fun getIstrazivanja():List<Istrazivanje>{
            return withContext(Dispatchers.IO){
                var brojac = 1
                var svaIstrazivanja = mutableListOf<Istrazivanje>()
                while(true){
                    var listaIstrazivanja = ApiAdapter.retrofit.getIstrazivanja(brojac)
                    if(listaIstrazivanja.size == 5){
                        for(istrazivanje in listaIstrazivanja){
                            svaIstrazivanja.add(istrazivanje)
                        }
                        brojac++
                    }else{
                        for(istrazivanje in listaIstrazivanja){
                            svaIstrazivanja.add(istrazivanje)
                        }
                        break
                    }
                }

                upisiIstrazivanjaUBazu(svaIstrazivanja)
                return@withContext svaIstrazivanja
            }
        }
        suspend fun getGrupe(): List<Grupa> {
            return withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.getGrupe()
                upisiGrupeUBazu(response)
                return@withContext response
            }
        }

        suspend fun getGrupeZaIstrazivanje(idIstrazivanja:Int):List<Grupa>{
            return withContext(Dispatchers.IO){
                var sveGrupe = ApiAdapter.retrofit.getGrupe()
                var istrazivanje = ApiAdapter.retrofit.getIstrazivanjeById(idIstrazivanja)
                var grupeZaIstrazivanje = mutableListOf<Grupa>()
                for(grupa in sveGrupe){
                    if(grupa.IstrazivanjeId == istrazivanje.id){
                        grupeZaIstrazivanje.add(grupa)
                    }
                }
                return@withContext grupeZaIstrazivanje
            }
        }


        suspend fun upisiUGrupu(idGrupa: Int): Boolean? {
            return withContext(Dispatchers.IO){
                Log.d("grupa1", "uslo u grupu")
                val message = ApiAdapter.retrofit.upisiUGrupu(idGrupa, AccountRepository.getHash())
                var dostupneAnkete = AnketaRepository.getMyAnkete() //pozivom funckije se upisuju upisaneAnkete

                if(message.message.contains("je dodan u grupu")){
                    return@withContext true
                }else if(message.message.contains("Ne postoji account") || message.toString().contains("Grupa not found")){
                    return@withContext false
                }
                return@withContext true
            }
        }

        suspend fun getUpisaneGrupe():List<Grupa>{
            return withContext(Dispatchers.IO){
                var response = ApiAdapter.retrofit.getGrupeByStudentId(AccountRepository.getHash())
                return@withContext response
            }
        }

        suspend fun getUpisanaIstrazivanjaId():List<Int>{
            return withContext(Dispatchers.IO){
                var grupe = getUpisaneGrupe()
                var idIstrazivanja = mutableListOf<Int>()
                for(grupa in grupe){
                    idIstrazivanja.add(grupa.IstrazivanjeId)
                }
                return@withContext idIstrazivanja.distinct()
            }
        }

        /////////////////////////////baza

        ///////////////////////////////istrazivanje
        suspend fun upisiIstrazivanjeUBazu(istrazivanje: Istrazivanje) : String?{
            return withContext(Dispatchers.IO) {
                try{
                    var db = AppDatabase.getInstance(context)
                    db!!.istrazivanjeDao().insertIstrazivanje(istrazivanje)
                    return@withContext "Upisano istrazivanje u bazu: " + istrazivanje.naziv
                }
                catch(error:Exception){
                    return@withContext null
                }
            }
        }

        suspend fun getIstrazivanjaIzBaze() : List<Istrazivanje>?{
            return withContext(Dispatchers.IO) {
                try{
                    var db = AppDatabase.getInstance(context)
                    return@withContext db!!.istrazivanjeDao().getAll()
                }
                catch(error:Exception){
                    return@withContext null
                }
            }
        }

        ////////////////////////////////grupa
        suspend fun upisiGrupuUBazu(grupa: Grupa) : String?{
            return withContext(Dispatchers.IO) {
                try{
                    var db = AppDatabase.getInstance(context)
                    db!!.grupaDao().insertGrupa(grupa)
                    return@withContext "Upisana grupa u bazu: " + grupa.naziv
                }
                catch(error:Exception){
                    return@withContext null
                }
            }
        }

        suspend fun getGrupeIzBaze() : List<Grupa>?{
            return withContext(Dispatchers.IO) {
                try{
                    var db = AppDatabase.getInstance(context)
                    return@withContext db!!.grupaDao().getAll()
                }
                catch(error:Exception){
                    return@withContext null
                }
            }
        }

    }
}