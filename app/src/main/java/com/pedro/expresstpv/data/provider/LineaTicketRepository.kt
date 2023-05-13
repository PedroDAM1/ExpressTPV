package com.pedro.expresstpv.data.provider

import com.pedro.expresstpv.data.database.dao.LineaTicketDao
import com.pedro.expresstpv.data.database.entities.LineaTicketEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LineaTicketRepository @Inject constructor(lineaTicketDao: LineaTicketDao) {

    private val _lineaTicketEntityFlow : Flow<List<LineaTicketEntity>> = lineaTicketDao.getAll()
    private val _lineaTicketFlow = _lineaTicketEntityFlow
        .map {

        }
        .flowOn(Dispatchers.IO)


    fun getAllLineaTicket() = _lineaTicketFlow

    private suspend fun lineaTicketToDomain(){

    }

}