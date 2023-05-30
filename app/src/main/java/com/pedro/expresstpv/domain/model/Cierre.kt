package com.pedro.expresstpv.domain.model

import java.time.LocalDateTime

data class Cierre(
    override val id : Int,
    val numCierre : Int = 0,
    val fecha : LocalDateTime? = LocalDateTime.now()
) : IBaseModel
