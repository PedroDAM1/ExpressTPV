package com.pedro.expresstpv.ui.recyclers

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
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lista_categoria_layout, parent, false)
        return ListaArticulosViewHodel(v)
    }

    override fun onBindViewHolder(holder: ListaArticulosViewHodel, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Articulo>() {
            override fun areItemsTheSame(oldItem: Articulo, newItem: Articulo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Articulo, newItem: Articulo): Boolean {
                return oldItem == newItem
            }
        }
    }


    class ListaArticulosViewHodel(v : View) : RecyclerView.ViewHolder(v) {

        private val binding = ListaArticulosLayoutBinding.bind(v)

        fun bind(articulo: Articulo) {
            binding.tvNombreArticuloListaArticulos.text = articulo.nombre
            binding.tvPrecioListaArticulos.text = articulo.precio.toString()
        }

    }
}