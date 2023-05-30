package com.pedro.expresstpv.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.usecase.ArticulosUseCase
import com.pedro.expresstpv.data.usecase.LineaTicketUseCases
import com.pedro.expresstpv.data.usecase.TicketUseCase
import com.pedro.expresstpv.domain.model.Articulo
import com.pedro.expresstpv.domain.model.LineaTicket
import com.pedro.expresstpv.ui.adapters.VentasCalculadoraListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VentasViewModel @Inject constructor(
    private val articulosUseCase: ArticulosUseCase,
    private val lineaTicketUseCases: LineaTicketUseCases,
    private val ticketUseCases: TicketUseCase
) : ViewModel() {

    private val _listaArticulos = articulosUseCase.getAllArticulosFlow()
    private val _lineaTicketActivoFlow = lineaTicketUseCases.getLineaTicketActivoFlow()
    private var _listaLineaTickets : MutableList<LineaTicket> = mutableListOf()

    private val _articuloConCantidadFlow : Flow<List<VentasCalculadoraListAdapter.ArticuloYCantidad>> = _listaArticulos.combine(_lineaTicketActivoFlow){ listaArticulos, lineasTickets ->
        listaArticulos.map {articulo ->
            Log.d("VENTASVIEWMODEL", "Mapeando el flow a articulosycantidades")
            val lineaTicket = lineasTickets.firstOrNull{ isLineaTicketOfArticulo(it, articulo) }
            //Si no obtenemos una lineaticket, devolveremos 0 de cantidad
            VentasCalculadoraListAdapter.ArticuloYCantidad(articulo, lineaTicket?.cantidad ?: 0)
        }
    }
        .flowOn(Dispatchers.Default)

    init {
        subscribeToFlow()
    }

    fun getArticulosConCantidad() = _articuloConCantidadFlow

    fun getLineaTicketActivo() = _lineaTicketActivoFlow

    suspend fun getTotalesTicket() = lineaTicketUseCases.getTotalFromLineaTicketsActivo()
    fun eliminarTicketActual(){
        viewModelScope.launch (Dispatchers.IO) {
            lineaTicketUseCases.eliminarTicketActivo()
        }
    }

    fun getListaArticulosPorCategoria(idCategoria : Int){
        //TODO Hacer que la funcion devuelva los articulos que pertenezcan a una categoria concreta
    }


    /**
     * Devuelve true si esa linaTicket pertenece a ese articulo. False si cualquiera de las caracteristicas
     * del articulo no coincide con las de la lineaTicket
     */
    private fun isLineaTicketOfArticulo(lineaTicket: LineaTicket, articulo: Articulo) : Boolean{
        return lineaTicket.descripcion == articulo.nombre &&
            lineaTicket.categoriaVenta == articulo.categoria.nombre &&
            lineaTicket.valorIva == articulo.tipoIva.porcentaje &&
            (lineaTicket.total/lineaTicket.cantidad) == articulo.precio
    }

    /**
     * Comprobamos si existe alguna lineaTicket que coincida con un articulo,
     * si coincide la devolvemos, sino devolveremos null
     */
    private suspend fun comprobarLineaTicket(articulo: Articulo): LineaTicket? {
        //Si no existe la linea ticket se devuelve null
        val linea = _listaLineaTickets.firstOrNull { isLineaTicketOfArticulo(it, articulo) }
        return if (linea?.ticket?.numTicket == ticketUseCases.getTicketActivo().numTicket){
            linea
        } else {
            null
        }
    }

    /**
     * Nos subscribimos a la lista de lineaTickets para cuando se actualice en tiempo real
     */
    private fun subscribeToFlow(){
        viewModelScope.launch (Dispatchers.Default) {
            _lineaTicketActivoFlow
                .catch {
                    Log.d("VENTAS VIEW MODEL", "Error al subscribir en el flow del ventas view model: ${it.message}")
                }
                .collect{
                    _listaLineaTickets = it.toMutableList()
                }
        }
    }

    /**
     * Obtenemos el articulo que se ha clicado en el boton del adapter, ademas deberemos de devolver la cantidad de lineaTicket de ese articulo
     */
    fun onArticuloItemClick(articulo: VentasCalculadoraListAdapter.ArticuloYCantidad) {
        viewModelScope.launch (Dispatchers.Default) {
            val lineaTicket : LineaTicket? = comprobarLineaTicket(articulo.articulo)
            if (lineaTicket == null){
                lineaTicketUseCases.crearLineaTicket(articulo.articulo)
            } else {
                aumentarCantidadLineaTicket(lineaTicket)
            }
        }
    }

    fun reducirCantidadLineaTicket(lineaTicket: LineaTicket){
        viewModelScope.launch(Dispatchers.IO) {
            lineaTicketUseCases.reducirCantidadLineaTicket(lineaTicket, 1)
        }
    }

    fun aumentarCantidadLineaTicket(lineaTicket: LineaTicket){
        viewModelScope.launch (Dispatchers.IO) {
            lineaTicketUseCases.aumentarCantidadLineaTicket(lineaTicket, 1)
        }
    }

}