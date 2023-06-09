package com.pedro.expresstpv.data.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * De esta clase heredaran todas las interfaces Daos que se gestionaran a traves de room.
 * Aqui definieremos los metodos comunes de cada uno, sin embargo las consultas de tipo @Query, deben
 * definirse especificamente en cada interfaz
 */
interface IBaseDao<Entity> {

    /**
     * Nos permite obtener un flow de datos de del dao que herede esta clase
     * @return Flow de datos de una clase Entity
     */
    fun getAll() : Flow<List<Entity>>

    /**
     * Permite la inserccion de datos directa en la base de datos de room
     * No importa que ids le pasemos, ya que se la generara room
     * @param entity Entidad a insertar
     */
    @Insert
    suspend fun insert(entity: Entity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entity: Entity)

    /**
     * Permite insertar una lista de entidades a la base de datos de room
     * @param list Lista de entidades a insertar
     */
    @Insert
    suspend fun insertAll(list: List<Entity>)
    /**
     * Permite la actualizacion de datos directa en la base de datos de room
     * @param entity Entidad a Actualizar
     */
    @Update
    suspend fun update(entity: Entity)

    /**
     * Permite actualizar una lista de entidades a la base de datos de room (La actualizacion la hace a traves del id)
     * @param list Lista de entidades a actualizar
     */
    @Update
    suspend fun updateAll(list: List<Entity>)

    /**
     * Permite borrar una row de la base de datos correspondiente al elemento pasado (la eliminacion la hace a traves del id)
     * @param entity Entidad a eliminar
     */
    @Delete
    suspend fun delete(entity: Entity)

    /**
     * Permite borrar una lista de rows de la base de datos correspondiente al elemento pasado (la eliminacion la hace a traves del id)
     * @param list Lista a eliminar
     */
    @Delete
    suspend fun deleteList(list: List<Entity>)

    /**
     * Elimina toda la tabla
     */
    suspend fun deleteAll()
}