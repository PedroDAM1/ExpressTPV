package com.pedro.expresstpv.domain.model

import java.time.LocalDateTime

data class Cierre(
    val numCierre : Int = 0,
    val fecha : LocalDateTime? = LocalDateTime.now()
)
