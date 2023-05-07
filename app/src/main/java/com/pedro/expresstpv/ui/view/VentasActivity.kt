package com.pedro.expresstpv.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
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

    private val ventasViewModel by viewModels<VentasViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentasBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                startActivity(Intent(this, ListaArticulosActivity::class.java))
            }

            R.id.miCrearCategoria -> {
                startActivity(Intent(this, CategoriaEditorActivity::class.java))
            }

            R.id.miConfiguracion -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }
}