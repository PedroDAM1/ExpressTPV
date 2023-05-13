package com.pedro.expresstpv.domain.model

data class LineaTicket(
    val id : Int= 0,
    val ticket: Ticket,
    val descripcion : String = "",
    val cantidad : Int = 0,
    val valorIva : Double = 0.0,
    val subTotal : Double? = 0.0,
    val total : Double = 0.0
)

