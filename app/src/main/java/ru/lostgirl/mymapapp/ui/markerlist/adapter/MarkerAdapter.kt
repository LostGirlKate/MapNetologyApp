package ru.lostgirl.mymapapp.ui.markerlist.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.lostgirl.mymapapp.R
import ru.lostgirl.mymapapp.databinding.MarkItemBinding
import ru.lostgirl.mymapapp.model.Marker

interface OnInteractionListener {
    fun onEditClick(marker: Marker)
    fun onDeleteClick(marker: Marker)
    fun onItemClick(marker: Marker)
}

class MarkerAdapter(private val onInteractionListener: OnInteractionListener) :
    ListAdapter<Marker, TrackViewHolder>(MarkerDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = MarkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val marker = getItem(position)
        val res: Resources = holder.itemView.context.resources
        holder.bind(marker, res)
    }
}

class TrackViewHolder(
    private val binding: MarkItemBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(marker: Marker, res: Resources) {
        binding.apply {
            markerTitle.text = marker.description
            markerCoords.text =
                res.getString(R.string.coords, marker.latitude.toString(), marker.longitude.toString())
            buttonEdit.setOnClickListener {
                onInteractionListener.onEditClick(marker)
            }
            buttonDelete.setOnClickListener {
                onInteractionListener.onDeleteClick(marker)
            }
            markerCard.setOnClickListener {
                onInteractionListener.onItemClick(marker)
            }
        }
    }
}

class MarkerDiffCallback : DiffUtil.ItemCallback<Marker>() {
    override fun areItemsTheSame(oldItem: Marker, newItem: Marker): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Marker, newItem: Marker): Boolean {
        return oldItem == newItem
    }
}