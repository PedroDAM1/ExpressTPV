package com.pedro.expresstpv.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.TiposCobroLayoutBinding
import com.pedro.expresstpv.domain.model.LineaTicket
import com.pedro.expresstpv.domain.model.MetodoPago

class TipoCobroListAdapter : ListAdapter<MetodoPago, TipoCobroListAdapter.TipoCobroViewHolder>(
    DiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipoCobroViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.tipos_cobro_layout, parent, false)
        return TipoCobroViewHolder(v)
    }

    override fun onBindViewHolder(holder: TipoCobroViewHolder, position: Int) {
        holder.render(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<MetodoPago>() {
            override fun areItemsTheSame(oldItem: MetodoPago, newItem: MetodoPago): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MetodoPago, newItem: MetodoPago): Boolean {
                return oldItem == newItem
            }
        }
    }

    class TipoCobroViewHolder(v : View) : ViewHolder(v){

        private val binding = TiposCobroLayoutBinding.bind(v)

        fun render(metodoPago: MetodoPago){
            binding.tvNombreTipoCobroLayout.text = metodoPago.nombre
        }

    }

}