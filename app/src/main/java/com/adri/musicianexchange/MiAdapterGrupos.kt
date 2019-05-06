package com.adri.musicianexchange

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.CardView
import android.util.Log
import android.view.View
import android.widget.Toast
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent



class MiAdapterGrupos(private val listGrupos: ArrayList<Grupo>) :

    RecyclerView.Adapter<MiAdapterGrupos.GruposViewHolder>() {

    override fun getItemCount(): Int {
        return listGrupos.size
    }

    override fun onBindViewHolder(p0: GruposViewHolder, p1: Int) {
        p0.nombreGrupo.text = listGrupos[p1].nombreGrupo.replace("%&%"," ")
        p0.generoGrupo.text = listGrupos[p1].generoGrupo.replace("%&%"," ")
        p0.ciudadGrupo.text = listGrupos[p1].ciudad.replace("%&%"," ")
        p0.plazasGrupo.text = "Buscan: "+listGrupos[p1].plazas.replace("%&%"," ")
        p0.cv.setOnClickListener{
            p0.nombreGrupo.text="tocado chacho"
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GruposViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.grupoview, p0, false)
        return GruposViewHolder(v)
    }

    class GruposViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var cv: CardView = itemView.findViewById(R.id.cv)
        internal var nombreGrupo: TextView=itemView.findViewById(R.id.nombreGrupo)
        internal var generoGrupo: TextView= itemView.findViewById(R.id.generoGrupo)
        internal var ciudadGrupo: TextView= itemView.findViewById(R.id.ciudadGrupo)
        internal var plazasGrupo: TextView= itemView.findViewById(R.id.plazasGrupo)
    }
}