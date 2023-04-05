package com.pedro.expresstpv.di

import android.content.Context
import androidx.room.Room
import com.pedro.expresstpv.data.database.AccesoDatos
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Con este objeto podremos hacer uso de hilt para inyectar dependencias en el resto de la aplicacion
 * y tener unicamente una instancia de la base de datos, de forma que no tengamos que instanciar una cada
 * vez que la necesitemos
 */

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    //Nombre de la base de datos
    private const val EXPRESSTPV_DATABASE_NAME = "ExpressDB"

    /**
     * La anotacion @Singleton sirve para decirle a hilt que solo nos cree una instancia, y que use esa misma
     * instancia para toda la app donde se le llame
     * La anotacion @Provider sirve para proveer la base de datos en las clases que esten preparadas para la inyeccion
     */
    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) : AccesoDatos {
        return Room.databaseBuilder(context, AccesoDatos::class.java, EXPRESSTPV_DATABASE_NAME)
            .build()
    }



    /**********************************************************************************************
     * Cada una de las funciones siguientes nos permitira obtener cada uno de los diferentes daos
     * para poder trabajar con ellos
     **********************************************************************************************/
    @Singleton
    @Provides
    fun provideArticuloDao(db: AccesoDatos) = db.getArticuloDao()

    @Singleton
    @Provides
    fun provideCategoriaDao(db: AccesoDatos) = db.getCategoriaDao()

    @Singleton
    @Provides
    fun provideTipoIvaDao(db: AccesoDatos) = db.getTipoIvaDAo()

    @Singleton
    @Provides
    fun provideLineaTicketDao(db: AccesoDatos) = db.getLineaTicketDao()

    @Singleton
    @Provides
    fun provideCierreDao(db: AccesoDatos) = db.getCierreDao()

    @Singleton
    @Provides
    fun provideMetodoPagoDao(db: AccesoDatos) = db.getMetodoPagoDao()

    @Singleton
    @Provides
    fun provideTicketDao(db: AccesoDatos) = db.getTicketDao()
}