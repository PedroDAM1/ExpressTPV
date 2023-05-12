package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.provider.ArticuloRepository
import com.pedro.expresstpv.data.provider.CategoriaRepository
import com.pedro.expresstpv.data.provider.TipoIvaRepository
import com.pedro.expresstpv.domain.model.Articulo
import com.pedro.expresstpv.domain.model.Categoria
import com.pedro.expresstpv.domain.model.TipoIva
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticuloEditorViewModel @Inject constructor(
    private val articuloRepository: ArticuloRepository,
    private val categoriaRepository: CategoriaRepository,
    private val tipoIvaRepository: TipoIvaRepository
    )
    : ViewModel() {

    //Flow
    val listaCategorias : Flow<List<Categoria>> = categoriaRepository.categoriaFlow

    val listaTipoIva : Flow<List<TipoIva>> = tipoIvaRepository.tipoIvaFlow


    fun guardarArticulo(nombre : String, precio : Double, categoria : Categoria, tipoIva : TipoIva){
        val articulo = Articulo(nombre = nombre, precio = precio, categoria = categoria, tipoIva = tipoIva)
        viewModelScope.launch {
            //articuloRepository.insertarArticulo(articulo)
        }
    }

}