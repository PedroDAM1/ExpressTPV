package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.provider.CategoriaRepository
import com.pedro.expresstpv.domain.model.Categoria
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriaEditorViewModel @Inject constructor(
    private val categoriaRepository: CategoriaRepository)
    : ViewModel() {

    fun guardarDatos(nombre : String, color: String){

        val categoria = Categoria(nombre = nombre, color = color)

        viewModelScope.launch {
            categoriaRepository.insert(categoria)
        }

    }



}