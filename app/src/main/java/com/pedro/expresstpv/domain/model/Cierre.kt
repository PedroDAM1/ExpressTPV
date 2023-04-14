package com.pedro.expresstpv.domain.model

import com.pedro.expresstpv.data.database.entities.CierreEntity
import java.time.LocalDate
import java.time.LocalDateTime

data class Cierre(
    val numCierre : Int = 0,
    val fecha : LocalDateTime = LocalDateTime.now()
)

fun CierreEntity.toDomain() = Cierre(
    numCierre = numCierre,
    fecha = fecha
)