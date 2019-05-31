@file:Suppress("DEPRECATION")

package com.adri.musicianexchange

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.nuevo_anuncio_venta.*
import java.util.*

class NuevoAnuncioVenta : AppCompatActivity() {

    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var stReference: StorageReference
    private lateinit var storage: FirebaseStorage

    private var filePath: Uri? = null
    private var urlFotoObjeto:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nuevo_anuncio_venta)

        database = FirebaseDatabase.getInstance()
        dbReference = database.getReference("Ventas")
        storage = FirebaseStorage.getInstance()
        stReference = storage.reference

        btInsVenta.setOnClickListener {
            if(txtObjeto.text.isEmpty() || txtTipo.text.isEmpty() || txtCiudad.text.isEmpty() || txtPrecio.text.isEmpty() || filePath==null){
                Toast.makeText(this,"Rellena todos los datos",Toast.LENGTH_LONG)
            }else {
                loadDatabase()
            }
        }

        btSelFoto.setOnClickListener {
            chooseFile()
        }
    }

    private fun loadDatabase() {
        uploadImageFile()
    }

    private fun chooseFile() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecciona la imágen"), 12345)
    }

    @SuppressLint("ShowToast")
    private fun uploadImageFile() {
        if (filePath != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Subiendo...")
            progressDialog.show()
            val ref = stReference.child("ventasImages/"+ UUID.randomUUID().toString())
            val uploadTask = ref.putFile(filePath!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    urlFotoObjeto = task.result.toString()
                    urlFotoObjeto = urlFotoObjeto.replace("/","barra").replace("=","igual").replace(":","points")
                    val key = dbReference.child("ventas").push().key
                    val venta = Venta(objeto = txtObjeto.text.toString().replace(" ","%&%"), ciudad = txtCiudad.text.toString().replace(" ","%&%")
                        , precio = txtPrecio.text.toString(),tipo = txtTipo.text.toString().replace(" ","%&%"),fotoObjeto = urlFotoObjeto
                        ,userId = FirebaseAuth.getInstance().currentUser!!.uid,keyId = key.toString())
                    dbReference.child("ventas").child(key!!).setValue(venta)
                    Toast.makeText(this,"Anuncio publicado con éxito",Toast.LENGTH_SHORT)
                }else{
                    task.exception?.let {
                        throw it
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12345 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
        }
    }
}
