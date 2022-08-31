package ba.etf.rma22.projekat.data.fragmenti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.models.AnketaInfo
import ba.etf.rma22.projekat.data.repositories.KorisnikRepository
import ba.etf.rma22.projekat.data.repositories.KorisnikRepository.upisanaGrupa
import ba.etf.rma22.projekat.data.repositories.KorisnikRepository.trenutnoOtvorenaAnketa
import ba.etf.rma22.projekat.data.repositories.KorisnikRepository.upisanaAnketa
import ba.etf.rma22.projekat.data.repositories.KorisnikRepository.upisanoIstrazivanje


class FragmentPoruka : Fragment() {
    companion object {
        fun newInstance(): FragmentPoruka = FragmentPoruka()
    }
    private lateinit var poruka : TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.poruka_fragment, container, false)
        poruka = view.findViewById(R.id.tvPoruka)
        var prikaz : String = ""

        if(upisanaGrupa != ""){
            prikaz = "Uspješno ste upisani u grupu " + upisanaGrupa + " istraživanja " + upisanoIstrazivanje+"!"
        }else if(upisanaAnketa != ""){
            prikaz = "Završili ste anketu " + upisanaAnketa +" u okviru istraživanja "+ upisanoIstrazivanje+"."
        }
        KorisnikRepository.trenutnoOtvorenaAnketa = AnketaInfo(null,false, false,mutableListOf()) // resetira se trenutno pokrenuta

        poruka = view.findViewById(R.id.tvPoruka)
        poruka.text = prikaz
        return view
    }
}