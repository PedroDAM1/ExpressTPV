package com.pedro.expresstpv.ui.adapters
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.CalculadoraVentasLayoutBinding
import com.pedro.expresstpv.databinding.ListaArticulosLayoutBinding
import com.pedro.expresstpv.domain.model.Articulo


class VentasCalculadoraListAdapter :
    ListAdapter<Articulo, VentasCalculadoraListAdapter.CalculadoraArticulosViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalculadoraArticulosViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.calculadora_ventas_layout, parent, false)
        return CalculadoraArticulosViewHolder(v)
    }

    override fun onBindViewHolder(holder: CalculadoraArticulosViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Articulo>() {
            override fun areItemsTheSame(oldItem: Articulo, newItem: Articulo): Boolean {
                Log.d("ADAPTER", "Comprobando las diferencias entre la id ${oldItem.id} y la id ${newItem.id} ")
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Articulo, newItem: Articulo): Boolean {
                Log.d("ADAPTER", "Comparando los objetos $oldItem y $newItem")
                return oldItem == newItem
            }
        }
    }


    class CalculadoraArticulosViewHolder(v : View) : RecyclerView.ViewHolder(v) {

        private val binding = CalculadoraVentasLayoutBinding.bind(v)

        fun bind(articulo: Articulo) {
            binding.tvNombreArticuloCalculadora.text = articulo.nombre
            binding.tvPrecioArticuloCalculadora.text = binding.root.context.getString(R.string.precio_articulo_calculadora).format(articulo.precio)
        }

    }


}