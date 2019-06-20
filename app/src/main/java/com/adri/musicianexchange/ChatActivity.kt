package com.adri.musicianexchange

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_chat.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var db : DatabaseReference
    var listMensajes: ArrayList<Mensaje> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        viewManager = LinearLayoutManager(this)
        viewAdapter = MiAdapterMensajes(listMensajes)

        val argumentos = intent.extras
        db = FirebaseDatabase.getInstance().getReference("Conversaciones").child("conversaciones")
        val userr =  FirebaseAuth.getInstance().currentUser!!.uid

        val type1 = userr + argumentos.get("user").toString()
        val type2 = argumentos.get("user").toString() + userr

        val oidor = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChild(type1)){
                    cargarMensajes(type1,true)
                }else if(p0.hasChild(type2)){
                    cargarMensajes(type2,true)
                }else{
                    if(type1<type2){
                        cargarMensajes(type2,false)
                    }else{
                        cargarMensajes(type1,false)
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        }
        db.addListenerForSingleValueEvent(oidor)
    }

    private fun cargarMensajes(type :String, existe: Boolean){
        if(!existe){
            Log.d("AAAA","Ey this das not exis")
            db.child(type).push()
            db.child(type).child("user1").setValue(intent.extras.get("user"))
            db.child(type).child("user2").setValue(FirebaseAuth.getInstance().currentUser!!.uid)
        }

        val conver = db.child(type)
        val txtMensaje:EditText = findViewById(R.id.txtInputMensaje)

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
        return string.replace("/","barra")
            .replace("=","igual").replace(":","points").replace(" ","%&%")
    }
}
