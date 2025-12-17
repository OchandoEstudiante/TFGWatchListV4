package com.example.tfgwatchlist.watchlist.ui.views.watchlistEpisodes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.tfgwatchlist.databinding.ItemWatchlistsearchepisodeBinding
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsEpisodesResponse

class WatchlistEpisodesAdapter: ListAdapter<SerieDetailsEpisodesResponse, WatchlistEpisodesViewHolder>(DIFF_CALLBACK) {

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SerieDetailsEpisodesResponse>(){
            override fun areItemsTheSame(oldItem: SerieDetailsEpisodesResponse, newItem: SerieDetailsEpisodesResponse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SerieDetailsEpisodesResponse, newItem: SerieDetailsEpisodesResponse): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistEpisodesViewHolder {
        val binding = ItemWatchlistsearchepisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WatchlistEpisodesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchlistEpisodesViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }



}