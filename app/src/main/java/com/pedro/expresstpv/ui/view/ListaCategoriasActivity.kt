@file:Suppress("UNCHECKED_CAST")

package com.pedro.expresstpv.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedro.expresstpv.databinding.ActivityListaCategoriasBinding
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.domain.functions.Functions.Companion.mostrarMensajeError
import com.pedro.expresstpv.domain.model.Articulo
import com.pedro.expresstpv.ui.adapters.ListaCategoriasAdapter
import com.pedro.expresstpv.ui.viewmodel.ListaCategoriasViewModel
import com.pedro.expresstpv.ui.viewmodel.UIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import com.pedro.expresstpv.domain.model.Categoria as Categoria

@AndroidEntryPoint
class ListaCategoriasActivity : AppCompatActivity() {

    private lateinit var binding : ActivityListaCategoriasBinding

    private lateinit var adapter : ListaCategoriasAdapter

    private val viewModel : ListaCategoriasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaCategoriasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cargarRecycler()
        getState()
        setListeners()
    }

    private fun setListeners(){
        binding.fabAddCategoria.setOnClickListener{
            startActivity(Intent(this, CategoriaEditorActivity::class.java))
        }
    }

    /**
     * Obtenemos el UIState para saber si los datos estan ya o no cargados
     */
    private fun getState(){
        //Lanzamos el hilo
        lifecycleScope.launch {
            viewModel.getListaCategoriasUIState()
                //Nos susbscribimos al estado del uiState para saber si han cargado ya los datos
                .collect { flow ->
                    when(flow){
                        //En caso de error mostraremos el mensaje de error
                        is UIState.Error -> {
                            binding.pbListaCategorias.visibility = View.GONE
                            mostrarMensajeError(this@ListaCategoriasActivity, "Error",
                            "Hubo un error al cargar\n: ${flow.msg}")
                        }
                        //Si no ocurre nada, cargaremos los datos en el adapter
                        //TODO mejorar el sistema del adapter para que sea un ListAdapter
                        is UIState.Succes<*> -> {
                            binding.pbListaCategorias.visibility = View.GONE
                            subscribeToFlow(flow.flow as Flow<List<Categoria>>)
                        }
                        //Si aun sigue cargando dejaremos la progressBar visible
                        UIState.Loading -> {
                            binding.pbListaCategorias.visibility = View.VISIBLE
                        }
                    }
                }
        }
    }

    private fun cargarRecycler(){
        val layoutManager = LinearLayoutManager(this)
        adapter = ListaCategoriasAdapter()

        binding.rvListaCategorias.layoutManager = layoutManager
        binding.rvListaCategorias.adapter = adapter
    }

    private suspend fun subscribeToFlow(flow : Flow<List<Categoria>>){
        flow.catch {
            mostrarMensajeError(this@ListaCategoriasActivity, "Error en los datos", "Hubo un error al cargar los datos")
        }
            .flowOn(Dispatchers.Main)
            .collect{
                adapter.submitList(it)
            }
    }

}