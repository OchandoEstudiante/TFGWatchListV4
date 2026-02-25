package com.example.tfgwatchlist.login.ui.views.loginScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.tfgwatchlist.R
import com.example.tfgwatchlist.databinding.FragmentLoginScreenBinding
import com.example.tfgwatchlist.ui.view.MainActivity
import kotlinx.coroutines.launch
import androidx.fragment.app.viewModels
import com.example.tfgwatchlist.core.datastore.AuthDataStore
import kotlinx.coroutines.flow.collectLatest

class LoginScreenFragment : Fragment() {

    private lateinit var binding: FragmentLoginScreenBinding

    private val viewModel by viewModels<LoginScreenViewModel>{
        LoginScreenViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
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
        binding.buttonRegisterUser.setOnClickListener {
            Log.i("ChandoLogin", "Click para crear usuario")
            val actionRegister = LoginScreenFragmentDirections.actionLoginFragmentToRegistrationFragment()
            findNavController().navigate(actionRegister)
        }

        binding.buttonStartSession.setOnClickListener {
            val nombreUsuario = binding.etLoginNameUser.text.toString()
            val passwordUsuario = binding.etLoginUserPassword.text.toString()
            if(nombreUsuario.length == 0 || passwordUsuario.length == 0){
                binding.tvInfoLogin.text = "Alguno de los campos está vacío"
            } else {
                viewModel.loginUser(binding.etLoginNameUser.text.toString(), binding.etLoginUserPassword.text.toString())
            }
            Log.i("ChandoLogin", "Click para iniciar sesión")
        }
    }

    private fun observeUI(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collectLatest { uiState ->
                    binding.pbInfoLogin.isVisible = false
                    when(uiState){
                        LoginScreenUiState.Empty -> {
                            Log.i("LoginEmpty", "Empty")
                        }
                        is LoginScreenUiState.Error -> {
                            binding.pbInfoLogin.isVisible = false
                            binding.tvInfoLogin.text = uiState.message
                            Log.i("LoginError", "${uiState.message}")
                        }
                        LoginScreenUiState.Loading -> {
                            Log.i("LoginLoad", "Cargando...")
                            binding.pbInfoLogin.isVisible = true
                            binding.tvInfoLogin.text = ""
                        }
                        is LoginScreenUiState.loginResponse -> {
                            binding.pbInfoLogin.isVisible = false
                            Log.i("LoginResponse", "${uiState.response}")
                            if(uiState.response.ok){
                                //Log.i("CHANDODEBUG", uiState.response.toString())
                                val actionAccess = LoginScreenFragmentDirections.actionLoginFragmentToYourWatchlistFragment(uiState.response.userName.orEmpty())
                                findNavController().navigate(actionAccess)
                            } else {
                                Log.i("Chando login error", "${uiState.response.message}")
                                binding.tvInfoLogin.text = uiState.response.message
                            }
                        }
                    }
                }
            }
        }
    }

    //Pendiente
    //Hacer que si pueda mostrar los mensajes de errores 404
}