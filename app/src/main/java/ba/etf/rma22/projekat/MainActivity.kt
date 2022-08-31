package ba.etf.rma22.projekat

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import ba.etf.rma22.projekat.data.AppDatabase
import ba.etf.rma22.projekat.data.fragmenti.FragmentAnkete
import ba.etf.rma22.projekat.data.fragmenti.FragmentIstrazivanje
import ba.etf.rma22.projekat.data.fragmenti.FragmentPoruka
import ba.etf.rma22.projekat.data.models.*
import ba.etf.rma22.projekat.data.repositories.*
import ba.etf.rma22.projekat.view.ViewPagerAdapter
import ba.etf.rma22.projekat.viewmodel.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var viewPager: ViewPager2
        lateinit var viewPagerAdapter: ViewPagerAdapter
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AnketaRepository.setContext(this@MainActivity)
        IstrazivanjeIGrupaRepository.setContext(this@MainActivity)
        GrupaRepository.setContext(this@MainActivity)
        IstrazivanjeIGrupaRepository.setContext(this@MainActivity)
        OdgovorRepository.setContext(this@MainActivity)
        PitanjeAnketaRepository.setContext(this@MainActivity)
        AccountRepository.setContext(this@MainActivity)
        DataBaseRepository.setContext(this@MainActivity)
        TakeAnketaRepository.setContext(this@MainActivity)
        FragmentAnkete.context = this@MainActivity
        //var databaseViewModel = DataBaseViewModel()
        //databaseViewModel.ocistiBazu(onSuccess = ::ciscenjeBaze, onError = ::onError)
        var accountViewModel = AccountViewModel()
        var data: Uri? = intent?.data
        if(data!=null){
            val stringExtra = intent.getStringExtra("payload")
            if(stringExtra!=null) {
                accountViewModel.postaviHash(stringExtra, onSuccess = ::postavljanjeHasha, onError = ::onError)
            }
            else {
                var lista  = data.path?.split("/")
                accountViewModel.postaviHash(lista!![lista.size - 1], onSuccess = ::postavljanjeHasha, onError = ::onError)
            }
        }else{
            accountViewModel.postaviHash(accountViewModel.getHash(), onSuccess = ::postavljanjeHasha, onError = ::onError)
        }
        viewPager = findViewById(R.id.pager)
        val fragments =
            mutableListOf<Fragment>(
                FragmentAnkete(),
                FragmentIstrazivanje()
            )

        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, fragments, lifecycle)
        viewPager.adapter = viewPagerAdapter
        viewPager.offscreenPageLimit = 15



        var pitanja = AppDatabase.getInstance(this@MainActivity).pitanjeBazaDao().getAll()
        var pitanja1 = mutableListOf<PitanjeBaza>()
        for(pitanje in pitanja){
            if(pitanje.AnketumId==5){
                pitanja1.add(pitanje)
            }
        }
        var pitanja2 = pitanja1.distinctBy { it.id }

        KorisnikRepository.isConnectedToInternet = isOnline(this@MainActivity)
        /*if(KorisnikRepository.isConnectedToInternet){
            var vm = DataBaseViewModel()
            vm.updateDataBase(onSuccess = ::onSuccess, onError = ::onError)
        }*/

    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    fun ciscenjeBaze(string : String?){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                if (string != null) {
                    Log.d("baza", string)
                }
            }
        }
    }

    fun postavljanjeHasha(postavilo : Boolean){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                Log.d("baza", "postavilo"+postavilo.toString())
            }
        }
    }


    fun onSuccess(acc : String?){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                /*for(odg in acc!!)
                    Log.d("account", odg.naziv+" "+odg.nazivIstrazivanja)*/
                Log.d("baza", acc.toString())
            }
        }

    }
    fun onError(){
        Log.d("apii", "error")
    }


    override fun onBackPressed() {

        if(KorisnikRepository.nemoguceOdgovarati){
            KorisnikRepository.trenutnoOtvorenaAnketa = AnketaInfo(null,false, false,mutableListOf()) // resetira se trenutno pokrenuta
            Log.d("asd", "uslo")
            val fragments =
                mutableListOf(
                    FragmentAnkete(),
                    FragmentIstrazivanje()
                )

            viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, fragments, lifecycle)
            viewPager.adapter = viewPagerAdapter
        }else if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

     fun refreshFragment() {
            viewPagerAdapter.refreshFragment(1, FragmentPoruka())
     }

}