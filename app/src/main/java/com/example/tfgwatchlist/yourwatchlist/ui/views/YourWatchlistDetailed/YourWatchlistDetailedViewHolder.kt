package com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlistDetailed

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.tfgwatchlist.core.utils.generarURL
import com.example.tfgwatchlist.databinding.ItemWacthlistsearchactorBinding
import com.example.tfgwatchlist.yourwatchlist.data.network.model.CastMongoItem

class YourWatchlistDetailedViewHolder(private val binding: ItemWacthlistsearchactorBinding): ViewHolder(binding.root) {
    fun onBind(itemCast: CastMongoItem){
        binding.itemActorName.text = itemCast.nombre
        binding.itemActorCharacterInterpreted.text = itemCast.personajeInterpretado

        Glide.with(binding.root.context)
            .load(generarURL("w780${itemCast.imagen}"))
            .into(binding.itemActorImage)
    }
}