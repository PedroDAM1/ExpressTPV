package com.pedro.expresstpv.ui.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ActivityDetalleVentasBinding
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.domain.model.LineaTicket
import com.pedro.expresstpv.ui.adapters.DetalleVentasListAdapter
import com.pedro.expresstpv.ui.viewmodel.DetalleVentasViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class DetalleVentasActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetalleVentasBinding
    private val viewModel : DetalleVentasViewModel by viewModels()

    private lateinit var datePickerDialogInicio : DatePickerDialog
    private lateinit var datePickerDialogFin : DatePickerDialog

    private lateinit var adapter : DetalleVentasListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleVentasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadDatesDialogs()
        setListeners()
        setRecycler()
        subscribeFlow()
    }

    @SuppressLint("SetTextI18n")
    private fun setListeners(){
        binding.etDateInicioDetalleVentas.setOnClickListener {
            datePickerDialogInicio.show()
        }
        binding.etDateFinDetalleVentas.setOnClickListener {
            datePickerDialogFin.show()
        }
        datePickerDialogInicio.setOnDateSetListener { _, year, month, dayOfMonth ->
            val mes = month+1
            binding.etDateInicioDetalleVentas.setText("$dayOfMonth/$mes/$year")

            val newFecha = LocalDate.of(year, mes, dayOfMonth)
            viewModel.updateFechas(newFecha, viewModel.getFechaFin())
        }
        datePickerDialogFin.setOnDateSetListener { _, year, month, dayOfMonth ->
            val mes = month+1
            binding.etDateFinDetalleVentas.setText("$dayOfMonth/$mes/$year")

            val newFecha = LocalDate.of(year, mes, dayOfMonth)
            viewModel.updateFechas(viewModel.getFechaInicio(), newFecha)
        }
    }

    private fun loadDatesDialogs(){
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        //Obtenemos por defecto la fecha menos 7 dias atras

        binding.etDateInicioDetalleVentas.setText(viewModel.getFechaInicio().format(formatter))
        binding.etDateFinDetalleVentas.setText(viewModel.getFechaFin().format(formatter))
        datePickerDialogInicio = DatePickerDialog(this)
        datePickerDialogFin = DatePickerDialog(this)
    }

    private fun setRecycler(){
        val linearLayout = LinearLayoutManager(this)
        adapter = DetalleVentasListAdapter()

        binding.rvListaArticulosDetalleVentas.layoutManager = linearLayout
        binding.rvListaArticulosDetalleVentas.adapter = adapter
    }

    private fun onUIStateSuccess(list : List<LineaTicket>){
        adapter.submitList(list)

        binding.etTotalDetalleVentas.setText(getString(R.string.precio_selector).format(viewModel.getTotalFromLineaTickets()))
        binding.etSubTotalDetalleVentas.setText(getString(R.string.precio_selector).format(viewModel.getSubtotalFromLineaTickets()))
    }

    private fun subscribeFlow(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState
                    .collect{
                        when(it) {
                            is DetalleVentasViewModel.UIStateDetalles.Succes -> {
                                onUIStateSuccess(it.lista)
                            }
                            is DetalleVentasViewModel.UIStateDetalles.Error -> {
                                Functions.mostrarMensajeError(this@DetalleVentasActivity,
                                    "Error",
                                    "Hubo un error al cargar los datos")
                            }
                            DetalleVentasViewModel.UIStateDetalles.Loading -> {
                            }
                        }
                    }
            }
        }
    }
}