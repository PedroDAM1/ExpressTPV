package com.pedro.expresstpv.domain.model

import java.time.LocalDateTime

data class Ticket(
    override val id : Int,
    val numTicket : Int = 0,
    var cierre : Cierre,
    val metodoPago: MetodoPago,
    val fecha : LocalDateTime? = LocalDateTime.now(),
    var subtotal : Double = 0.0,
    var total : Double = 0.0
) : IBaseModel