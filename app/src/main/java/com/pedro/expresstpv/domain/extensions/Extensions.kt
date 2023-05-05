package com.pedro.expresstpv.domain.extensions

import com.pedro.expresstpv.data.database.AccesoDatos
import com.pedro.expresstpv.data.database.entities.*
import com.pedro.expresstpv.data.provider.toDomain
import com.pedro.expresstpv.domain.model.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Singleton


public class Extensions @Inject constructor(private val db : AccesoDatos) {



    /* CIERRE */

    fun CierreEntity.toDomain() = Cierre(
        numCierre = numCierre,
        fecha = fecha
    )

    fun Cierre.toEntity() = CierreEntity(
        numCierre = numCierre,
        fecha = fecha
    )

    /* METODO PAGO */

    fun MetodoPagoEntity.toDomain() = MetodoPago(
        id = id,
        nombre = nombre
    )

    fun MetodoPago.toEntity() = MetodoPagoEntity(
        id = id,
        nombre = nombre
    )

    /* TICKET */

    suspend fun TicketEntity.toDomain() = Ticket(
        numTicket = numTicket,
        cierre = db.getCierreDao().getByNumCierre(numCierre)!!.toDomain(),
        metodoPago = db.getMetodoPagoDao().getById(idMetodopago)!!.toDomain(),
        fecha = fecha,
        total = total
    )

    fun TicketConCierreYMetodoPago.toDomain() = Ticket(
        numTicket = ticketEntity.numTicket,
        cierre = cierreEntity.toDomain(),
        metodoPago = metodoPagoEntity.toDomain(),
        fecha = ticketEntity.fecha,
        total = ticketEntity.total
    )

    fun Ticket.toEntity() = TicketEntity(
        numTicket = numTicket,
        numCierre = cierre.numCierre,
        idMetodopago = metodoPago.id,
        fecha = fecha,
        total = total
    )


    /* LINEA TICKET */

    suspend fun LineaTicketEntity.toDomain() : LineaTicket{
        return LineaTicket(
            id = id,
            articulo = db.getArticuloDao().getArticuloConCategoriaYTipoIvaById(idArticulo)!!.toDomain(),
            ticket = db.getTicketDao().getTicketConCierreYMetodoPagoById(numTicket)!!.toDomain(),
            descripcion = descripcion,
            cantidad = cantidad,
            valorIva = valorIva,
            subTotal = subtotal,
            total = total
        )
    }

//    suspend fun LineaTicketConArticuloYTicket.toDomain() = LineaTicket(
//        id = lineaTicketEntity.id,
//        articulo = articuloEntity.toDomain(),
//        ticket = ticketEntity.toDomain(),
//        descripcion = lineaTicketEntity.descripcion,
//        cantidad = lineaTicketEntity.cantidad,
//        valorIva = lineaTicketEntity.valorIva,
//        subTotal = lineaTicketEntity.subtotal,
//        total = lineaTicketEntity.total
//    )
}