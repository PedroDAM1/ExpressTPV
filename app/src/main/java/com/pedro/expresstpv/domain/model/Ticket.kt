package com.pedro.expresstpv.domain.model

import java.time.LocalDateTime

data class Ticket(
    override val id : Int,
    val numTicket : Int = 0,
    val cierre : Cierre,
    val metodoPago: MetodoPago,
    val fecha : LocalDateTime? = LocalDateTime.now(),
    var total : Double = 0.0
) : IBaseModel