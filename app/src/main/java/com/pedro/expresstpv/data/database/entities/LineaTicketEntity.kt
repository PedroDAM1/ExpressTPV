package com.pedro.expresstpv.data.database.entities

import androidx.room.*

@Entity(
    tableName = "tb_lineaticket",
    foreignKeys = [
        ForeignKey(entity = ArticuloEntity::class, parentColumns = ["id"], childColumns = ["id_articulo"]),
        ForeignKey(entity = TicketEntity::class, parentColumns = ["num_ticket"], childColumns = ["num_ticket"])
    ]
)
data class LineaTicketEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "id_articulo")
    val idArticulo: Int,
    @ColumnInfo(name = "num_ticket")
    val numTicket : Int,
    @ColumnInfo(defaultValue = "")
    val descripcion: String = "",
    @ColumnInfo(defaultValue = "0")
    val cantidad: Int = 0,
    @ColumnInfo(name = "valor_iva")
    val valorIva: Double?,
    @ColumnInfo(defaultValue = "0.0")
    val subtotal: Double = 0.0,
    @ColumnInfo(defaultValue = "0.0")
    val total: Double = 0.0
)

data class LineaTicketConArticuloYTicket(
    @Embedded
    val lineaTicketEntity: LineaTicketEntity,
    @Relation(
        entity = ArticuloEntity::class,
        parentColumn = "id_articulo",
        entityColumn = "id"
    )
    val articuloEntity: ArticuloEntity,
    @Relation(
        entity = TicketEntity::class,
        parentColumn = "num_ticket",
        entityColumn = "num_ticket"
    )
    val ticketEntity: TicketEntity

)



