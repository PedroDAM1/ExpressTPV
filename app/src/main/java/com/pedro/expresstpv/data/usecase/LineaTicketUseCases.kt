package com.pedro.expresstpv.data.usecase

import android.util.Log
import com.pedro.expresstpv.data.provider.LineaTicketRepository
import com.pedro.expresstpv.data.provider.TicketRepository
import com.pedro.expresstpv.domain.model.Articulo
import com.pedro.expresstpv.domain.model.LineaTicket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LineaTicketUseCases @Inject constructor(
    private val lineaTicketRepository: LineaTicketRepository,
    private val ticketRepository: TicketRepository
) {

    private val _lineaTicketFlow = lineaTicketRepository.getAllLineaTicket()

    /**
     * Crearemos una nueva LineaTicket con la cantidad iniciada en 1
     */
    suspend fun crearLineaTicket(articulo: Articulo) {
        //La cantidad inicial al crear un ticket siempre sera uno
        val cantidad = 1
        //TODO, A implementar el subtotal
        val subtotal = 0.0
        val ticket = ticketRepository.getTicketByNumTicket(0)!!

        val lineaTicket = LineaTicket(
            ticket = ticket,
            descripcion = articulo.nombre,
            categoriaVenta = articulo.categoria.nombre,
            cantidad = 1,
            valorIva = articulo.tipoIva.porcentaje,
            subTotal = subtotal,
            total = articulo.precio*cantidad
        )

        Log.d("CREAR LINEATICKET", "Se ha creado la lineaTicket: $lineaTicket")
        lineaTicketRepository.insertLineaTicket(lineaTicket)
    }

    /**
     * Obtendremos todas las lineasTickets que tengan el idTicket en 0
     * lo que signiffica que son los tickets actuales de la pantalla de ventas
     */
    fun getLineaTicketActivoFlow() : Flow<List<LineaTicket>>{
        return _lineaTicketFlow.map {listaLineaTickets ->
            listaLineaTickets.filter {
                it.ticket.numTicket == 0
            }
        }
            .flowOn(Dispatchers.IO)
    }

    suspend fun getLineaTicketActivo() : List<LineaTicket> = withContext(Dispatchers.IO){
        return@withContext lineaTicketRepository.getLineasTicketsByNumTicket(0).first()
    }

    /**
     * Aumenta la cantidad indicada por parametro
     */
    suspend fun aumentarCantidadLineaTicket(lineaTicket: LineaTicket, cantidad : Int){
        val precioIndividual = (lineaTicket.total/lineaTicket.cantidad) //Obtenemos el precio individual de la linea
        lineaTicket.cantidad += cantidad //Aumentamos la cantidad que le pasemos por parametro
        lineaTicket.total = (precioIndividual * lineaTicket.cantidad) // Actualizamos el total una vez que se haya aumentado la cantidad
        //TODO a implementar el subtotal de la linea
        lineaTicketRepository.updateLineaTicket(lineaTicket)
    }

    suspend fun reducirCantidadLineaTicket(lineaTicket: LineaTicket, cantidad: Int){
        if (lineaTicket.cantidad == 1){
            // Si nos llega la ultima linea ticket deberemos de eliminar la row
            lineaTicketRepository.deleteLineaTicket(lineaTicket)
        } else {
            aumentarCantidadLineaTicket(lineaTicket, -cantidad)
        }
    }

    suspend fun eliminarTicketActivo(){
        lineaTicketRepository.deleteListaLineaTickets(getLineaTicketActivo())
    }

    suspend fun getTotalFromLineaTicketsActivo() : Double{
        return getLineaTicketActivo().sumOf {
            it.total
        }
    }

}