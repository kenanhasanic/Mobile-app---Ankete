package ba.etf.rma22.projekat.data.fragmenti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import ba.etf.rma22.projekat.MainActivity
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.models.Grupa
import ba.etf.rma22.projekat.data.models.Istrazivanje
import ba.etf.rma22.projekat.data.repositories.KorisnikRepository
import ba.etf.rma22.projekat.viewmodel.AnketaViewModel
import ba.etf.rma22.projekat.viewmodel.GrupaViewModel
import ba.etf.rma22.projekat.viewmodel.IstrazivanjeIGrupaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentIstrazivanje : Fragment(){

    companion object{
        fun newInstance() : FragmentIstrazivanje = FragmentIstrazivanje()
    }

    private lateinit var spinnerGodina: Spinner
    private lateinit var spinnerIstrazivanje: Spinner
    private lateinit var spinnerGrupa: Spinner
    private lateinit var dodajIstrazivanjeDugme: Button

    private var istrazivanjeigrupaViewModel = IstrazivanjeIGrupaViewModel()

    private lateinit var odabirGodine: String
    private lateinit var odabirIstrazivanja: String
    private lateinit var odabirGrupe: String

    var listaIstrazivanjaZaSpinner = mutableListOf<String>() // kreiranje liste za drugi spinner
    var listaGrupaZaSpinner = mutableListOf<String>() // kreiranje liste za treci spinner

    private lateinit var svaIstrazivanja : List<Istrazivanje>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.istrazivanja_fragment, container, false)

        spinnerGodina = view.findViewById(R.id.odabirGodina)
        spinnerIstrazivanje = view.findViewById(R.id.odabirIstrazivanja)
        spinnerGrupa = view.findViewById(R.id.odabirGrupa)
        dodajIstrazivanjeDugme = view.findViewById(R.id.dodajIstrazivanjeDugme)

        var listaGodinaZaSpinner = mutableListOf(
            "Odabir godine",
            "1",
            "2",
            "3",
            "4",
            "5"
        ) //kreiranje liste za prvi spinner
        spinnerGodina.adapter =
            ArrayAdapter(inflater.context, android.R.layout.simple_list_item_1, listaGodinaZaSpinner)

        if(!KorisnikRepository.isConnectedToInternet){
            spinnerGodina.setSelection(0)

            listaIstrazivanjaZaSpinner = mutableListOf<String>()
            listaIstrazivanjaZaSpinner.add("Odabir istrazivanja") // dodavanje nultog odabira
            spinnerIstrazivanje.adapter = ArrayAdapter(inflater.context, android.R.layout.simple_list_item_1, listaIstrazivanjaZaSpinner)
            spinnerIstrazivanje.setSelection(0)

            listaGrupaZaSpinner = mutableListOf<String>()
            listaGrupaZaSpinner.add("Odabir grupe") // dodavanje nultog odabira
            spinnerGrupa.adapter = ArrayAdapter(spinnerGodina.context, android.R.layout.simple_list_item_1, listaGrupaZaSpinner)
            spinnerGrupa.setSelection(0)

            spinnerGodina.isEnabled = false
            spinnerIstrazivanje.isEnabled = false
            spinnerGrupa.isEnabled = false
            dodajIstrazivanjeDugme.isEnabled = false

        }else {
            spinnerGodina.setSelection(KorisnikRepository.spasenOdabirGodine)
            spinnerGodina.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    p3: Long
                ) {
                    odabirGodine =
                        listaGodinaZaSpinner[position] // spasavanje trenutno odabrane godine
                    if (listaGodinaZaSpinner[position] != "Odabir godine")
                        KorisnikRepository.spasenOdabirGodine =
                            listaGodinaZaSpinner[position].toInt() // spasavanje godine za ponovni ulazak u aktivnost
                    else
                        KorisnikRepository.spasenOdabirGodine = 0
                    listaIstrazivanjaZaSpinner = mutableListOf<String>()
                    listaIstrazivanjaZaSpinner.add("Odabir istrazivanja") // dodavanje nultog odabira
                    spinnerIstrazivanje.adapter = ArrayAdapter(
                        inflater.context,
                        android.R.layout.simple_list_item_1,
                        listaIstrazivanjaZaSpinner
                    )
                    //omogucavanje spinnera na osnovu odabira
                    if (odabirGodine != "Odabir godine") {
                        spinnerIstrazivanje.isEnabled = true
                        istrazivanjeigrupaViewModel.getIstrazivanja(
                            onSuccess = ::dobavljanjeIstrazivanja,
                            onError = ::onError
                        )
                    } else {
                        spinnerIstrazivanje.isEnabled = false
                    }
                }
            }
        }
        return view;
    }

    /*fun onSuccess(istrazivanja: List<Istrazivanje>?){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                neupisaniPredmeti = predmeti
                popuniNeupisanePredmete(neupisaniPredmeti)
            }
        }
    }

    fun onSuccess2(grupeNove: List<Grupa>){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                popuniGrupe(grupeNove)
            }
        }
    }*/

    fun dobavljanjeIstrazivanja(listaIstrazivanja : List<Istrazivanje>?) {
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){


                istrazivanjeigrupaViewModel.getUpisanaIstrazivanjaId(onSuccess = ::dobavljanjeUpisanihIstrazivanja, onError = ::onError)
                if (listaIstrazivanja != null) {
                    svaIstrazivanja = listaIstrazivanja
                }


            }
        }

    }

    fun dobavljanjeUpisanihIstrazivanja(listaIdIstrazivanja : List<Int>?){
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {

                var listaSaIzbacenimUpisanim = mutableListOf<Istrazivanje>()
                if (listaIdIstrazivanja != null) {
                    for(istrazivanje in svaIstrazivanja){
                        var pronadjen = false
                        for(istrazivanjeId in listaIdIstrazivanja){
                            if(istrazivanjeId == istrazivanje.id){
                                pronadjen = true
                            }
                        }
                        if(!pronadjen){
                            listaSaIzbacenimUpisanim.add(istrazivanje)
                        }
                    }
                }
                var istrazivanjaPoGodini = mutableListOf<Istrazivanje>()
                //naci istrazivanja za godinu
                if (listaSaIzbacenimUpisanim != null) {
                    for (istrazivanje in listaSaIzbacenimUpisanim) {
                        if (istrazivanje.godina == KorisnikRepository.spasenOdabirGodine) {
                            istrazivanjaPoGodini.add(istrazivanje)
                        }
                    }

                    for (jednoIstrazivanje in istrazivanjaPoGodini) {
                        listaIstrazivanjaZaSpinner.add(jednoIstrazivanje.naziv)
                    }
                    spinnerIstrazivanje.adapter = ArrayAdapter(
                        spinnerGodina.context,
                        android.R.layout.simple_list_item_1,
                        listaIstrazivanjaZaSpinner
                    )

                    spinnerIstrazivanje.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {}
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                position: Int,
                                p3: Long
                            ) {
                                odabirIstrazivanja =
                                    listaIstrazivanjaZaSpinner[position] // spasavanje trenutno odabranog istrazivanja
                                listaGrupaZaSpinner = mutableListOf<String>()
                                listaGrupaZaSpinner.add("Odabir grupe") // dodavanje nultog odabira
                                spinnerGrupa.adapter = ArrayAdapter(
                                    spinnerGodina.context,
                                    android.R.layout.simple_list_item_1,
                                    listaGrupaZaSpinner
                                )
                                //omogucavanje spinnera na osnovu odabira
                                if (odabirIstrazivanja != "Odabir istrazivanja") {
                                    spinnerGrupa.isEnabled = true
                                    var idIstrazivanjaKliknutog = 0
                                    for (istrazivanje in listaSaIzbacenimUpisanim) {
                                        if (istrazivanje.naziv == listaIstrazivanjaZaSpinner[position]) {
                                            idIstrazivanjaKliknutog = istrazivanje.id
                                        }
                                    }
                                    istrazivanjeigrupaViewModel.getGrupeZaIstrazivanje(
                                        idIstrazivanjaKliknutog,
                                        onSuccess = ::dobavljanjeGrupaPoIstrazivanju,
                                        onError = ::onError
                                    )
                                } else {
                                    spinnerGrupa.isEnabled = false
                                }
                            }
                        }
                } else {
                    val toast =
                        Toast.makeText(
                            context,
                            "Error nisu dobavljena istrazivanja",
                            Toast.LENGTH_SHORT
                        )
                    toast.show()
                }
            }
        }
    }

    fun dobavljanjeGrupaPoIstrazivanju(sveGrupe : List<Grupa>){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main) {
                for (jednaGrupa in sveGrupe) {
                    listaGrupaZaSpinner.add(jednaGrupa.naziv)
                }
                spinnerGrupa.adapter = ArrayAdapter(
                    spinnerGodina.context,
                    android.R.layout.simple_list_item_1,
                    listaGrupaZaSpinner
                )

                spinnerGrupa.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(p0: AdapterView<*>?) {}
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            position: Int,
                            p3: Long
                        ) {

                            if (listaGrupaZaSpinner[position] == "Odabir grupe") {
                                dodajIstrazivanjeDugme.isEnabled = false
                            } else {
                                dodajIstrazivanjeDugme.isEnabled = true
                                odabirGrupe = listaGrupaZaSpinner[position] // spasavanje trenutno odabrane grupe

                                var idIzabraneGrupe = 0
                                for(grupa in sveGrupe){
                                    if(grupa.naziv == listaGrupaZaSpinner[position]){
                                        idIzabraneGrupe = grupa.id
                                    }
                                }
                                dodajIstrazivanjeDugme.setOnClickListener() {
                                    upisiAnketu(
                                        odabirGodine,
                                        odabirIstrazivanja,
                                        odabirGrupe,
                                        idIzabraneGrupe
                                    ) // poziv fje koja upisuje u varijablu korisnika trenutno prijavljenu anketu
                                    KorisnikRepository.upisanaGrupa = listaGrupaZaSpinner[position]
                                }
                            }

                        }
                    }
            }
        }
    }

    fun onError(){
        val toast = Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun upisiAnketu(odabirGodine: String, odabirIstrazivanja: String, odabirGrupe: String, idOdabraneGrupe : Int) {

        istrazivanjeigrupaViewModel.upisiUGrupu(idOdabraneGrupe, onSuccess = ::dodanaGrupa, onError = ::onError)

    }

    fun dodanaGrupa(isDodano : Boolean?){
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {



                KorisnikRepository.upisanaAnketa = ""
                KorisnikRepository.upisanaGrupa = odabirGrupe
                KorisnikRepository.upisanoIstrazivanje = odabirIstrazivanja

                MainActivity.viewPagerAdapter.refreshFragment(1, FragmentPoruka())
            }
        }
    }
}