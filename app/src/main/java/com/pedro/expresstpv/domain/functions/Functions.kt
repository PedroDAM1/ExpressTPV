package com.pedro.expresstpv.domain.functions

import android.graphics.Color
import android.util.Log

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
    }
}