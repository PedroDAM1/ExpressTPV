package com.pedro.expresstpv.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.pedro.expresstpv.databinding.ActivityCierresBinding
import com.pedro.expresstpv.ui.viewmodel.CierresViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CierresActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCierresBinding
    private val viewModel : CierresViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCierresBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}