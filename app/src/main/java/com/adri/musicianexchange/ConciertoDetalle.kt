package com.adri.musicianexchange

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.concierto_detalle.*

class ConciertoDetalle: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.concierto_detalle)

        val argumentos=intent.extras
        val url=argumentos.get("url")

        Glide.with(this).load(url).into(imgGrande)
    }
}