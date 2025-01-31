@file:Suppress("DEPRECATION")

package com.adri.musicianexchange

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_perfil.*
import android.content.Intent
import android.provider.MediaStore
import android.net.Uri
import android.widget.ImageView
import java.io.IOException
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.FirebaseStorage
import android.app.ProgressDialog
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import java.util.*
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.UploadTask
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse

class PerfilActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private val pickImageRequest = 0
    private lateinit var imagen: ImageView
    private var filePath: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var urlFoto: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

        imagen= findViewById(R.id.imgUsr)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        btnImagen.setOnClickListener {
            chooseImage()
        }

        btnGuardar.setOnClickListener {
            uploadImage()

            if(txt_nomUser.text!!.isEmpty()){
                Toast.makeText(this,"Rellena todos los datos",Toast.LENGTH_LONG)
            }else {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(txt_nomUser.text.toString()).build()
                user.updateProfile(profileUpdates).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this,"Perfil actualizado con éxito",Toast.LENGTH_SHORT)
                    }
                }
                val database = FirebaseDatabase.getInstance()
                val dbReference = database.getReference("Usuarios").child("usuarios")

                dbReference.child(FirebaseAuth.getInstance().currentUser!!.uid).child("displayName").setValue(txt_nomUser.text.toString())
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, Main2Activity::class.java)
        startActivity(intent)
        finish()
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecciona la imágen"), pickImageRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageRequest && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imagen.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage() {
        if (filePath != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Subiendo...")
            progressDialog.show()
            val ref = storageReference.child("profileImages/"+UUID.randomUUID().toString())
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
                    val database = FirebaseDatabase.getInstance()
                    val dbReference = database.getReference("Usuarios").child("usuarios")
                    urlFoto = strPrepare(task.result.toString())
                    dbReference.child(FirebaseAuth.getInstance().currentUser!!.uid).child("urlFoto").setValue(urlFoto)
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(task.result.toString())).build()
                    user.updateProfile(profileUpdates).addOnCompleteListener {
                        if(it.isSuccessful){
                            Log.d("ProfileUpdate","El perfil se ha actualizado")
                        }
                    }
                }else{
                    task.exception?.let {
                        throw it
                    }
                }
            }
        }
    }

    private fun strPrepare(string: String): String{
        return string.replace("/","barra")
            .replace("=","igual").replace(":","points").replace(" ","%&%")
    }
}
