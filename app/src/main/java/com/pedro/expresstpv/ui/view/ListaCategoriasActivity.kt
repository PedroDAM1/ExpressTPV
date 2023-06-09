package com.pedro.expresstpv.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ActivityListaCategoriasBinding
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.ui.adapters.ListaCategoriasAdapter
import com.pedro.expresstpv.ui.viewmodel.ListaCategoriasViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListaCategoriasActivity : AppCompatActivity() {

    private lateinit var binding : ActivityListaCategoriasBinding

    private lateinit var adapter : ListaCategoriasAdapter

    private val viewModel : ListaCategoriasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaCategoriasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBack()
        cargarRecycler()
        onUiState()
        setListeners()
    }


    //=======================FUNCIONES BASICAS===============================//
    /**
     * Al presionar la tecla de atras, primero desmarcaremos todos, y al darle de nuevo saldra de la actividad
     */
   private fun onBack(){
            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.isSelectedMode()) {
                        viewModel.quitarSeleccionar()
                    } else {
                        super.isEnabled = false
                        this@ListaCategoriasActivity.onBackPressedDispatcher.onBackPressed()
                    }
                }

            }

            this.onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun setListeners(){
        binding.fabAddCategoria.setOnClickListener{
            startActivity(Intent(this, CategoriaEditorActivity::class.java))
        }
    }


    //==================OPCIONES DEL MENU===============================//
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_items, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miSeleccionarTodoListaCategorias -> {
                viewModel.seleccionarTodo()
            }
            R.id.miQuitarSeleccionesListaCategoria -> {
                viewModel.quitarSeleccionar()
            }
            R.id.miBorrarListaCategoria -> {
                viewModel.borrar()
            }

        }
        return super.onOptionsItemSelected(item)
    }


    //==================OPCIONES DEL UiState===============================//


    /**
     * Obtenemos el estado desde el viewmodel
     */
    private fun onUiState(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{
                    when (it) {
                        ListaCategoriasViewModel.UiState.Loading -> {
                            binding.pbListaCategorias.visibility = View.VISIBLE
                        }
                        is ListaCategoriasViewModel.UiState.Succes -> {
                            onUiStateSucces(it.list.toList())
                        }
                        is ListaCategoriasViewModel.UiState.OnDeleteResponsive -> {
                            onUiStateDelete(it.title, it.message)
                        }
                    }
                }
            }
        }
    }

    private fun onUiStateDelete(title : String, msg : String){
        Functions.mostrarMensajeError(this, title, msg)
    }

    private fun onUiStateSucces(list: List<ListaCategoriasViewModel.CategoriaIsSelected>){
        binding.pbListaCategorias.visibility = View.INVISIBLE
        adapter.submitList(list)
    }

    //==================OPCIONES DEL RECYCLER===============================//


    private fun cargarRecycler(){
        val layoutManager = LinearLayoutManager(this)
        adapter = ListaCategoriasAdapter(onItemClick = {onItemClickListener(it)}, onLongClick = {onLongItemClickListener(it)} )

        binding.rvListaCategorias.layoutManager = layoutManager
        binding.rvListaCategorias.adapter = adapter
    }

    private fun onItemClickListener(cat : ListaCategoriasViewModel.CategoriaIsSelected){
        //Si estamos en modo seleccion, pasaremos directamente a editar la pantalla
        if (!viewModel.isSelectedMode()){
            val i = Intent(this, CategoriaEditorActivity::class.java).apply {
                putExtra("CATEGORIA", cat.categoria)
            }
            startActivity(i)
            // Si no lo marcamos para editar
        } else {
            viewModel.uploadCategoriaSelected(cat)
        }
    }

    private fun onLongItemClickListener(cat : ListaCategoriasViewModel.CategoriaIsSelected){
        // Marcamos directamente la categoria para editar
        viewModel.uploadCategoriaSelected(cat)
    }


}