package com.adri.musicianexchange

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.firebase.database.*
import com.google.gson.Gson

class ListasActivity : AppCompatActivity(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase

    var listPlaylists: ArrayList<Playlists> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listas)

        viewManager = LinearLayoutManager(this)
        viewAdapter = MiAdapterPlaylists(listPlaylists)

        database = FirebaseDatabase.getInstance()
        dbReference = database.getReference("Playlists")

        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listPlaylists.clear()
                val gson= Gson()
                for (objj  in dataSnapshot.children){
                    val registro=objj.value
                    try {
                        val reg:Playlists=gson.fromJson(registro.toString(),Playlists::class.java)
                        listPlaylists.add(reg)
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
        dbReference.child("playlists").addValueEventListener(menuListener)
    }
}