package com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlistEpisodes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.tfgwatchlist.databinding.ItemWatchlistsearchepisodeBinding
import com.example.tfgwatchlist.yourwatchlist.data.network.model.EpisodioMongoItem

class YourWatchlistEpisodesAdapter: ListAdapter<EpisodioMongoItem, YourWatchlistEpisodesViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YourWatchlistEpisodesViewHolder {
        val binding = ItemWatchlistsearchepisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return YourWatchlistEpisodesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YourWatchlistEpisodesViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object{
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<EpisodioMongoItem>(){
            override fun areItemsTheSame(oldItem: EpisodioMongoItem, newItem: EpisodioMongoItem): Boolean {
                //Usar Cinderella Gray para probarlo
                return oldItem.numeroEpisodio == newItem.numeroEpisodio
            }

            override fun areContentsTheSame(oldItem: EpisodioMongoItem, newItem: EpisodioMongoItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}