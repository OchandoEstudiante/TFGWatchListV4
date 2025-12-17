package com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.tfgwatchlist.databinding.ItemYourwatchlistmediaBinding
import com.example.tfgwatchlist.yourwatchlist.data.network.model.ItemMediaMongoItem

class YourWatchlistAdapter(
    val onItemClickListener: (ItemMediaMongoItem) -> Unit= {}
): ListAdapter<ItemMediaMongoItem, YourWatchlistViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context).let{ layoutInflater ->
            ItemYourwatchlistmediaBinding.inflate(layoutInflater, parent, false)
        }.let { binding ->
            YourWatchlistViewHolder(binding).also{ viewHolder ->
                viewHolder.itemView.setOnClickListener {
                    onItemClickListener(currentList[viewHolder.adapterPosition])
                }
            }
        }

    override fun onBindViewHolder(holder: YourWatchlistViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object{
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<ItemMediaMongoItem>(){
            override fun areItemsTheSame(oldItem: ItemMediaMongoItem, newItem: ItemMediaMongoItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemMediaMongoItem, newItem: ItemMediaMongoItem): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}