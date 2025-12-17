package com.example.tfgwatchlist.watchlist.ui.views.watchlistSearch

import android.util.Log
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.tfgwatchlist.core.utils.generarURL
import com.example.tfgwatchlist.databinding.ItemWatchlistsearchBinding
import com.example.tfgwatchlist.watchlist.data.network.model.MediaItem

class WatchlistViewHolder(private val binding: ItemWatchlistsearchBinding): ViewHolder(binding.root) {
    fun onBind(item: MediaItem) {
        binding.itemMediaName.text = "${item.titulo}"
        binding.itemMediaPreview.text = "${item.premisa}"
        binding.itemMediaDate.text = "${item.fechaEstreno}"

        /*when(item){
            is PeliculaResponse -> Log.i("Chando", "Es una pelicula")
            is SerieResponse -> Log.i("Chando", "Es una serie")
        }
        */
        Glide.with(binding.root.context)
            .load(generarURL("w780${item.poster}"))
            .into(binding.itemMediaImage)

        Glide.with(binding.root.context)
            .load(generarURL("original${item.banner}"))
            .into(binding.ivMediaBackground)


    }
}