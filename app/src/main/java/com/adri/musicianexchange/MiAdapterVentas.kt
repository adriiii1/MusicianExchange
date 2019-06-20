package com.adri.musicianexchange

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.gms.dynamic.SupportFragmentWrapper
import java.util.*

class MiAdapterVentas(private val listVentas: ArrayList<Venta>,private val sfm: FragmentManager) :

    RecyclerView.Adapter<MiAdapterVentas.VentasViewHolder>() {

    override fun getItemCount(): Int {
        return listVentas.size
    }

    override fun onBindViewHolder(p0: VentasViewHolder, p1: Int) {
        p0.objeto.text = listVentas[p1].objeto.replace("%&%"," ")
        p0.tipo.text = listVentas[p1].tipo.replace("%&%"," ")
        p0.ciudad.text = listVentas[p1].ciudad.replace("%&%"," ")
        p0.precio.text = listVentas[p1].precio
        p0.precio.text = p0.precio.text.toString() + "€"
        var url=listVentas[p1].fotoObjeto
        url=url.replace("igual","=").replace("barra","/").replace("points",":")
        Glide.with(p0.cv.context).load(url).into(p0.fotoObjeto)
        p0.cv.setOnClickListener {
            val intent= Intent(p0.cv.context,VentaDetalle::class.java)
            var bun=Bundle()
            bun.putString("userId",listVentas[p1].userId)
            bun.putString("objeto",p0.objeto.text.toString())
            bun.putString("tipo",p0.tipo.text.toString())
            bun.putString("ciudad",p0.ciudad.text.toString())
            bun.putString("precio",p0.precio.text.toString())
            bun.putString("url",url)
            intent.putExtras(bun)
            p0.cv.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VentasViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.ventaview, p0, false)
        return VentasViewHolder(v)
    }

    class VentasViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var cv: CardView = itemView.findViewById(R.id.cv)
        internal var objeto: TextView = itemView.findViewById(R.id.objeto)
        internal var tipo: TextView = itemView.findViewById(R.id.tipoObjeto)
        internal var ciudad: TextView = itemView.findViewById(R.id.ciudadObjeto)
        internal var precio: TextView = itemView.findViewById(R.id.precioObjeto)
        internal var fotoObjeto: ImageView = itemView.findViewById(R.id.fotoObjeto)
    }
}