package com.pedro.expresstpv.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.GrillaDetalleVentasLayoutBinding
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.domain.model.LineaTicket

class DetalleVentasListAdapter : ListAdapter<LineaTicket, DetalleVentasListAdapter.DetalleVentasViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetalleVentasViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.grilla_detalle_ventas_layout, parent, false)
        return DetalleVentasViewHolder(v)
    }

    override fun onBindViewHolder(holder: DetalleVentasViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<LineaTicket>() {
            override fun areItemsTheSame(oldItem: LineaTicket, newItem: LineaTicket): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: LineaTicket, newItem: LineaTicket): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class DetalleVentasViewHolder(v : View) : ViewHolder(v){

        private val binding = GrillaDetalleVentasLayoutBinding.bind(v)

        fun bind(lineaTicket: LineaTicket, pos : Int){
            binding.tvCantidadGrillaDetalleVentas.text = lineaTicket.cantidad.toString()
            binding.tvNombreGrillaDetalleVentas.text = lineaTicket.descripcion
//            binding.tvIvaGrillaDetalleVentas.text = binding.root.context.getString(R.string.iva_formated).format(lineaTicket.valorIva)
            binding.tvIvaGrillaDetalleVentas.text = lineaTicket.valorIva.toString()
            binding.tvTotalGrillaDetalleVentas.text = binding.root.context.getString(R.string.precio_selector).format(lineaTicket.total)

            //Si es par pintamos la row de una forma diferente para que se pinte una par y otra impar
            Functions.pintarBackgroundSegunLineaGrilla(pos, binding.layoutGrillaDetalleVentas)
        }


    }

}