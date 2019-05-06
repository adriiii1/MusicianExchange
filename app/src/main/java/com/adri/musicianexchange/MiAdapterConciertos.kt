package com.adri.musicianexchange

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView

class MiAdapterConciertos(private val listConciertos: ArrayList<Concierto>) :

    RecyclerView.Adapter<MiAdapterConciertos.ConciertosViewHolder>() {

    override fun getItemCount(): Int {
        return listConciertos.size
    }

    override fun onBindViewHolder(p0: ConciertosViewHolder, p1: Int) {
        p0.grupo.text = listConciertos[p1].grupo.nombreGrupo.replace("%&%"," ")
        p0.lugar.text = listConciertos[p1].lugar.replace("%&%"," ")
        p0.fecha.text = listConciertos[p1].fecha.toString()
        p0.precio.text = listConciertos[p1].precio.toString()
        p0.fotoConcierto.setImageURI(Uri.parse(listConciertos[p1].foto))
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