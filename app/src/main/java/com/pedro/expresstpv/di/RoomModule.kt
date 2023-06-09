package com.pedro.expresstpv.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
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

    private fun inizialiteRoom(db : SupportSQLiteDatabase){
        db.execSQL("INSERT OR REPLACE INTO tb_tipoiva (id, nombre, porcentaje) VALUES (0, 'SIN IVA', 0.00), (1, 'SUPER REDUCIDO', 4.00), (2, 'REDUCIDO', 10.00), (3, 'NORMAL', 21.00), (4, 'EXENTO', 0.00);")
        db.execSQL("INSERT OR REPLACE INTO tb_metodopago(id, nombre) VALUES (0, 'SIN DEFINIR'), (1, 'TARJETA'), (2, 'EFECTIVO');")
        db.execSQL("INSERT OR REPLACE INTO tb_categoria(id, nombre, color) VALUES (0, 'SIN CATEGORIA', '#FFFFFF');")
        db.execSQL("INSERT OR REPLACE INTO tb_cierre(id, num_cierre, fecha) VALUES (0, 0, NULL);")
        db.execSQL("INSERT OR REPLACE INTO tb_ticket(id, num_ticket, num_cierre, id_metodopago, fecha, total) VALUES (0, 0, 0, 0, NULL, 0.00);")
    }

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
                inizialiteRoom(db)
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                onCreate(db)
            }
        }

        database = Room.databaseBuilder(context, AccesoDatos::class.java, EXPRESSTPV_DATABASE_NAME)
            .fallbackToDestructiveMigration()
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


//val migration2to3 = object : Migration(2,3){
//    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL("DROP TABLE IF EXISTS tb_lineaticket;")
//        database.execSQL("""CREATE TABLE tb_lineaticket (
//                  id INTEGER PRIMARY KEY NOT NULL,
//                  num_ticket INTEGER NOT NULL,
//                  descripcion TEXT NOT NULL DEFAULT '',
//                  categoria_venta TEXT NOT NULL DEFAULT '',
//                  cantidad INTEGER NOT NULL DEFAULT 0,
//                  valor_iva REAL NOT NULL DEFAULT 0.0,
//                  subtotal REAL NOT NULL DEFAULT 0.0,
//                  total REAL NOT NULL DEFAULT 0.0,
//                  FOREIGN KEY (num_ticket) REFERENCES tb_ticket(num_ticket)
//                );
//                """.trimMargin())
//
//        database.execSQL("DROP TABLE IF EXISTS tb_ticket;")
//        database.execSQL("DROP TABLE IF EXISTS tb_cierre")
//
//        database.execSQL("""CREATE TABLE tb_cierre (
//                    |id INTEGER PRIMARY KEY NOT NULL,
//                    |num_cierre INTEGER NOT NULL,
//                    |fecha TEXT NULL DEFAULT CURRENT_TIMESTAMP
//                    |);""".trimMargin())
//        database.execSQL("CREATE UNIQUE INDEX index_tb_cierre_num_cierre ON tb_cierre(num_cierre);")
//
//
//        database.execSQL("""CREATE TABLE tb_ticket (
//                    |id INTEGER PRIMARY KEY NOT NULL,
//                    |num_ticket INTEGER NOT NULL,
//                    |num_cierre INTEGER NOT NULL,
//                    |id_metodopago INTEGER NOT NULL,
//                    |fecha TEXT NULL DEFAULT CURRENT_TIMESTAMP,
//                    |subtotal REAL NOT NULL DEFAULT 0.0,
//                    |total REAL NOT NULL DEFAULT 0.0,
//                    |FOREIGN KEY (num_cierre) REFERENCES tb_cierre(num_cierre),
//                    |FOREIGN KEY (id_metodopago) REFERENCES tb_metodopago(id)
//                    |);
//                """.trimMargin())
//        database.execSQL("CREATE UNIQUE INDEX index_tb_ticket_num_ticket ON tb_ticket(num_ticket);")
//    }
//}

//val migration1to2 = object : Migration(2,3){
//    override fun migrate(database: SupportSQLiteDatabase) {
//        // Agregar la nueva columna "id" como clave primaria
//        database.execSQL("ALTER TABLE tb_ticket ADD COLUMN id INTEGER NOT NULL DEFAULT 0;")
//        database.execSQL("UPDATE tb_ticket SET id = num_ticket;")
//        database.execSQL("""CREATE TABLE tb_ticket_temp (
//                    |id INTEGER NOT NULL PRIMARY KEY,
//                    |num_ticket INTEGER NOT NULL,
//                    |num_cierre INTEGER NOT NULL,
//                    |id_metodopago INTEGER NOT NULL,
//                    |fecha TEXT NULL,
//                    |subtotal REAL NOT NULL DEFAULT 0.0,
//                    |total REAL NOT NULL DEFAULT 0.0
//                    |);""".trimMargin())
//        database.execSQL("INSERT INTO tb_ticket_temp (id, num_ticket, num_cierre, id_metodopago, fecha, total) SELECT id, num_ticket, num_cierre, id_metodopago, fecha, total FROM tb_ticket;")
//        database.execSQL("DROP TABLE tb_ticket;")
//        database.execSQL("ALTER TABLE tb_ticket_temp RENAME TO tb_ticket;")
//        // Agregar un índice en la columna "num_ticket"
//
//
//        database.execSQL("ALTER TABLE tb_cierre ADD COLUMN id INTEGER NOT NULL DEFAULT 0;")
//        database.execSQL("UPDATE tb_cierre SET id = num_cierre;")
//        database.execSQL("""CREATE TABLE tb_cierre_temp (
//                    |id INTEGER NOT NULL PRIMARY KEY,
//                    |num_cierre INTEGER NOT NULL,
//                    |fecha TEXT NULL
//                    |);""".trimMargin())
//        database.execSQL("INSERT INTO tb_cierre_temp (id, num_cierre, fecha) SELECT id, num_cierre, fecha FROM tb_cierre;")
//        database.execSQL("DROP TABLE tb_cierre;")
//        database.execSQL("ALTER TABLE tb_cierre_temp RENAME TO tb_cierre;")
//        // Agregar un índice en la columna "num_ticket"
//    }
//
//}