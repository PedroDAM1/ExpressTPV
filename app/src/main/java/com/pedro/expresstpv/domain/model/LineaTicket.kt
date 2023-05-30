package com.pedro.expresstpv.domain.model

data class LineaTicket(
    override val id : Int= 0,
    var ticket: Ticket,
    var descripcion : String = "",
    var categoriaVenta : String = "",
    var cantidad : Int = 0,
    var valorIva : Double = 0.0,
    val subTotal : Double = 0.0,
    var total : Double = 0.0
) : IBaseModel

