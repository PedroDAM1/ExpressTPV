package com.pedro.expresstpv.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ActivityCategoriaEditorBinding
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.ui.viewmodel.ArticuloEditorViewModel
import com.pedro.expresstpv.ui.viewmodel.CategoriaEditorViewModel
import dagger.hilt.android.AndroidEntryPoint
import top.defaults.colorpicker.ColorObserver

@AndroidEntryPoint
class CategoriaEditorActivity : AppCompatActivity() {

    //Binding
    private lateinit var binding : ActivityCategoriaEditorBinding

    //ViewModel
    private val viewModel : CategoriaEditorViewModel by viewModels()

    //Atributos
    private var observerColorPicker : ColorObserver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriaEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configurarColorPicker()
        setListeners()
    }

    private fun setListeners(){
        //EditText del color
        binding.etColorCategoriaEditor.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Antes de modficar el texto deberemos de subscribirnos
                binding.colorPickerCategoriaEditor.unsubscribe(observerColorPicker)
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Nothing
            }
            override fun afterTextChanged(p0: Editable?) {
                //Convertimmos el texto del et en un color
                onTextChangeColor(p0.toString())
                //Deberemos de volver a subscribirnos
                binding.colorPickerCategoriaEditor.subscribe(observerColorPicker)
            }
        })

        //Boton de cancelar
        binding.btnCancelarCategoriaEditor.setOnClickListener {
            finish()
        }

        //Boton de guardar
        binding.btnAceptarArticuloEditor.setOnClickListener {
            recogerDatos()
            finish()
        }
    }

    private fun validarDatos() : Boolean{
        if (binding.etNombreCategoriaEditor.text.toString().isEmpty()){
            binding.etNombreCategoriaEditor.error = "Debes de rellenar este campo"
            return false
        }
        val textoColor = binding.etColorCategoriaEditor.text.toString()
        try {
            //Si el texto no es valido, entonces nos devolvera la excepcion
            Functions.hexToColorInt(textoColor)

        } catch (e : IllegalArgumentException){
            binding.etColorCategoriaEditor.error = "El color que has introducido no es válido"
            return false
        }

        return true
    }

    private fun recogerDatos(){
        // si los datos no son validos automaticamente saldremos sin hacer nada
        if (!validarDatos()){
            return
        }
        val nombre = binding.etNombreCategoriaEditor.text.toString().trim()
        val color = binding.etColorCategoriaEditor.text.toString().trim()

        //Le pasamos los datos al viewmodel donde se gestionara lo demas
        viewModel.guardarDatos(nombre, color)
    }

    private fun onTextChangeColor(text : String){
        try{
            // si al intentar obtener el color, no es válido, saltara excepcion
            val color = Functions.hexToColorInt(text)

            // una vez tenemos el color, modificaremos el colorPicker para que nos muestre ese color
            binding.colorPickerCategoriaEditor.setInitialColor(color)
            binding.colorPickerCategoriaEditor.reset()

            //Ademas del color picker, tambien modificaremos el textView
            binding.tvColorCategoriaEditor.setBackgroundColor(color)

        } catch (e : java.lang.IllegalArgumentException){
            //Si salta una excepcion no cambiaremos ningun color
            Log.d("EXCEPCION", "Hubo una excepcion: ${e.message}")
        }
    }

    private fun configurarColorPicker(){
        //Instanvciamos el observer para poder subscribirlo y desusbribirlo cuando queramos
        observerColorPicker = ColorObserver { color, _, _ ->
            binding.tvColorCategoriaEditor.setBackgroundColor(color)
            binding.etColorCategoriaEditor.setText(Functions.colorToHex(color))
        }
        binding.colorPickerCategoriaEditor.subscribe(observerColorPicker)
        binding.colorPickerCategoriaEditor.setInitialColor(getColor(R.color.red))
    }


}