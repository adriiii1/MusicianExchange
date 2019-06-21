package com.adri.musicianexchange

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.CardView
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class MiAdapterConversaciones(private val listConversaciones: ArrayList<Conversacion>) :

    RecyclerView.Adapter<MiAdapterConversaciones.ConversacionesViewHolder>() {

    val database = FirebaseDatabase.getInstance()
    val dbReference = database.getReference("Usuarios").child("usuarios")

    override fun getItemCount(): Int {
        return listConversaciones.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: ConversacionesViewHolder, p1: Int) {
       val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                p0.nomUsu.text = dataSnapshot.child("displayName").value.toString()
                val urlPreparada: String = dataSnapshot.child("urlFoto").value.toString().replace("%&%"," ")
                    .replace("igual","=").replace("barra","/").replace("points",":")
                Glide.with(p0.cv.context).load(urlPreparada).into(p0.imgUsu)
            }
            override fun onCancelled(p0: DatabaseError) {

            }
       }
        dbReference.child(listConversaciones[p1].usuario).addListenerForSingleValueEvent(listener)
        p0.cv.setOnClickListener{
            val intent = Intent(p0.cv.context,ChatActivity::class.java)
            intent.putExtra("user",listConversaciones[p1].usuario)
            p0.cv.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ConversacionesViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.conversacionview, p0, false)
        return ConversacionesViewHolder(v)
    }

    class ConversacionesViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var cv: CardView = itemView.findViewById(R.id.cv)
        internal var imgUsu: CircleImageView = itemView.findViewById(R.id.imgUsuConver)
        internal var nomUsu: TextView = itemView.findViewById(R.id.txtNomUsuConver)
    }
}