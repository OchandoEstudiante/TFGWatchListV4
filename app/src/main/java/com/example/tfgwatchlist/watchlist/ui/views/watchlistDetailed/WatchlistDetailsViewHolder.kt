package com.example.tfgwatchlist.watchlist.ui.views.watchlistDetailed

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfgwatchlist.core.utils.generarURL
import com.example.tfgwatchlist.databinding.ItemWacthlistsearchactorBinding
import com.example.tfgwatchlist.databinding.ItemWatchlistsearchepisodeBinding
import com.example.tfgwatchlist.watchlist.data.network.model.General.CastResponse
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsEpisodesResponse

class WatchlistDetailsViewHolder(private val binding: ItemWacthlistsearchactorBinding): RecyclerView.ViewHolder(binding.root){

    fun OnBind(actor: CastResponse){
        binding.itemActorName.text = actor.name
        binding.itemActorCharacterInterpreted.text = actor.character
        Glide.with(binding.root.context)
            .load(generarURL("w780${actor.imagenPerfil}"))
            .into(binding.itemActorImage)
    }
}
