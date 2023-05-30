package com.pedro.expresstpv.data.database.dao

import androidx.room.*
import com.pedro.expresstpv.data.database.entities.TicketEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao : IBaseDao<TicketEntity> {
//    @Insert
//    override suspend fun insert(entity: TicketEntity)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertOrUpdateAll(listTicket: List<TicketEntity>)
//
//    @Update
//    suspend fun update(ticket: TicketEntity)
//
//    @Delete
//    suspend fun delete(ticket: TicketEntity)

    @Query("DELETE FROM tb_ticket")
    override suspend fun deleteAll()

    @Query("SELECT * FROM tb_ticket WHERE num_ticket=:numTicket")
    fun getByNumTicket(numTicket: Int): Flow<TicketEntity?>

    @Query("SELECT * FROM tb_ticket")
    override fun getAll(): Flow<List<TicketEntity>>

    @Query("SELECT num_ticket FROM tb_ticket ORDER BY num_ticket DESC LIMIT 1")
    suspend fun getLastNumTicket() : Int

}