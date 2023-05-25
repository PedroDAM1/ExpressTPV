package com.pedro.expresstpv.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ActivityCobrosBinding
import com.pedro.expresstpv.ui.adapters.TipoCobroListAdapter
import com.pedro.expresstpv.ui.viewmodel.CobrosViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CobrosActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCobrosBinding

    private val viewModel : CobrosViewModel by viewModels()

    private lateinit var adapter : TipoCobroListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCobrosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cargarDatos()
        setRecycler()
        setListeners()
    }

    private fun cargarDatos(){
        lifecycleScope.launch (Dispatchers.Main) {
            binding.tvTotalCobros.text = getString(R.string.precio_selector).format(viewModel.getTotalTicket())
            binding.tvEntregaCobros.text = getString(R.string.precio_selector).format(0.00)
            binding.tvCambioCobros.text = getString(R.string.precio_selector).format(0.00)
        }
    }

    private fun setRecycler(){
        val adapter = TipoCobroListAdapter()

        binding.rvMetodosPagoCobro.adapter = adapter

        lifecycleScope.launch {
            adapter.submitList(viewModel.getMetodosPago())
        }
    }

    private fun setListeners(){

    }

    private fun setListenerCalculadoras(){

    }


    val onSumClickListener = OnClickListener {
        val contentDescription = it.contentDescription
        try {
            val valor = contentDescription.toString().toDouble()

        }catch (e: java.lang.NumberFormatException){
            Log.d("ERROR", "Error al parsear en la pantalla de cobros: ${e.message}")
        }
    }
}