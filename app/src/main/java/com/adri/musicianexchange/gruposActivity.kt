package com.adri.musicianexchange

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.firebase.database.*
import com.google.gson.Gson

class gruposActivity : AppCompatActivity(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    var listGrupos: ArrayList<Grupo> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listas)

        viewManager = LinearLayoutManager(this)
        viewAdapter = miAdapterGrupos(listGrupos)

        database = FirebaseDatabase.getInstance()
        dbReference = database.getReference("Grupos")

        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listGrupos.clear()
                val gson= Gson()
                for (objj  in dataSnapshot.children){
                    val registro=objj.getValue().toString()
                    try {
                        val reg:Grupo=gson.fromJson(registro,Grupo::class.java)
                        listGrupos.add(reg)
                    }
                    catch (e: com.google.gson.JsonSyntaxException) {}
                }
                recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        dbReference.child("grupos").addValueEventListener(menuListener)
    }
}