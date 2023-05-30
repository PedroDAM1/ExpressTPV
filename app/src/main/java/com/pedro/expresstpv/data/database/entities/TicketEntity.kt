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
    ],
    indices = [Index(value = ["num_ticket"], unique = true)]
)
data class TicketEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override val id : Int,
    @ColumnInfo(name = "num_ticket")
    val numTicket: Int,
    @ColumnInfo(name = "num_cierre")
    val numCierre: Int,
    @ColumnInfo(name = "id_metodopago")
    val idMetodopago: Int,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val fecha: LocalDateTime? = LocalDateTime.now(),
    @ColumnInfo(name = "subtotal", defaultValue = "0.00")
    val subtotal : Double = 0.0,
    @ColumnInfo(name = "total", defaultValue = "0.0")
    val total: Double = 0.0
) : IBaseEntity
