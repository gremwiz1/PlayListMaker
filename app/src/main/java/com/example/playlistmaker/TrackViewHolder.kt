package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageTrack: ImageView = itemView.findViewById(R.id.imageTrack)
    private val nameTrack: TextView = itemView.findViewById(R.id.nameTrack)
    private val durationTrack: TextView = itemView.findViewById(R.id.durationTrack)
    private val nameArtist: TextView = itemView.findViewById(R.id.nameArtist)

    fun bind(model: Track) {
        nameTrack.text = model.trackName
        durationTrack.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis.toInt())
        nameArtist.text = model.artistName
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(10))
            .into(imageTrack)
    }
}