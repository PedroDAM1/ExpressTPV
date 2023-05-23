package com.pedro.expresstpv.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ActivityVentasBinding
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.domain.model.LineaTicket
import com.pedro.expresstpv.ui.adapters.GrillaLIneaTicketsListAdapter
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
    private lateinit var adapterVentas : VentasCalculadoraListAdapter
    private lateinit var adapterGrillaLineaTickets : GrillaLIneaTicketsListAdapter

    private val ventasViewModel by viewModels<VentasViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecyclerVentas()
        setRecyclerGrillaLineaTicket()
        subscribeToFlow()
        setListeners()
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
                ventasViewModel.eliminarTicketActual()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /*****************************************************************************************************/

    private fun setListeners(){
        binding.btnMenosVentasActivity.setOnClickListener {
            reducirLineaTicket()
        }
        binding.btnMasVentasActivity.setOnClickListener {
            aumentarLineaTicket()
        }
        binding.btnEliminarTodoVentasActivity.setOnClickListener {
            borrarTicketCompleto()
        }
        binding.btnCobrarVentasActivity.setOnClickListener {
            cobrarTicket()
        }
    }

    private fun cobrarTicket(){
        startActivity(Intent(this, CobrosActivity::class.java))
    }

    private fun borrarTicketCompleto(){
        AlertDialog.Builder(this)
            .setPositiveButton("Aceptar"){ _, _ ->
                ventasViewModel.eliminarTicketActual()
            }
            .setNegativeButton("Cancelar", null)
            .setTitle("¿Eliminar ticket?")
            .setMessage("¿Quieres eliminar este ticket completo?")
            .create()
            .show()
    }

    private fun reducirLineaTicket(){
        val item = adapterGrillaLineaTickets.getSelectedItem()
        if (item != null){
            ventasViewModel.reducirCantidadLineaTicket(item)
        }
    }

    private fun aumentarLineaTicket(){
        val item = adapterGrillaLineaTickets.getSelectedItem()
        if (item != null){
            ventasViewModel.aumentarCantidadLineaTicket(item)
        }
    }

    /**
     * Preparamos el recycler
     */
    private fun setRecyclerVentas(){
        val layoutManager = GridLayoutManager(this, 3)
        adapterVentas = VentasCalculadoraListAdapter { ventasViewModel.onArticuloItemClick(it) }
        binding.rvArticulosCalculadora.layoutManager = layoutManager
        binding.rvArticulosCalculadora.adapter = adapterVentas
    }

    private fun setRecyclerGrillaLineaTicket(){
        val layoutManager = LinearLayoutManager(this)
        adapterGrillaLineaTickets = GrillaLIneaTicketsListAdapter { onLineaTicketItemClick(it)}
        binding.rvGridLineasTickets.layoutManager = layoutManager
        binding.rvGridLineasTickets.adapter = adapterGrillaLineaTickets
    }


    /**
     * Nos subscribimos a la lista que nos dice cuando un articulo se actualiza o cuando se actualiza una linea ticket
     */
    private fun subscribeToFlow() {
        ventasViewModel.getArticulosConCantidad()
            .onEach {
                adapterVentas.submitList(it)
            }
            .catch {
                Functions.mostrarMensajeError(
                this@VentasActivity,
                "Error",
                "Hubo un error al cargar los datos")
            }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)

        ventasViewModel.getLineaTicketActivo()
            .onEach {
                //Actualizamos la grilla de lineas tickets
                adapterGrillaLineaTickets.submitList(it)

                //Marcamos el total en el textView
                val total = it.sumOf { linea -> linea.total }
                binding.tvTotal.text = getString(R.string.precio_selector).format(total)

                //Habilitar o deshabilitar el boton de borrado
                checkearBotonesLineasTickets(it)
            }
            .catch {
                Functions.mostrarMensajeError(
                    this@VentasActivity,
                    "Error",
                    "Hubo un error al cargar las lineas de tickets"
                )
            }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope)

    }

    private fun onLineaTicketItemClick(lineaTicket: LineaTicket?){
        if (lineaTicket == null){
            // Desmarcamos los botones
            deshabilitarBotonesMasYMenos()
        } else {
            // Marcamos los botones
            habilitarBotonesMasYMenos()
        }

    }

    private fun deshabilitarBotonesMasYMenos(){
        binding.btnMasVentasActivity.isEnabled = false
        binding.btnMenosVentasActivity.isEnabled = false
    }

    private fun habilitarBotonesMasYMenos(){
        binding.btnMasVentasActivity.isEnabled = true
        binding.btnMenosVentasActivity.isEnabled = true
    }

    private fun checkearBotonesLineasTickets(lista: List<LineaTicket>){
        if (lista.isNotEmpty()){
            binding.btnEliminarTodoVentasActivity.isEnabled = true
            binding.btnCobrarVentasActivity.isEnabled = true
        } else {
            binding.btnEliminarTodoVentasActivity.isEnabled = false
            binding.btnCobrarVentasActivity.isEnabled = false
            deshabilitarBotonesMasYMenos()
        }
    }
}