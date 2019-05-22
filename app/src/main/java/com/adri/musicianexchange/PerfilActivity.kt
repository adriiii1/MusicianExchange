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
import com.google.android.gms.tasks.Continuation
import java.util.*
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.UploadTask
import com.google.android.gms.tasks.Task
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
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(txt_nomUser.text.toString()).build()
            user.updateProfile(profileUpdates).addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d("ProfileUpdate","El perfil se ha actualizado")
                }
            }
        }

        btnSpotify.setOnClickListener {
            val REQUEST_CODE = 1337
            val REDIRECT_URI = "http://localhost/callback"

            val builder = AuthenticationRequest.Builder("00868c003ec44f00921ec16a27f18014", AuthenticationResponse.Type.TOKEN, REDIRECT_URI)

            builder.setScopes(arrayOf("streaming"))
            val request = builder.build()

            AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request)
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
}
