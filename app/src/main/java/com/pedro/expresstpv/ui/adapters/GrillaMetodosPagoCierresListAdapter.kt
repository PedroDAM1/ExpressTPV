package com.pedro.expresstpv.ui.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.GrillaMetodosPagoCierresLayoutBinding
import com.pedro.expresstpv.domain.model.MetodoPago

class GrillaMetodosPagoCierresListAdapter (
    private val onEditTextChange : (String, Int) -> Unit,
    ) : ListAdapter<GrillaMetodosPagoCierresListAdapter.MetodosPagoYTotalesTicket,
    GrillaMetodosPagoCierresListAdapter.GrillaMetodosPagosCierresViewHolder>(
     DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GrillaMetodosPagosCierresViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.grilla_metodos_pago_cierres_layout, parent, false)
        return GrillaMetodosPagosCierresViewHolder(v)
    }

    override fun onBindViewHolder(holder: GrillaMetodosPagosCierresViewHolder, position: Int) {
        holder.bind(getItem(position), onEditTextChange)
    }

    companion object{
        val DiffCallback = object : DiffUtil.ItemCallback<MetodosPagoYTotalesTicket>(){
            override fun areItemsTheSame(
                oldItem: MetodosPagoYTotalesTicket,
                newItem: MetodosPagoYTotalesTicket
            ): Boolean {
                return oldItem.metodoPago.id == newItem.metodoPago.id
            }

            override fun areContentsTheSame(
                oldItem: MetodosPagoYTotalesTicket,
                newItem: MetodosPagoYTotalesTicket
            ): Boolean {
                return oldItem == newItem
            }

        }
    }


    inner class GrillaMetodosPagosCierresViewHolder(v : View) : ViewHolder(v){

        private val binding = GrillaMetodosPagoCierresLayoutBinding.bind(v)

        fun bind(
            metodosPagoYTotalesTicket: MetodosPagoYTotalesTicket,
            onEditTextChange: (String, Int) -> Unit,
        ){
            binding.tvNombreGrillaMetodoPagoCierres.text = metodosPagoYTotalesTicket.metodoPago.nombre
            binding.tvTotalPagadoGrillaMetodoPagoCierres.text = binding.root.context.getString(R.string.precio_selector).format(metodosPagoYTotalesTicket.total)


            binding.etCantidadGrillaMetodoPagoCierres.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun afterTextChanged(p0: Editable?) {
                    onEditTextChange(p0.toString(), metodosPagoYTotalesTicket.metodoPago.id)
                }
            })

            binding.etCantidadGrillaMetodoPagoCierres.setOnFocusChangeListener{_, hasFocus ->
                if (!hasFocus && binding.etCantidadGrillaMetodoPagoCierres.text.toString().isNotEmpty()){
                    val value = binding.etCantidadGrillaMetodoPagoCierres.text.toString().toDouble()
                    binding.etCantidadGrillaMetodoPagoCierres.setText(binding.root.context.getString(R.string.precio_selector).format(value))
                }
            }
        }

//        fun isEditTextEmpty(){
//            if (binding.etCantidadGrillaMetodoPagoCierres.text.toString().isEmpty()){
//                binding.etCantidadGrillaMetodoPagoCierres.error = "Debes introducir una cantidad"
//            }
//        }

    }

    data class MetodosPagoYTotalesTicket(
        val metodoPago: MetodoPago,
        val total : Double
    )
}