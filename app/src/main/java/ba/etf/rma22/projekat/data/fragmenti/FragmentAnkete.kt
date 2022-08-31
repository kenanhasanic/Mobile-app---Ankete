package ba.etf.rma22.projekat.data.fragmenti

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma22.projekat.MainActivity
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.AppDatabase
import ba.etf.rma22.projekat.data.models.*
import ba.etf.rma22.projekat.data.repositories.KorisnikRepository
import ba.etf.rma22.projekat.view.AnketaAdapter
import ba.etf.rma22.projekat.view.ViewPagerAdapter
import ba.etf.rma22.projekat.viewmodel.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class FragmentAnkete : Fragment(), AnketaAdapter.OnItemClickListener{

    companion object{
        fun newInstance() : FragmentAnkete = FragmentAnkete()
        private var filterAnketaOnResume = 0
        lateinit var anketaKliknuta : Anketa
        lateinit var context : Context
        var anketeZaOffline = listOf<Anketa>()
    }

    private lateinit var listaAnketa : RecyclerView
    private lateinit var filterAnketa : Spinner
    private lateinit var anketaAdapter : AnketaAdapter
    private var anketaViewModel : AnketaViewModel = AnketaViewModel()
    private var pitanjeAnketaViewModel : PitanjeAnketaViewModel = PitanjeAnketaViewModel()
    private var takeAnketaViewModel : TakeAnketaViewModel = TakeAnketaViewModel()
    private var odgovoriViewModel : OdgovorViewModel = OdgovorViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.ankete_fragment, container, false)


        listaAnketa = view.findViewById(R.id.listaAnketa)
        listaAnketa.layoutManager = GridLayoutManager(activity,2)
        anketaAdapter = AnketaAdapter(arrayListOf(), this)
        listaAnketa.adapter = anketaAdapter




        val opcijeFilterAnketa = mutableListOf("Sve moje ankete","Sve ankete","Urađene ankete","Buduće ankete","Prošle ankete")
        filterAnketa = view.findViewById(R.id.filterAnketa)
        filterAnketa.adapter = ArrayAdapter(listaAnketa.context, android.R.layout.simple_list_item_1, opcijeFilterAnketa)
        filterAnketa.setSelection(1)
        filterAnketa.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterAnketaOnResume = position // spasavamo vrijednost za onResume fju
                when (position) {
                    0 -> {
                        anketaViewModel.getMyAnkete(onSuccess = ::dobijanjeAnketa, onError = ::onError)
                    }
                    1 -> {
                        anketaViewModel.getAllSaUmnozenim(onSuccess = ::dobijanjeAnketa, onError = ::onError)
                    }
                    2 -> {
                        anketaViewModel.getDone(onSuccess = ::dobijanjeAnketa, onError = ::onError)
                    }
                    3 -> {
                        anketaViewModel.getFuture(onSuccess = ::dobijanjeAnketa, onError = ::onError)
                    }
                    4 -> {
                        anketaViewModel.getNotTaken(onSuccess = ::dobijanjeAnketa, onError = ::onError)
                    }
                }
            }
        }

        return view;
    }


    fun dobijanjeAnketa(ankete : List<Anketa>){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                if(ankete != null){
                    anketeZaOffline = ankete
                    anketaAdapter.updateAnkete(ankete)
                }
            }
        }
    }



    override fun onResume() {
        super.onResume()
        zamjenaFragmenata()
        when (filterAnketaOnResume) {
            0 -> {
                anketaViewModel.getMyAnkete(onSuccess = ::dobijanjeAnketa, onError = ::onError)
            }
            1 -> {
                anketaViewModel.getAllSaUmnozenim(onSuccess = ::dobijanjeAnketa, onError = ::onError)
            }
            2 -> {
                anketaViewModel.getDone(onSuccess = ::dobijanjeAnketa, onError = ::onError)
            }
            3 -> {
                anketaViewModel.getFuture(onSuccess = ::dobijanjeAnketa, onError = ::onError)
            }
            4 -> {
                anketaViewModel.getNotTaken(onSuccess = ::dobijanjeAnketa, onError = ::onError)
            }
        }

    }

    /*fun dobijanjeAnketaUpdateovanjeBaze(ankete : List<Anketa>){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                if(ankete != null){
                    anketaAdapter.updateAnkete(ankete)
                    anketaViewModel.getMyAnkete(onSuccess = ::updateUpisanihAnketa, onError = ::onError)
                }
            }
        }
    }

    fun updateUpisanihAnketa(ankete : List<Anketa>){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.IO){
                if(ankete != null){
                    AppDatabase.getInstance(requireContext()).upisanaAnketaDao().deleteAll()
                    for(anketa in ankete){
                        var upisana = UpisanaAnketa(anketa.id, anketa.naziv,anketa.nazivIstrazivanja, anketa.datumPocetak,anketa.datumKraj,anketa.datumRada,anketa.trajanje,anketa.nazivGrupe,anketa.progres)
                        AppDatabase.getInstance(requireContext()).upisanaAnketaDao().insertAnketa(upisana)
                    }
                }
            }
        }
    }*/

    private fun zamjenaFragmenata() {
        Handler(Looper.getMainLooper()).postDelayed({
            MainActivity.viewPagerAdapter.refreshFragment(1, FragmentIstrazivanje())
        }, 100)
    }

    override fun onItemClick(position: Int) {
        if(KorisnikRepository.isConnectedToInternet){
            KorisnikRepository.pozicijaKliknuteAnkete = position
            if (filterAnketaOnResume == 0) { // hvatanje liste anketa u zavisnosti od odabira spinnera
                anketaViewModel.getMyAnkete(onSuccess = ::dobavljanjeListeAnketa, onError = ::onError)
            } else if (filterAnketaOnResume == 1) {
                anketaViewModel.getAllSaUmnozenim(onSuccess = ::dobavljanjeListeAnketa, onError = ::onError)
            } else if (filterAnketaOnResume == 2) {
                anketaViewModel.getDone(onSuccess = ::dobavljanjeListeAnketa, onError = ::onError)
            } else if (filterAnketaOnResume == 3) {
                anketaViewModel.getFuture(onSuccess = ::dobavljanjeListeAnketa, onError = ::onError)
            } else if (filterAnketaOnResume == 4) {
                anketaViewModel.getNotTaken(onSuccess = ::dobavljanjeListeAnketa, onError = ::onError)
            }
        }else{

            //ovdje se otvara sve za pokusaj, dobijaju pitanja, kreira lista fragmenata
            anketaKliknuta = anketeZaOffline[position]
            if(daLiSeSmijeOtvoriti()){
                Log.d("offline", "uslo u otvaranje offline")
                val fragments = mutableListOf<Fragment>()

                var pitanja = AppDatabase.getInstance(FragmentAnkete.context).pitanjeBazaDao().getAll()
                var pitanja1 = mutableListOf<PitanjeBaza>()
                for(pitanje in pitanja){
                    if(pitanje.AnketumId==anketaKliknuta.id){
                        pitanja1.add(pitanje)
                    }
                }
                var svaPitanjaBaza = pitanja1.distinctBy { it.id }

                var svaPitanja = mutableListOf<Pitanje>()
                for(jednoPitanje in svaPitanjaBaza){
                    var pitanje = Pitanje(jednoPitanje.id, jednoPitanje.naziv,jednoPitanje.tekstPitanja,jednoPitanje.opcije)
                    svaPitanja.add(pitanje)
                    fragments.add(FragmentPitanje(pitanje)) // stavljanje pitanja u viewPager
                }
                fragments.add(FragmentPredaj())
                MainActivity.viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager, fragments, lifecycle)
                MainActivity.viewPager.adapter = MainActivity.viewPagerAdapter

                KorisnikRepository.pitanjaKliknuteAnkete = svaPitanja
            }else{
                Log.d("offline", "uslo u neotvaranje offline")
                KorisnikRepository.trenutnoOtvorenaAnketa = AnketaInfo(null, false,false, mutableListOf())// resetovanje ukoliko klikne i onda pokusa otici na upis, zbog ispisa poruke
            }



        }
    }

    fun dobavljanjeListeAnketa(listaAnketa : List<Anketa>){

        KorisnikRepository.trenutnoOtvorenaAnketa = AnketaInfo(null,false, false,mutableListOf()) // resetira se trenutno pokrenuta
        /*var pitanja = pitanjeAnketaViewModel.getPitanja(listaAnketa[ KorisnikRepository.pozicijaKliknuteAnkete].naziv,listaAnketa[ KorisnikRepository.pozicijaKliknuteAnkete].nazivIstrazivanja!! ) // dohvatanje pitanja za odredjenu anketu
        KorisnikRepository.trenutnoOtvorenaAnketa.anketa = listaAnketa[ KorisnikRepository.pozicijaKliknuteAnkete] // postavlja se da se zna koja se poruka treba ispisivat
        KorisnikRepository.brojPitanjaNaAnketi = pitanja.size*/
        KorisnikRepository.brojOdgovorenihPitanja = 0

        for(anketaInfo in KorisnikRepository.pokrenuteAnkete){ // ako je pronadje u anketama da prezume citav info sa odgovorima
            if(listaAnketa[ KorisnikRepository.pozicijaKliknuteAnkete].naziv == anketaInfo.anketa!!.naziv && listaAnketa[ KorisnikRepository.pozicijaKliknuteAnkete].nazivIstrazivanja == anketaInfo.anketa!!.nazivIstrazivanja){
                KorisnikRepository.trenutnoOtvorenaAnketa = AnketaInfo(anketaInfo.anketa, anketaInfo.predana,anketaInfo.zaustavljena, anketaInfo.listaOdgovora)
                KorisnikRepository.brojOdgovorenihPitanja = anketaInfo.listaOdgovora.size
                Log.d("keno", "broj odg pitanja(kad je pronadje u zapocetim) je: " + KorisnikRepository.brojOdgovorenihPitanja.toString())
            }
        }


        KorisnikRepository.idKliknuteAnekte = listaAnketa[KorisnikRepository.pozicijaKliknuteAnkete].id
        pitanjeAnketaViewModel.getPitanjaApi(listaAnketa[ KorisnikRepository.pozicijaKliknuteAnkete].id, onSuccess = ::dobavljanjePitanjaZaAnketu, onError = ::onError)

        //podesavanje za testiranje mogucnosti otvaranja
        anketaKliknuta = listaAnketa[KorisnikRepository.pozicijaKliknuteAnkete]

        //podesavanje za ispis poruke
        KorisnikRepository.upisanaGrupa = ""
        KorisnikRepository.upisanoIstrazivanje = listaAnketa[KorisnikRepository.pozicijaKliknuteAnkete].nazivIstrazivanja!!
        KorisnikRepository.upisanaAnketa = listaAnketa[KorisnikRepository.pozicijaKliknuteAnkete].naziv!!

    }

    fun dobavljanjePitanjaZaAnketu(svaPitanja : List<Pitanje>){
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main){
                //Log.d("keno","anketa: "+KorisnikRepository.trenutnoOtvorenaAnketa.anketa!!.datumRada)
                KorisnikRepository.pitanjaKliknuteAnkete = svaPitanja
                if(daLiSeSmijeOtvoriti()){
                    Log.d("keno", "uslo u otvaranje")
                    val fragments = mutableListOf<Fragment>()
                    for(jednoPitanje in svaPitanja){
                        fragments.add(FragmentPitanje(jednoPitanje)) // stavljanje pitanja u viewPager
                    }
                    fragments.add(FragmentPredaj())
                    MainActivity.viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager, fragments, lifecycle)
                    MainActivity.viewPager.adapter = MainActivity.viewPagerAdapter

                    takeAnketaViewModel.getPoceteAnkete(onSuccess = ::dobavljaPoceteAnkete, onError = ::onError)
                }else{
                    KorisnikRepository.trenutnoOtvorenaAnketa = AnketaInfo(null, false,false, mutableListOf())// resetovanje ukoliko klikne i onda pokusa otici na upis, zbog ispisa poruke
                }
            }
        }
    }

    fun dobavljaPoceteAnkete(listaPocetih : List<AnketaTaken>?){
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                var pronadjena = false
                KorisnikRepository.prviPutOtvorenaAnketa = false
                KorisnikRepository.nemoguceOdgovarati=false

                if (listaPocetih == null) {
                    Log.d("account", "napravilo zapocetu kad je null")
                    takeAnketaViewModel.zapocniAnketu(KorisnikRepository.idKliknuteAnekte, onSuccess = ::zapocniAnketu, onError = ::onError)
                    KorisnikRepository.prviPutOtvorenaAnketa = true
                }else{
                    for (anketa in listaPocetih) {
                        if (anketa.AnketumId == KorisnikRepository.idKliknuteAnekte) {
                            pronadjena = true
                            KorisnikRepository.idAnketaTakenKliknuteAnkete = anketa.id //postavljanje id pocete ankete radi slanja odgovora u pitanjeFragment
                        }
                    }
                    if (!pronadjena) {
                        Log.d("account", "napravilo zapocetu")
                        takeAnketaViewModel.zapocniAnketu(KorisnikRepository.idKliknuteAnekte, onSuccess = ::zapocniAnketu, onError = ::onError)
                        KorisnikRepository.prviPutOtvorenaAnketa = true
                    } else {
                        Log.d("account", "nije napravilo zapocetu")
                        //odgovoriViewModel.getOdgovoriAnketa(KorisnikRepository.idKliknuteAnekte, onSuccess = ::dobavljanjeOdgovoraZapocete, onError = ::onError)
                        KorisnikRepository.prviPutOtvorenaAnketa = false
                    }
                }
            }
        }
    }



    fun zapocniAnketu(at : AnketaTaken?){
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                if (at != null) {
                    Log.d("account", at.id.toString())
                    KorisnikRepository.idAnketaTakenKliknuteAnkete = at.id //postavljanje id pocete ankete radi slanja odgovora u pitanjeFragment
                }
            }
        }
    }

    fun daLiSeSmijeOtvoriti() : Boolean{
        var smijeSeOtvoriti = false
        if(KorisnikRepository.isConnectedToInternet){
            var anketa = anketaKliknuta

            val formater = SimpleDateFormat("yyyy-MM-dd")
            if((anketa.datumRada != null) || (anketa.datumRada == null && formater.parse(anketa.datumPocetak).before(Calendar.getInstance().time))){

                Log.d("keno", "zeleni plavi")
                //zelene i plave
                /*for(jednaAnketa in  KorisnikRepository.korisnik.upisaneAnkete){ // ako postoji u upisanim zbog sprjecavanja otvaranja anketa u svim anketama
                    if(jednaAnketa.naziv == anketa.naziv && jednaAnketa.nazivIstrazivanja == anketa.nazivIstrazivanja){
                        Log.d("keno", "zeleni plavi moze")
                        smijeSeOtvoriti=true
                    }
                }*/
                smijeSeOtvoriti=true

            }else{
                if (formater.parse(anketa.datumPocetak).after(Calendar.getInstance().time)) {
                    Log.d("keno", "zuti ")
                    //zute
                    /*for(jednaAnketa in  KorisnikRepository.korisnik.upisaneAnkete){ // ako postoji u upisanim zbog sprjecavanja otvaranja anketa u svim anketama
                        if(jednaAnketa.naziv == anketa.naziv && jednaAnketa.nazivIstrazivanja == anketa.nazivIstrazivanja){
                            Log.d("keno", "zuti ne moze")
                            smijeSeOtvoriti=false
                        }
                    }*/
                    smijeSeOtvoriti=false
                } else if (anketa.datumRada == null && formater.parse(anketa.datumPocetak).before(Calendar.getInstance().time) && formater.parse(anketa.datumKraj)!!.before(
                        Calendar.getInstance().time
                    )
                ) {
                    Log.d("keno", "crveni ")
                    //crvene
                    /*for(jednaAnketa in  KorisnikRepository.korisnik.upisaneAnkete){ // ako postoji u upisanim zbog sprjecavanja otvaranja anketa u svim anketama
                        if(jednaAnketa.naziv == anketa.naziv && jednaAnketa.nazivIstrazivanja == anketa.nazivIstrazivanja){
                            Log.d("keno", "crveni  moze")
                            smijeSeOtvoriti=true
                            //KorisnikRepository.nemoguceOdgovarati=true
                        }
                    }*/
                    smijeSeOtvoriti=true
                }
            }
            return smijeSeOtvoriti
        }else{
            Log.d("offline", "uslo u daLiSmijeOtvoriti")

            var poceteAnkete = AppDatabase.getInstance(FragmentAnkete.context).anketaTakenDao().getAll()
            Log.d("offline", "pocete size: "+ poceteAnkete.size.toString())
            for(poceta in poceteAnkete){
                Log.d("offline", "anketa id: "+ anketaKliknuta.id.toString())
                if(poceta.AnketumId == anketaKliknuta.id){
                    KorisnikRepository.idAnketaTakenKliknuteAnkete = poceta.id
                    smijeSeOtvoriti = true
                }
            }
            Log.d("offline", "smije li: "+ smijeSeOtvoriti)
            return smijeSeOtvoriti

        }
        return false


    }

    fun onError(){
        val toast = Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
        toast.show()
    }

}