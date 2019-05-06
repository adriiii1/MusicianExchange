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
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.nuevo_anuncio_grupo.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.nuevo_anuncio_venta.*
import java.io.IOException
import java.util.*

class NuevoAnuncioConcierto : AppCompatActivity() {

    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var stReference: StorageReference
    private lateinit var storage: FirebaseStorage

    private var filePath: Uri? = null
    private var previewImage: ImageView? = null
    private var urlFotoConcierto:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nuevo_anuncio_concierto)

        database = FirebaseDatabase.getInstance()
        dbReference = database.getReference("Conciertos")
        storage = FirebaseStorage.getInstance()
        stReference = storage.reference

        btInsVenta.setOnClickListener {
            loadDatabase(dbReference)
        }

        btSelCartel.setOnClickListener {
            chooseFile()
        }
    }

    private fun loadDatabase(firebaseData: DatabaseReference) {
        uploadImageFile()
        val concierto= null
        val key = firebaseData.child("conciertos").push().key
        firebaseData.child("conciertos").child(key!!).setValue(concierto)
        Toast.makeText(this,"Anuncio publicado con éxito",Toast.LENGTH_SHORT)
    }

    private fun chooseFile() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecciona la imágen"), 12345)
    }

    private fun uploadImageFile() {
        if (filePath != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Subiendo...")
            progressDialog.show()
            val ref = stReference.child("conciertosImages/"+ UUID.randomUUID().toString())
            var uploadTask = ref.putFile(filePath!!)
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
                    urlFotoConcierto = task.result.toString()
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
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath) as Bitmap
                previewImage!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
