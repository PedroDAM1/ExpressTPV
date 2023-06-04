package com.pedro.expresstpv.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ActivityCierresBinding
import com.pedro.expresstpv.ui.adapters.GrillaMetodosPagoCierresListAdapter
import com.pedro.expresstpv.ui.adapters.GrillaTicketsCierreListAdapter
import com.pedro.expresstpv.ui.viewmodel.CierresViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CierresActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCierresBinding
    private val viewModel : CierresViewModel by viewModels()

    private lateinit var adapterTickets : GrillaTicketsCierreListAdapter
    private lateinit var adapterMetodoPago : GrillaMetodosPagoCierresListAdapter

    private var total = 0.0
    private var totalRecuento = 0.0
    private var totalDescuadre = 0.0

    private val recuentoMetodoPagos : MutableMap<Int, Double> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCierresBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadData()
    }

    private fun loadData(){
        loadTotales()
        loadGrillaTickets()
        loadGrillaMetodosPago()
    }

    private fun loadTotales(){
        lifecycleScope.launch {
            total = viewModel.getTotalTickets()
            updateViewsCantidades()
        }
    }

    private fun loadGrillaTickets(){
        val layoutManager = LinearLayoutManager(this)
        adapterTickets = GrillaTicketsCierreListAdapter()

        binding.rvGrillaTicketsCierres.layoutManager = layoutManager
        binding.rvGrillaTicketsCierres.adapter = adapterTickets

        lifecycleScope.launch {
            viewModel.getListaTickets()
                .collect{
                    adapterTickets.submitList(it)
                }
        }
    }

    private fun loadGrillaMetodosPago(){
        val layoutManager = LinearLayoutManager(this)
        adapterMetodoPago = GrillaMetodosPagoCierresListAdapter { txt, idMetodo ->
            onTextChangeListener(txt, idMetodo)
        }

        binding.rvMetodosPagoCierres.layoutManager = layoutManager
        binding.rvMetodosPagoCierres.adapter = adapterMetodoPago

        lifecycleScope.launch {
            viewModel.getTotalPorMetodoPagoFlow()
                .collect{
                    adapterMetodoPago.submitList(it)
                }
        }
    }

    private fun onTextChangeListener(txt : String, idMetodo : Int){
        var resultado = txt
        if (resultado.isEmpty()) {
            resultado = "0"
        }
        val value = resultado.filterNot { it == '€' }.replace(',', '.').toDouble()
        recuentoMetodoPagos[idMetodo] = value
        updateCantidades()
    }

    private fun updateCantidades(){
        totalRecuento = recuentoMetodoPagos.values.sumOf {
            it
        }
        totalDescuadre = totalRecuento - total
        updateViewsCantidades()
    }

    private fun updateViewsCantidades(){
        binding.tvTotalCierres.text = getString(R.string.precio_selector).format(total)
        binding.tvRecuentoCierres.text = getString(R.string.precio_selector).format(totalRecuento)
        binding.tvDescuadreCierres.text = getString(R.string.precio_selector).format(totalDescuadre)
        // si el descuadre es negativo lo pintaremos de rojo
        if(totalDescuadre < 0){
            binding.tvDescuadreCierres.setTextColor(getColor(R.color.red))
        //Si el desccuedre es positivo lo pintaremos de verde
        } else if (totalDescuadre > 0){
            binding.tvDescuadreCierres.setTextColor(getColor(R.color.green))
        //si el descuadre es 0 entonces lo dejamos pintado de forma normal
        } else {
            binding.tvDescuadreCierres.setTextColor(getColor(R.color.foreground_textviews))
        }
    }
}