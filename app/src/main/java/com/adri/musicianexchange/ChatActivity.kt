package com.adri.musicianexchange

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    var listMensajes: ArrayList<Mensaje> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        viewManager = LinearLayoutManager(this)
        viewAdapter = MiAdapterMensajes(listMensajes)

        val txtMensaje:EditText = findViewById(R.id.txtInputMensaje)
        val argumentos = intent.extras
        val db = FirebaseDatabase.getInstance().getReference("Conversaciones").child("conversaciones")
        val userr =  FirebaseAuth.getInstance().currentUser!!.uid
        val convers = db.equalTo(userr+argumentos.get("user")).path.isEmpty
        val conver: DatabaseReference

        if(convers) {
            val key = userr+argumentos.get("user")
            db.child(key).push().key
            conver = db.child(key).ref
        } else {
            conver = db.equalTo(userr+argumentos.get("user")).ref
        }

        btnEnviarMensaje.setOnClickListener {
            val mensaje = strPrepare(txtMensaje.text.toString())
            if(mensaje != ""){
                val kei = conver.push().key
                val mensahe = Mensaje(emisor = FirebaseAuth.getInstance().currentUser!!.displayName!!,cuerpo = mensaje
                    ,hora = System.currentTimeMillis())
                conver.child(kei!!).setValue(mensahe)
            }else{
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("Mensaje")
                    .setMessage("Escribe un mensaje")
                    .setCancelable(true)
                alertDialog.show()
            }
        }

        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listMensajes.clear()
                val gson= Gson()
                for (objj  in dataSnapshot.children){
                    val registro=objj.value.toString()
                    try {
                        val reg:Mensaje=gson.fromJson(registro,Mensaje::class.java)
                        listMensajes.add(reg)
                    }
                    catch (e: com.google.gson.JsonSyntaxException) {}
                }
                recyclerView = findViewById<RecyclerView>(R.id.recyclerViewChat).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        conver.addValueEventListener(menuListener)
    }

    private fun strPrepare(string: String): String{
        val retString = string.replace("/","barra")
            .replace("=","igual").replace(":","points").replace(" ","%&%")
        return retString
    }
}
