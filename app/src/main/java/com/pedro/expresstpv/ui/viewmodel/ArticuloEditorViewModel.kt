package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.provider.ArticuloRepository
import com.pedro.expresstpv.data.provider.CategoriaRepository
import com.pedro.expresstpv.data.provider.TipoIvaRepository
import com.pedro.expresstpv.data.usecase.ArticulosUseCase
import com.pedro.expresstpv.data.usecase.CategoriaUseCase
import com.pedro.expresstpv.data.usecase.TipoIvaUseCase
import com.pedro.expresstpv.domain.model.Articulo
import com.pedro.expresstpv.domain.model.Categoria
import com.pedro.expresstpv.domain.model.TipoIva
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticuloEditorViewModel @Inject constructor(
    private val articulosUseCase: ArticulosUseCase,
    private val categoriaUseCase: CategoriaUseCase,
    private val tipoIvaUseCase: TipoIvaUseCase
) : ViewModel() {

    //Flow
    fun getListaCategorias() = categoriaUseCase.getAllFlow()
    fun getListaTipoIva() = tipoIvaUseCase.getAllFlow()

    fun guardarArticulo(nombre : String, precio : Double, categoria : Categoria, tipoIva : TipoIva){
        viewModelScope.launch (Dispatchers.IO) {
            articulosUseCase.insertArticulo(nombre, precio, categoria, tipoIva)
        }
    }

}