package ba.etf.rma22.projekat.data.fragmenti

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import ba.etf.rma22.projekat.MainActivity
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.AppDatabase
import ba.etf.rma22.projekat.data.models.Odgovor
import ba.etf.rma22.projekat.data.models.Pitanje
import ba.etf.rma22.projekat.data.models.PitanjeNovo
import ba.etf.rma22.projekat.data.repositories.KorisnikRepository
import ba.etf.rma22.projekat.view.ViewPagerAdapter
import ba.etf.rma22.projekat.viewmodel.OdgovorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.RoundingMode


class FragmentPitanje(pitanje: Pitanje) : Fragment() {
    companion object {
        fun newInstance(pitanje: Pitanje): FragmentPitanje = FragmentPitanje(pitanje)
    }

    private var pitanje = pitanje
    private lateinit var tekst: TextView
    private lateinit var listaOdgovora: ListView
    private lateinit var dugmeZaustavi : Button
    private var odgovorViewModel : OdgovorViewModel = OdgovorViewModel()

    private lateinit var poruka: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.pitanje_fragment, container, false)
        dugmeZaustavi = view.findViewById(R.id.dugmeZaustavi)
        tekst = view.findViewById(R.id.tekstPitanja)
        tekst.text = pitanje.tekstPitanja
        listaOdgovora = view.findViewById(R.id.odgovoriLista)
        listaOdgovora.adapter =
            ArrayAdapter(inflater.context, android.R.layout.simple_list_item_1, pitanje.opcije.split(","))


        if(KorisnikRepository.isConnectedToInternet){
            odgovorViewModel.getOdgovoriAnketa(KorisnikRepository.idKliknuteAnekte, onSuccess = ::dobavljanjeOdgovoraZapocete, onError = ::onError)
        }else{

            var odgovori = mutableListOf<Odgovor>()
            var odgovorUBazi = AppDatabase.getInstance(FragmentAnkete.context).odgovorDao().getAll()
            for(odgovor in odgovorUBazi){
                if(odgovor.PitanjeId == pitanje.id && odgovor.AnketaTakenId == KorisnikRepository.idAnketaTakenKliknuteAnkete){
                    odgovori.add(odgovor)
                }
            }
            KorisnikRepository.odgovoriKliknuteAnkete = odgovori //set-a odgovore lokalno
            for(jedanOdgovor in odgovori){ //trazi da li ima odgovora za pitanje koje je otvoreno
                if(jedanOdgovor.odgovoreno != 0){
                    listaOdgovora.post{ (listaOdgovora[jedanOdgovor.odgovoreno-1] as TextView).setTextColor(Color.parseColor("#0000FF")) }
                    listaOdgovora.isEnabled = false;
                }else{
                    Log.d("account", "odgovoreno 0")
                }
            }
            listaOdgovora.isEnabled = false
            dugmeZaustavi.isEnabled = false

            KorisnikRepository.nemoguceOdgovarati=true
        }



        /*if(isPredanaAnketa() || KorisnikRepository.nemoguceOdgovarati){ // provejra da li je anketa vec predana, ako jeste ne smije se vise odgovarati
            listaOdgovora.isEnabled = false;
            dugmeZaustavi.isEnabled = false
        }*/



        listaOdgovora.setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id ->
            KorisnikRepository.brojOdgovorenihPitanja += 1
            KorisnikRepository.trenutnoOtvorenaAnketa.listaOdgovora.add(Pair(pitanje.naziv, position+1))
            (view as TextView).setTextColor(Color.parseColor("#0000FF"))

            odgovorViewModel.postaviOdgovorAnketa(KorisnikRepository.idAnketaTakenKliknuteAnkete, pitanje.id, position+1, onSuccess = ::postavljanjeOdgovora, onError = ::onError)
            listaOdgovora.isEnabled = false;
        }

        dugmeZaustavi.setOnClickListener(){

            val fragments = mutableListOf<Fragment>(
                FragmentAnkete(),
                FragmentIstrazivanje()
            )
            MainActivity.viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager, fragments, lifecycle)
            MainActivity.viewPager.adapter = MainActivity.viewPagerAdapter
            MainActivity.viewPager.currentItem = 0
        }
        return view
    }
    fun dobavljanjeOdgovoraZapocete(odgovori : List<Odgovor>){
        GlobalScope.launch (Dispatchers.IO){
            withContext(Dispatchers.Main){
                KorisnikRepository.odgovoriKliknuteAnkete = odgovori
                for(jedanOdgovor in odgovori){ //trazi da li ima odgovora za pitanje koje je otvoreno
                    if(jedanOdgovor.PitanjeId == pitanje.id){
                        if(jedanOdgovor.odgovoreno != 0){
                            listaOdgovora.post{ (listaOdgovora[jedanOdgovor.odgovoreno-1] as TextView).setTextColor(Color.parseColor("#0000FF")) }
                            listaOdgovora.isEnabled = false;
                        }else{
                            Log.d("account", "odgovoreno 0")
                        }
                    }
                }

                if(KorisnikRepository.pitanjaKliknuteAnkete.size == KorisnikRepository.odgovoriKliknuteAnkete.size){
                    Log.d("account","uslo u gledanje broja p[itanja i odg")
                    listaOdgovora.isEnabled = false
                    dugmeZaustavi.isEnabled = false
                    KorisnikRepository.nemoguceOdgovarati=true

                }else
                    KorisnikRepository.prviPutOtvorenaAnketa = true
            }
        }
    }

    fun postavljanjeOdgovora(progres : Int?){
        Log.d("account", "PROGRES: "+progres.toString())
    }

    fun onError(){
        val toast = Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
        toast.show()
    }
}