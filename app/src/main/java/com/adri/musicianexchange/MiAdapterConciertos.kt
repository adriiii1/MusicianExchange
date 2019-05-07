package com.adri.musicianexchange

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

class MiAdapterConciertos(private val listConciertos: ArrayList<Concierto>) :

    RecyclerView.Adapter<MiAdapterConciertos.ConciertosViewHolder>() {

    override fun getItemCount(): Int {
        return listConciertos.size
    }

    override fun onBindViewHolder(p0: ConciertosViewHolder, p1: Int) {
        p0.grupo.text = listConciertos[p1].grupo.replace("%&%"," ")
        p0.lugar.text = listConciertos[p1].lugar.replace("%&%"," ")
        p0.fecha.text = listConciertos[p1].fecha
        p0.precio.text = listConciertos[p1].precio.replace("%&%"," ")
        p0.precio.text = p0.precio.text.toString() + "€"
        var url=listConciertos[p1].foto
        url=url.replace("igual","=").replace("barra","/").replace("points",":")
        Glide.with(p0.cv.context).load(url).into(p0.fotoConcierto)
        p0.cv.setOnClickListener {

        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ConciertosViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.conciertoview, p0, false)
        return ConciertosViewHolder(v)
    }

    class ConciertosViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var cv: CardView = itemView.findViewById(R.id.cv)
        internal var grupo: TextView=itemView.findViewById(R.id.grupo)
        internal var lugar: TextView= itemView.findViewById(R.id.lugarConcierto)
        internal var fecha: TextView= itemView.findViewById(R.id.fecha)
        internal var precio: TextView= itemView.findViewById(R.id.precio)
        internal var fotoConcierto: ImageView = itemView.findViewById(R.id.fotoConcierto)
    }
}