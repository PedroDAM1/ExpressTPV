package com.pedro.expresstpv.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.provider.ArticuloRepository
import com.pedro.expresstpv.data.provider.LineaTicketRepository
import com.pedro.expresstpv.data.usecase.ArticulosUseCase
import com.pedro.expresstpv.data.usecase.LineaTicketUseCases
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
    private val articuloRepository: ArticuloRepository,
    private val lineaTicketRepository: LineaTicketRepository,
    private val lineaTicketUseCases: LineaTicketUseCases,
    private val articulosUseCase: ArticulosUseCase
) : ViewModel() {

    private val _listaArticulos = articuloRepository.getAllArticulos()
    private val _lineaTicketFlow = lineaTicketRepository.getAllLineaTicket()
    private val _lineaTicketActivoFlow = lineaTicketUseCases.getLineaTicketActivoFlow()
    private var _listaLineaTickets : MutableList<LineaTicket> = mutableListOf()

    private val _articuloConCantidadFlow : Flow<List<VentasCalculadoraListAdapter.ArticuloYCantidad>> = _listaArticulos.combine(_lineaTicketActivoFlow){ listaArticulos, lineasTickets ->
        listaArticulos.map {articulo ->
            var articuloYCantidad = VentasCalculadoraListAdapter.ArticuloYCantidad(articulo, 0)
            lineasTickets.forEach {lineaTicket ->
                if (isLineaTicketOfArticulo(lineaTicket, articulo)){
                    articuloYCantidad = VentasCalculadoraListAdapter.ArticuloYCantidad(articulo, lineaTicket.cantidad)
                }
            }
            articuloYCantidad
        }
    }
        .flowOn(Dispatchers.IO)

    init {
        subscribeToFlow()
    }

    fun getArticulosConCantidad() = _articuloConCantidadFlow

    fun getLineaTicketActivo() = _lineaTicketActivoFlow
    fun deleteAllLineaTickets(){
        viewModelScope.launch (Dispatchers.IO) {
            lineaTicketRepository.deleteAll()
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
        return if (lineaTicket.descripcion == articulo.nombre &&
            lineaTicket.categoriaVenta == articulo.categoria.nombre &&
            lineaTicket.valorIva == articulo.tipoIva.porcentaje &&
            (lineaTicket.total/lineaTicket.cantidad) == articulo.precio) {
            Log.d("COMPROBADOR", "El archivo es valido")
            true
        } else {
            false
        }
    }

    /**
     * Comprobamos si existe alguna lineaTicket que coincida con un articulo,
     * si coincide la devolvemos, sino devolveremos null
     */
    private fun comprobarLineaTicket(articulo : Articulo) : LineaTicket?{
        var lineaTicket : LineaTicket? = null
        _listaLineaTickets.forEach {
            //Vamos a comprobar si las caracteristicas del articulo coinciden con las de la linea ticket
             if (isLineaTicketOfArticulo(it, articulo)) {
                //Existe la lineaTicket, asique la devolvemos
                lineaTicket = it
            }
        }
        //Si no existe la linea ticket se devuelve null
        return lineaTicket
    }

    /**
     * Nos subscribimos a la lista de lineaTickets para cuando se actualice en tiempo real
     */
    private fun subscribeToFlow(){
        viewModelScope.launch (Dispatchers.IO) {
            _lineaTicketFlow
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
        viewModelScope.launch (Dispatchers.IO) {
            Log.d("ARTICULO CLICADO", "Se ha clicado al articulo: $articulo")
            val lineaTicket : LineaTicket? = comprobarLineaTicket(articulo.articulo)
            if (lineaTicket == null){
                lineaTicketUseCases.crearLineaTicket(articulo.articulo)
            } else {
                lineaTicketUseCases.aumentarCantidadLineaTicket(lineaTicket, 1)
            }
        }
    }

}