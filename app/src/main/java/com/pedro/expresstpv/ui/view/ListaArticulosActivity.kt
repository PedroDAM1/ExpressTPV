package com.pedro.expresstpv.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ActivityListaArticulosBinding

class ListaArticulosActivity : AppCompatActivity() {

    private lateinit var binding : ActivityListaArticulosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaArticulosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
    }

    private fun setListeners(){
        binding.fabAddArticulo.setOnClickListener{
            startActivity(Intent(this, ArticuloEditorActivity::class.java))
        }
    }
}