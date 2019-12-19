package com.rondao.shufflesongs.ui.songslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rondao.shufflesongs.databinding.ListItemSongBinding
import com.rondao.shufflesongs.domain.Track

// TODO: Add a click listener with [binding], allowing [SongsListFragment] to
//  retrieve clicked element and share with [SongsListViewModel].
//  [SongsListViewModel] can set a [LiveEvent] for navigation, passing the
//  selected [Track] with SafeArgs to another fragment. Showing song details
//  with another layout and biding with [Track].
class SongsListAdapter : ListAdapter<Track,
        SongsListAdapter.ViewHolder>(SongsListDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ListItemSongBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Track) {
            binding.track = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSongBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class SongsListDiffCallback : DiffUtil.ItemCallback<Track>() {
    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem == newItem
    }
}
