package com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlistEpisodes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tfgwatchlist.R
import com.example.tfgwatchlist.core.utils.generarURL
import com.example.tfgwatchlist.databinding.FragmentYourWatchlistEpisodesBinding
import com.example.tfgwatchlist.yourwatchlist.data.network.model.TemporadaMongoItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class YourWatchlistEpisodesFragment : Fragment() {

    private lateinit var binding: FragmentYourWatchlistEpisodesBinding
    private lateinit var adapterEpisodios: YourWatchlistEpisodesAdapter
    private val viewModel by viewModels<YourWatchlistEpisodesViewModel> {
        YourWatchlistEpisodesViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentYourWatchlistEpisodesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: YourWatchlistEpisodesFragmentArgs by navArgs()
        val serieId = args.serieId

        adapterEpisodios = YourWatchlistEpisodesAdapter()
        binding.rvWatchlistEpisodes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWatchlistEpisodes.adapter = adapterEpisodios

        initUI(serieId)
        observeUI(serieId)
    }

    private fun initUI(idSerie: Int) {
        Log.i("Menu temporadas", "Obtenida id: ${idSerie}")
        viewModel.fetchSeasonsNames(idSerie)
    }

    private fun observeUI(idSerie: Int){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collectLatest { uiState ->
                    Log.i("UI_STATE_DEBUG", uiState.toString())
                    when(uiState){
                        YourWatchlistEpisodesUiState.Empty -> {
                            Log.i("ObserveUI Empty", "Vacio...")
                        }
                        is YourWatchlistEpisodesUiState.Error -> {
                            binding.pbEpisodesWatchlist.isVisible = false
                            binding.rvWatchlistEpisodes.isVisible = false
                            //Pendiente agregar mensaje de error
                        }
                        YourWatchlistEpisodesUiState.Loading -> {
                            Log.i("ObserveUI Loading", "Cargando...")
                            binding.rvWatchlistEpisodes.isVisible = false
                            binding.pbEpisodesWatchlist.isVisible = true
                        }
                        is YourWatchlistEpisodesUiState.Success -> {
                            binding.pbEpisodesWatchlist.isVisible = false
                            binding.rvWatchlistEpisodes.isVisible = true
                            Log.i("Ejemplo", "LIsta de la temporada: ${uiState}")
                            //Metodo para modificar algunas cosas, sacar además lo de los caps
                            adapterEpisodios.submitList(uiState.season.episodios)
                            showSeasonData(uiState.season)
                        }

                        is YourWatchlistEpisodesUiState.SuccessNames -> {
                            setSpinner(idSerie, uiState.temporadas)
                        }
                    }
                }
            }
        }
    }

    private fun setSpinner(idSerie: Int, temporadas: List<String>){
        //Pendiente hacer el traspaso de array de nombres de temporadas
        val seasonsAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, temporadas)
        seasonsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spSeasonsEpisodes.adapter = seasonsAdapter
        //if(temporadas[0].nombreTemporada == "Specials"){
        //    binding.spSeasonsEpisodes.setSelection(1)
       // }
        binding.spSeasonsEpisodes.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    //Mejorar esto más tarde, ahora con los errores que tiene ni ejecuta
                    if(temporadas[0] == "Specials"){
                        viewModel.fetchTemporada(idSerie, position)
                    } else {
                        viewModel.fetchTemporada(idSerie, position+1)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //Tengo sueño jaja salu2
                }
            }
    }
    private fun showSeasonData(temporadaMongoItem: TemporadaMongoItem){
        binding.tvPremisaTemporadaWatchlistTemporada.text = temporadaMongoItem.premisaTemporada
        binding.tvTituloTemporadaWatchlistTemporada.text = temporadaMongoItem.nombreTemporada

        Glide.with(binding.root.context)
            .load(generarURL("w342${temporadaMongoItem.posterTemporada}"))
            .into(binding.ivSerieImageWatchlistTemporada)
    }
}

//Semifunciona, hacer más tarde pruebas EXTENSAS para probar su funcionamiento en detalles