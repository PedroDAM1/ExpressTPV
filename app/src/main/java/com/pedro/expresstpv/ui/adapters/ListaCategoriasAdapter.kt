package com.pedro.expresstpv.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ListaCategoriaLayoutBinding
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.ui.viewmodel.ListaCategoriasViewModel

class ListaCategoriasAdapter (
    private val onItemClick : (ListaCategoriasViewModel.CategoriaIsSelected) -> Unit,
    private val onLongClick : (ListaCategoriasViewModel.CategoriaIsSelected) -> Unit
    )
    : ListAdapter<ListaCategoriasViewModel.CategoriaIsSelected, ListaCategoriasAdapter.ListaCategoriasViewHolder>(DiffCallback) {

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
        val item = getItem(position)
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
        holder.itemView.setOnLongClickListener {
            onLongClick(item)
            true
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ListaCategoriasViewModel.CategoriaIsSelected>() {
            override fun areItemsTheSame(
                oldItem: ListaCategoriasViewModel.CategoriaIsSelected,
                newItem: ListaCategoriasViewModel.CategoriaIsSelected
            ): Boolean {
                return oldItem.categoria.id == newItem.categoria.id
            }

            override fun areContentsTheSame(
                oldItem: ListaCategoriasViewModel.CategoriaIsSelected,
                newItem: ListaCategoriasViewModel.CategoriaIsSelected
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    class ListaCategoriasViewHolder(v  : View) : RecyclerView.ViewHolder(v) {

        private var binding = ListaCategoriaLayoutBinding.bind(v)
        fun bind(categoria : ListaCategoriasViewModel.CategoriaIsSelected){
            pintarSelected(categoria)

            binding.tvNombreCategoriaListaCategorias.text = categoria.categoria.nombre
            binding.tvColorCategoriaListaCategorias.text = categoria.categoria.color
            binding.tvColorCategoriaListaCategorias.setTextColor(
                Functions.getContrastColor(Functions.hexToColorInt(categoria.categoria.color)))

            binding.tvColorCategoriaListaCategorias.setBackgroundColor(Functions.hexToColorInt(categoria.categoria.color))
        }

        private fun pintarSelected(categoria: ListaCategoriasViewModel.CategoriaIsSelected){
            if (categoria.isSelected){
                binding.cbListaCategorias.visibility = View.VISIBLE
                binding.cbListaCategorias.isChecked = true
                binding.layoutListaCategoriaLayout.setBackgroundColor(binding.root.context.getColor(R.color.secundary_color))
            } else {
                binding.cbListaCategorias.visibility = View.INVISIBLE
                binding.cbListaCategorias.isChecked = false
                binding.layoutListaCategoriaLayout.setBackgroundColor(binding.root.context.getColor(R.color.white))
            }
        }

    }

}

