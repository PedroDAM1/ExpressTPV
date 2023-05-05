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

}