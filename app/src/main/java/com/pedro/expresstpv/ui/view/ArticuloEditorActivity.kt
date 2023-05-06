package com.pedro.expresstpv.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ActivityArticuloEditorBinding
import com.pedro.expresstpv.domain.model.Categoria
import com.pedro.expresstpv.ui.viewmodel.ArticuloEditorViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@AndroidEntryPoint
class ArticuloEditorActivity : AppCompatActivity() {

    //Bindings
    private lateinit var binding : ActivityArticuloEditorBinding

    //Atributos
    private var nombre = ""
    private var precio = 0.0

    private val articuloEditorViewModel : ArticuloEditorViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticuloEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        articuloEditorViewModel.onCreate()
        setListeners()
        cargarDatosSpinner()
    }

    /**
     * Inicializamos los listeners de todos los elementos
     */
    private fun setListeners(){
        binding.btnAceptarArticuloEditor.setOnClickListener {
             recuperarDatos()
        }
        binding.btnCancelarArticuloEditor.setOnClickListener {

        }
    }

    private fun recuperarDatos(){
        try {
            nombre = binding.etNombreArticuloEditor.text.toString().trim()
            precio = binding.etPrecioArticuloEditor.text.toString().toDouble()

            //Redondeamos el precio a los 3 ultimos decimales
            precio = BigDecimal(precio).setScale(3, RoundingMode.HALF_EVEN).toDouble()

            //TODO: Recoger los datos de los spinners

        } catch (e : java.lang.NumberFormatException){
            Log.d("EXCEPCION", "Error al tratar de convertir un string en double en el activity de editor de articulos")
        }
    }

    private fun cargarDatosSpinner(){
        articuloEditorViewModel.categoriasLiveData.observe(this) { liveData ->
            binding.spCategoriaArticuloEditor.adapter = ArrayAdapter(this, R.layout.elemento_spinner_textview, liveData.values.toMutableList())
        }
        articuloEditorViewModel.tipoIvaLiveData.observe(this) { liveData ->
            binding.spTipoIvaArticuloEditor.adapter = ArrayAdapter(this, R.layout.elemento_spinner_textview, liveData.values.toMutableList())
        }
    }

}