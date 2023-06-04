package com.pedro.expresstpv.data.usecase

import com.pedro.expresstpv.data.provider.TicketRepository
import com.pedro.expresstpv.domain.model.Cierre
import com.pedro.expresstpv.domain.model.MetodoPago
import com.pedro.expresstpv.domain.model.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketUseCase @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val cierreUseCase: CierreUseCase,
) : BaseUseCase<Ticket>(ticketRepository) {


    suspend fun crearTicket(metodoPago: MetodoPago, total : Double, subtotal : Double) : Ticket = withContext(Dispatchers.IO){
        val cierreActivo = cierreUseCase.getCierreActivo()
        val newNumTicket = getLastNumTicket()+1

        val ticket = Ticket(
            //Insertamos el ticket con un nuevo id
            id = 0,
            numTicket = newNumTicket,
            cierre = cierreActivo,
            metodoPago = metodoPago,
            total = total
        )

        ticketRepository.insert(ticket)

        return@withContext getByNumTicket(newNumTicket)!!
    }

    suspend fun getLastNumTicket() : Int = ticketRepository.getLastNumTicket()

    suspend fun getAllTicketFromCierreActivo() : List<Ticket> = withContext(Dispatchers.Default) {
        return@withContext this@TicketUseCase.getAll().filter {
            it.cierre == cierreUseCase.getCierreActivo()
        }
    }

    suspend fun getAllTicketFromCierreActivoFlow() : Flow<List<Ticket>>{
        return this.getAllFlow().map {
            it.filter {ticket ->
                ticket.cierre == cierreUseCase.getCierreActivo()
            }
        }
            .flowOn(Dispatchers.Default)
    }

    suspend fun getSumOfTicketsFromCierreActivo() : Double = withContext(Dispatchers.Default) {
        return@withContext getAllTicketFromCierreActivo().sumOf {
            it.total
        }
    }

    suspend fun getByNumTicket(num : Int) : Ticket? = withContext(Dispatchers.Default) {
        return@withContext ticketRepository.getAll()
            .firstOrNull {
                it.numTicket == num
            }
    }

    suspend fun getTicketActivo() : Ticket = withContext(Dispatchers.IO) {
        return@withContext getByNumTicket(0)!!
    }

    /**
     * Retornaremos el total de los tickets segun el metodo de pago que se ha usado,
     * es decir si hemos vendido 200 con efectivo en 3 tickets y 150 con tarjeta en 4 tickets,
     * retornaremos 200 si por parametro le pasamos el metodo de pago efectivo
     */
    suspend fun getTotalTicketPorMetodoPago(metodoPago: MetodoPago, filter : (Ticket) -> Boolean = {true}) : Double{
        return getAll().filter {
            it.metodoPago == metodoPago && filter(it)
        }.sumOf {
            it.total
        }
    }

    suspend fun getTotalTicketPorMetodoPagoParaCierreActivo(metodoPago: MetodoPago) : Double = withContext(Dispatchers.IO){
        val cierreActivo = cierreUseCase.getCierreActivo()
        return@withContext getTotalTicketPorMetodoPago(metodoPago) {
            it.cierre == cierreActivo
        }
    }

    suspend fun updateTicketsFromCierreActivoToNewCierre(cierre : Cierre){
        val lista = getAllTicketFromCierreActivo()
        lista.forEach {
            it.cierre = cierre
        }
        updateAll(lista)

    }

}