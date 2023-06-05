package com.pedro.expresstpv.ui.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.GrillaLineaticketCalculadoraLayoutBinding
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.domain.model.LineaTicket

class GrillaLIneaTicketsListAdapter (
    private val onItemClick : (LineaTicket?) -> Unit)
    : ListAdapter<LineaTicket, GrillaLIneaTicketsListAdapter.GrillaLineaTicketsViewHolder>(
    DiffCallback) {

    // Si hay algun item seleccionado tendra un valor, en caso de que no tendra un nulo
    private var selectedItem : LineaTicket? = null

    fun getSelectedItem() = selectedItem

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GrillaLineaTicketsViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.grilla_lineaticket_calculadora_layout, parent, false)
        return GrillaLineaTicketsViewHolder(v)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: GrillaLineaTicketsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
        // Hacer click notificaremos al viewmodel para que actualice los botones para añadir y eliminar
        holder.itemView.setOnClickListener {
            // Si selectedItem no tiene valor le asignamos el valor correspondiente
            selectedItem = if(selectedItem != item || selectedItem==null){
                item
            // Si selectedItem tiene un valor, y clicamos de nuevo, es para desmarcarlo
            } else {
                null
            }
            // Enviamos el selected item a la funcion lambda
            onItemClick(selectedItem)
            notifyDataSetChanged()
        }
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
        fun bind(lineaTicket: LineaTicket, pos : Int){
            binding.tvCantidadGrillaLineaTickets.text = lineaTicket.cantidad.toString()
            binding.tvNombreGrillaLineaTickets.text = lineaTicket.descripcion
            binding.tvCategoriaGrillaLineaTickets.text = lineaTicket.categoriaVenta
            binding.tvTotalGrillaLineaTickets.text = binding.root.context.getString(R.string.precio_selector).format(lineaTicket.total)

            Functions.pintarBackgroundSegunLineaGrilla(pos, binding.layoutGrillaLineaTicket)

            if (selectedItem == lineaTicket){
                //Pintamos el color del item seleccionado
                lineaTicketSelected()
            }
        }

        private fun lineaTicketSelected(){
            binding.layoutGrillaLineaTicket.setBackgroundColor(binding.root.context.getColor(R.color.orange))
        }

    }

}