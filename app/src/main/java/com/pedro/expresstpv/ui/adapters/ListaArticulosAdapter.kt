package com.pedro.expresstpv.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ListaArticulosLayoutBinding
import com.pedro.expresstpv.domain.model.Articulo


class ListaArticulosAdapter :
    ListAdapter<Articulo, ListaArticulosAdapter.ListaArticulosViewHodel>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaArticulosViewHodel {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lista_articulos_layout, parent, false)
        return ListaArticulosViewHodel(v)
    }

    override fun onBindViewHolder(holder: ListaArticulosViewHodel, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Articulo>() {
            override fun areItemsTheSame(oldItem: Articulo, newItem: Articulo): Boolean {
                Log.d("ADAPTER", "Comprobando las diferencias entre la id ${oldItem.id} y la id ${newItem.id} ")
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Articulo, newItem: Articulo): Boolean {
                Log.d("ADAPTER", "Comparando los objetos $oldItem y $newItem")
                return oldItem == newItem
            }
        }
    }


    class ListaArticulosViewHodel(v : View) : RecyclerView.ViewHolder(v) {

        private val binding = ListaArticulosLayoutBinding.bind(v)

        fun bind(articulo: Articulo) {
            Log.d("HOLDER", "Pintando el articulo $articulo")
            binding.tvNombreArticuloListaArticulos.text = articulo.nombre
            //Mostramos el precio mejor formateado formateando la cadena existente en los strings recursos
            binding.tvPrecioListaArticulos.text = binding.tvPrecioListaArticulos.context.getString(R.string.precio_selector).format(articulo.precio)
        }

    }
}