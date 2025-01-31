package com.adri.musicianexchange

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.nuevo_anuncio_grupo.*
import com.google.firebase.database.FirebaseDatabase

class NuevoAnuncioGrupo : AppCompatActivity() {

    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nuevo_anuncio_grupo)

        database = FirebaseDatabase.getInstance()
        dbReference = database.getReference("Grupos")

        btInsGrupo.setOnClickListener {
            if(txtNom.text.isEmpty() || txtgen.text.isEmpty() || txtPlazas.text.isEmpty() || ciudadGrupo.text.isEmpty()){
                Toast.makeText(this,"Rellena todos los datos",Toast.LENGTH_LONG)
            }else {
                loadDatabase(dbReference)
            }
        }
    }

    private fun loadDatabase(firebaseData: DatabaseReference) {
        val key = firebaseData.child("grupos").push().key
        val grupo = Grupo(nombreGrupo = txtNom.text.toString().replace(" ","%&%") ,generoGrupo = txtgen.text.toString().replace(" ","%&%")
            ,ciudad = ciudadGrupo.text.toString().replace(" ","%&%"),plazas = txtPlazas.text.toString().replace(" ","%&%")
            ,userId = FirebaseAuth.getInstance().currentUser!!.uid,keyId = key.toString())
        firebaseData.child("grupos").child(key!!).setValue(grupo)
        Toast.makeText(this,"Anuncio publicado con éxito",Toast.LENGTH_SHORT)
    }
}