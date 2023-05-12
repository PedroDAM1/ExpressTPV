package com.pedro.expresstpv.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ActivityArticuloEditorBinding
import com.pedro.expresstpv.domain.model.Categoria
import com.pedro.expresstpv.domain.model.TipoIva
import com.pedro.expresstpv.ui.viewmodel.ArticuloEditorViewModel
import com.pedro.expresstpv.ui.viewmodel.UIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
            enviarDatos()
            finish()
        }
        binding.btnCancelarArticuloEditor.setOnClickListener {
            finish()
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
        viewModel.guardarArticulo(nombre, precio, categoria!!, tipoIva!!)
    }
    /**
     * Obtendremos los datos del viewModel y los cargaremos en el spinner.
     * Es importante mencionar que en el spinner al hacerlo de esta forma estamos cargando el toString de cada clase, por lo cual
     * para mostrarlo aqui correctamente sin necesidad de obtener los nombres, directamente sobreescribi el toString de cada clase para que
     * solo me proporcionara el nombre
     */
    private fun cargarDatosSpinner(){
        viewModel.listaCategorias
            .onEach {
                binding.spCategoriaArticuloEditor.adapter = ArrayAdapter(this@ArticuloEditorActivity, R.layout.elemento_spinner_textview, it)
            }
            .launchIn(lifecycleScope)

        viewModel.listaTipoIva
            .onEach{
                binding.spTipoIvaArticuloEditor.adapter = ArrayAdapter(this@ArticuloEditorActivity, R.layout.elemento_spinner_textview, it)
            }
            .launchIn(lifecycleScope)

    }
}