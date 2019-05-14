package com.adri.musicianexchange

data class Mensaje(
    val emisor: String = "",
    val cuerpo: String = "",
    val hora: Long = 0
)