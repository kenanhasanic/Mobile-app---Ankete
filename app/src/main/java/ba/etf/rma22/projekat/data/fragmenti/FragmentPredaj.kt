package ba.etf.rma22.projekat.data.fragmenti

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import ba.etf.rma22.projekat.MainActivity
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.models.Anketa
import ba.etf.rma22.projekat.data.models.Odgovor
import ba.etf.rma22.projekat.data.repositories.KorisnikRepository
import ba.etf.rma22.projekat.data.repositories.OdgovorRepository
import ba.etf.rma22.projekat.data.repositories.PitanjeAnketaRepository
import ba.etf.rma22.projekat.view.ViewPagerAdapter
import ba.etf.rma22.projekat.viewmodel.AnketaViewModel
import ba.etf.rma22.projekat.viewmodel.OdgovorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.RoundingMode


class FragmentPredaj : Fragment() {
    companion object {
        fun newInstance(): FragmentPredaj = FragmentPredaj()
    }
    private lateinit var poruka : TextView
    private lateinit var dugmePredaj : Button
    private var odgovorViewModel = OdgovorViewModel()
    private var anketaViewModel = AnketaViewModel()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.predaj_fragment, container, false)

        poruka = view.findViewById(R.id.progresTekst)
        var procenatAnkete = izracunajProcenat()
        poruka.text = procenatAnkete.toString()+ "%"
        dugmePredaj = view.findViewById(R.id.dugmePredaj)

        if(KorisnikRepository.isConnectedToInternet){
            odgovorViewModel.getOdgovoriAnketa(KorisnikRepository.idKliknuteAnekte, onSuccess = ::dobavljanjeOdgovoraZapocete, onError = ::onError)
        }else{
            dugmePredaj.isEnabled = false
            KorisnikRepository.prviPutOtvorenaAnketa = false
            KorisnikRepository.nemoguceOdgovarati=true
        }



        dugmePredaj.setOnClickListener(){

            if(KorisnikRepository.pitanjaKliknuteAnkete.size != KorisnikRepository.odgovoriKliknuteAnkete.size){
                anketaViewModel.predavanjeNeodgovorenih(KorisnikRepository.idKliknuteAnekte)
            }

            KorisnikRepository.prviPutOtvorenaAnketa = false
            KorisnikRepository.nemoguceOdgovarati=true

            val fragments = mutableListOf<Fragment>(
                FragmentAnkete(),
                FragmentPoruka()
            )
            MainActivity.viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager, fragments, lifecycle)
            MainActivity.viewPager.adapter = MainActivity.viewPagerAdapter
            MainActivity.viewPager.currentItem = 1
        }



        return view
    }

    fun dobavljanjeOdgovoraZapocete(odgovori : List<Odgovor>){
        GlobalScope.launch (Dispatchers.IO){
            withContext(Dispatchers.Main){
                KorisnikRepository.odgovoriKliknuteAnkete = odgovori
                poruka.text = izracunajProcenat().toString() + "%"
                if(KorisnikRepository.pitanjaKliknuteAnkete.size == KorisnikRepository.odgovoriKliknuteAnkete.size){
                    Log.d("account","Jednak broj pitanja i odg PREDAJ")
                    // ovdje dodat ako je prvi put otvorena anketa da svejedno ostavi dugme upaljeno koje nece nista uradit jer ce se svejedno opet testirat jel broj pitanja jednak pa nece prolazit u for petlji
                    //da posalje neodgovorena pitanja
                    dugmePredaj.isEnabled = KorisnikRepository.prviPutOtvorenaAnketa
                }
            }
        }
    }

    override fun onResume() {
        Log.d("keno", "uslo u on resume")
        super.onResume()
        if(KorisnikRepository.isConnectedToInternet){
            odgovorViewModel.getOdgovoriAnketa(KorisnikRepository.idKliknuteAnekte, onSuccess = ::dobavljanjeOdgovoraZapocete, onError = ::onError)
        }
    }

    fun izracunajProcenat() : Int{

        var progres : Float= ((KorisnikRepository.odgovoriKliknuteAnkete.size)/KorisnikRepository.pitanjaKliknuteAnkete.size.toFloat())

        Log.d("account", "progres u predaj: "+progres.toString())
        var rounded = (progres*10).toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble()
        var finalProgress = rounded.toInt()
        if(rounded.toInt() % 2 != 0){
            finalProgress += 1
        }
        finalProgress*=10;
        return finalProgress
    }

    fun onError(){
        val toast = Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
        toast.show()
    }
}