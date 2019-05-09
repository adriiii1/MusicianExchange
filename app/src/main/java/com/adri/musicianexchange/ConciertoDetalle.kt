package com.adri.musicianexchange

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.concierto_detalle.*




@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ConciertoDetalle: AppCompatActivity(){

    private var clicked=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.concierto_detalle)

        val argumentos=intent.extras
        val url=argumentos.get("url")
        val nombre=argumentos.get("grupo")
        val lugar=argumentos.get("lugar")
        val precio=argumentos.get("precio")
        val fecha=argumentos.get("fecha")

        Glide.with(this).load(url).into(imgGrandeConcierto)

        imgGrandeConcierto.setOnClickListener {
            if(!clicked){
                imgGrandeConcierto.setColorFilter(Color.argb(160, 40, 40, 40))
                txtConciertoGrupo.text = nombre.toString()
                txtConciertoLugar.text = lugar.toString()
                txtConciertoFecha.text = fecha.toString()
                txtConciertoPrecio.text = precio.toString()
                clicked=true
            }else{
                imgGrandeConcierto.clearColorFilter()
                txtConciertoGrupo.text=""
                txtConciertoLugar.text = ""
                txtConciertoFecha.text = ""
                txtConciertoPrecio.text = ""
                clicked=false
            }
        }

        /*var bder= AlertDialog.Builder(this)
        bder.setMessage("Deseas borrar este anuncio?").setTitle("Eliminar anuncio")
        bder.setPositiveButton("Borrar") { _, _ ->
            FirebaseDatabase.getInstance().getReference("Conciertos").child("conciertos").child(listConciertos[p1].keyId).removeValue()
        }
        bder.setNegativeButton("Cancelar"){ _,_ ->
            this.dismiss()
        }
        bder.create()*/
    }
}