package com.adri.musicianexchange

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.nuevo_anuncio_grupo.*
import com.google.firebase.database.FirebaseDatabase

class nuevoAnuncioGrupo : AppCompatActivity() {

    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nuevo_anuncio_grupo)

        database = FirebaseDatabase.getInstance()
        dbReference = database.getReference("Grupos")

        btInsGrupo.setOnClickListener {
            loadDatabase(dbReference)
        }
    }

    fun loadDatabase(firebaseData: DatabaseReference) {
        val grupo = Grupo(nombreGrupo = txtNom.text.toString() ,generoGrupo = txtgen.text.toString()
            ,ciudad = ciudadGrupo.text.toString(),plazas = txtPlazas.text.toString())
        val key = firebaseData.child("grupos").push().key
        firebaseData.child("grupos").child(key!!).setValue(grupo)
    }
}