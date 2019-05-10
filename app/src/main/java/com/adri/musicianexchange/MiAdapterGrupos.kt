package com.adri.musicianexchange

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.google.firebase.auth.FirebaseAuth


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
            if(FirebaseAuth.getInstance().currentUser!!.uid!=listGrupos[p1].userId){
                val alertDialog = AlertDialog.Builder(p0.cv.context)
                    .setTitle("Contactar")
                    .setMessage("Quieres contactar al anunciante?")
                    .setPositiveButton("Sí") { dialog, i ->
                        val intent=Intent(p0.cv.context,ChatActivity::class.java)
                        p0.cv.context.startActivity(intent)
                    }
                    .setNegativeButton("No") { dialogInterface, i ->
                        dialogInterface.cancel()
                    }

                alertDialog.show()
            }
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