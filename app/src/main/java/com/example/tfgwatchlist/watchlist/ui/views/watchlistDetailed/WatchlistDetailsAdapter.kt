package com.example.tfgwatchlist.watchlist.ui.views.watchlistDetailed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.layout.Layout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.tfgwatchlist.databinding.ItemWacthlistsearchactorBinding
import com.example.tfgwatchlist.databinding.ItemWatchlistsearchepisodeBinding
import com.example.tfgwatchlist.watchlist.data.network.model.General.CastResponse
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsEpisodesResponse

class WatchlistDetailsAdapter: ListAdapter<CastResponse, WatchlistDetailsViewHolder>(DIFF_CALLBACK) {
    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CastResponse>(){
            override fun areItemsTheSame(oldItem: CastResponse, newItem: CastResponse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CastResponse, newItem: CastResponse): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistDetailsViewHolder {
        val binding = ItemWacthlistsearchactorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WatchlistDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchlistDetailsViewHolder, position: Int) {
        holder.OnBind(getItem(position))
    }
}