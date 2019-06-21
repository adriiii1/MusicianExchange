package com.adri.musicianexchange

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.google.firebase.database.*
import com.google.gson.Gson
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

class ListasActivity : AppCompatActivity(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    lateinit var mSpotifyAppRemote: SpotifyAppRemote

    var listPlaylists: ArrayList<Playlists> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listas)

        var connectionParams = ConnectionParams.Builder("00868c003ec44f00921ec16a27f18014")
            .setRedirectUri("http://musicianexchange.com/callback")
            .showAuthView(true)
            .build()

        val contex=this

        SpotifyAppRemote.connect(this, connectionParams,
            object : Connector.ConnectionListener{

                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote

                    viewManager = LinearLayoutManager(contex)
                    viewAdapter = MiAdapterPlaylists2(listPlaylists,mSpotifyAppRemote)

                    database = FirebaseDatabase.getInstance()
                    dbReference = database.getReference("Playlists")
                    dbReference.keepSynced(true)

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

                override fun onFailure(throwable: Throwable) {
                    Log.d("MainActivity", "chowchow")
                }
            })
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    }
}