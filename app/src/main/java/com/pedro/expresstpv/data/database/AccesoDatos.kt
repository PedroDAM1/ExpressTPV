package com.pedro.expresstpv.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pedro.expresstpv.data.database.dao.*
import com.pedro.expresstpv.data.database.entities.*

/**
 * Inicializador de room de la base de datos
 */
@Database(entities = [CategoriaEntity::class, TipoIvaEntity::class, ArticuloEntity::class, LineaTicketEntity::class, CierreEntity::class, MetodoPagoEntity::class, TicketEntity::class],
    version = 3)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AccesoDatos : RoomDatabase() {

    /* En caso de añadir una nueva entidad con un nuevo dao, deberemos de agregarlo aqui */

    //DAOS
    abstract fun getCategoriaDao(): CategoriaDaoI
    abstract fun getTipoIvaDAo(): TipoIvaDaoI
    abstract fun getArticuloDao(): ArticuloDaoI
    abstract fun getCierreDao(): CierreDao
    abstract fun getLineaTicketDao(): LineaTicketDao
    abstract fun getMetodoPagoDao(): MetodoPagoDao
    abstract fun getTicketDao(): TicketDao

}