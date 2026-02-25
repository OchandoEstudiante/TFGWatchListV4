package com.example.tfgwatchlist.yourwatchlist.ui.views.YourWatchlistEpisodes

import android.util.Log
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.tfgwatchlist.core.utils.generarURL
import com.example.tfgwatchlist.databinding.ItemWatchlistsearchepisodeBinding
import com.example.tfgwatchlist.yourwatchlist.data.network.model.EpisodioMongoItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class YourWatchlistEpisodesViewHolder(private val binding: ItemWatchlistsearchepisodeBinding): ViewHolder(binding.root) {

    fun onBind(item: EpisodioMongoItem){
        binding.tvItemEpisodeName.text = "${item.numeroEpisodio} : ${item.nombreEpisodio}"
        binding.tvItemEpisodePreview.text = item.premisaEpisodio
        binding.tvItemEpisodeAirDateAndLength.text = "${tratoFechas(item.fecha_emision)}" +
                "- ${item.duracion} minutos"
        Glide.with(binding.root.context)
            .load(generarURL("w342${item.imagenEpisodio}"))
            .into(binding.ivItemEpisodeImage)

    }

    fun tratoFechas(fechaString: String?): String{
        if(fechaString == null){
            return ""
        } else {
            val fecha = LocalDate.parse(fechaString, DateTimeFormatter.ISO_LOCAL_DATE)
            if(fecha.isAfter(LocalDate.now())){
                Log.i("Tiempo", "Después")
                binding.tvItemEpisodePreview.text = "Este episodio todavía no se ha emitido"
            }
            return "${fecha.dayOfMonth} ${fecha.month} ${fecha.year}"
        }
    }
}