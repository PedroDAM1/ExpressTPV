package com.pedro.expresstpv.domain.model

import java.time.LocalDateTime

data class Ticket(
    val numTicket : Int = 0,
    val cierre : Cierre,
    val metodoPago: MetodoPago,
    val fecha : LocalDateTime = LocalDateTime.now(),
    val total : Double = 0.0
)