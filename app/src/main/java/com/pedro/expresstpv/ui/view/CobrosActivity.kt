package com.pedro.expresstpv.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.pedro.expresstpv.databinding.ActivityCobrosBinding
import com.pedro.expresstpv.ui.viewmodel.CobrosViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CobrosActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCobrosBinding

    private val viewModel : CobrosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCobrosBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}