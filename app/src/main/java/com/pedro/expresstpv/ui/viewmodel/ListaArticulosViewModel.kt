package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.usecase.ArticulosUseCase
import com.pedro.expresstpv.domain.model.Articulo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaArticulosViewModel @Inject constructor(
    private val articulosUseCase: ArticulosUseCase
) : ViewModel() {

    //FLOWS
    private val _uiState : MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    //En la uiState mostraremos el loading mientras carga la lista de articulos
    val uiState : StateFlow<UiState> = _uiState

    //Atributos privados
    private val _mapArticulos : MutableMap<Int, ArticulosIsSelected> = mutableMapOf()
    private var isSelectionMode = false

    fun isSelectedMode() = isSelectionMode
    init {
        subscribeToFlow()
    }

    private fun subscribeToFlow(){
        viewModelScope.launch (Dispatchers.IO) {
            articulosUseCase.getAllArticulosFlow()
                .map {
                    it.map {art ->
                        ArticulosIsSelected(art.copy(), false)
                    }
                }
                .collectLatest {
                    _mapArticulos.clear()
                    it.sortedBy { art ->
                        art.articulo.id
                    }
                    .forEach {art ->
                        _mapArticulos[art.articulo.id] = art
                    }
                    updateUiStateSucces()
                }
        }
    }


    /**
     * Insertaremos un nuevo item para ser marcado como selected
     */
    fun uploadCategoriaSelected(articulosIsSelected: ArticulosIsSelected){
        isSelectionMode = true
        //Si estaba seleccionada
        if (articulosIsSelected.isSelected){
            _mapArticulos[articulosIsSelected.articulo.id] = articulosIsSelected.copy(isSelected = false)
        } else {
            _mapArticulos[articulosIsSelected.articulo.id] = articulosIsSelected.copy(isSelected = true)
        }
        comprobarListaIsSelected()
    }

    /**
     * Comprobamos la lista, si no hay ningun item seleccionado, desmarcamos el modoSelected
     */
    private fun comprobarListaIsSelected(){
        //Contamos cuantos valores hay ya seleccionados
        val seleccionados = _mapArticulos.count {
            it.value.isSelected
        }
        //Si no hay seleccionados, entonces desactivaremos el modoSelected
        if (seleccionados == 0){
            isSelectionMode = false
        }
        updateUiStateSucces()
    }

    private fun convertirMapa(bool : Boolean){
        //Convertimos el mapa para convertirlo a true
        _mapArticulos.replaceAll { _, articulo ->
            articulo.copy(isSelected = bool)
        }
    }
    fun seleccionarTodo(){
        convertirMapa(true)
        updateUiStateSucces()
        isSelectionMode = true
    }

    fun quitarSeleccionar(){
        convertirMapa(false)
        updateUiStateSucces()
        isSelectionMode = false
    }

    /**
     * Permite borrar los articulos que el usuario a seleccionado
     */
    fun borrar(){
        viewModelScope.launch (Dispatchers.IO){
            borrarCategorias(getArticulosSeleccionados())
        }
    }

    /**
     * Obtiene una lista de los articulos que el usuario a seleccionado
     * @return List<Articulo>
     */
    private fun getArticulosSeleccionados() : List<Articulo>{
        val filtrado = _mapArticulos.values.toList()
            .filter { it.isSelected }
        return filtrado.map { it.articulo }
    }

    private fun borrarCategorias(list : List<Articulo>){
        viewModelScope.launch(Dispatchers.IO) {
            articulosUseCase.deleteList(list)
        }
    }

    /**
     * Actualiza la ui del usuario
     */
    private fun updateUiStateSucces(){
        //Le pasamos una copia de la lsita para evitar errores de referencias
        _uiState.value = UiState.Success(_mapArticulos.values.toList())
    }

    sealed class UiState{
        object Loading : UiState()
        data class Success(val list: List<ArticulosIsSelected>) : UiState()
    }



    data class ArticulosIsSelected (val articulo: Articulo, val isSelected: Boolean)

}