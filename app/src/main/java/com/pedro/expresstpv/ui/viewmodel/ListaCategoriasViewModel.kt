package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.usecase.ArticulosUseCase
import com.pedro.expresstpv.data.usecase.CategoriaUseCase
import com.pedro.expresstpv.domain.model.Categoria
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListaCategoriasViewModel @Inject constructor(
    private val categoriaUseCase: CategoriaUseCase,
    private val articuloUseCase : ArticulosUseCase
) : ViewModel() {


    //Flow
    private val _categoriaFlow = categoriaUseCase.getAllFlow()
    private val _uiState : MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState : StateFlow<UiState> = _uiState

    private var _listaCategoriasSelected : MutableMap<Int, CategoriaIsSelected> = mutableMapOf()

    private var isSelectionMode = false

    fun getIsSelected() = isSelectionMode

    init {
        subscribeFlow()
    }

    private fun subscribeFlow(){
        viewModelScope.launch {
            _categoriaFlow
                .distinctUntilChanged()
                .map {
                    it.map {cat ->
                        CategoriaIsSelected(categoria = cat, false)
                    }
                }
                    .collectLatest {
                        _listaCategoriasSelected.clear()
                        it.sortedBy { cat ->
                            cat.categoria.id
                        }.forEach {cat ->
                            _listaCategoriasSelected[cat.categoria.id] = cat
                        }
                        updateUiState()
                    }
        }
    }

    /**
     * Insertaremos un nuevo item para ser marcado como selected
     */
    fun uploadCategoriaSelected(categoria: CategoriaIsSelected){
        isSelectionMode = true
        //Si estaba seleccionada
        if (categoria.isSelected){
            _listaCategoriasSelected[categoria.categoria.id] = categoria.copy(isSelected = false)
        } else {
            _listaCategoriasSelected[categoria.categoria.id] = categoria.copy(isSelected = true)
        }
        comprobarListaIsSelected()
    }

    /**
     * Comprobamos la lista, si no hay ningun item seleccionado, desmarcamos el modoSelected
     */
    private fun comprobarListaIsSelected(){
        //Contamos cuantos valores hay ya seleccionados
        val seleccionados = _listaCategoriasSelected.count {
            it.value.isSelected
        }
        //Si no hay seleccionados, entonces desactivaremos el modoSelected
        if (seleccionados == 0){
            isSelectionMode = false
        }
        updateUiState()
    }

    private fun convertirMapa(bool : Boolean){
        //Convertimos el mapa para convertirlo a true
        _listaCategoriasSelected.replaceAll { _, categoriaIsSelected ->
            categoriaIsSelected.copy(isSelected = bool)
        }
    }
    fun seleccionarTodo(){
        convertirMapa(true)
        updateUiState()
        isSelectionMode = true
    }

    fun quitarSeleccionar(){
        convertirMapa(false)
        updateUiState()
        isSelectionMode = false
    }

    fun borrar(){
        viewModelScope.launch {
            val listaCategoriasConArtigulos = getCategoriasConArticulos()
            if (listaCategoriasConArtigulos.isEmpty()){
                //Si la lista de categorias esta vacia, podemos borrar esas categorias sin problema
                borrarCategorias(getCategoriasIsSelected())
            } else {
                //Si alguna categoria tiene articulos, entonces no eliminaremos nada
                val titulo = "Error al eliminar"
                var respuesta = "No se pueden eliminar las categorias por que tienen articulos asociados: \n"
                listaCategoriasConArtigulos.forEach {
                    respuesta += "-${it.nombre}\n"
                }

                _uiState.value = UiState.OnDeleteResponsive(titulo, respuesta)
            }

        }
    }

    private fun borrarCategorias(list : List<Categoria>){
        viewModelScope.launch(Dispatchers.IO) {
            categoriaUseCase.deleteList(list)
        }
    }

    /**
     * Obtenemos una lista de todos los articulos seleccionados del mapa de categorias
     */
    private fun getCategoriasIsSelected() : List<Categoria> {
        val filtrado = _listaCategoriasSelected.values.toList().filter {
            it.isSelected
        }
        return filtrado.map {
            it.categoria
        }
    }

    /**
     * Devolveremos una lista de categorias que tienen algun articulo asociado
     * Esta comprobacion se hace en funcion de las categorias seleccionaads de la lista
     */
    private suspend fun getCategoriasConArticulos() : List<Categoria> = withContext(Dispatchers.Default){
        return@withContext getCategoriasIsSelected().filter {
            articuloUseCase.getArticulosByCategoria(it).isNotEmpty()
        }
    }


    /**
     * Nos encargaremos de actualizar la uiState
     */
    private fun updateUiState(){
        _uiState.value = UiState.Loading
        _uiState.value = UiState.Succes(_listaCategoriasSelected.values.toList())
    }


    sealed class UiState{
        object Loading : UiState()
        data class Succes(val list : List<CategoriaIsSelected>) : UiState()

        data class OnDeleteResponsive(val title : String, val message : String) : UiState()
    }

    data class CategoriaIsSelected(
        val categoria: Categoria,
        val isSelected: Boolean
    )
}