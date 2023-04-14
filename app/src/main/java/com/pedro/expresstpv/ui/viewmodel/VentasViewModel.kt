package com.pedro.expresstpv.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedro.expresstpv.data.database.dao.ArticuloDao
import com.pedro.expresstpv.data.database.entities.ArticuloConCategoriaETipoIva
import com.pedro.expresstpv.data.database.entities.CategoriaEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VentasViewModel @Inject constructor(private val articuloDao: ArticuloDao) : ViewModel() {

    val liveDataArticulos = MutableLiveData<List<ArticuloConCategoriaETipoIva>>()

    lateinit var categoria : CategoriaEntity

    val liveDataCategoria = MutableLiveData<CategoriaEntity>()
    init {
        onCreate()
    }

    private fun onCreate() {
    }
}