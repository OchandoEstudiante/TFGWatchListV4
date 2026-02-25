package com.example.tfgwatchlist.login.ui.views.RegistrationScreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.BlurEffect
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.tfgwatchlist.R
import com.example.tfgwatchlist.databinding.FragmentRegistrationScreenBinding
import com.example.tfgwatchlist.ui.view.MainActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegistrationScreenFragment : Fragment() {

    private lateinit var binding : FragmentRegistrationScreenBinding

    private val viewModel by viewModels<RegistrationScreenViewModel>{
        RegistrationScreenViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI();
        observeUI();
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showBottomNav(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).showBottomNav(true)
    }

    private fun initUI(){
        binding.buttonRegister.setOnClickListener {
            val registerName = binding.etRegisterNameUser.text.toString()
            val registerPassword = binding.etRegisterPassUser.text.toString()
            //val registerMail = binding.etRegisterMailUser.text.toString()
            Log.i("ChandoRegister", "Click en register")
            if(registerName.length == 0 || registerPassword.length == 0){
                binding.tvInfoRegister.text = "Alguno de los campos están vaciós"
            } else {
                viewModel.registerUser(registerName, registerPassword)
            }
        }
    }

    private fun observeUI(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collectLatest { uiState ->
                    when(uiState){
                        RegistrationScreenUiState.Empty -> {
                            Log.i("RegisterEmpty", "Empty")
                        }
                        is RegistrationScreenUiState.Error -> {
                            Log.i("RegisterError", "${uiState.message}")
                            binding.pbInfoRegister.isVisible = false
                            binding.tvInfoRegister.text = uiState.message
                        }
                        RegistrationScreenUiState.Loading -> {
                            Log.i("RegisterLoad", "Cargando...")
                            binding.pbInfoRegister.isVisible = true
                            binding.tvInfoRegister.text = ""
                        }

                        is RegistrationScreenUiState.registerResponse -> {
                            Log.i("Register response", "${uiState.response}")
                            if(uiState.response.ok){
                                Log.i("TestRegister","Exitoso")
                                binding.pbInfoRegister.isVisible = false
                                binding.tvInfoRegister.text = uiState.response.message
                            } else {
                                Log.i("TestRegister","Erroneo")
                                binding.pbInfoRegister.isVisible = false
                                binding.tvInfoRegister.text = uiState.response.message
                            }
                        }
                    }
                }
            }
        }
    }
}