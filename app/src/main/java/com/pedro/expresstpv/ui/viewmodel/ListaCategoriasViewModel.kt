package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.provider.CategoriaRepository
import com.pedro.expresstpv.domain.model.Categoria
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaCategoriasViewModel @Inject constructor(
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {

    //Listas
    private lateinit var listaCategorias : MutableMap<Int, Categoria>

    //LiveDatas
    private val _categoriasLiveData = MutableLiveData<MutableMap<Int, Categoria>>()
    val categoriasLiveData = _categoriasLiveData

    init {
        onCreate()
    }

    fun onCreate(){
        viewModelScope.launch {
            //TODO: Arreglar que cuando se hace una insercion en el repositorio, no se actualizan los livesDatas de forma inmediata
            listaCategorias = categoriaRepository.getAllCategorias()

            _categoriasLiveData.postValue(listaCategorias)
        }
    }
}