package com.pedro.expresstpv.data.database.dao

import androidx.room.*
import com.pedro.expresstpv.data.database.entities.LineaTicketEntity
import com.pedro.expresstpv.domain.model.LineaTicket
import kotlinx.coroutines.flow.Flow

@Dao
interface LineaTicketDao {
    @Insert
    suspend fun insert(lineaTicket: LineaTicketEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(listLineaTicket: List<LineaTicketEntity>)

    @Update
    suspend fun update(lineaTicket: LineaTicketEntity)

    @Delete
    suspend fun delete(lineaTicket: LineaTicketEntity)

    @Query("DELETE FROM tb_lineaticket")
    suspend fun deleteAll()

    @Query("SELECT * FROM tb_lineaticket WHERE id=:id")
    fun getById(id: Int): Flow<LineaTicketEntity?>

    @Query("SELECT * FROM tb_lineaticket")
    fun getAll(): Flow<List<LineaTicketEntity>>

    @Query("SELECT * FROM tb_lineaticket WHERE num_ticket=:numTicket")
    fun getLineaTicketByNumTicket(numTicket : Int) : Flow<List<LineaTicketEntity>>



}