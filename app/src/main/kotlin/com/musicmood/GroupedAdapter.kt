package com.musicmood

import android.graphics.drawable.GradientDrawable
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class GroupedAdapter(
    private val onSongClick: (Song) -> Unit,
    private val onSongLongClick: (Song) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_SONG   = 1
    }

    sealed class ListItem {
        data class Header(
            val title: String,
            val subtitle: String,
            val icon: String,
            val songs: List<Song>
        ) : ListItem()
        data class SongItem(val song: Song) : ListItem()
    }

    private val items = mutableListOf<ListItem>()

    fun submitGroups(groups: List<Triple<String, String, List<Song>>>) {
        items.clear()
        for ((title, sub, songs) in groups) {
            val icon = title.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
            items += ListItem.Header(title, sub, icon, songs)
            songs.forEach { items += ListItem.SongItem(it) }
        }
        notifyDataSetChanged()
    }

    fun submitSongs(songs: List<Song>) {
        items.clear()
        songs.forEach { items += ListItem.SongItem(it) }
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size
    override fun getItemViewType(pos: Int) = when (items[pos]) {
        is ListItem.Header   -> TYPE_HEADER
        is ListItem.SongItem -> TYPE_SONG
    }

    inner class HeaderVH(v: View) : RecyclerView.ViewHolder(v) {
        val tvIcon:  TextView = v.findViewById(R.id.tvGroupIcon)
        val tvName:  TextView = v.findViewById(R.id.tvGroupName)
        val tvCount: TextView = v.findViewById(R.id.tvGroupCount)

        fun bind(h: ListItem.Header) {
            tvIcon.text  = h.icon
            tvName.text  = h.title
            tvCount.text = "${h.songs.size} brani"
            itemView.setOnClickListener {
                h.songs.firstOrNull()?.let { onSongClick(it) }
            }
        }
    }

    inner class SongVH(v: View) : RecyclerView.ViewHolder(v) {
        val root:     View                = v.findViewById(R.id.cardSong)
        val ivArt:    ShapeableImageView  = v.findViewById(R.id.ivAlbumArt)
        val tvLetter: TextView            = v.findViewById(R.id.tvArtLetter)
        val tvTitle:  TextView            = v.findViewById(R.id.tvTitle)
        val tvArtist: TextView            = v.findViewById(R.id.tvArtist)
        val tvMeta:   TextView            = v.findViewById(R.id.tvMeta)
        val tvMood:   TextView            = v.findViewById(R.id.tvMood)
        val tvDur:    TextView            = v.findViewById(R.id.tvDuration)
        val moodDot:  View                = v.findViewById(R.id.moodDot)

        fun bind(song: Song) {
            // ── Album art asincrono ──
            ArtLoader.load(ivArt, tvLetter, song)

            tvTitle.text = song.title.ifBlank { "Sconosciuto" }
            tvArtist.text = buildString {
                append(song.artist.ifBlank { "Artista sconosciuto" })
                if (song.year.isNotBlank()) append("  ·  ${song.year}")
            }
            val dur = song.duration.toInt()
            tvDur.text = "%d:%02d".format(dur / 60, dur % 60)

            if (song.analyzed && song.effectiveMood.isNotBlank()) {
                val color = SongAdapter.MOOD_COLORS[song.effectiveMood] ?: 0xFF6C63FFL.toInt()
                (moodDot.background as? GradientDrawable)?.setColor(color)
                    ?: run {
                        moodDot.background = GradientDrawable().apply {
                            shape = GradientDrawable.OVAL; setColor(color) }
                    }
                moodDot.visibility = android.view.View.VISIBLE
                tvMood.text = if (song.hasManualMood) "✏ ${song.effectiveMood}" else song.effectiveMood
                tvMood.setTextColor(color)
                tvMood.visibility = android.view.View.VISIBLE
                tvMeta.text = buildString {
                    if (song.genreResolved.isNotBlank()) append(song.genreResolved)
                    if (song.tempo > 0) {
                        if (isNotEmpty()) append("  ·  ")
                        append("${song.tempo.toInt()} BPM")
                    }
                }
            } else {
                moodDot.visibility = android.view.View.GONE
                tvMood.visibility  = android.view.View.GONE
                tvMeta.text = if (!song.analyzed) "analisi in corso…" else ""
            }

            root.setOnClickListener { onSongClick(song) }
            root.setOnLongClickListener { onSongLongClick(song); true }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inf = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> HeaderVH(inf.inflate(R.layout.item_group_header, parent, false))
            else        -> SongVH(inf.inflate(R.layout.item_song, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        when (val item = items[pos]) {
            is ListItem.Header   -> (holder as HeaderVH).bind(item)
            is ListItem.SongItem -> (holder as SongVH).bind(item.song)
        }
    }
}
