package com.pedro.expresstpv.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ActivityVentasBinding
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.ui.adapters.VentasCalculadoraListAdapter
import com.pedro.expresstpv.ui.viewmodel.VentasViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class VentasActivity() : AppCompatActivity() {

    private lateinit var binding : ActivityVentasBinding
    private lateinit var adapter : VentasCalculadoraListAdapter

    private val ventasViewModel by viewModels<VentasViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecycler()
        subscribeToFlow()
    }

    /**
     * Pintamos el menu en la pantalla prinicipal
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Damos funcionalidad a los botones dle menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //Vamos al activity para crear los articulos
        when (item.itemId){
            R.id.miCrearArticulo ->{
                startActivity(Intent(this, ListaArticulosActivity::class.java))
            }

            R.id.miCrearCategoria -> {
                startActivity(Intent(this, ListaCategoriasActivity::class.java))
            }

            R.id.miConfiguracion -> {
                ventasViewModel.deleteAllLineaTickets()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /*****************************************************************************************************/

    /**
     * Preparamos el recycler
     */
    private fun setRecycler(){
        val layoutManager = GridLayoutManager(this, 3)
        adapter = VentasCalculadoraListAdapter { ventasViewModel.onArticuloItemClick(it) }

        binding.rvArticulosCalculadora.layoutManager = layoutManager
        binding.rvArticulosCalculadora.adapter = adapter
    }


    /**
     * Nos subscribimos a la lista que nos dice cuando un articulo se actualiza o cuando se actualiza una linea ticket
     */
    private fun subscribeToFlow() {
        ventasViewModel.getArticulosConCantidad()
            .onEach {
                adapter.submitList(it)
            }
            .catch {
                Functions.mostrarMensajeError(
                this@VentasActivity,
                "Error",
                "Hubo un error al cargar los datos")
            }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)
    }
}