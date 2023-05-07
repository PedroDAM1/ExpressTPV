package com.pedro.expresstpv.ui.recyclers

import android.view.View
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pedro.expresstpv.databinding.ListaCategoriaLayoutBinding
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.domain.model.Categoria

class ListaCategoriasViewHolder(v  :View) : ViewHolder(v) {

    private var binding = ListaCategoriaLayoutBinding.bind(v)

    fun render(categoria : Categoria){
        binding.tvNombreCategoriaListaCategorias.text = categoria.nombre
        binding.tvColorCategoriaListaCategorias.text = categoria.color
        binding.tvColorCategoriaListaCategorias.setTextColor(Functions.getContrastColor(Functions.hexToColorInt(categoria.color)))
        binding.tvColorCategoriaListaCategorias.setBackgroundColor(Functions.hexToColorInt(categoria.color))
    }

}