package com.pedro.expresstpv.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ListaArticulosLayoutBinding
import com.pedro.expresstpv.ui.viewmodel.ListaArticulosViewModel


class ListaArticulosAdapter (
    private val onItemClickListener : (ListaArticulosViewModel.ArticulosIsSelected) -> Unit,
    private val onLongItemClick : (ListaArticulosViewModel.ArticulosIsSelected) -> Unit
    ) : ListAdapter<ListaArticulosViewModel.ArticulosIsSelected, ListaArticulosAdapter.ListaArticulosViewHodel>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaArticulosViewHodel {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lista_articulos_layout, parent, false)
        return ListaArticulosViewHodel(v)
    }

    override fun onBindViewHolder(holder: ListaArticulosViewHodel, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onItemClickListener(getItem(position))
        }
        holder.itemView.setOnLongClickListener {
            onLongItemClick(getItem(position))
            true
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ListaArticulosViewModel.ArticulosIsSelected>() {
            override fun areItemsTheSame(oldItem: ListaArticulosViewModel.ArticulosIsSelected, newItem: ListaArticulosViewModel.ArticulosIsSelected): Boolean {
                return oldItem.articulo.id == newItem.articulo.id
            }

            override fun areContentsTheSame(oldItem: ListaArticulosViewModel.ArticulosIsSelected, newItem: ListaArticulosViewModel.ArticulosIsSelected): Boolean {
                return oldItem == newItem
            }
        }
    }


    class ListaArticulosViewHodel(v : View) : RecyclerView.ViewHolder(v) {

        private val binding = ListaArticulosLayoutBinding.bind(v)

        fun bind(articulo: ListaArticulosViewModel.ArticulosIsSelected) {
            pintarSelected(articulo)

            binding.tvNombreArticuloListaArticulos.text = articulo.articulo.nombre
            //Mostramos el precio mejor formateado formateando la cadena existente en los strings recursos
            binding.tvPrecioListaArticulos.text = binding.tvPrecioListaArticulos.context.getString(R.string.precio_selector).format(articulo.articulo.precio)
        }

        private fun pintarSelected(articulo: ListaArticulosViewModel.ArticulosIsSelected){
            if (articulo.isSelected){
                binding.cbListaArticulos.visibility = View.VISIBLE
                binding.cbListaArticulos.isChecked = true
                binding.layoutListaArticulosLayout.setBackgroundColor(binding.root.context.getColor(R.color.secundary_color))
            } else {
                binding.cbListaArticulos.visibility = View.INVISIBLE
                binding.cbListaArticulos.isChecked = false
                binding.layoutListaArticulosLayout.setBackgroundColor(binding.root.context.getColor(R.color.white))
            }
        }

    }
}