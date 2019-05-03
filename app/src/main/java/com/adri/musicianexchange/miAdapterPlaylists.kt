package com.adri.musicianexchange

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView

class miAdapterPlaylists(private val listPlaylists: ArrayList<Playlists>) :

    RecyclerView.Adapter<miAdapterPlaylists.playlistsViewHolder>() {

    override fun getItemCount(): Int {
        return listPlaylists.size
    }

    override fun onBindViewHolder(p0: playlistsViewHolder, p1: Int) {
        p0.nombrePlaylist.text = listPlaylists[p1].nombrePlaylist.replace("%&%"," ")
        p0.urlPlaylist.text = listPlaylists[p1].urlPlaylists
        p0.fotoPlaylist.setImageURI(Uri.parse(listPlaylists[p1].fotoPlaylists))
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): playlistsViewHolder {
        val v = LayoutInflater.from(p0.getContext()).inflate(R.layout.playlistview, p0, false)
        return playlistsViewHolder(v)
    }

    class playlistsViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var cv: CardView = itemView.findViewById(R.id.cv)
        internal var nombrePlaylist: TextView= itemView.findViewById(R.id.nombrePlaylist)
        internal var urlPlaylist: TextView= itemView.findViewById(R.id.urlPlaylist)
        internal var fotoPlaylist: ImageView= itemView.findViewById(R.id.fotoPlaylist) as ImageView
    }
}