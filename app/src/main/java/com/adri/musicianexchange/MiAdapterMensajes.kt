package com.adri.musicianexchange

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.CardView
import android.view.View

class MiAdapterMensajes(private val listMensajes: ArrayList<Mensaje>) :

    RecyclerView.Adapter<MiAdapterMensajes.MensajesViewHolder>() {

    override fun getItemCount(): Int {
        return listMensajes.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: MensajesViewHolder, p1: Int) {
        p0.txtUsuarioMensaje.text = listMensajes[p1].emisor
        p0.txtCuerpoMensaje.text = listMensajes[p1].cuerpo.replace("%&%"," ")
            .replace("igual","=").replace("barra","/").replace("points",":")
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MensajesViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.mensajeview1, p0, false)
        return MensajesViewHolder(v)
    }

    class MensajesViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var cv: CardView = itemView.findViewById(R.id.cv)
        internal var txtUsuarioMensaje: TextView = itemView.findViewById(R.id.txtUsuarioMensaje)
        internal var txtCuerpoMensaje: TextView = itemView.findViewById(R.id.txtCuerpoMensaje)
    }
}