package ba.etf.rma22.projekat.data.repositories

import android.util.Log
import ba.etf.rma22.projekat.data.fragmenti.FragmentAnkete
import ba.etf.rma22.projekat.data.fragmenti.FragmentIstrazivanje
import ba.etf.rma22.projekat.data.models.*
import java.util.*

object KorisnikRepository {



    //vezano za pitanje
    var brojOdgovorenihPitanja = 0
    var brojPitanjaNaAnketi = 0
    var trenutnoOtvorenaAnketa : AnketaInfo = AnketaInfo(null, false,false, mutableListOf())
    var pokrenuteAnkete = mutableListOf<AnketaInfo>()
    var nemoguceOdgovarati = false

    //vezano za spinere u intrazivanju
    var spasenOdabirGodine = 0

    //porukaFragment
    lateinit var upisanaAnketa : String
    lateinit var upisanaGrupa : String
    lateinit var upisanoIstrazivanje : String


    //api
    var pozicijaKliknuteAnkete = -111
    var idKliknuteAnekte = -1
    var prviPutOtvorenaAnketa = false


    //povuceni odgovori odredjene ankete
    var odgovoriKliknuteAnkete : List<Odgovor> = mutableListOf()
    var pitanjaKliknuteAnkete : List<Pitanje> = mutableListOf()

    //anketa taken id kliknutog pokusaja
    var idAnketaTakenKliknuteAnkete : Int = 0


    //baza
    var isConnectedToInternet: Boolean = true


    private fun datum(dan : Int, mjesec : Int, godina : Int) : Date {
        return Calendar.getInstance().run{
            set(godina, mjesec-1, dan)
            time
        }
    }

}