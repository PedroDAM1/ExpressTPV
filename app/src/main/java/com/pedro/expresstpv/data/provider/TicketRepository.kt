package com.pedro.expresstpv.data.provider

import android.util.Log
import com.pedro.expresstpv.data.database.dao.TicketDao
import com.pedro.expresstpv.data.database.entities.TicketEntity
import com.pedro.expresstpv.data.usecase.CierreUseCase
import com.pedro.expresstpv.data.usecase.MetodoPagoUseCase
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.domain.model.Cierre
import com.pedro.expresstpv.domain.model.MetodoPago
import com.pedro.expresstpv.domain.model.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketRepository @Inject constructor(
    private val ticketDao: TicketDao,
    private val cierreUseCase: CierreUseCase,
    private val metodoPagoUseCase: MetodoPagoUseCase
) : BaseRepository<Ticket, TicketEntity>(ticketDao) {



    suspend fun getLastNumTicket() : Int = withContext(Dispatchers.IO){
        return@withContext ticketDao.getLastNumTicket()
    }

    private fun Ticket.toEntity() : TicketEntity{
        return TicketEntity(
            id = this.id,
            numTicket = this.numTicket,
            numCierre = this.cierre.numCierre,
            idMetodopago = this.metodoPago.id,
            fecha = this.fecha,
            total = this.total
        )
    }
    override suspend fun toEntity(domain: Ticket): TicketEntity {
        return domain.toEntity()
    }

    override suspend fun toDomain(entity: TicketEntity): Ticket {
        //Los objetos con id 0 de la base de datos son objetos base para cuando aun no se le asigno nada al objeto
        val cierre : Cierre = cierreUseCase.getCierreByNumCierre(entity.numCierre) ?: cierreUseCase.getCierreActivo()
        val metodoPago : MetodoPago = metodoPagoUseCase.getById(entity.idMetodopago) ?: metodoPagoUseCase.getMetodoPagoByDefault()

        var fecha = entity.fecha
        if(fecha!=null){
            fecha = Functions.formatLocalDateTime(fecha)
        }
        Log.d("FECHA", "$fecha")

        val ticket = Ticket(
            id = entity.id,
            numTicket = entity.numTicket,
            cierre = cierre,
            metodoPago = metodoPago,
            fecha = fecha,
            total = entity.total

        )
        Log.d("GET TICKET", "Se ha mapeado un ticket: $ticket")
        return ticket
    }

}