package com.pedro.expresstpv.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ActivityListaArticulosBinding
import com.pedro.expresstpv.ui.adapters.ListaArticulosAdapter
import com.pedro.expresstpv.ui.viewmodel.ListaArticulosViewModel
import dagger.hilt.android.AndroidEntryPoint
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
        onBack()
        setListeners()
        setRecycler()
        enlazarFlow()
    }
    //============================= MENU ====================================//
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_items, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miSeleccionarTodoListaCategorias -> {
                viewModel.seleccionarTodo()
            }
            R.id.miQuitarSeleccionesListaCategoria ->{
                viewModel.quitarSeleccionar()
            }
            R.id.miBorrarListaCategoria -> {
                viewModel.borrar()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    //============================= FUNCIONES BASICAS ====================================//
    private fun onBack(){
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.isSelectedMode()) {
                    viewModel.quitarSeleccionar()
                } else {
                    super.isEnabled = false
                    this@ListaArticulosActivity.onBackPressedDispatcher.onBackPressed()
                }
            }

        }

        this.onBackPressedDispatcher.addCallback(this, callback)
    }


    private fun setListeners(){
        binding.fabAddArticulo.setOnClickListener{
            startActivity(Intent(this, ArticuloEditorActivity::class.java))
        }
    }


    //============================= RECYCLER ====================================//

    private fun setRecycler(){
        val layoutManager = LinearLayoutManager(this)
        adapter = ListaArticulosAdapter({onItemClickListener(it)}, {onLongItemClick(it)})

        binding.rvListaArticulos.layoutManager = layoutManager
        binding.rvListaArticulos.adapter = adapter
    }

    private fun onItemClickListener(articulosIsSelected: ListaArticulosViewModel.ArticulosIsSelected){
        //Si estamos en modo seleccion, pasaremos directamente a editar la pantalla
        if (!viewModel.isSelectedMode()){
            val i = Intent(this, ArticuloEditorActivity::class.java).apply {
                putExtra("ID", articulosIsSelected.articulo.id)
            }
            startActivity(i)
            // Si no lo marcamos para editar
        } else {
            viewModel.uploadArticuloSelected(articulosIsSelected)
        }
    }

    private fun onLongItemClick(articulo : ListaArticulosViewModel.ArticulosIsSelected){
        // Marcamos directamente la categoria para editar
        viewModel.uploadArticuloSelected(articulo)
    }

    //=============================UI STATE====================================//
    private fun enlazarFlow(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                //Nos susbscribimos al estado del uiState para saber si han cargado ya los datos
                viewModel.uiState.collect{
                    when (it) {
                        ListaArticulosViewModel.UiState.Loading -> {
                            onUiStateLoading()
                        }
                        is ListaArticulosViewModel.UiState.Success -> {
                            onUiStateSucces(it.list)
                        }
                    }
                }
            }
        }
    }

    private fun onUiStateSucces(list : List<ListaArticulosViewModel.ArticulosIsSelected>){
        binding.pbListaArticulos.visibility = View.GONE
        adapter.submitList(list)
    }

    private fun onUiStateLoading (){
        binding.pbListaArticulos.visibility = View.VISIBLE
    }

}