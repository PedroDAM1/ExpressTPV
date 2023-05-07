package com.pedro.expresstpv.ui.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.pedro.expresstpv.R
import com.pedro.expresstpv.domain.model.Categoria

class ListaCategoriasAdapter (var data : MutableList<Categoria>) : Adapter<ListaCategoriasViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaCategoriasViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lista_categoria_layout, parent, false)
        return ListaCategoriasViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ListaCategoriasViewHolder, position: Int) {
        holder.render(data[position])
    }
}