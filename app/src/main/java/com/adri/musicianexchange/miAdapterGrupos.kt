package com.adri.musicianexchange

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.CardView
import android.view.View

class miAdapterGrupos(private val listGrupos: ArrayList<Grupo>) :

    RecyclerView.Adapter<miAdapterGrupos.gruposViewHolder>() {

    override fun getItemCount(): Int {
        return listGrupos.size
    }

    override fun onBindViewHolder(p0: gruposViewHolder, p1: Int) {
        p0.nombreGrupo.text = listGrupos[p1].nombreGrupo.replace("%&%"," ")
        p0.generoGrupo.text = "Género: "+listGrupos[p1].generoGrupo.replace("%&%"," ")
        p0.ciudadGrupo.text = "Ciudad: "+listGrupos[p1].ciudad.replace("%&%"," ")
        p0.plazasGrupo.text = "Buscan: "+listGrupos[p1].plazas.replace("%&%"," ")
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): gruposViewHolder {
        val v = LayoutInflater.from(p0.getContext()).inflate(R.layout.grupoview, p0, false)
        return gruposViewHolder(v)
    }

    class gruposViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var cv: CardView = itemView.findViewById(R.id.cv)
        internal var nombreGrupo: TextView=itemView.findViewById(R.id.nombreGrupo)
        internal var generoGrupo: TextView= itemView.findViewById(R.id.generoGrupo)
        internal var ciudadGrupo: TextView= itemView.findViewById(R.id.ciudadGrupo)
        internal var plazasGrupo: TextView= itemView.findViewById(R.id.plazasGrupo)
    }
}