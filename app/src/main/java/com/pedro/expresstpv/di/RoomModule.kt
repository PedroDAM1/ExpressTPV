package com.pedro.expresstpv.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pedro.expresstpv.data.database.AccesoDatos
import com.pedro.expresstpv.data.database.dao.ArticuloDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
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

    private lateinit var database : AccesoDatos


    /**
     * La anotacion @Singleton sirve para decirle a hilt que solo nos cree una instancia, y que use esa misma
     * instancia para toda la app donde se le llame
     * La anotacion @Provider sirve para proveer la base de datos en las clases que esten preparadas para la inyeccion
     */
    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) : AccesoDatos {
        //Este metodo se ejecutara unicamente al crear la base de datos por primera vez, añadiendo algo de informacion
        val callback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL("INSERT INTO tb_tipoiva (id, nombre, porcentaje) VALUES (1, 'SUPER REDUCIDO', 4.00), (2, 'REDUCIDO', 10.00), (3, 'NORMAL', 21.00), (4, 'EXENTO', 0.00);")
                db.execSQL("INSERT INTO tb_metodopago(id, nombre) VALUES (1, 'TARJETA'), (2, 'EFECTIVO');")
                db.execSQL("INSERT INTO tb_categoria(id, nombre, color) VALUES (1, 'SIN CATEGORIA', '#FFFFFF');")
                db.execSQL("INSERT INTO tb_cierre(num_cierre, fecha) VALUES (1, '0001-01-01 00:00');")
            }
        }
        database = Room.databaseBuilder(context, AccesoDatos::class.java, EXPRESSTPV_DATABASE_NAME)
            .addCallback(callback)
            .build()

        return database
    }



    /**********************************************************************************************
     * Cada una de las funciones siguientes nos permitira obtener cada uno de los diferentes daos
     * para poder trabajar con ellos
     **********************************************************************************************/
    @Singleton
    @Provides
    fun provideArticuloDao(db: AccesoDatos): ArticuloDao{
        return db.getArticuloDao()
    }

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