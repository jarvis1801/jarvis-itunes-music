package com.jarvis.itunesmusic.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.itunesmusic.R
import com.jarvis.itunesmusic.data.model.api.Song
import com.jarvis.itunesmusic.databinding.SongListItemBinding
import kotlinx.android.synthetic.main.song_list_item.view.*

class SongAdapter(var songList: MutableList<Song>, private val onAdapterItemClickListener: OnAdapterItemClickListener) : RecyclerView.Adapter<SongAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val songListItemBinding: SongListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.song_list_item, parent, false)

        return DataViewHolder(songListItemBinding.root, onAdapterItemClickListener, parent.context)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentSong: Song = songList[position]
        holder.mSongListItemBinding.song = currentSong
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    interface OnAdapterItemClickListener {
        fun onAdapterItemClick(song: Song)
    }

    class DataViewHolder(itemView: View, onAdapterItemClickListener: OnAdapterItemClickListener, context: Context) : RecyclerView.ViewHolder(itemView) {
        val mSongListItemBinding: SongListItemBinding = DataBindingUtil.bind(itemView)!!

        init {
            itemView.cardview.setOnClickListener {
                onAdapterItemClickListener.onAdapterItemClick(mSongListItemBinding.song!!)
            }

            itemView.textArtistNameAndTrackName.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mSongListItemBinding.song!!.trackViewUrl))
                context.startActivity(browserIntent)
            }

            itemView.textCollectionName.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mSongListItemBinding.song!!.collectionViewUrl))
                context.startActivity(browserIntent)
            }
        }

    }
}