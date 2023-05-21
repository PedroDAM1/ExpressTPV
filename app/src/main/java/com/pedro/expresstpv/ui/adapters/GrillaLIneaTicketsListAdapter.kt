package com.pedro.expresstpv.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.GrillaLineaticketCalculadoraLayoutBinding
import com.pedro.expresstpv.domain.model.LineaTicket

class GrillaLIneaTicketsListAdapter (private val onItemClick : (LineaTicket) -> Unit) : ListAdapter<LineaTicket, GrillaLIneaTicketsListAdapter.GrillaLineaTicketsViewHolder>(
    DiffCallback) {

    private var selectedItem : LineaTicket? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GrillaLineaTicketsViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.grilla_lineaticket_calculadora_layout, parent, false)
        return GrillaLineaTicketsViewHolder(v)
    }

    override fun onBindViewHolder(holder: GrillaLineaTicketsViewHolder, position: Int) {
        holder.bind(getItem(position))
        onItemClick(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<LineaTicket>() {
            override fun areItemsTheSame(oldItem: LineaTicket, newItem: LineaTicket): Boolean {
                Log.d("ADAPTER", "Comprobando las diferencias entre la id ${oldItem.id} y la id ${newItem.id} ")
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: LineaTicket, newItem: LineaTicket): Boolean {
                Log.d("ADAPTER", "Comparando los objetos $oldItem y $newItem")
                return oldItem == newItem
            }
        }
    }

    inner class GrillaLineaTicketsViewHolder(v : View) : ViewHolder(v){

        private val binding = GrillaLineaticketCalculadoraLayoutBinding.bind(v)

        // Pintamos los articulos
        fun bind(lineaTicket: LineaTicket){
            binding.tvCantidadGrillaLineaTickets.text = lineaTicket.cantidad.toString()
            binding.tvNombreGrillaLineaTickets.text = lineaTicket.descripcion
            binding.tvCategoriaGrillaLineaTickets.text = lineaTicket.categoriaVenta
            binding.tvTotalGrillaLineaTickets.text = binding.root.context.getString(R.string.precio_selector).format(lineaTicket.total)

            if (selectedItem == lineaTicket){
                //Pintamos el color del item seleccionado
            } else {
                // Despintamos el color del articulo seleccionado
            }
        }

    }

}