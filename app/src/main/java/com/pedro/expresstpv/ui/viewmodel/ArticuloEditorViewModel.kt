package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.provider.CategoriaRepository
import com.pedro.expresstpv.data.provider.TipoIvaRepository
import com.pedro.expresstpv.domain.model.Articulo
import com.pedro.expresstpv.domain.model.Categoria
import com.pedro.expresstpv.domain.model.TipoIva
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticuloEditorViewModel @Inject constructor(
    private val categoriaRepository: CategoriaRepository,
    private val tipoIvaRepository: TipoIvaRepository
    )
    : ViewModel() {

    //Atributos privados
    private lateinit var listaCategorias : MutableMap<Int, Categoria>
    private lateinit var listaTipoIva : MutableMap<Int, TipoIva>

    //LiveData
    var categoriasLiveData = MutableLiveData<MutableMap<Int, Categoria>>()
    var tipoIvaLiveData  = MutableLiveData<MutableMap<Int, TipoIva>>()


    init {
        onCreate()
    }

    private fun onCreate(){
        viewModelScope.launch{
            listaCategorias = categoriaRepository.getAllCategorias()
            listaTipoIva = tipoIvaRepository.getAllTipoIva()

            categoriasLiveData.postValue(listaCategorias)
            tipoIvaLiveData.postValue(listaTipoIva)
        }

    }

    fun guardarArticulo(nombre : String, precio : Double, categoria : Categoria, tipoIva : TipoIva){
        val articulo = Articulo(nombre = nombre, precio = precio, categoria = categoria, tipoIva = tipoIva)
    }

}