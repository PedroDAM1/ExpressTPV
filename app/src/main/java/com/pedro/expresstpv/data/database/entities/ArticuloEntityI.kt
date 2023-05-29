package com.pedro.expresstpv.data.database.entities

import androidx.room.*

@Entity(
    tableName = "tb_articulo",
    foreignKeys = [
        ForeignKey(entity = CategoriaEntityI::class, parentColumns = ["id"], childColumns = ["id_categoria"]),
        ForeignKey(entity = TipoIvaEntityI::class, parentColumns = ["id"], childColumns = ["id_tipoiva"])
    ],
    //indices = [Index("id_tipoiva", "id_categoria")]
)
data class ArticuloEntityI(
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,
    @ColumnInfo(name = "id_categoria")
    val idCategoria: Int,
    @ColumnInfo(name = "id_tipoiva")
    val idIva: Int,
    val nombre: String,
    @ColumnInfo(defaultValue = "0.0")
    val precio: Double = 0.0
) : IBaseEntity

