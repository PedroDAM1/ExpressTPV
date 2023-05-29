package com.pedro.expresstpv.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ActivityCobrosBinding
import com.pedro.expresstpv.domain.model.MetodoPago
import com.pedro.expresstpv.ui.adapters.TipoCobroListAdapter
import com.pedro.expresstpv.ui.viewmodel.CobrosViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CobrosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCobrosBinding

    private val viewModel: CobrosViewModel by viewModels()

    private lateinit var adapter: TipoCobroListAdapter

    private var totalTicket = 0.0
    private var totalEntrega = 0.00
    private var totalCambio = 0.00
    private var modoDecimal = false
    private var modoBilletes = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCobrosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cargarDatos()
        setRecycler()
        setListeners()
    }

    private fun cargarDatos() {
        lifecycleScope.launch(Dispatchers.Main) {
            totalTicket = viewModel.getTotalTicket()
            binding.tvTotalCobros.text = getString(R.string.precio_selector).format(totalTicket)
            updateViewsTotales()
        }
    }

    private fun setRecycler() {
        adapter = TipoCobroListAdapter { metodoPagoItemClick(it) }

        binding.rvMetodosPagoCobro.adapter = adapter

        lifecycleScope.launch {
            adapter.submitList(viewModel.getMetodosPago())
        }
    }

    private fun metodoPagoItemClick(metodoPago: MetodoPago) {
        AlertDialog.Builder(this)
            .setTitle("Información de pago")
            .setMessage("¿Desea pagar el ticket usando ${metodoPago.nombre.lowercase()}?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Aceptar") { _, _ ->
                viewModel.crearTicket(metodoPago)
                finish()
            }
            .create()
            .show()
    }

    private fun setListeners() {
        setListenerCalculadoras()
    }

    private fun setListenerCalculadoras() {
        //BOTONES BASICOS
        binding.btnNum1Cobro.setOnClickListener(onAddClickListener)
        binding.btnNum2Cobro.setOnClickListener(onAddClickListener)
        binding.btnNum3Cobro.setOnClickListener(onAddClickListener)
        binding.btnNum4Cobro.setOnClickListener(onAddClickListener)
        binding.btnNum5Cobro.setOnClickListener(onAddClickListener)
        binding.btnNum6Cobro.setOnClickListener(onAddClickListener)
        binding.btnNum7Cobro.setOnClickListener(onAddClickListener)
        binding.btnNum8Cobro.setOnClickListener(onAddClickListener)
        binding.btnNum9Cobro.setOnClickListener(onAddClickListener)
        binding.btnNum0Cobro.setOnClickListener(onAddClickListener)

        //BOTONES ESPECIALES
        binding.btnCommaCobro.setOnClickListener(onCommaClickListener)
        binding.btnBorrarCobro.setOnClickListener(onCEClickListener)

        //BOTONES MONEDAS
        binding.ibtn2euros.setOnClickListener(onSumClickListener)
        binding.ibtn1euro.setOnClickListener(onSumClickListener)
        binding.ibtn50cents.setOnClickListener(onSumClickListener)
        binding.ibtn20cents.setOnClickListener(onSumClickListener)
        binding.ibtn10cents.setOnClickListener(onSumClickListener)
        binding.ibtn5cents.setOnClickListener(onSumClickListener)
        binding.ibtn2cents.setOnClickListener(onSumClickListener)
        binding.ibtn1cents.setOnClickListener(onSumClickListener)

        //BILLETES
        binding.ibtnBillete200.setOnClickListener(onSumClickListener)
        binding.ibtnBillete100.setOnClickListener(onSumClickListener)
        binding.ibtnBillete50.setOnClickListener(onSumClickListener)
        binding.ibtnBillete20.setOnClickListener(onSumClickListener)
        binding.ibtnBillete10.setOnClickListener(onSumClickListener)
        binding.ibtnBillete5.setOnClickListener(onSumClickListener)
    }

    private val onAddClickListener = OnClickListener {
//        try{
//            val decimalFormat = DecimalFormat("0.00")
//
//            if (modoBilletes){
//                totalEntrega = 0.00
//            }
//            validarSizeEntrega()
//
//            //Comprobaremos si la entrega estaba alterada por los billetes o no
//            val content = it.contentDescription
//
//            var strTotal = decimalFormat.format(totalEntrega)
//            val substr = strTotal.split(",")
//
//            var parteEntera = substr[0]
//            var parteDecimal = substr[1]
//            //Comprobaremos si estamos en la comma o no
//
//            if (!modoDecimal){
//                parteEntera+=content
//            } else {
//                parteDecimal += content
//            }
//            strTotal = "$parteEntera.$parteDecimal"
//
//            totalEntrega = decimalFormat.parse(strTotal)?.toDouble() ?: 0.00
//            modoBilletes = false
//            updateViewsTotales()
//        } catch (e : java.lang.NumberFormatException){
//            Log.d("ERROR", "Error al parsear en la pantalla de cobros: ${e.message}")
//        }

        if (modoBilletes) {
            totalEntrega = 0.0
        }
        validarSizeEntrega()

        val value = it.contentDescription
        val parsedValue = value.toString().toDoubleOrNull() ?: return@OnClickListener

        if (!modoDecimal) {
            val parteEntera = totalEntrega.toInt()
            val parteDecimal = getDecimalPart(totalEntrega)
            val temp = parteEntera * 10 + parsedValue
            totalEntrega = temp + parteDecimal
        }
        updateDecimalPart(parsedValue)


        modoBilletes = false
        updateViewsTotales()
    }

    private fun updateDecimalPart(value: Double) {
        if (modoDecimal) {
            val decimalPart = getDecimalPart(totalEntrega)
            val decimalPartString = totalEntrega.toString().split(".")[1]
            if (decimalPartString != "0" && decimalPartString.length > 1) {
                modoDecimal = false
                return
            }

            totalEntrega += if (decimalPart != 0.0) {
                modoDecimal = false
                value / 100
            } else {
                value / 10
            }
        }
    }

    private fun getDecimalPart(number: Double): Double {
        val integerValue = number.toInt()
        return number - integerValue
    }

    private val onSumClickListener = OnClickListener {
        if(!modoBilletes){
            totalEntrega = 0.0
        }
        val contentDescription = it.contentDescription
        modoBilletes = true
        try {
            val valor = contentDescription.toString().toDouble()
            totalEntrega += valor
            updateViewsTotales()

            Log.d("TOTAL ENTREGA", "Dinero entregado: $totalEntrega")

        } catch (e: java.lang.NumberFormatException) {
            Log.d("ERROR", "Error al parsear en la pantalla de cobros: ${e.message}")
        }
    }

    private val onCommaClickListener = OnClickListener {
        modoDecimal = true
        //Eliminamos los decimales del numero
        totalEntrega = totalEntrega.toInt().toDouble()
    }

    /**
     * Borramos el total y actualizamos las vistas para el usuario
     */
    private val onCEClickListener = OnClickListener {
        totalEntrega = 0.00
        modoDecimal = false
        modoBilletes = false
        updateViewsTotales()
    }

    /**
     * Si el total excede de este limite, lo setearemos a 0 para evitar problemas
     */
    private fun validarSizeEntrega() {
        if (totalEntrega >= 999999999.00) {
            totalEntrega = 0.00
        }
    }

    private fun calcularCambio(){
        if (totalEntrega == 0.0){
            totalCambio = 0.0
            return
        }
        totalCambio = totalEntrega - totalTicket
    }

    private fun updateViewsTotales() {
        calcularCambio()
        binding.tvEntregaCobros.text = getString(R.string.precio_selector).format(totalEntrega)
        binding.tvCambioCobros.text = getString(R.string.precio_selector).format(totalCambio)
    }


}