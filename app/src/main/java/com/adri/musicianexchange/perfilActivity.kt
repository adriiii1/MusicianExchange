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
import android.widget.Toast
import android.app.ProgressDialog
import android.util.Log
import java.util.*
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.UploadTask
import com.google.android.gms.tasks.OnSuccessListener




class perfilActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    val PICK_IMAGE_REQUEST = 0
    lateinit var imagen: ImageView
    private var filePath: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var urlFoto: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        auth = FirebaseAuth.getInstance()
        user = auth.getCurrentUser()!!

        imagen= findViewById(R.id.imgUsr)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()

        btnImagen.setOnClickListener {
            chooseImage()
        }

        btnGuardar.setOnClickListener {
            uploadImage()
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(txt_nomUser.text.toString()).setPhotoUri(Uri.parse(urlFoto)).build()
            user.updateProfile(profileUpdates)

        }
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
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
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            val ref = storageReference.child("images/" + UUID.randomUUID().toString())
            ref.putFile(filePath!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        ref.metadata.addOnSuccessListener {
                            urlFoto = it.path
                        }
                        Log.d(urlFoto,"AAAA")
                    }
                    progressDialog.dismiss()
                    Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(this, "Failed " + e.message, Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { taskSnapshot ->
                    val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                        .totalByteCount
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }
            }
        }
    }
