package com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlistDetailed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.tfgwatchlist.databinding.ItemWacthlistsearchactorBinding
import com.example.tfgwatchlist.watchlist.ui.views.watchlistDetailed.WatchlistDetailsViewHolder
import com.example.tfgwatchlist.yourwatchlist.data.network.model.CastMongoItem

//Pendiente programar el adapter de cast
class YourWatchlistDetailedAdapter: ListAdapter<CastMongoItem, YourWatchlistDetailedViewHolder>(DIFF_CALLBACK) {
    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CastMongoItem>(){
            override fun areItemsTheSame(oldItem: CastMongoItem, newItem: CastMongoItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CastMongoItem, newItem: CastMongoItem): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YourWatchlistDetailedViewHolder {
        val binding = ItemWacthlistsearchactorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return YourWatchlistDetailedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YourWatchlistDetailedViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}