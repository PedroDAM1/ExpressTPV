package com.pedro.expresstpv.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ListaCategoriaLayoutBinding
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.domain.model.Categoria

class ListaCategoriasAdapter
    : ListAdapter<Categoria, ListaCategoriasAdapter.ListaCategoriasViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListaCategoriasViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lista_categoria_layout, parent, false)
        return ListaCategoriasViewHolder(v)
    }

    override fun onBindViewHolder(
        holder: ListaCategoriasViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Categoria>() {
            override fun areItemsTheSame(oldItem: Categoria, newItem: Categoria): Boolean {
                Log.d("ADAPTER", "Comprobando las diferencias entre la id ${oldItem.id} y la id ${newItem.id} ")
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Categoria, newItem: Categoria): Boolean {
                Log.d("ADAPTER", "Comparando los objetos $oldItem y $newItem")
                return oldItem == newItem
            }
        }
    }

    class ListaCategoriasViewHolder(v  : View) : RecyclerView.ViewHolder(v) {

        private var binding = ListaCategoriaLayoutBinding.bind(v)
        fun bind(categoria : Categoria){
            binding.tvNombreCategoriaListaCategorias.text = categoria.nombre
            binding.tvColorCategoriaListaCategorias.text = categoria.color
            binding.tvColorCategoriaListaCategorias.setTextColor(
                Functions.getContrastColor(
                    Functions.hexToColorInt(categoria.color)))
            binding.tvColorCategoriaListaCategorias.setBackgroundColor(Functions.hexToColorInt(categoria.color))
        }

    }

}

