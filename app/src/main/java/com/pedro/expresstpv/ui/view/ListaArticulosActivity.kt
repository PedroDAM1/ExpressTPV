package com.pedro.expresstpv.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedro.expresstpv.databinding.ActivityListaArticulosBinding
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.domain.model.Articulo
import com.pedro.expresstpv.ui.adapters.ListaArticulosAdapter
import com.pedro.expresstpv.ui.viewmodel.ListaArticulosViewModel
import com.pedro.expresstpv.ui.viewmodel.UIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ListaArticulosActivity : AppCompatActivity() {

    private lateinit var binding : ActivityListaArticulosBinding
    private lateinit var adapter : ListaArticulosAdapter

    private val viewModel : ListaArticulosViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaArticulosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setRecycler()
        enlazarFlow()
    }

    private fun setListeners(){
        binding.fabAddArticulo.setOnClickListener{
            startActivity(Intent(this, ArticuloEditorActivity::class.java))
        }
    }

    private fun setRecycler(){
        val layoutManager = LinearLayoutManager(this)
        adapter = ListaArticulosAdapter()

        binding.rvListaArticulos.layoutManager = layoutManager
        binding.rvListaArticulos.adapter = adapter
    }

    private fun enlazarFlow(){
        lifecycleScope.launch {
            viewModel.listaArticulos
                //Nos susbscribimos al estado del uiState para saber si han cargado ya los datos
                .collect { flow ->
                    when(flow){
                        //En caso de error mostraremos el mensaje de error
                        is UIState.Error -> {
                            binding.pbListaArticulos.visibility = View.GONE
                            Functions.mostrarMensajeError(
                                this@ListaArticulosActivity, "Error",
                                "Hubo un error al cargar\n: ${flow.msg}"
                            )
                        }
                        //Si no ocurre nada, cargaremos los datos en el adapter
                        is UIState.Succes<*> -> {
                            //Al estar subscritos al viewmodel sabemos que lo que nos llega sera un flow de ListArticulos
                            binding.pbListaArticulos.visibility = View.GONE
                            subscribeToFlow(flow.flow as Flow<List<Articulo>>)
                        }
                        //Si aun sigue cargando dejaremos la progressBar visible
                        UIState.Loading -> {
                            binding.pbListaArticulos.visibility = View.VISIBLE
                        }
                    }
                }
        }
    }

    private suspend fun subscribeToFlow(flow : Flow<List<Articulo>>){
        flow.catch {
            Functions.mostrarMensajeError(this@ListaArticulosActivity, "Error en los datos", "Hubo un error al cargar los datos")
        }
            .flowOn(Dispatchers.Main)
        .collect{
            Log.d("ADAPTER", "Actualizando el adapter del ListArticulo")
            adapter.submitList(it)
        }
    }


}