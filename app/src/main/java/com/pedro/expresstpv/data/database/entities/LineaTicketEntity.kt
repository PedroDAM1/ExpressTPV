package com.pedro.expresstpv.data.database.entities

import androidx.room.*

@Entity(
    tableName = "tb_lineaticket",
    foreignKeys = [
        ForeignKey(entity = TicketEntity::class, parentColumns = ["num_ticket"], childColumns = ["num_ticket"])
    ]
)
data class LineaTicketEntity(
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,
    @ColumnInfo(name = "num_ticket")
    val numTicket : Int,
    @ColumnInfo(defaultValue = "")
    var descripcion: String = "",
    @ColumnInfo(name = "categoria_venta",defaultValue = "")
    var categoriaVenta : String = "",
    @ColumnInfo(defaultValue = "0")
    var cantidad: Int = 0,
    @ColumnInfo(name = "valor_iva", defaultValue = "0.0")
    var valorIva: Double = 0.0,
    @ColumnInfo(defaultValue = "0.0")
    var subtotal: Double = 0.0,
    @ColumnInfo(defaultValue = "0.0")
    var total: Double = 0.0
) : IBaseEntity



