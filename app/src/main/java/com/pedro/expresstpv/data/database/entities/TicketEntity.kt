package com.pedro.expresstpv.data.database.entities

import androidx.room.*
import com.pedro.expresstpv.domain.model.Ticket
import java.time.LocalDateTime

@Entity(
    tableName = "tb_ticket",
    foreignKeys = [
        ForeignKey(
            entity = CierreEntity::class,
            parentColumns = ["num_cierre"],
            childColumns = ["num_cierre"]
        ),
        ForeignKey(
            entity = MetodoPagoEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_metodopago"]
        )
    ]
)
data class TicketEntity(
    @PrimaryKey
    @ColumnInfo(name = "num_ticket")
    val numTicket: Int,
    @ColumnInfo(name = "num_cierre")
    val numCierre: Int,
    @ColumnInfo(name = "id_metodopago")
    val idMetodopago: Int,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val fecha: LocalDateTime? = LocalDateTime.now(),
    val total: Double
)
