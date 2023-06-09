package com.pedro.expresstpv.data.usecase

import com.pedro.expresstpv.data.provider.TicketRepository
import com.pedro.expresstpv.domain.model.Cierre
import com.pedro.expresstpv.domain.model.MetodoPago
import com.pedro.expresstpv.domain.model.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketUseCase @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val cierreUseCase: CierreUseCase,
) : BaseUseCase<Ticket>(ticketRepository) {


    /**
     * Permite crear un ticket, deberemos de pasar el metodo de pago con el que se pago el ticket, el total y el subtotal(Que lo obtendremos de las lineaTickets)
     * @param metodoPago MetodoPago
     * @param total Double
     * @param subtotal Double
     */
    suspend fun crearTicket(metodoPago: MetodoPago, total : Double, subtotal : Double) : Ticket = withContext(Dispatchers.IO){
        val cierreActivo = cierreUseCase.getCierreActivo()
        val newNumTicket = getLastNumTicket()+1

        val ticket = Ticket(
            //Insertamos el ticket con un nuevo id
            id = 0,
            numTicket = newNumTicket,
            cierre = cierreActivo,
            subtotal = subtotal,
            metodoPago = metodoPago,
            total = total
        )

        this@TicketUseCase.insert(ticket)

        return@withContext getByNumTicket(newNumTicket)!!
    }

    /**
     * Devuelve el numero de ticket del ultimo ticket
     * @return Double
     */
    suspend fun getLastNumTicket() : Int = ticketRepository.getLastNumTicket()

    /**
     * Devuelve la lista de tickets cuyo cierre sea el activo
     * @return List<Ticket>
     */
    suspend fun getAllTicketFromCierreActivo() : List<Ticket> = withContext(Dispatchers.Default) {
        return@withContext this@TicketUseCase.getAll().filter {
            it.cierre == cierreUseCase.getCierreActivo()
        }
    }

    /**
     * Devuelve el flow de tickets cuyo cierre sea el activo
     * @return Flow<List<Ticket>>
     */
    suspend fun getAllTicketFromCierreActivoFlow() : Flow<List<Ticket>>{
        return this.getAllFlow().map {
            it.filter {ticket ->
                ticket.cierre == cierreUseCase.getCierreActivo()
            }
        }
            .flowOn(Dispatchers.Default)
    }


    /**
     * Devuelve el total de la suma de tickets que sean del cierre activo
     * @return Double
     */
    suspend fun getSumOfTicketsFromCierreActivo() : Double = withContext(Dispatchers.Default) {
        return@withContext getAllTicketFromCierreActivo().sumOf {
            it.total
        }
    }

    /**
     * Devuelve el ticket cuyo numero de ticket sea el pasado por parametro, si no lo encuentra, devolvera nulo
     * @param num Int. Numero del ticket que queremos buscar
     * @return Ticket?
     */
    suspend fun getByNumTicket(num : Int) : Ticket? = withContext(Dispatchers.Default) {
        return@withContext ticketRepository.getAll()
            .firstOrNull {
                it.numTicket == num
            }
    }

    /**
     * Devolveremos el ticket cuyo id sea 0
     * @return Ticket
     */
    suspend fun getTicketActivo() : Ticket = withContext(Dispatchers.IO) {
//        return@withContext getByNumTicket(0)!!
        return@withContext getById(0)!!
    }

    /**
     * Retornaremos el total de los tickets segun el metodo de pago que se ha usado,
     * es decir si hemos vendido 200 con efectivo en 3 tickets y 150 con tarjeta en 4 tickets,
     * retornaremos 200 si por parametro le pasamos el metodo de pago efectivo
     * @param metodoPago MetodoPago. Metodo de pago por el que filtraremos
     * @param filter Funcion lambda (opcional) por la que podremos meter algun filtro adicional
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

    /**
     * Actualiza la lista de tickets que antes tenian el cierre activo al nuevo cierre pasado por parametro
     * @param cierre Cierre. Cierre al que queremos actualizar
     */
    suspend fun updateTicketsFromCierreActivoToNewCierre(cierre : Cierre){
        val lista = getAllTicketFromCierreActivo()
        lista.forEach {
            it.cierre = cierre
        }
        updateAll(lista)

    }

    /**
     * Devuelve una lista de tickets cuya fecha se encuentre entre la fecha de inicio y la fecha de fin
     * @param fechaInicio LocalDateTime
     * @param fechaFin LocalDateTime
     * @return List<Ticket>. Lista de tickets filtrada por fechas
     */
    suspend fun getTicketBetweenFechas(fechaInicio : LocalDateTime, fechaFin : LocalDateTime) : List<Ticket> = withContext(Dispatchers.Default){
        val lista = this@TicketUseCase.getAll()
            .filter {
                //Devolveremos la lista de tickets cuya fecha este entre la fecha de inicio y la fecha de fin
                //Si la fecha trae valores nulos, no devolveremos ese ticket
                return@filter fechaInicio.isBefore(it.fecha) && fechaFin.isAfter(it.fecha)
            }
        return@withContext lista
    }

    fun getTicketBetweenFechasFlow(fechaInicio : LocalDateTime, fechaFin : LocalDateTime) =
        this.getAllFlow().map {
            it.filter { ticket ->
                ticket.fecha?.isAfter(fechaInicio) ?: false && ticket.fecha?.isBefore(fechaFin) ?: false
            }
        }
            .flowOn(Dispatchers.Default)

}