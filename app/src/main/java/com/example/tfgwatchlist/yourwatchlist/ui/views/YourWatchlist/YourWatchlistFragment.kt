package com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.compose.ui.graphics.BlurEffect
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tfgwatchlist.R
import com.example.tfgwatchlist.core.datastore.AuthDataStore
import com.example.tfgwatchlist.databinding.FragmentWatchlistDetailedBinding
import com.example.tfgwatchlist.databinding.FragmentYourWatchlistBinding
import com.example.tfgwatchlist.shared.UserViewModel
import com.example.tfgwatchlist.yourwatchlist.data.network.model.ItemMediaMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.MovieMongoItem
import com.example.tfgwatchlist.yourwatchlist.data.network.model.SerieMongoItem
import com.example.tfgwatchlist.yourwatchlist.domain.MediaStatus
import com.example.tfgwatchlist.yourwatchlist.ui.model.SpinnerItemUi
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class YourWatchlistFragment : Fragment() {

    private lateinit var binding: FragmentYourWatchlistBinding
    private lateinit var modo: String
    private lateinit var nombreUsuario: String
    private val userViewModel: UserViewModel by activityViewModels()
    private val viewModel by viewModels<YourWatchlistViewModel>{
        YourWatchlistViewModel.Factory
    }

    private val adapter by lazy { YourWatchlistAdapter{onItemClick(it)} }

    private fun onItemClick(item: ItemMediaMongoItem){
        when(item){
            is MovieMongoItem -> {
                Log.i("Chando", "Click en serie de tu watchlist: ${item.nombre}")
                val action = YourWatchlistFragmentDirections
                    .actionYourWatchlistFragmentToYourWatchlistDetailedFragment(item, nombreUsuario)
                findNavController().navigate(action)
            }
            is SerieMongoItem -> {
                Log.i("Chando", "Click en serie de tu watchlist: ${item.nombre}")
                val action = YourWatchlistFragmentDirections
                    .actionYourWatchlistFragmentToYourWatchlistDetailedFragment(item, nombreUsuario)
                findNavController().navigate(action)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentYourWatchlistBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authDataStore = AuthDataStore(requireContext())
        lifecycleScope.launch {
            authDataStore.userToken.collect { storedUserName ->
                storedUserName?.let {
                    nombreUsuario = it
                    initUI(nombreUsuario)
                }
            }
        }
        //val args = YourWatchlistFragmentArgs.fromBundle(requireArguments())
        //userViewModel.setUserName(args.userName)
        //nombreUsuario = arguments?.getSerializable("userName") as String
        //initUI(nombreUsuario)
        observeUI()
        binding.rvWatchlistSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWatchlistSearch.adapter = adapter
        modo = "Series"
    }

    private fun observeUI(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collectLatest { uiState ->
                    binding.pbYourWatchlist.isVisible = false
                    when(uiState){
                        YourWatchlistUiState.Empty -> {
                            Log.i("Chando", "Vacio")
                            binding.tvWatchlistSearchInfo.text = ""
                        }
                        YourWatchlistUiState.Loading -> {
                            binding.rvWatchlistSearch.isVisible = false
                            binding.pbYourWatchlist.isVisible = true
                            binding.tvWatchlistSearchInfo.text = ""
                            Log.i("Chando", "Cargando tu watchlist")
                        }
                        is YourWatchlistUiState.Error -> {
                            //Pendiente agregar que saque por pantalla el fallo o algo no se
                            Log.i("Chando", "Error en tu watchlist ${uiState.message}")
                            binding.tvWatchlistSearchInfo.text = uiState.message
                        }
                        is YourWatchlistUiState.Success -> {
                            if(modo == "Series"){
                                if(uiState.items.size == 0){
                                    binding.rvWatchlistSearch.isVisible = false
                                    binding.tvWatchlistSearchInfo.text = getString(R.string.noSeriesFoundAtYourWatchlist)
                                } else {
                                    binding.rvWatchlistSearch.isVisible = true
                                    binding.tvWatchlistSearchInfo.text = ""
                                    adapter.submitList(uiState.items)
                                }
                            } else {
                                if(uiState.items.size == 0){
                                    binding.rvWatchlistSearch.isVisible = false
                                    binding.tvWatchlistSearchInfo.text = getString(R.string.noMoviesFoundAtYourWatchlist)
                                } else {
                                    binding.rvWatchlistSearch.isVisible = true
                                    binding.tvWatchlistSearchInfo.text = ""
                                    adapter.submitList(uiState.items)
                                }
                            }
                            Log.i("Chando", "Exito en tu watchlist")
                        }

                        is YourWatchlistUiState.SuccessFiltered -> {
                            if(modo == "Series"){
                                if(uiState.items.size == 0){
                                    binding.rvWatchlistSearch.isVisible = false
                                    binding.tvWatchlistSearchInfo.text = getString(R.string.noSeriesFoundByFiltersAtYourWatchlist)
                                } else {
                                    binding.rvWatchlistSearch.isVisible = true
                                    binding.tvWatchlistSearchInfo.text = ""
                                    adapter.submitList(uiState.items)
                                }
                            } else {
                                if(uiState.items.size == 0){
                                    binding.rvWatchlistSearch.isVisible = false
                                    binding.tvWatchlistSearchInfo.text = getString(R.string.noMoviesFoundByFiltersAtYourWatchlist)
                                } else {
                                    binding.rvWatchlistSearch.isVisible = true
                                    binding.tvWatchlistSearchInfo.text = ""
                                    adapter.submitList(uiState.items)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initUI(userName: String){
        //Pendiente programar el cambio de iconos
        binding.btnChangeWatchlistSearch.setOnClickListener {
            Log.i("Chando", "Click en cambio")
            if(modo == "Series"){
                modo = "Peliculas"
                binding.btnChangeWatchlistSearch.setImageResource(R.drawable.ic_movie)
            } else {
                modo = "Series"
                binding.btnChangeWatchlistSearch.setImageResource(R.drawable.ic_television)
            }
            searchLocal(userName)
        }

        binding.tvInfoUser.text = getString(R.string.welcomeText) + ", ${nombreUsuario}"

        binding.btnReloadWatchlistSearch.setOnClickListener {
            Log.i("Chando", "Click en recarga")
            searchLocal(userName)
        }

        binding.btnCloseSessionUser.setOnClickListener {
            lifecycleScope.launch {
                val authDataStore = AuthDataStore(requireContext())
                authDataStore.clearSession()

                /*val action = YourWatchlistFragmentDirections
                    .actionYourWatchlistFragmentToLoginScreenFragment()
                findNavController().navigate(action)*/
            }
        }
        setSpinner(userName)
        setSearchBar(userName)
    }

    //Pendiente programar el spinner
    private fun setSpinner(userName: String){
        val estados = listOf(
            SpinnerItemUi(MediaStatus.ALL, getString(R.string.statusMediaTextAll)),
            SpinnerItemUi(MediaStatus.WATCHING, getString(R.string.statusMediaTextWatching)),
            SpinnerItemUi(MediaStatus.WATCHED,getString(R.string.statusMediaTextWatched)),
            SpinnerItemUi(MediaStatus.HALF_WATCHED, getString(R.string.statusMediaTextHalfWatched)),
            SpinnerItemUi(MediaStatus.TO_BE_SEEN, getString(R.string.statusMediaTextToBeSeen))
        )
        //val estados = listOf(getString(R.string.statusMediaTextAll),getString(R.string.statusMediaTextWatching) ,
            //getString(R.string.statusMediaTextWatched), getString(R.string.statusMediaTextHalfWatched),
            //getString(R.string.statusMediaTextToBeSeen))
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spFiltersYourWatchlist.adapter = adapter
        binding.spFiltersYourWatchlist.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    searchLocal(userName)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //
                }
            }
    }

    private fun setSearchBar(userName: String){
        binding.svWatchlistSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchLocal(userName)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    //Funcion general de busqueda
    private fun searchLocal(userName: String){
        val spinnerItem = binding.spFiltersYourWatchlist.selectedItem as SpinnerItemUi
        val status = spinnerItem.status
        val searchBarQuery = binding.svWatchlistSearch.query.toString()
        if(searchBarQuery.length == 0 && status == MediaStatus.ALL){
            if(modo == "Series"){
                viewModel.fetchSeries(userName)
            } else {
                viewModel.fetchPeliculas(userName)
            }
        } else {
            if(modo == "Series"){
                viewModel.fetchSeriesFiltered(userName, status.key, searchBarQuery)
            } else {
                viewModel.fetchMoviesFiltered(userName, status.key, searchBarQuery)
            }
        }
    }
}

//Bugs encontrados:
//Ninguno de momento
//Pendiente:
//-Poner un texto informatico en caso de errores, timeouts, falta de respuesta, etc
//- Agregar traduccion y que sea funcional(ver mayormente como traducir objetos de viewHolder y como hacer que se traten cosas de forma distinta según el idioma del usuario)
//Completo:
//-Busqueda filtrada de series y películas
//-Boton cambiante
