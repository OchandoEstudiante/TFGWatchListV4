package com.example.tfgwatchlist.watchlist.ui.views.watchlistEpisodes

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfgwatchlist.core.utils.generarURL
import com.example.tfgwatchlist.databinding.ItemWatchlistsearchepisodeBinding
import com.example.tfgwatchlist.watchlist.data.network.model.SerieDetailsEpisodesResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WatchlistEpisodesViewHolder(private val binding: ItemWatchlistsearchepisodeBinding): RecyclerView.ViewHolder(binding.root) {
    fun onBind(episodio: SerieDetailsEpisodesResponse){
        binding.tvItemEpisodeName.text = episodio.numero_episodio.toString() + ":" + episodio.nombre_episodio
        binding.tvItemEpisodePreview.text = episodio.premisa_episodio
        binding.tvItemEpisodeAirDateAndLength.text = "${tratoFecha(episodio.fecha_emision)}"+
                " - " + episodio.duracion + " minutos"
        Glide.with(binding.root.context)
            .load(generarURL("w342${episodio.imagen_episodio}"))
            .into(binding.ivItemEpisodeImage)
    }

    fun tratoFecha(fechaString: String?): String{
        val fecha = LocalDate.parse(fechaString, DateTimeFormatter.ISO_LOCAL_DATE)
        return "${fecha.dayOfMonth} ${fecha.month} ${fecha.year}"
    }

    fun tratoDuracion(){

    }
}