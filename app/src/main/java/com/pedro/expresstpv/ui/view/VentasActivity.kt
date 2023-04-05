package com.pedro.expresstpv.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.pedro.expresstpv.ExpressTPVApp
import com.pedro.expresstpv.R
import com.pedro.expresstpv.databinding.ActivityVentasBinding
import com.pedro.expresstpv.ui.viewmodel.VentasViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VentasActivity() : AppCompatActivity() {

    private lateinit var binding : ActivityVentasBinding

    private val ventasViewModel : VentasViewModel by viewModels()

    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ventasViewModel.onCreate()

        ventasViewModel.liveDataArticulos.observe(this, Observer {
            binding.tvExample.text = it[0].articuloEntity.nombre
        })
    }

    /**
     * Pintamos el menu en la pantalla prinicipal
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Damos funcionalidad a los botones dle menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //Vamos al activity para crear los articulos
        when (item.itemId){
            R.id.miCrearArticulo ->{
                //startActivity(Intent(this, SelectorArticulosActivity::class.java))
            }

            R.id.miCrearCategoria -> {

            }

            R.id.miConfiguracion -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }
}