package com.pedro.expresstpv.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pedro.expresstpv.data.database.LocalDateTimeConverter
import com.pedro.expresstpv.data.database.dao.*
import com.pedro.expresstpv.data.database.entities.*
import java.time.LocalDateTime

@Database(entities = [CategoriaEntity::class, TipoIvaEntity::class, ArticuloEntity::class, LineaTicketEntity::class, CierreEntity::class, MetodoPagoEntity::class, TicketEntity::class],
    version = 1)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AccesoDatos : RoomDatabase() {

    abstract fun getCategoriaDao(): CategoriaDao
    abstract fun getTipoIvaDAo(): TipoIvaDao
    abstract fun getArticuloDao(): ArticuloDao
    abstract fun getCierreDao(): CierreDao
    abstract fun getLineaTicketDao(): LineaTicketDao
    abstract fun getMetodoPagoDao(): MetodoPagoDao
    abstract fun getTicketDao(): TicketDao


    companion object{
        val TIPOS_IVA = arrayOf(
            TipoIvaEntity(nombre = "SUPER REDUCIDO", porcentaje = 4.0),
            TipoIvaEntity(nombre = "REDUCIDO", porcentaje = 10.0),
            TipoIvaEntity(nombre = "NORMAL", porcentaje = 21.0),
            TipoIvaEntity(nombre = "EXENTO", porcentaje = 0.0)
        )

        val METODOS_PAGO = arrayOf(
            MetodoPagoEntity(nombre = "TARJETA"),
            MetodoPagoEntity(nombre = "EFECTIVO")
        )

        val CATEGORIAS = arrayOf(
            CategoriaEntity(id = 0, nombre = "SIN CATEGORIA")
        )

        val CIERRES = arrayOf(
            CierreEntity(numCierre = 0, fecha = LocalDateTime.of(1, 1, 1, 0, 0))
        )

        val ARTICULO = arrayOf(
            ArticuloEntity(id= 1, idCategoria = 1, idIva = 1, "BASE", 0.0)
        )
    }
}