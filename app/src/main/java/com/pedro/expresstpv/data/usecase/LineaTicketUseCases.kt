package com.pedro.expresstpv.data.usecase

import android.util.Log
import com.pedro.expresstpv.data.provider.LineaTicketRepository
import com.pedro.expresstpv.data.provider.TicketRepository
import com.pedro.expresstpv.domain.model.Articulo
import com.pedro.expresstpv.domain.model.LineaTicket
import com.pedro.expresstpv.domain.model.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LineaTicketUseCases @Inject constructor(
    private val lineaTicketRepository: LineaTicketRepository,
    private val ticketRepository: TicketRepository
) : BaseUseCase<LineaTicket>(lineaTicketRepository) {


//    @Inject
//    lateinit var ticketUseCase: TicketUseCase


    /**
     * Crearemos una nueva LineaTicket con la cantidad iniciada en 1
     */
    suspend fun crearLineaTicket(articulo: Articulo) {
        //La cantidad inicial al crear un ticket siempre sera uno
        val cantidad = 1
        //TODO, A implementar el subtotal
        val subtotal = 0.0
        val ticket = ticketRepository.getById(0)!!

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
        lineaTicketRepository.insert(lineaTicket)
    }

    /**
     * Aumenta la cantidad indicada por parametro
     */
    suspend fun aumentarCantidadLineaTicket(lineaTicket: LineaTicket, cantidad : Int){
        val precioIndividual = (lineaTicket.total/lineaTicket.cantidad) //Obtenemos el precio individual de la linea
        lineaTicket.cantidad += cantidad //Aumentamos la cantidad que le pasemos por parametro
        lineaTicket.total = (precioIndividual * lineaTicket.cantidad) // Actualizamos el total una vez que se haya aumentado la cantidad
        //TODO a implementar el subtotal de la linea
        lineaTicketRepository.update(lineaTicket)
    }

    suspend fun reducirCantidadLineaTicket(lineaTicket: LineaTicket, cantidad: Int){
        if (lineaTicket.cantidad == 1){
            // Si nos llega la ultima linea ticket deberemos de eliminar la row
           this.delete(lineaTicket)
        } else {
            aumentarCantidadLineaTicket(lineaTicket, -cantidad)
        }
    }

    suspend fun getLineaTicketActivo() : List<LineaTicket> = withContext(Dispatchers.Default){
        return@withContext lineaTicketRepository.getAll()
            .filter {
                it.ticket == ticketRepository.getById(0)!!
            }
    }

    fun getLineaTicketActivoFlow() : Flow<List<LineaTicket>>{
        return lineaTicketRepository.getAllFlow()
            .map {
                it.filter { it.ticket == ticketRepository.getById(0)!! }
            }
            .flowOn(Dispatchers.Default)
    }

    suspend fun eliminarTicketActivo(){
        val listaActivo = getLineaTicketActivo()
        Log.d("LINEATICKET USECASE", "Eliminando lista: $listaActivo")
        deleteList(listaActivo)
    }

    suspend fun getTotalFromLineaTicketsActivo() : Double{
        return getLineaTicketActivo().sumOf {
            it.total
        }
    }

    suspend fun updateLineaTicketsActivoToNewTicket(ticket: Ticket){
        val lista = getLineaTicketActivo()
        lista.forEach {
            it.ticket = ticket
        }
        updateAll(lista)
    }

}