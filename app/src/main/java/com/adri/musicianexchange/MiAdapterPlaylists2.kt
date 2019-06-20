package com.adri.musicianexchange

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.CardView
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.spotify.android.appremote.api.SpotifyAppRemote


class MiAdapterPlaylists2(private val listPlaylists: ArrayList<Playlists>, private val mSpotifyAppRemote: SpotifyAppRemote) :

    RecyclerView.Adapter<MiAdapterPlaylists2.PlaylistsViewHolder>() {

    override fun getItemCount(): Int {
        return listPlaylists.size
    }

    override fun onBindViewHolder(p0: PlaylistsViewHolder, p1: Int) {
        p0.nombrePlaylist.text = listPlaylists[p1].nombrePlaylist.replace("igual","=").replace("barra","/").replace("points",":").replace("%&%"," ").replace("aterisko","#")
        p0.urlPlaylist.text = listPlaylists[p1].urlPlaylists.replace("igual","=").replace("barra","/").replace("points",":").replace("%&%"," ").replace("aterisko","#")
        p0.userId.text = listPlaylists[p1].userID.replace("igual","=").replace("barra","/").replace("points",":").replace("%&%"," ").replace("aterisko","#")
        val url = listPlaylists[p1].fotoPlaylists.replace("igual","=").replace("barra","/").replace("points",":").replace("%&%"," ").replace("aterisko","#")
        Glide.with(p0.cv.context).load(url).into(p0.fotoPlaylist)
        p0.cv.setOnClickListener {
            Log.d("Player","La wea we")
            mSpotifyAppRemote.playerApi.play(p0.urlPlaylist.text.toString())
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
        internal var userId: TextView = itemView.findViewById(R.id.txtUserP)
    }
}