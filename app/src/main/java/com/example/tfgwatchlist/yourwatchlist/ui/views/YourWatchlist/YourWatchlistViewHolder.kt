package com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlist

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.tfgwatchlist.core.utils.generarURL
import com.example.tfgwatchlist.databinding.ItemWatchlistsearchBinding
import com.example.tfgwatchlist.databinding.ItemYourwatchlistmediaBinding
import com.example.tfgwatchlist.yourwatchlist.data.network.model.ItemMediaMongoItem

class YourWatchlistViewHolder(private val binding: ItemYourwatchlistmediaBinding): ViewHolder(binding.root) {
    fun onBind(item: ItemMediaMongoItem){
        binding.itemMediaName.text = "${item.nombre}"
        binding.itemMediaPreview.text = "${item.premisa}"
        binding.itemMediaDate.text = "${item.fechaEstreno}"
        binding.itemState.text = "Estado: ${item.estado}"

        Glide.with(binding.root.context)
            .load(generarURL("w780${item.imagen}"))
            .into(binding.itemMediaImage)

        Glide.with(binding.root.context)
            .load(generarURL("original${item.banner}"))
            .into(binding.ivMediaBackground)
    }

}