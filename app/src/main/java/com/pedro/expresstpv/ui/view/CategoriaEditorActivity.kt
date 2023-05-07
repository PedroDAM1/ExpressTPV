package com.pedro.expresstpv.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ActivityCategoriaEditorBinding
import com.pedro.expresstpv.domain.functions.Functions
import top.defaults.colorpicker.ColorObserver

class CategoriaEditorActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCategoriaEditorBinding

    private var observerColorPicker : ColorObserver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriaEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configurarColorPicker()
        setListeners()
    }

    private fun setListeners(){
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