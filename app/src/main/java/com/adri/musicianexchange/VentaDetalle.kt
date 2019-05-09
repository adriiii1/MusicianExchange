package com.adri.musicianexchange

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
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

        Glide.with(this).load(url).into(imgGrandeVenta)

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
