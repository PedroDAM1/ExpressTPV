package com.pedro.expresstpv.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ActivityArticuloEditorBinding
import com.pedro.expresstpv.domain.model.Categoria
import com.pedro.expresstpv.domain.model.TipoIva
import com.pedro.expresstpv.ui.viewmodel.ArticuloEditorViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.math.RoundingMode

@AndroidEntryPoint
class ArticuloEditorActivity : AppCompatActivity() {

    //Bindings
    private lateinit var binding : ActivityArticuloEditorBinding

    //Atributos
    private var nombre = ""
    private var precio = 0.0
    private var categoria: Categoria? = null
    private var tipoIva: TipoIva? = null

    private val viewModel : ArticuloEditorViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticuloEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

    /**
     * Antes de convertir ningun tipo de datos vamos a pasar las validaciones difentes
     */
    private fun validarDatos() : Boolean{
        var isValido = true
        if (binding.etNombreArticuloEditor.text.toString().isEmpty()){
            binding.etNombreArticuloEditor.error = "Debes rellenar este campo"
            isValido = false
        }
        if (binding.etPrecioArticuloEditor.text.toString().isEmpty()){
            binding.etPrecioArticuloEditor.error = "Debes rellenar este campo"
            isValido = false
        }

        return isValido
    }

    private fun recuperarDatos(){
        if (!validarDatos()) return

        try {
            nombre = binding.etNombreArticuloEditor.text.toString().trim()
            precio = binding.etPrecioArticuloEditor.text.toString().toDouble()
            //Redondeamos el precio a los 3 ultimos decimales
            precio = BigDecimal(precio).setScale(3, RoundingMode.HALF_EVEN).toDouble()

            //Obtenemos los datos de los spinners
            categoria = binding.spCategoriaArticuloEditor.selectedItem as Categoria
            tipoIva = binding.spTipoIvaArticuloEditor.selectedItem as TipoIva

        } catch (e : java.lang.NumberFormatException){
            Log.d("EXCEPCION", "Error al tratar de convertir un string en double en el activity de editor de articulos: ${e.message}")
        }
    }


    private fun enviarDatos(){

    }
    /**
     * Obtendremos los datos del viewModel y los cargaremos en el spinner.
     * Es importante mencionar que en el spinner al hacerlo de esta forma estamos cargando el toString de cada clase, por lo cual
     * para mostrarlo aqui correctamente sin necesidad de obtener los nombres, directamente sobreescribi el toString de cada clase para que
     * solo me proporcionara el nombre
     */
    private fun cargarDatosSpinner(){
        viewModel.categoriasLiveData.observe(this) { liveData ->
            binding.spCategoriaArticuloEditor.adapter = ArrayAdapter(this, R.layout.elemento_spinner_textview, liveData.values.toMutableList())
        }
        viewModel.tipoIvaLiveData.observe(this) { liveData ->
            binding.spTipoIvaArticuloEditor.adapter = ArrayAdapter(this, R.layout.elemento_spinner_textview, liveData.values.toMutableList())
        }
    }

}