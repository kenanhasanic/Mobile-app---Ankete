package ba.etf.rma22.projekat.data.repositories

import android.content.Context
import ba.etf.rma22.projekat.data.AppDatabase
import ba.etf.rma22.projekat.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

class DataBaseRepository {
    companion object{
        private lateinit var context: Context
        private val formater = SimpleDateFormat("yyyy-MM-dd")

        fun setContext(_context: Context){
            context =_context
        }

        suspend fun ocistiBazu() : String {
            return withContext(Dispatchers.IO) {
                AppDatabase.getInstance(context).anketaDao().deleteAll()
                AppDatabase.getInstance(context).istrazivanjeDao().deleteAll()
                AppDatabase.getInstance(context).grupaDao().deleteAll()
                AppDatabase.getInstance(context).pitanjeDao().deleteAll()
                AppDatabase.getInstance(context).odgovorDao().deleteAll()
                AppDatabase.getInstance(context).accountDao().deleteAll()
                AppDatabase.getInstance(context).anketaTakenDao().deleteAll()
                AppDatabase.getInstance(context).upisanaAnketaDao().deleteAll()
                return@withContext "ociscena baza"
            }
        }

        /*suspend fun updateDataBase() : String{
            return withContext(Dispatchers.IO){
                AppDatabase.getInstance(context).anketaDao().deleteAll()
                AppDatabase.getInstance(context).istrazivanjeDao().deleteAll()
                AppDatabase.getInstance(context).grupaDao().deleteAll()
                AppDatabase.getInstance(context).pitanjeDao().deleteAll()
                AppDatabase.getInstance(context).odgovorDao().deleteAll()
                AppDatabase.getInstance(context).accountDao().deleteAll()
                AppDatabase.getInstance(context).anketaTakenDao().deleteAll()
                AppDatabase.getInstance(context).upisanaAnketaDao().deleteAll()

                //upisivanje anketa u bazu
                var ankete: List<Anketa> = AnketaRepository.getAll()
                for(anketa in ankete){
                    AppDatabase.getInstance(context).anketaDao().insertAnketa(anketa)
                }

                //zakomentarisano upisana istrazivanja
                *//*var istrazivanja = mutableListOf<Istrazivanje>()
                var upisanaIstrazivanjaID = IstrazivanjeIGrupaRepository.getUpisanaIstrazivanjaId()
                var svaIstrazivanja = IstrazivanjeIGrupaRepository.getIstrazivanja()
                for(upisano in upisanaIstrazivanjaID){
                    for(istrazivanje in svaIstrazivanja){
                        if(upisano == istrazivanje.id){
                            istrazivanja.add(istrazivanje)
                        }
                    }
                }
                for(istrazivanje in istrazivanja){
                    AppDatabase.getInstance(context).istrazivanjeDao().insertIstrazivanje(istrazivanje)
                }*//*

                //upisivanje istrazivanja u bazu
                var svaIstrazivanja = IstrazivanjeIGrupaRepository.getIstrazivanja()
                for(istrazivanje in svaIstrazivanja){
                    AppDatabase.getInstance(context).istrazivanjeDao().insertIstrazivanje(istrazivanje)
                }

                //zakomentarisano upisane grupe
                *//*var grupe = IstrazivanjeIGrupaRepository.getUpisaneGrupe()
                if(grupe != null){
                    for(grupa in grupe){
                        AppDatabase.getInstance(context).grupaDao().insertGrupa(grupa)
                    }
                }*//*

                //upisivanje grupa u bazu
                var sveGrupe = IstrazivanjeIGrupaRepository.getGrupe()
                for(grupa in sveGrupe){
                    AppDatabase.getInstance(context).grupaDao().insertGrupa(grupa)
                }


                //upisivanje svih pitanja u bazu
                var svaPitanja = mutableListOf<Pitanje>()
                var sveAnkete = AnketaRepository.getAll()
                for(anketa in sveAnkete){
                    var pitanjaAnketa = PitanjeAnketaRepository.getPitanja(anketa.id)
                    for(pitanje in pitanjaAnketa){
                        svaPitanja.add(pitanje)
                    }
                }
                var razvrstanaPitanja = svaPitanja.distinctBy {it.id }
                for(pitanje in razvrstanaPitanja){
                    AppDatabase.getInstance(context).pitanjeDao().insertPitanje(pitanje)
                }

                //upisivanje svih odgovora u bazu
                var odgovori = mutableListOf<Odgovor>()
                var poceteAnkete = TakeAnketaRepository.getPoceteAnkete()
                if (poceteAnkete != null) {
                    for(pocetaAnketa in poceteAnkete){
                        var odgovoriPoceteAnkete = OdgovorRepository.getOdgovoriAnketa(pocetaAnketa.AnketumId)
                        for(odgovor in odgovoriPoceteAnkete){
                            odgovori.add(odgovor)
                        }
                    }
                }
                var brojac = 1
                for(odgovor in odgovori){
                    var ispravljenID = Odgovor(brojac, odgovor.odgovoreno, odgovor.AnketaTakenId,odgovor.PitanjeId)
                    AppDatabase.getInstance(context).odgovorDao().insertOdgovor(ispravljenID)
                    brojac++
                }

                //upisivanje svih pocetih Anketa

                if (poceteAnkete != null) {
                    for(pocetaAnketa in poceteAnkete){
                        var ispravljena = AnketaTakenNovo(pocetaAnketa.id, pocetaAnketa.student,pocetaAnketa.progres,formater.format(pocetaAnketa.datumRada),pocetaAnketa.AnketumId)
                        AppDatabase.getInstance(context).anketaTakenDao().insertAnketaTaken(ispravljena)
                    }
                }

                //upisivanje accounta u bazu
                var account = AccountRepository.getAccount(AccountRepository.getHash())
                AppDatabase.getInstance(context).accountDao().insertAccount(account)

                //upisivanje anketa dostupnik korisniku u bazu
                var dostupneAnkete = AnketaRepository.getMyAnkete()
                for(anketa in dostupneAnkete){
                    var upisana = UpisanaAnketa(anketa.id, anketa.naziv,anketa.nazivIstrazivanja, anketa.datumPocetak,anketa.datumKraj,anketa.datumRada,anketa.trajanje,anketa.nazivGrupe,anketa.progres)
                    AppDatabase.getInstance(context).upisanaAnketaDao().insertAnketa(upisana)
                }


                return@withContext "Uspjesno update-ovana Baza podataka!"
            }
        }*/
    }
}