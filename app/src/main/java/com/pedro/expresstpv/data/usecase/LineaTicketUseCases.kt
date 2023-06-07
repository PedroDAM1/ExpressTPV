package com.pedro.expresstpv.data.usecase

import android.util.Log
import com.pedro.expresstpv.data.provider.LineaTicketRepository
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
    private val ticketUseCase: TicketUseCase
) : BaseUseCase<LineaTicket>(lineaTicketRepository) {

    /**
     * Crearemos una nueva LineaTicket con la cantidad iniciada en 1
     */
    suspend fun crearLineaTicket(articulo: Articulo) {
        //La cantidad inicial al crear un ticket siempre sera uno
        val cantidad = 1
        //TODO, A implementar el subtotal
        val subtotal = 0.0
        val ticket = ticketUseCase.getTicketActivo()

        val lineaTicket = LineaTicket(
            ticket = ticket,
            descripcion = articulo.nombre,
            categoriaVenta = articulo.categoria.nombre,
            cantidad = cantidad,
            valorIva = articulo.tipoIva.porcentaje,
            subTotal = subtotal,
            total = articulo.precio * cantidad
        )

        this.insert(lineaTicket)
    }

    suspend fun crearLineaTicketActivo(articulo: Articulo){
        val linea = getLineaTicketWhenFeaturesActivo(articulo.nombre, articulo.categoria.nombre, articulo.tipoIva.porcentaje)
        //Si la liena existe, debemos actualizarla
        if (linea != null){
            aumentarCantidadLineaTicket(linea, 1)
        } else {
            crearLineaTicket(articulo)
        }
    }

    suspend fun getLineaTicketWhenFeaturesActivo(descripcion : String, categoriaVenta : String, valorIva : Double) : LineaTicket?{
        return getLineaTicketWhenFeatures(descripcion, categoriaVenta, valorIva, ticketUseCase.getTicketActivo())
    }

    suspend fun getLineaTicketWhenFeatures(descripcion : String, categoriaVenta : String, valorIva : Double, ticket : Ticket) : LineaTicket?{
        return this.getAll()
            .firstOrNull {
                it.descripcion == descripcion &&
                it.categoriaVenta == categoriaVenta &&
                it.valorIva == valorIva &&
                it.ticket == ticket
            }
    }

    /**
     * Aumenta la cantidad indicada por parametro
     */
    suspend fun aumentarCantidadLineaTicket(lineaTicket: LineaTicket, cantidad: Int) {
        val copyLineaTicket = lineaTicket.copy()
        val precioIndividual = (copyLineaTicket.total / copyLineaTicket.cantidad) //Obtenemos el precio individual de la linea
        copyLineaTicket.cantidad += cantidad //Aumentamos la cantidad que le pasemos por parametro
        copyLineaTicket.total = (precioIndividual * copyLineaTicket.cantidad) // Actualizamos el total una vez que se haya aumentado la cantidad
        //TODO a implementar el subtotal de la linea
        this.update(copyLineaTicket)
    }

    /**
     * Reduce la cantidad de las lineatickets en funcion de la cantidad
     */
    suspend fun reducirCantidadLineaTicket(lineaTicket: LineaTicket, cantidad: Int) {
        if (lineaTicket.cantidad <= cantidad) {
            // Si nos llega la ultima linea ticket deberemos de eliminar la row
            this.delete(lineaTicket)
        } else {
            aumentarCantidadLineaTicket(lineaTicket, -cantidad)
        }
    }

    /**
     * Obtiene la lista de lineatickets cuyo ticket sea 0
     */
    suspend fun getLineaTicketActivo(): List<LineaTicket> = withContext(Dispatchers.Default) {
        val ticketActivo = ticketUseCase.getTicketActivo()
        return@withContext lineaTicketRepository.getAll()
            .filter {
                it.ticket == ticketActivo
            }
    }

    /**
     * Obtiene todas las linea tickets cuyo ticket sea 0.
     * Devuelve un flow
     */
    fun getLineaTicketActivoFlow(): Flow<List<LineaTicket>> {
        return lineaTicketRepository.getAllFlow()
            .map {
                it.filter { it.ticket == ticketUseCase.getTicketActivo() }
            }
            .flowOn(Dispatchers.Default)
    }

    /**
     * Elimina todas las lineatickets que tengan el ticket 0
     */
    suspend fun eliminarTicketActivo() {
        val listaActivo = getLineaTicketActivo()
        Log.d("LINEATICKET USECASE", "Eliminando lista: $listaActivo")
        deleteList(listaActivo)
    }

    /**
     * Obtiene el total de sumar todas las lineatickets con el ticket 0
     */
    suspend fun getTotalFromLineaTicketsActivo() = getTotalFromLineaTickets(getLineaTicketActivo())

    suspend fun getSubtotalFromLineaTicketActivo() = getSubtotalFromLineaTickets(getLineaTicketActivo())

    fun getTotalFromLineaTickets(list : List<LineaTicket>) = list.sumOf { it.total }

    fun getSubtotalFromLineaTickets(list : List<LineaTicket>) = list.sumOf { it.subTotal }

    /**
     * Actualiza todas las lineatickets con el ticket 0 al nuevo ticket pasado por parametro
     */
    suspend fun updateLineaTicketsActivoToNewTicket(ticket: Ticket) {
        val lista = this.getLineaTicketActivo()
            .map {
                it.copy(
                    ticket = ticket
                )
            }
        updateAll(lista)
    }

    suspend fun getLineaTicketAcumuladosFromTickets(listaTickets: List<Ticket>) : List<LineaTicket> = withContext(Dispatchers.Default){
        val lista : List<LineaTicket> = getAll()
            .filter {linea ->
                listaTickets.contains(linea.ticket)
            }
            .map {
                it.copy(
                    ticket = null
                )
            }

        Log.d("LISTA DEVUELTA", lista.toString())

        return@withContext acumularLineasTickets(lista)
    }

    fun getLineaTicketsAcumuladosFromTicketsFlow(listaTickets: List<Ticket>): Flow<List<LineaTicket>> {
        return getAllFlow().map {
            it.filter { linea ->
                //Comprobamos si la lineaticket pertenece a alguno de los tickets que se han pasado
                listaTickets.contains(linea.ticket)
            }
        }
            .map {
                acumularLineasTickets(it)
            }
            .flowOn(Dispatchers.Default)
    }

    suspend fun acumularLineasTickets(lista: List<LineaTicket>): List<LineaTicket> = withContext(Dispatchers.Default){
        val grouped = lista.groupBy {
            //Agrupamos cada lineaticket por los campos que lo definen
            it.descripcion + it.categoriaVenta + it.valorIva
        }.map { (_, lineas) ->
            //Mapeamos las lineas haciendo que los campos de cantidad, subtotal y total se sumen en uno solo
            /* De esta forma para una lineaticket cuya descripcion, categoria y iva sean iguales, sumaremos las cantidades y los totales */
            lineas.reduce { acc, linea ->
                acc.cantidad += linea.cantidad
                acc.subTotal += linea.subTotal
                acc.total += linea.total

                acc
            }
        }
        return@withContext grouped
    }
}