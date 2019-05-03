package com.adri.musicianexchange

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView

class miAdapterVentas(private val listVentas: ArrayList<Venta>) :

    RecyclerView.Adapter<miAdapterVentas.ventasViewHolder>() {

    override fun getItemCount(): Int {
        return listVentas.size
    }

    override fun onBindViewHolder(p0: ventasViewHolder, p1: Int) {
        p0.objeto.text = listVentas[p1].objeto.replace("%&%"," ")
        p0.tipo.text = listVentas[p1].tipo.replace("%&%"," ")
        p0.ciudad.text = listVentas[p1].ciudad.replace("%&%"," ")
        p0.precio.text = listVentas[p1].precio.toString()
        p0.fotoObjeto.setImageURI(Uri.parse(listVentas[p1].fotoObjeto))
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ventasViewHolder {
        val v = LayoutInflater.from(p0.getContext()).inflate(R.layout.ventaview, p0, false)
        return ventasViewHolder(v)
    }

    class ventasViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var cv: CardView = itemView.findViewById(R.id.cv)
        internal var objeto: TextView = itemView.findViewById(R.id.objeto)
        internal var tipo: TextView = itemView.findViewById(R.id.tipoObjeto)
        internal var ciudad: TextView = itemView.findViewById(R.id.ciudadObjeto)
        internal var precio: TextView = itemView.findViewById(R.id.precioObjeto)
        internal var fotoObjeto: ImageView = itemView.findViewById(R.id.fotoObjeto)
    }
}