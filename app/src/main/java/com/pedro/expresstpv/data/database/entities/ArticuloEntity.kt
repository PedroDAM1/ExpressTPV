package com.pedro.expresstpv.data.database.entities

import androidx.room.*
import com.pedro.expresstpv.domain.model.Articulo

@Entity(
    tableName = "tb_articulo",
    foreignKeys = [
        ForeignKey(entity = CategoriaEntity::class, parentColumns = ["id"], childColumns = ["id_categoria"]),
        ForeignKey(entity = TipoIvaEntity::class, parentColumns = ["id"], childColumns = ["id_tipoiva"])
    ]
)
data class ArticuloEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "id_categoria")
    val idCategoria: Int,
    @ColumnInfo(name = "id_tipoiva")
    val idIva: Int,
    val nombre: String,
    @ColumnInfo(defaultValue = "0.0")
    val precio: Double = 0.0
)

data class ArticuloConCategoriaETipoIva(
    @Embedded
    val articuloEntity: ArticuloEntity,
    @Relation(
        entity = CategoriaEntity::class,
        parentColumn = "id_categoria",
        entityColumn = "id"
    )
    val categoriaEntity: CategoriaEntity,
    @Relation(
        entity = TipoIvaEntity::class,
        parentColumn = "id_tipoiva",
        entityColumn = "id"
    )
    val tipoIvaEntity: TipoIvaEntity,
)

