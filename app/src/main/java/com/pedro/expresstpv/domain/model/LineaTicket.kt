package com.pedro.expresstpv.domain.model

data class LineaTicket(
    val id : Int= 0,
    var ticket: Ticket,
    val descripcion : String = "",
    val categoriaVenta : String = "",
    var cantidad : Int = 0,
    val valorIva : Double = 0.0,
    val subTotal : Double = 0.0,
    var total : Double = 0.0
)

