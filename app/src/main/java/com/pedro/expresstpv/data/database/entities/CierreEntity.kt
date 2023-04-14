package com.pedro.expresstpv.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pedro.expresstpv.domain.model.Cierre
import java.time.LocalDateTime

@Entity(tableName = "tb_cierre")
data class CierreEntity(
    @PrimaryKey
    @ColumnInfo(name = "num_cierre") val numCierre: Int,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val fecha: LocalDateTime = LocalDateTime.now()
)


