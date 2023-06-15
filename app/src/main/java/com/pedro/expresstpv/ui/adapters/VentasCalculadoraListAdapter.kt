package com.pedro.expresstpv.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.CalculadoraVentasLayoutBinding
import com.pedro.expresstpv.domain.model.Articulo

class VentasCalculadoraListAdapter (val onItemClicked : (ArticuloYCantidad) -> Unit) : ListAdapter<VentasCalculadoraListAdapter.ArticuloYCantidad, VentasCalculadoraListAdapter.VentasCalculadoraViewHolder>(
    DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VentasCalculadoraViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.calculadora_ventas_layout, parent, false)
        return VentasCalculadoraViewHolder(v)
    }

    override fun onBindViewHolder(holder: VentasCalculadoraViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        //Al hacer click sobre un item del adapter
        holder.itemView.setOnClickListener {
            //Obtenemos un entero que sera la cantidad de veces que se ha clicado el boton (Lo equivalente a lineaTicket)
            onItemClicked(item)
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ArticuloYCantidad>() {
            override fun areItemsTheSame(oldItem: ArticuloYCantidad, newItem: ArticuloYCantidad): Boolean {
                return oldItem.articulo.id == newItem.articulo.id
            }

            override fun areContentsTheSame(oldItem: ArticuloYCantidad, newItem: ArticuloYCantidad): Boolean {
                return oldItem == newItem
            }
        }
    }


    class VentasCalculadoraViewHolder(v : View) : ViewHolder(v){

        private val binding = CalculadoraVentasLayoutBinding.bind(v)

        fun bind(articuloYCantidad: ArticuloYCantidad){
            binding.tvNombreArticuloCalculadora.text = articuloYCantidad.articulo.nombre
            binding.tvPrecioArticuloCalculadora.text = binding.root.context.getString(R.string.precio_articulo_calculadora).format(articuloYCantidad.articulo.precio)
            //Si la cantidad es mayor que cero significa que tenemos una lineaTicket
            if(articuloYCantidad.cantidad > 0) {
                binding.tvCantidadArticuloCalculadora.text = articuloYCantidad.cantidad.toString()
                binding.tvCantidadArticuloCalculadora.setBackgroundColor(binding.root.context.getColor(R.color.fondo_boton_cantidad_clicada))
            } else {
                //Si nos llega un 0 deberemos de repintar para evitar problemas
                binding.tvCantidadArticuloCalculadora.text = articuloYCantidad.cantidad.toString()
                binding.tvCantidadArticuloCalculadora.setBackgroundColor(binding.root.context.getColor(R.color.fondo_boton_cantidad_noclicada))
            }
        }
    }

    data class ArticuloYCantidad(
        val articulo: Articulo,
        val cantidad : Int
    )


}