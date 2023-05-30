package com.pedro.expresstpv.data.database.dao

import androidx.room.*
import com.pedro.expresstpv.data.database.entities.LineaTicketEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LineaTicketDao : IBaseDao<LineaTicketEntity> {
//    @Insert
//    override suspend fun insert(entity: LineaTicketEntity)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertOrUpdateAll(listLineaTicket: List<LineaTicketEntity>)
//
//    @Update
//    suspend fun update(lineaTicket: LineaTicketEntity)
//
//    @Update
//    suspend fun updateAll(listLineaTicket: List<LineaTicketEntity>)

//    @Delete
//    suspend fun delete(lineaTicket: LineaTicketEntity)

//    @Delete
//    suspend fun deleteList(listaLineaTicket : List<LineaTicketEntity>)

    @Query("DELETE FROM tb_lineaticket")
    override suspend fun deleteAll()

    @Query("SELECT * FROM tb_lineaticket WHERE id=:id")
    fun getById(id: Int): Flow<LineaTicketEntity?>

    @Query("SELECT * FROM tb_lineaticket")
    override fun getAll(): Flow<List<LineaTicketEntity>>

    @Query("SELECT * FROM tb_lineaticket WHERE num_ticket=:numTicket")
    fun getLineaTicketByNumTicket(numTicket : Int) : Flow<List<LineaTicketEntity>>



}