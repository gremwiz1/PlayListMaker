package com.example.playlistmaker.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class TrackAdapter(
    private val trackList: ArrayList<Track>,
    private val onTrackClicked: (Track) -> Unit,
) :
    RecyclerView.Adapter<TrackViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            onTrackClicked(track)
        }
    }

    override fun getItemCount() = trackList.size
}