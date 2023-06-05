package com.pedro.expresstpv.domain.functions

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.pedro.expresstpv.R
import com.pedro.expresstpv.ui.viewmodel.UIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Functions {
    companion object{
        /**
         * Convierte un INT que proceda de un color en codigo hexadecimal en formato #FFFFFF
         * @param color Color que queremos convertir a hexadecimal
         */
        fun colorToHex(color: Int): String {
            return String.format("#%06X", (0xFFFFFF and color))
        }

        /**
         * Convierte un hexadecimal en un numero.
         * Si el hexadecimal no esta completo, se completará mediante 0
         * @param hex String que hace referencia al hexadecimal
         * @throws IllegalArgumentException Si el hexadecimal tiene algun caracter invalido, devolvera esta excepcion
         *
         */
        fun hexToColorInt(hex: String): Int {
            try {
                // Eliminamos el # si existe en la cadena
                val hexDigits = hex.trimStart('#')
                // Aseguramos que la cadena tenga 6 caracteres hexadecimales
                val paddedHex = hexDigits.padEnd(6, '0')
                // Parseamos la cadena hexadecimal a un color Int
                return Color.parseColor("#$paddedHex")
            } catch (e : java.lang.IllegalArgumentException){
                Log.d("Excepcion", "Excepcion en el formateo de colores: ${e.message}")
                throw e
            }
        }

        /**
         * Devuelve el color contrario al pasado por parametro
         */
        fun getContrastColor(color: Int): Int {
            val red = Color.red(color)
            val green = Color.green(color)
            val blue = Color.blue(color)
            val yiq = (red * 299 + green * 587 + blue * 114) / 1000
            return if (yiq >= 128) Color.BLACK else Color.WHITE
        }

        /**
         * Cambia el formato de un LocalDateTime recibido a yyyy-MM-dd HH:mm:ss
         */
        fun formatLocalDateTime(localDateTime: LocalDateTime?) : LocalDateTime{
            val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val sFormated = localDateTime?.format(pattern)
            return LocalDateTime.parse(sFormated, pattern)
        }

        /**
         * La funcion nos devolvera un MutableState sabiendo que nos llegara un UIState.
         *
         * @param scope donde correra nuestro escuchador
         * @param flow Flow que se encontrara en el repositorio del que queramos obtener el MutableState
         */
        fun <T> getStateFlow(scope : CoroutineScope, flow : Flow<T>) : MutableStateFlow<UIState> {
            //Empezamos a escuchar el flow que conecta con la base de datos
            val state = MutableStateFlow<UIState>(UIState.Loading)
            flow
                .onEach {
                    state.value = UIState.Succes(flow)
                }
                .catch {
                    state.value = UIState.Error(it.message.orEmpty())
                }
                .flowOn(Dispatchers.IO)
                .launchIn(scope)

            return state
        }

        fun mostrarMensajeError(context : Context, titulo : String, mensaje : String){
            Log.d("EXCEPCION", mensaje)
            AlertDialog.Builder(context)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("Aceptar", null)
                .create()
                .show()
        }
        fun pintarBackgroundSegunLineaGrilla(pos : Int, v: View){
            if (pos % 2 == 0){
                v.setBackgroundColor(v.context.getColor(R.color.background_elements))
            } else {
                v.setBackgroundColor(v.context.getColor(R.color.white))
            }
        }

    }
}