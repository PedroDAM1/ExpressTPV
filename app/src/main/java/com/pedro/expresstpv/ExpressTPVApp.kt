package com.pedro.expresstpv

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pedro.expresstpv.data.database.AccesoDatos
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@HiltAndroidApp
class ExpressTPVApp : Application(){

//    companion object{
//        lateinit var database : AccesoDatos
//        private set
//    }
//
//    private val callback = object : RoomDatabase.Callback(){
//        override fun onCreate(db: SupportSQLiteDatabase) {
//            super.onCreate(db)
//            MainScope().launch {
//                database.getCategoriaDao().insertOrUpdateAll(AccesoDatos.CATEGORIAS.toList())
//                database.getTipoIvaDAo().insertOrUpdateAll(AccesoDatos.TIPOS_IVA.toList())
//                database.getArticuloDao().insertOrUpdateAll(AccesoDatos.ARTICULO.toList())
//            }
//        }
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        database = Room.databaseBuilder(this, AccesoDatos::class.java, "ExpressDB")
//            .addCallback(callback)
//            .build()
//    }

}