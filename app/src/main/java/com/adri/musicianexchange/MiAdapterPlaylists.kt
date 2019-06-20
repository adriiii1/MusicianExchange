package com.adri.musicianexchange

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.CardView
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kaaes.spotify.webapi.android.models.PlaylistSimple

class MiAdapterPlaylists(private val listPlaylists: List<PlaylistSimple>) :

    RecyclerView.Adapter<MiAdapterPlaylists.PlaylistsViewHolder>() {

    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase

    override fun getItemCount(): Int {
        return listPlaylists.size
    }

    override fun onBindViewHolder(p0: PlaylistsViewHolder, p1: Int) {
        p0.nombrePlaylist.text = listPlaylists[p1].name
        p0.urlPlaylist.text = ("spotify:playlist:"+listPlaylists[p1].id).replace(":","points")
        val url = listPlaylists[p1].images[0].url
        Glide.with(p0.cv.context).load(url).into(p0.fotoPlaylist)
        p0.cv.setOnClickListener {
            Log.d("Playlist",listPlaylists[p1].id)
            val alertDialog = AlertDialog.Builder(p0.cv.context)
                .setTitle("Compartir")
                .setMessage("Compartir esta playlist? "+p0.nombrePlaylist.text)
                .setPositiveButton("Sí") { dialog, i ->
                    database = FirebaseDatabase.getInstance()
                    dbReference = database.getReference("Playlists")
                    val p=Playlists(nombrePlaylist = p0.nombrePlaylist.text.toString().replace("/","barra").replace("=","igual")
                            .replace(":","points").replace(" ","%&%").replace("#","aterisko")
                        ,urlPlaylists = p0.urlPlaylist.text.toString().replace("/","barra").replace("=","igual")
                                .replace(":","points").replace(" ","%&%").replace("#","aterisko")
                        , fotoPlaylists = listPlaylists[p1].images[0].url.replace("/","barra").replace("=","igual")
                                .replace(":","points").replace(" ","%&%").replace("#","aterisko")
                        ,userID = FirebaseAuth.getInstance().currentUser!!.displayName.toString()
                    )
                    val key = dbReference.child("playlists").push().key
                    dbReference.child("playlists").child(key!!).setValue(p)
                }
                .setNegativeButton("No") { dialogInterface, i ->
                    dialogInterface.cancel()
                }
            alertDialog.show()
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PlaylistsViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.playlistview, p0, false)
        return PlaylistsViewHolder(v)
    }

    class PlaylistsViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var cv: CardView = itemView.findViewById(R.id.cv)
        internal var nombrePlaylist: TextView= itemView.findViewById(R.id.nombrePlaylist)
        internal var urlPlaylist: TextView= itemView.findViewById(R.id.urlPlaylist)
        internal var fotoPlaylist: ImageView= itemView.findViewById(R.id.fotoPlaylist) as ImageView
    }
}