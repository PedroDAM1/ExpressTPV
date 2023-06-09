package com.pedro.expresstpv.ui.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ActivityArticuloEditorBinding
import com.pedro.expresstpv.domain.model.Categoria
import com.pedro.expresstpv.domain.model.TipoIva
import com.pedro.expresstpv.ui.viewmodel.ArticuloEditorViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode

@AndroidEntryPoint
class ArticuloEditorActivity : AppCompatActivity() {

    //Bindings
    private lateinit var binding : ActivityArticuloEditorBinding

    //Atributos
    private var id = 0
    private var nombre = ""
    private var precio = 0.0
    private var categoria: Categoria? = null
    private var tipoIva: TipoIva? = null

    private val viewModel : ArticuloEditorViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticuloEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadData()
        setListeners()
    }

    private fun loadData(){
        lifecycleScope.launch {
            cargarDatosSpinner()
            id = intent.getIntExtra("ID", 0)
            if (id != 0){
                val articulo = viewModel.getArticuloById(id) ?: return@launch
                binding.etNombreArticuloEditor.setText(articulo.nombre)
                binding.etPrecioArticuloEditor.setText("${articulo.precio}")
                binding.spCategoriaArticuloEditor.setSelection(articulo.categoria.id)
                binding.spTipoIvaArticuloEditor.setSelection(articulo.tipoIva.id)
            }
        }
    }

    /**
     * Inicializamos los listeners de todos los elementos
     */
    private fun setListeners(){
        binding.btnAceptarArticuloEditor.setOnClickListener {
            if (!validarDatos()) return@setOnClickListener
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
        binding.tvErrorSpCategoriaArticuloEditor.visibility = View.INVISIBLE
        binding.tvErrorSpTipoIvaArticuloEditor.visibility = View.INVISIBLE

        var isValido = true
        if (binding.etNombreArticuloEditor.text.toString().isEmpty()){
            binding.etNombreArticuloEditor.error = "Debes rellenar este campo"
            isValido = false
        }
        if (binding.etPrecioArticuloEditor.text.toString().isEmpty()){
            binding.etPrecioArticuloEditor.error = "Debes rellenar este campo"
            isValido = false
        }

        // Si la categoria es la predefinida, no dejaremos crearlo
        if(binding.spCategoriaArticuloEditor.selectedItemPosition == 0){
            binding.tvErrorSpCategoriaArticuloEditor.visibility = View.VISIBLE
            isValido = false
        }

        // Si el tipo de iva es el predefinido, no dejaremos crearlo
        if (binding.spTipoIvaArticuloEditor.selectedItemPosition == 0){
            binding.tvErrorSpTipoIvaArticuloEditor.visibility = View.VISIBLE
            isValido = false
        }

        return isValido
    }

    private fun recuperarDatos(){
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
        viewModel.guardarArticulo(id, nombre, precio, categoria!!, tipoIva!!)
    }
    /**
     * Obtendremos los datos del viewModel y los cargaremos en el spinner.
     * Es importante mencionar que en el spinner al hacerlo de esta forma estamos cargando el toString de cada clase, por lo cual
     * para mostrarlo aqui correctamente sin necesidad de obtener los nombres, directamente sobreescribi el toString de cada clase para que
     * solo me proporcionara el nombre
     */
    private suspend fun cargarDatosSpinner() = withContext(Dispatchers.Main){
        val listaCategorias = viewModel.getListaCategorias().first()
        binding.spCategoriaArticuloEditor.adapter = ArrayAdapter(this@ArticuloEditorActivity, R.layout.elemento_spinner_textview, listaCategorias)

        val listaTipoIva = viewModel.getListaTipoIva().first()
        binding.spTipoIvaArticuloEditor.adapter = ArrayAdapter(this@ArticuloEditorActivity, R.layout.elemento_spinner_textview, listaTipoIva)
    }
}