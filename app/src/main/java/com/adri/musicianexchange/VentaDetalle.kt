package com.adri.musicianexchange

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.venta_detalle.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class VentaDetalle : AppCompatActivity(){

    private var clicked=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.venta_detalle)

        val argumentos=intent.extras
        val url=argumentos.get("url")
        val objeto=argumentos.get("objeto")
        val ciudad=argumentos.get("ciudad")
        val precio=argumentos.get("precio")
        val tipo=argumentos.get("tipo")
        val userId:String=argumentos.get("userId").toString()

        Glide.with(this).load(url).into(imgGrandeVenta)

        if(FirebaseAuth.getInstance().currentUser!!.uid!=userId){
            btnContactar.visibility= View.VISIBLE
            btnContactar.setOnClickListener {
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("Contactar")
                    .setMessage("Quieres contactar al anunciante?")
                    .setPositiveButton("Sí") { dialog, i ->
                        val intent = Intent(this,ChatActivity::class.java)
                        intent.putExtra("user",userId)
                        this.startActivity(intent)
                    }
                    .setNegativeButton("No") { dialogInterface, i ->
                        dialogInterface.cancel()
                    }
                alertDialog.show()
            }
        }else{
            btnContactar.visibility= View.GONE
        }



        imgGrandeVenta.setOnClickListener {
            if(!clicked){
                imgGrandeVenta.setColorFilter(Color.argb(160, 40, 40, 40))
                txtVentaObjeto.text = objeto.toString()
                txtVentaTipo.text = tipo.toString()
                txtVentaCiudad.text = ciudad.toString()
                txtVentaPrecio.text = precio.toString()
                clicked=true
            }else{
                imgGrandeVenta.clearColorFilter()
                txtVentaObjeto.text = ""
                txtVentaTipo.text = ""
                txtVentaCiudad.text = ""
                txtVentaPrecio.text = ""
                clicked=false
            }
        }
    }
}
