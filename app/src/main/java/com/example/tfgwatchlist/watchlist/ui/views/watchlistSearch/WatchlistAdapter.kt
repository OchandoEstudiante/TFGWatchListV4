package com.example.tfgwatchlist.watchlist.ui.views.watchlistSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tfgwatchlist.databinding.ItemWatchlistsearchBinding
import com.example.tfgwatchlist.watchlist.data.network.model.MediaItem

class WatchlistAdapter(
    val onItemClickListener: (MediaItem) -> Unit = {}
): ListAdapter<MediaItem, WatchlistViewHolder>(DIFF_CALLBACK){

    companion object{
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<MediaItem>(){
            override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context).let{ layoutInflater ->
            ItemWatchlistsearchBinding.inflate(layoutInflater, parent, false)
        } .let{ binding ->
            WatchlistViewHolder(binding).also { viewHolder ->
                viewHolder.itemView.setOnClickListener {
                    onItemClickListener(currentList[viewHolder.adapterPosition])
                }
            }
        }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}