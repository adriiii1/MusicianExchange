package com.adri.musicianexchange

import java.util.*

data class Concierto(
    val foto: String = "",
    val grupo: Grupo,
    val lugar: String = "",
    val fecha: Date,
    val precio: Int = 0
)