package com.pedro.expresstpv.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ActivityListaCategoriasBinding
import com.pedro.expresstpv.domain.model.LineaTicket
import com.pedro.expresstpv.ui.recyclers.ListaCategoriasAdapter
import com.pedro.expresstpv.ui.viewmodel.ListaCategoriasViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListaCategoriasActivity : AppCompatActivity() {

    private lateinit var binding : ActivityListaCategoriasBinding

    private lateinit var adapter : ListaCategoriasAdapter

    private val viewModel : ListaCategoriasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaCategoriasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        cargarRecycler()
        observarLiveData()
    }

    private fun setListeners(){
        binding.fabAddCategoria.setOnClickListener{
            startActivity(Intent(this, CategoriaEditorActivity::class.java))
        }
    }

    private fun cargarRecycler(){

        val layoutManager = LinearLayoutManager(this)
        adapter = ListaCategoriasAdapter(viewModel.categoriasLiveData.value?.values?.toMutableList() ?: mutableListOf())

        binding.rvListaCategorias.layoutManager = layoutManager
        binding.rvListaCategorias.adapter = adapter
    }

    private fun observarLiveData(){
        viewModel.categoriasLiveData.observe(this){
            adapter.data = it.values.toMutableList()
            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("","")
    }

}