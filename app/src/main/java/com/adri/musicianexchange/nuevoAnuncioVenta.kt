package com.adri.musicianexchange

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.nuevo_anuncio_grupo.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.nuevo_anuncio_venta.*
import java.io.IOException

class nuevoAnuncioVenta : AppCompatActivity() {

    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var stReference: StorageReference
    private lateinit var storage: FirebaseStorage

    private var imagePath: Uri? = null
    private var previewImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nuevo_anuncio_venta)

        database = FirebaseDatabase.getInstance()
        dbReference = database.getReference("Ventas")
        storage = FirebaseStorage.getInstance()
        stReference = storage.getReference("Imagenes")

        btInsVenta.setOnClickListener {
            loadDatabase(dbReference)
        }

        btSelCartel.setOnClickListener {
            chooseFile()
        }
    }

    fun loadDatabase(firebaseData: DatabaseReference) {
        uploadImageFile()
        val venta = Venta(objeto = txtObjeto.text.toString().replace(" ","%&%"), ciudad = txtCiudad.text.toString().replace(" ","%&%")
            , precio = txtPrecio.text.toString().toDouble(),tipo = txtTipo.text.toString().replace(" ","%&%"))
        val key = firebaseData.child("ventas").push().key
        firebaseData.child("ventas").child(key!!).setValue(venta)
        Toast.makeText(this,"Anuncio publicado con éxito",Toast.LENGTH_SHORT)
    }

    private fun chooseFile() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imágen"),12345)
    }

    private fun uploadImageFile() {
        if (imagePath != null) {
            //progressDialog is a deprecated control for API 26 and above-Just using for demo purposes.
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Subiendo la imagen")
            progressDialog.show()
            val fbStoreRef = stReference//.child("Images/")
            fbStoreRef.putFile(imagePath!!)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    //Success
                    Toast.makeText(applicationContext, "Imágen subida", Toast.LENGTH_LONG).show()
                    /*previewImage!!.setImageBitmap(
                        BitmapFactory.decodeResource(applicationContext.resources,
                        R.drawable.upload))*/
                }
                .addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    //Failure
                    Toast.makeText(applicationContext, exception.message, Toast.LENGTH_LONG).show()
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    //Update UI
                    progressDialog.setMessage("Subida un ${progress.toInt()}%...")
                }
        } else {
            Toast.makeText(applicationContext, "Selecciona una imágen", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12345 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imagePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagePath) as Bitmap
                previewImage!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
