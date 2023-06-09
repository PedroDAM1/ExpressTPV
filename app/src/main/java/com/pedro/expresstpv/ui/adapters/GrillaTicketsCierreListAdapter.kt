package com.pedro.expresstpv.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.GrillaTicketCierresLayoutBinding
import com.pedro.expresstpv.domain.functions.Functions
import com.pedro.expresstpv.domain.model.Ticket
import java.time.format.DateTimeFormatter


class GrillaTicketsCierreListAdapter : ListAdapter<Ticket ,GrillaTicketsCierreListAdapter.GrillaTicketsCierreViewHolder>(
    DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GrillaTicketsCierreViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.grilla_ticket_cierres_layout, parent, false)
        return GrillaTicketsCierreViewHolder(v)
    }

    override fun onBindViewHolder(holder: GrillaTicketsCierreViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }


    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Ticket>() {
            override fun areItemsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
                return oldItem.numTicket == newItem.numTicket
            }

            override fun areContentsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class GrillaTicketsCierreViewHolder(v : View) : ViewHolder(v){
        private val binding = GrillaTicketCierresLayoutBinding.bind(v)

        private val formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm")
        fun bind(ticket : Ticket, pos: Int){
            binding.tvNumTicketGrillaTicketsCierre.text = ticket.numTicket.toString()
            binding.tvFechaGrillaTicketCierres.text = ticket.fecha?.format(formatter)
            binding.tvMetodoPagoGrillaTicketCierres.text = ticket.metodoPago.nombre
            binding.tvTotalGrillaTicketsCierres.text = binding.root.context.getString(R.string.precio_selector).format(ticket.total)

            Functions.pintarBackgroundSegunLineaGrilla(pos, binding.layoutGrillaTicketCierres)
        }
    }

}