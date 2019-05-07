package com.adri.musicianexchange

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.nuevo_anuncio_concierto.*
import java.util.*

class NuevoAnuncioConcierto : AppCompatActivity() {

    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var stReference: StorageReference
    private lateinit var storage: FirebaseStorage

    private var filePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nuevo_anuncio_concierto)

        database = FirebaseDatabase.getInstance()
        dbReference = database.getReference("Conciertos")
        storage = FirebaseStorage.getInstance()
        stReference = storage.reference

        txtFecha.setOnClickListener {
            showDatePickerDialog()
        }

        btInsConcierto.setOnClickListener {
            loadDatabase()
        }

        btSelCartel.setOnClickListener {
            chooseFile()
        }
    }

    private fun showDatePickerDialog() {
        var fragment = DatePickerFragment.newInstance { view, year, month, dayOfMonth ->
            val selectedDate: String = dayOfMonth.toString()+"-"+(month+1)+"-"+year
            txtFecha.setText(selectedDate)
        }
        fragment.show(this.supportFragmentManager,"Fecha")
    }

    private fun loadDatabase() {
        uploadImageFile(dbReference)
    }

    private fun chooseFile() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecciona la imágen"), 12345)
    }

    private fun uploadImageFile(firebaseData: DatabaseReference) {
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
                    var urlFotoConcierto:String = task.result.toString()
                    urlFotoConcierto=urlFotoConcierto.replace("/","barra").replace("=","igual").replace(":","points")
                    val concierto=Concierto(foto = urlFotoConcierto,grupo = txtGrupoConcierto.text.toString().replace(" ","%&%"),lugar = txtLugar.text.toString().replace(" ","%&%"),
                        fecha = txtFecha.text.toString(),precio = txtPrecioConcierto.text.toString().replace(" ","%&%"))
                    val key = firebaseData.child("conciertos").push().key
                    firebaseData.child("conciertos").child(key!!).setValue(concierto)
                    Toast.makeText(this,"Anuncio publicado con éxito",Toast.LENGTH_SHORT)
                }else{
                    task.exception?.let {
                        throw it
                    }
                }
            }
        }
    }

    //https://firebasestorage.googleapis.com/v0/b/musician-exchange.appspot.com/o/conciertosImages%2F3695a213-92f7-46e3-898b-12bda415da58?alt=media&token=83aef141-61b7-4701-9075-f1cb293fac17

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12345 && resultCode == Activity.RESULT_OK && data != null && data.data != null){
            filePath = data.data
        }
    }
}
