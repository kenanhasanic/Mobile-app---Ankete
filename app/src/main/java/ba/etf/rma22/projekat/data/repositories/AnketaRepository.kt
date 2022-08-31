package ba.etf.rma22.projekat.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import ba.etf.rma22.projekat.data.AppDatabase
import ba.etf.rma22.projekat.data.fragmenti.FragmentAnkete
import ba.etf.rma22.projekat.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("StaticFieldLeak")
object AnketaRepository {
    private lateinit var context: Context
    private val formater = SimpleDateFormat("yyyy-MM-dd")


    fun setContext(context: Context){
        this.context = context
    }

    fun upisiAnketeUBazu(ankete : List<Anketa>){
        var anketeUBazi = AppDatabase.getInstance(context).anketaDao().getAll()
        var dostupneAnkete = ankete
        for(anketa in dostupneAnkete){
            var pronadjen = false
            for(anketaBaza in anketeUBazi){
                if( anketa.id == anketaBaza.id){
                    pronadjen = true
                }
            }
            if(!pronadjen){
                AppDatabase.getInstance(context).anketaDao().insertAnketa(anketa)
            }
        }
    }


    fun sortiraj(lista : List<Anketa>?) : List<Anketa>{
        if (lista != null) {
            return lista.sortedBy { it.datumPocetak }
        }
        return mutableListOf()
    }

    fun dajAnketeSaOffsetomOffline(offset : Int) : List<Anketa>{
        var sveAnkete = AppDatabase.getInstance(context).anketaDao().getAll()
        var ankete = mutableListOf<Anketa>()
        var pocetni = offset*5-5
        var krajnji = offset*5
        var brojac=0
        for(anketa in sveAnkete){
            if(brojac >= pocetni && brojac < krajnji){
                ankete.add(anketa)
            }
            brojac++
        }
        return ankete
    }


    suspend fun getAll(offset:Int):List<Anketa>{
        return withContext(Dispatchers.IO){
            if(KorisnikRepository.isConnectedToInternet){
                var ankete = sortiraj(ApiAdapter.retrofit.getAnkete(offset))
                upisiAnketeUBazu(ankete)
                return@withContext ankete
            }else{
                return@withContext sortiraj((dajAnketeSaOffsetomOffline(offset)))
            }

        }
    }

    suspend fun getAll():List<Anketa>{
        return withContext(Dispatchers.IO){
            if(KorisnikRepository.isConnectedToInternet){
                var brojac = 1
                var sveAnkete = mutableListOf<Anketa>()
                while(true){
                    var listaAnketa = ApiAdapter.retrofit.getAnkete(brojac)
                    if(listaAnketa.size == 5){
                        for(anketa in listaAnketa){
                            sveAnkete.add(anketa)
                        }
                        brojac++
                    }else{
                        for(anketa in listaAnketa){
                            sveAnkete.add(anketa)
                        }
                        break
                    }
                }
                var ankete = sortiraj((sveAnkete))
                upisiAnketeUBazu(ankete)
                return@withContext ankete
            }else{
                var sveAnkete = AppDatabase.getInstance(context).anketaDao().getAll()
                return@withContext sortiraj((sveAnkete))
            }


        }
    }

    suspend fun getAllSaUmnozenim():List<Anketa>{
        return withContext(Dispatchers.IO){
            if(KorisnikRepository.isConnectedToInternet){
                return@withContext sortiraj(postaviDatume(postaviProgres(umnoziPoIstrazivanjima(getAll()))))
            }else{
                return@withContext sortiraj((getAll()))
            }

        }
    }
    suspend fun getAllSaUmnozenim(offset:Int):List<Anketa>{
        return withContext(Dispatchers.IO){
            if(KorisnikRepository.isConnectedToInternet){
                return@withContext sortiraj(postaviDatume(postaviProgres(umnoziPoIstrazivanjima(getAll(offset)))))
            }else{
                return@withContext sortiraj((getAll(offset)))
            }

        }
    }

    suspend fun getMyAnkete():List<Anketa>{
        return withContext(Dispatchers.IO){
            if(KorisnikRepository.isConnectedToInternet){
                var ankete = sortiraj(postaviDatume(postaviProgres(getUpisane())))
                postaviUpisaneAnkete(ankete)
                return@withContext ankete
            }
            return@withContext sortiraj(postaviDatumeOffline(dajUpisaneAnkete()))
        }
    }


    suspend fun getDone():List<Anketa>{
        return withContext(Dispatchers.IO){
            var mojeAnkete = getMyAnkete()
            var zavrseneAnketeKorisnika = mutableListOf<Anketa>()
            for(jednaAnketa in mojeAnkete){
                if(jednaAnketa.datumRada != null){
                    zavrseneAnketeKorisnika.add(jednaAnketa)
                }
            }
            if(KorisnikRepository.isConnectedToInternet){
                return@withContext sortiraj(postaviDatume(postaviProgres(zavrseneAnketeKorisnika)))
            }else{
                return@withContext sortiraj(zavrseneAnketeKorisnika)
            }

        }
    }

    suspend fun getFuture():List<Anketa>{
        return withContext(Dispatchers.IO){
            var mojeAnkete = getMyAnkete()
            var buduceAnketeKorisnika = mutableListOf<Anketa>()
            for(jednaAnketa in mojeAnkete){
                if(formater.parse(jednaAnketa.datumPocetak).after(Calendar.getInstance().time)){
                    buduceAnketeKorisnika.add(jednaAnketa)
                }
            }
            if(KorisnikRepository.isConnectedToInternet){
                return@withContext sortiraj(postaviDatume(postaviProgres(buduceAnketeKorisnika)))
            }else{
                return@withContext sortiraj(buduceAnketeKorisnika)
            }

        }
    }

    suspend fun getNotTaken():List<Anketa>{
        return withContext(Dispatchers.IO){
            var mojeAnkete = getMyAnkete()
            var neuradjeneAnketeKorisnika = mutableListOf<Anketa>()
            for(jednaAnketa in mojeAnkete){

                if(jednaAnketa.datumKraj!=null){
                    var datum = formater.parse(jednaAnketa.datumKraj)
                    if(datum.before(Calendar.getInstance().time) && jednaAnketa.datumRada == null){
                        neuradjeneAnketeKorisnika.add(jednaAnketa)
                    }
                }
            }
            if(KorisnikRepository.isConnectedToInternet){
                return@withContext sortiraj(postaviDatume(postaviProgres(neuradjeneAnketeKorisnika)))
            }else{
                return@withContext sortiraj(neuradjeneAnketeKorisnika)
            }

        }
    }

    suspend fun getById(id:Int):Anketa{
        return withContext(Dispatchers.IO){
            return@withContext ApiAdapter.retrofit.getAnketaById(id)
        }
    }

    fun postaviUpisaneAnkete(ankete: List<Anketa>) {
        Log.d("grupa1", "postavlja u bzau postaviUpisaneAnkete")


        var anketeUBazi = AppDatabase.getInstance(context).upisanaAnketaDao().getAll()
        var dostupneAnkete = ankete
        for(anketa in dostupneAnkete){
            var pronadjen = false
            for(anketaBaza in anketeUBazi){
                if(anketa.id == anketaBaza.id && anketa.naziv == anketaBaza.naziv && anketa.nazivIstrazivanja == anketaBaza.nazivIstrazivanja){
                    pronadjen = true
                }
            }
            if(!pronadjen){
                var upisana = UpisanaAnketa(anketa.id, anketa.naziv,anketa.nazivIstrazivanja, anketa.datumPocetak,anketa.datumKraj,anketa.datumRada,anketa.trajanje,anketa.nazivGrupe,anketa.progres)
                AppDatabase.getInstance(context).upisanaAnketaDao().insertAnketa(upisana)
            }
        }
    }

    fun dajUpisaneAnkete() : List<Anketa>{
        var ankete = mutableListOf<Anketa>()
        for(upisanaAnketa in AppDatabase.getInstance(context).upisanaAnketaDao().getAll()){
            var nova = Anketa(upisanaAnketa.id, upisanaAnketa.naziv,upisanaAnketa.nazivIstrazivanja, upisanaAnketa.datumPocetak,upisanaAnketa.datumKraj,upisanaAnketa.datumRada,upisanaAnketa.trajanje,upisanaAnketa.nazivGrupe,upisanaAnketa.progres)
            ankete.add(nova)
        }
        return ankete
    }

    suspend fun getUpisane():List<Anketa>{

        return withContext(Dispatchers.IO){
            var grupeNaKojeJeUpisan = ApiAdapter.retrofit.getGrupeByStudentId(AccountRepository.getHash())
            var sveAnkete = mutableListOf<Anketa>()
            for(grupa in grupeNaKojeJeUpisan){
                var istrazivanje = ApiAdapter.retrofit.getIstrazivanjeById(grupa.IstrazivanjeId)
                var anketeZaGrupu = ApiAdapter.retrofit.getAnketeByGroupId(grupa.id)
                for(anketa in anketeZaGrupu){
                    var nova = Anketa(anketa.id,anketa.naziv,istrazivanje.naziv,anketa.datumPocetak, null,anketa.datumRada,anketa.trajanje,grupa.naziv,anketa.progres)
                    sveAnkete.add(nova)
                }
            }
            var razvrstanePoImenu = sveAnkete.distinctBy { it.id }
            return@withContext sveAnkete
        }
    }

    suspend fun umnoziPoIstrazivanjima(ankete : List<Anketa>) : List<Anketa>{
        return withContext(Dispatchers.IO){
            var sveAnkete = mutableListOf<Anketa>()
            for(anketa in ankete){
                var grupe = ApiAdapter.retrofit.getGrupeByAnketaId(anketa.id)
                var izdvojenePremaIstrazivanju = grupe.distinctBy { it.IstrazivanjeId }
                for(grupa in izdvojenePremaIstrazivanju){

                    var istrazivanje = ApiAdapter.retrofit.getIstrazivanjeById(grupa.IstrazivanjeId)
                    var nova = Anketa(anketa.id,anketa.naziv,istrazivanje.naziv,anketa.datumPocetak, null,anketa.datumRada,anketa.trajanje,grupa.naziv,anketa.progres)
                    Log.d("account","naziv istrazivanja: " +istrazivanje.naziv)
                    nova.nazivIstrazivanja = istrazivanje.naziv
                    sveAnkete.add(nova)

                }

            }
            /*for(anketa in sveAnkete){
                Log.d("account", "naziv istrazivanja ankete: " + anketa.nazivIstrazivanja)
            }*/
            return@withContext sveAnkete
        }
    }

    suspend fun postaviProgres(ankete : List<Anketa>) : List<Anketa>{
        return withContext(Dispatchers.IO){
            var listaSaPostavljenimProgresom = mutableListOf<Anketa>()
            var poceteAnkete = ApiAdapter.retrofit.getPoceteAnkete(AccountRepository.getHash())
            for(anketa in ankete){
                var pronadjena = false
                var progres = 0.0f
                for(pocetaAnketa in poceteAnkete){
                    if(anketa.id == pocetaAnketa.AnketumId){
                        pronadjena=true
                        progres = (pocetaAnketa.progres.toFloat()/100)
                        Log.d("account", progres.toString())
                    }
                }
                if(!pronadjena){
                    //Log.d("account", "nije pronadjena")
                    listaSaPostavljenimProgresom.add(anketa)
                }else{
                    //Log.d("account", "pronadjena")
                    var nova = Anketa(anketa.id,anketa.naziv,anketa.nazivIstrazivanja,anketa.datumPocetak, null,anketa.datumRada,anketa.trajanje,anketa.nazivGrupe,progres)
                    listaSaPostavljenimProgresom.add(nova)
                }
            }
            return@withContext listaSaPostavljenimProgresom
        }
    }

    fun postaviDatumeOffline(ankete: List<Anketa>?) : List<Anketa>?{
        val poceteAnkete = AppDatabase.getInstance(context).anketaTakenDao().getAll()
        if (poceteAnkete != null) {
            for(pocetaAnketa in poceteAnkete){
                if(ankete != null){
                    for(anketa in ankete){
                        if(anketa.id == pocetaAnketa.AnketumId){
                            var pitanja = AppDatabase.getInstance(FragmentAnkete.context).pitanjeBazaDao().getAll()
                            var pitanja1 = mutableListOf<PitanjeBaza>()
                            for(pitanje in pitanja){
                                if(pitanje.AnketumId== anketa.id){
                                    pitanja1.add(pitanje)
                                }
                            }
                            var svaPitanjaBaza = pitanja1.distinctBy { it.id }
                            val odgovori = mutableListOf<Odgovor>()
                            for(odg in AppDatabase.getInstance(context).odgovorDao().getAll()){
                                if(odg.AnketaTakenId == pocetaAnketa.id){
                                    odgovori.add(odg)
                                }
                            }

                            anketa.progres = pocetaAnketa.progres.toFloat()
                            if(odgovori.size == svaPitanjaBaza.size)
                                anketa.datumRada = pocetaAnketa.datumRada
                        }
                    }
                }
            }
        }
        return ankete
    }

    suspend fun postaviDatume(ankete: List<Anketa>?) : List<Anketa>?{
        val poceteAnkete = TakeAnketaRepository.getPoceteAnkete()
        if (poceteAnkete != null) {
            for(pocetaAnketa in poceteAnkete)
                if(ankete != null)
                    for(anketa in ankete)
                        if(anketa.id == pocetaAnketa.AnketumId){
                            val pitanja = PitanjeAnketaRepository.getPitanja(anketa.id)
                            val odgovori = OdgovorRepository.getOdgovoriAnketa(anketa.id)
                            anketa.progres = pocetaAnketa.progres.toFloat()
                            if(odgovori.size == pitanja.size)
                                anketa.datumRada = formater.format(pocetaAnketa.datumRada)
                        }
        }
        return ankete
    }

    suspend fun predavanjeNeodgovorenih(idAnkete : Int){
        var pitanja = PitanjeAnketaRepository.getPitanja(idAnkete)
        var odgovori = OdgovorRepository.getOdgovoriAnketa(idAnkete)
        for(pitanje in pitanja){
            var pronadjenOdgovorZaPitanje = false
            for(odgovor in odgovori){
                if(odgovor.PitanjeId == pitanje.id){
                    pronadjenOdgovorZaPitanje = true
                }
            }
            if(!pronadjenOdgovorZaPitanje){
                OdgovorRepository.postaviOdgovorAnketa(KorisnikRepository.idAnketaTakenKliknuteAnkete, pitanje.id, 0)
            }
        }
    }


    ////////////////////////////baza
    suspend fun upisiAnketuUBazu(anketa: Anketa) : String?{
        return withContext(Dispatchers.IO) {
            try{
                var db = AppDatabase.getInstance(context)
                db!!.anketaDao().insertAnketa(anketa)
                return@withContext "Upisana anketa u bazu: " + anketa.naziv
            }
            catch(error:Exception){
                return@withContext null
            }
        }
    }

    suspend fun getAnketeIzBaze() : List<Anketa>?{
        return withContext(Dispatchers.IO) {
            try{
                var db = AppDatabase.getInstance(context)
                return@withContext db!!.anketaDao().getAll()
            }
            catch(error:Exception){
                return@withContext null
            }
        }
    }

}