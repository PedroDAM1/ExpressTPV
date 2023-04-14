package com.pedro.expresstpv.domain.extensions

import com.pedro.expresstpv.data.database.AccesoDatos
import com.pedro.expresstpv.data.database.entities.*
import com.pedro.expresstpv.domain.model.*
import javax.inject.Inject



class Extensions @Inject constructor(private val db : AccesoDatos) {

    /* ARTICULO */

    fun ArticuloConCategoriaETipoIva.toDomain() = Articulo(
        id = articuloEntity.id,
        categoria = categoriaEntity.toDomain(),
        tipoIva = tipoIvaEntity.toDomain(),
        nombre = articuloEntity.nombre,
        precio = articuloEntity.precio
    )

    suspend fun ArticuloEntity.toDomain() : Articulo{
        val tempId = this@toDomain.id
        val tempCategoria = db.getCategoriaDao().getById(this@toDomain.idCategoria)!!.toDomain()
        val tempTipoIva = db.getTipoIvaDAo().getById(this@toDomain.idIva)!!.toDomain()
        val tempNombre = this@toDomain.nombre
        val tempPrecio = this@toDomain.precio

        return Articulo(tempId, tempCategoria, tempTipoIva, tempNombre, tempPrecio)
    }

    fun Articulo.toEntity() = ArticuloEntity(
        id = id,
        idCategoria = categoria.id,
        idIva = tipoIva.id,
        nombre = nombre,
        precio = precio
    )

    /* CATEGORIA */

    fun CategoriaEntity.toDomain() = Categoria(
        id = id,
        nombre = nombre,
        color = color
    )

    fun Categoria.toEntity() = CategoriaEntity(
        id = id,
        nombre = nombre,
        color = color
    )

    /* TIPO IVA */

    fun TipoIvaEntity.toDomain() = TipoIva(
        id = id,
        nombre = nombre,
        porcentaje = porcentaje
    )

    fun TipoIva.toEntity() = TipoIvaEntity(
        id = id,
        nombre = nombre,
        porcentaje = porcentaje
    )

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

    fun LineaTicketConArticuloYTicket.toDomain() = LineaTicket(
        id = lineaTicketEntity.id,
        articulo = articuloConCategoriaETipoIva.toDomain(),
        ticket = ticketConCierreYMetodoPago.toDomain(),
        descripcion = lineaTicketEntity.descripcion,
        cantidad = lineaTicketEntity.cantidad,
        valorIva = lineaTicketEntity.valorIva,
        subTotal = lineaTicketEntity.subtotal,
        total = lineaTicketEntity.total
    )
}