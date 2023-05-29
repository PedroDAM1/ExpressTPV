package com.pedro.expresstpv.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pedro.expresstpv.data.database.AccesoDatos
import com.pedro.expresstpv.data.database.dao.ArticuloDaoI
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
                db.execSQL("INSERT INTO tb_tipoiva (id, nombre, porcentaje) VALUES (0, 'SIN IVA', 0.00), (1, 'SUPER REDUCIDO', 4.00), (2, 'REDUCIDO', 10.00), (3, 'NORMAL', 21.00), (4, 'EXENTO', 0.00);")
                db.execSQL("INSERT INTO tb_metodopago(id, nombre) VALUES (0, 'SIN DEFINIR'), (1, 'TARJETA'), (2, 'EFECTIVO');")
                db.execSQL("INSERT INTO tb_categoria(id, nombre, color) VALUES (0, 'SIN CATEGORIA', '#FFFFFF');")
                db.execSQL("INSERT INTO tb_cierre(num_cierre, fecha) VALUES (0, NULL);")
                db.execSQL("INSERT INTO tb_ticket(num_ticket, num_cierre, id_metodopago, fecha, total) VALUES (0, 0, 0, NULL, 0.00);")
            }
        }
        val migration2to1 = object : Migration(2,1){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE IF EXISTS tb_lineaticket;")
                database.execSQL("""CREATE TABLE tb_lineaticket (
                  id INTEGER PRIMARY KEY NOT NULL,
                  num_ticket INTEGER NOT NULL,
                  descripcion TEXT NOT NULL DEFAULT '',
                  categoria_venta TEXT NOT NULL DEFAULT '',
                  cantidad INTEGER NOT NULL DEFAULT 0,
                  valor_iva REAL NOT NULL DEFAULT 0.0,
                  subtotal REAL NOT NULL DEFAULT 0.0,
                  total REAL NOT NULL DEFAULT 0.0,
                  FOREIGN KEY (num_ticket) REFERENCES tb_ticket(num_ticket)
                );
                """.trimMargin())
            }

        }

        database = Room.databaseBuilder(context, AccesoDatos::class.java, EXPRESSTPV_DATABASE_NAME)
            .addMigrations(migration2to1)
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
    fun provideArticuloDao(db: AccesoDatos): ArticuloDaoI{
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