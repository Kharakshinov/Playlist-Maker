package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.*

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
    private val photo: ImageView = itemView.findViewById(R.id.photoImageView)
    private val track: TextView = itemView.findViewById(R.id.track_name_TextView)
    private val author: TextView = itemView.findViewById(R.id.author_name)
    private val length: TextView = itemView.findViewById(R.id.track_length)
    private val ellipse: ImageView = itemView.findViewById(R.id.icon_ellipse)
    private val iconForward: ImageView = itemView.findViewById(R.id.icon_forward)

    fun bind (item: Track){
        track.text = item.trackName
        length.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
        author.text = item.artistName

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .centerInside()
            .placeholder(R.drawable.icon_no_reply)
            .transform(RoundedCorners(5))
            .into(photo)

        Glide.with(itemView)
            .load(R.drawable.icon_ellipse)
            .centerInside()
            .into(ellipse)

        Glide.with(itemView)
            .load(R.drawable.icon_arrow_forward)
            .centerInside()
            .into(iconForward)

    }
}