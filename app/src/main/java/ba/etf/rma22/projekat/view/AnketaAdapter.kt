package ba.etf.rma22.projekat.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma22.projekat.R
import ba.etf.rma22.projekat.data.models.Anketa
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class AnketaAdapter (private var listaAnketa : List<Anketa>, private val listener: OnItemClickListener) : RecyclerView.Adapter<AnketaAdapter.AnketaViewHolder>(){

    override fun getItemCount(): Int = listaAnketa.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnketaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_anketa, parent, false)
        return AnketaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnketaViewHolder, position: Int) {
        val formater = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val datumFormat = SimpleDateFormat("yyyy-MM-dd")
        holder.nazivAnkete.text = listaAnketa[position].naziv
        holder.nazivIstrazivanja.text = listaAnketa[position].nazivIstrazivanja
        holder.progresZavrsetka.min = 0
        holder.progresZavrsetka.max = 100
        var rounded = 0
        if(listaAnketa[position].progres != null){
            rounded = dajProgresZavrsetka(listaAnketa[position].progres!!)
        }

        if(listaAnketa[position].datumRada!=null){ //plave ankete

            holder.oznaka.setImageResource(R.drawable.plava)
            holder.datumAnkete.text = "Anketa urađena: "+(listaAnketa[position].datumRada);
            holder.progresZavrsetka.setProgress(rounded)

        }else if(datumFormat.parse(listaAnketa[position].datumPocetak).before(Calendar.getInstance().time) && listaAnketa[position].datumRada == null){ // zelene

            holder.oznaka.setImageResource(R.drawable.zelena)
           // holder.datumAnkete.text = "Vrijeme zatvaranja: "+formatter.format(listaAnketa[position].datumKraj); // treba vidjet kako ce se mijenjat get u anketaRepo
            holder.datumAnkete.text = "Vrijeme zatvaranja: ne postoji jos"
            holder.progresZavrsetka.setProgress(rounded)

        }else if(datumFormat.parse(listaAnketa[position].datumPocetak).after(Calendar.getInstance().time) ){ // zute ankete

            holder.oznaka.setImageResource(R.drawable.zuta)
            holder.datumAnkete.text = "Vrijeme aktiviranja: "+listaAnketa[position].datumPocetak;

        }else if(formater.parse(listaAnketa[position].datumKraj)!!.before(Calendar.getInstance().time) && listaAnketa[position].datumRada == null){ //crvene ankete

            holder.oznaka.setImageResource(R.drawable.crvena)
            holder.datumAnkete.text = "Anketa zatvorena: "+listaAnketa[position].datumKraj;

        }


    }

    fun updateAnkete(listaAnketa: List<Anketa>) {
        this.listaAnketa = listaAnketa
        notifyDataSetChanged()
    }

    inner class AnketaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val nazivAnkete: TextView = itemView.findViewById(R.id.nazivAnkete)
        val nazivIstrazivanja: TextView = itemView.findViewById(R.id.nazivIstrazivanja)
        val oznaka: ImageView = itemView.findViewById(R.id.oznaka)
        val progresZavrsetka: ProgressBar = itemView.findViewById(R.id.progresZavrsetka);
        val datumAnkete: TextView = itemView.findViewById(R.id.datumAnkete);

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private fun dajProgresZavrsetka(progres : Float) : Int{
        var rounded = (progres).toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble()
        var finalProgress = rounded.toInt()
        if(rounded.toInt() % 2 != 0){
            finalProgress += 1
        }
        return finalProgress;
    }
}