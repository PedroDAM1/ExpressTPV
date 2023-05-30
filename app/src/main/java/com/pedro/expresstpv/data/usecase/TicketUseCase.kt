package com.pedro.expresstpv.data.usecase

import com.pedro.expresstpv.data.provider.TicketRepository
import com.pedro.expresstpv.domain.model.MetodoPago
import com.pedro.expresstpv.domain.model.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketUseCase @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val cierreUseCase: CierreUseCase
) : BaseUseCase<Ticket>(ticketRepository) {

    /* Esta dependencia la inyectamos directamente aqui por temas de que hilt peta ya que lineaTicket necesita tambien dependencia con ticket y se hace un ciclo xD */
    @Inject
    lateinit var lineaTicketUseCases: LineaTicketUseCases

    suspend fun crearTicket(metodoPago: MetodoPago, total : Double, subtotal : Double) = withContext(Dispatchers.IO){
        val cierreActivo = cierreUseCase.getCierreActivo()
        val lastTicket = getLastNumTicket()

        val ticket = Ticket(
            //Insertamos el ticket con un nuevo id
            id = 0,
            numTicket = lastTicket+1,
            cierre = cierreActivo,
            metodoPago = metodoPago,
            total = total
        )

        ticketRepository.insert(ticket)

        lineaTicketUseCases.updateLineaTicketsActivoToNewTicket(ticket)
    }

    suspend fun getLastNumTicket() : Int = ticketRepository.getLastNumTicket()

    suspend fun getByNumTicket(num : Int) : Ticket? = withContext(Dispatchers.Default) {
        return@withContext ticketRepository.getAll()
            .firstOrNull {
                it.numTicket == num
            }
    }

    suspend fun getTicketActivo() : Ticket = withContext(Dispatchers.IO) {
        return@withContext getByNumTicket(0)!!
    }

}