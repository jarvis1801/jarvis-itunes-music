package com.jarvis.itunesmusic.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.itunesmusic.R
import com.jarvis.itunesmusic.data.model.api.Song
import com.jarvis.itunesmusic.databinding.SongListItemBinding

class SongAdapter(var songList: MutableList<Song>) : RecyclerView.Adapter<SongAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val songListItemBinding: SongListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.song_list_item, parent, false)

        return DataViewHolder(songListItemBinding.root)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentSong: Song = songList[position]
        holder.mSongListItemBinding.song = currentSong
        holder.mSongListItemBinding
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mSongListItemBinding: SongListItemBinding = DataBindingUtil.bind(itemView)!!
    }
}