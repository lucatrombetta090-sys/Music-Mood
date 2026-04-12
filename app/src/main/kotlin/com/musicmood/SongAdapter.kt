package com.musicmood

import android.graphics.drawable.GradientDrawable
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class SongAdapter(
    private val onClick: (Song) -> Unit,
    private val onLongClick: (Song) -> Unit
) : ListAdapter<Song, SongAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Song>() {
            override fun areItemsTheSame(a: Song, b: Song) = a.path == b.path
            override fun areContentsTheSame(a: Song, b: Song) =
                a.mood == b.mood && a.analyzed == b.analyzed &&
                a.title == b.title && a.artist == b.artist &&
                a.coverPath == b.coverPath && a.moodOverride == b.moodOverride
        }
        val MOOD_COLORS = mapOf(
            "Energico"       to (0xFFF59E0BL).toInt(),
            "Positivo"       to (0xFF1DB954L).toInt(),
            "Aggressivo"     to (0xFFEF4444L).toInt(),
            "Malinconico"    to (0xFF7C3AEDL).toInt(),
            "Romantico"      to (0xFFFF6B9DL).toInt(),
            "Rilassato"      to (0xFF38BDF8L).toInt(),
            "Nostalgico"     to (0xFFA78BFAL).toInt(),
            "Concentrazione" to (0xFF64748BL).toInt(),
            "Festivo"        to (0xFFFF9500L).toInt(),
        )
        val MOODS = listOf(
            "Energico", "Positivo", "Aggressivo", "Malinconico",
            "Romantico", "Rilassato", "Nostalgico", "Concentrazione", "Festivo"
        )
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val root: View                  = v.findViewById(R.id.cardSong)
        val ivArt: ShapeableImageView   = v.findViewById(R.id.ivAlbumArt)
        val tvLetter: TextView          = v.findViewById(R.id.tvArtLetter)
        val tvTitle: TextView           = v.findViewById(R.id.tvTitle)
        val tvArtist: TextView          = v.findViewById(R.id.tvArtist)
        val tvMeta: TextView            = v.findViewById(R.id.tvMeta)
        val tvMood: TextView            = v.findViewById(R.id.tvMood)
        val tvDuration: TextView        = v.findViewById(R.id.tvDuration)
        val moodDot: View               = v.findViewById(R.id.moodDot)

        fun bind(song: Song) {
            // ── Album art: asincrono via ArtLoader (MediaStore → coverPath → lettera) ──
            ArtLoader.load(ivArt, tvLetter, song)

            tvTitle.text = song.title.ifBlank { "Sconosciuto" }
            tvArtist.text = buildString {
                append(song.artist.ifBlank { "Artista sconosciuto" })
                if (song.year.isNotBlank()) append("  ·  ${song.year}")
            }
            val dur = song.duration.toInt()
            tvDuration.text = "%d:%02d".format(dur / 60, dur % 60)

            if (song.analyzed && song.effectiveMood.isNotBlank()) {
                val color = MOOD_COLORS[song.effectiveMood] ?: (0xFF1DB954L).toInt()

                (moodDot.background as? GradientDrawable)?.setColor(color)
                    ?: run {
                        val dot = GradientDrawable().apply {
                            shape = GradientDrawable.OVAL; setColor(color) }
                        moodDot.background = dot
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

            root.setOnClickListener { onClick(song) }
            root.setOnLongClickListener {
                showMoodCorrectionDialog(song)
                true
            }
        }

        private fun showMoodCorrectionDialog(song: Song) {
            val ctx = itemView.context
            val currentIdx = MOODS.indexOf(song.effectiveMood).coerceAtLeast(0)

            AlertDialog.Builder(ctx)
                .setTitle("Correggi il mood")
                .setMessage(song.title.take(40))
                .setSingleChoiceItems(MOODS.toTypedArray(), currentIdx) { dialog, which ->
                    val newMood = MOODS[which]
                    SongCache.saveMoodOverride(ctx, song.path, newMood)
                    song.mood = newMood
                    val pos = currentList.indexOfFirst { it.path == song.path }
                    if (pos >= 0) notifyItemChanged(pos)
                    dialog.dismiss()
                }
                .setNegativeButton("Annulla", null)
                .also { builder ->
                    if (song.effectiveMood.isNotBlank()) {
                        builder.setNeutralButton("Ripristina algoritmo") { _, _ ->
                            SongCache.clearMoodOverride(ctx, song.path)
                        }
                    }
                }
                .show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))
}
