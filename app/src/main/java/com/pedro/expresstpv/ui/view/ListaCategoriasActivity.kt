package com.pedro.expresstpv.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedro.expresstpv.databinding.ActivityListaCategoriasBinding
import com.pedro.expresstpv.domain.functions.Functions.Companion.mostrarMensajeError
import com.pedro.expresstpv.ui.recyclers.ListaCategoriasAdapter
import com.pedro.expresstpv.ui.viewmodel.ListaCategoriasViewModel
import com.pedro.expresstpv.ui.viewmodel.UIState
import dagger.hilt.android.AndroidEntryPoint
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

    private fun getState(){
        lifecycleScope.launch {
            viewModel.listaCategoriasUIState
                .collect { flow ->
                    when(flow){
                        is UIState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            mostrarMensajeError(this@ListaCategoriasActivity, "Error",
                            "Hubo un error al cargar\n: ${flow.msg}")
                        }
                        is UIState.Succes<*> -> {
                                flow.flow.collect{
                                    adapter.data = (it as List<Categoria>).toMutableList()
                                    adapter.notifyItemChanged(adapter.data.size-1)
                                    binding.progressBar.visibility = View.GONE
                                }

                        }
                        UIState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE

                        }
                    }
                }
        }
    }

    private fun cargarRecycler(){
        val layoutManager = LinearLayoutManager(this)
        adapter = ListaCategoriasAdapter(mutableListOf())

        binding.rvListaCategorias.layoutManager = layoutManager
        binding.rvListaCategorias.adapter = adapter
    }

}