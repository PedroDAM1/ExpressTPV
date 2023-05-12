package com.pedro.expresstpv.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pedro.expresstpv.databinding.ActivityListaArticulosBinding
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.domain.model.Articulo
import com.pedro.expresstpv.domain.model.Categoria
import com.pedro.expresstpv.ui.recyclers.ListaArticulosAdapter
import com.pedro.expresstpv.ui.viewmodel.ListaArticulosViewModel
import com.pedro.expresstpv.ui.viewmodel.UIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
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
        lifecycleScope.launch (Dispatchers.Main) {
            Log.d("HILO", "Se ha lanzado un hilo desde la funcion enlazar flow")
            viewModel._listaArticulos
                .catch {
                    Log.d("EXCEPCION", "Hubo una excepcion al cargar el viewModel de la clase ListaArticulo")
                }
                .collect{
                    Log.d("SIZE", it.size.toString())
                    Log.d("ADAPTER", "Subiendo lista al adapter: ${it.size}")
                    adapter.submitList(it)
                }
        }
    }


}