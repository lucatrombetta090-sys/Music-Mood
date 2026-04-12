package com.musicmood

import android.content.ContentUris
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File

object ArtLoader {

    /**
     * Carica la copertina album in modo asincrono con fallback.
     * Prova prima MediaStore (albumId), poi il file scaricato (coverPath).
     * Mostra la lettera iniziale mentre carica, la nasconde se la copertina arriva.
     * Usato in RecyclerView adapter (SongAdapter, GroupedAdapter, MoodCardAdapter).
     */
    fun load(imageView: ImageView, letterView: TextView, song: Song) {
        // Placeholder lettera visibile subito — verrà nascosta se l'immagine carica
        letterView.text = song.title.firstOrNull()?.uppercaseChar()?.toString() ?: "♪"
        letterView.visibility = View.VISIBLE
        imageView.visibility  = View.INVISIBLE

        val ctx = imageView.context

        val albumUri: Uri? = if (song.albumId > 0L)
            ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"), song.albumId)
        else null

        val coverFile: File? = song.coverPath
            .takeIf { it.isNotBlank() }
            ?.let { File(it) }
            ?.takeIf { it.exists() }

        fun showImage() {
            imageView.post {
                imageView.visibility  = View.VISIBLE
                letterView.visibility = View.GONE
            }
        }

        fun loadFile(file: File) {
            Glide.with(ctx).load(file)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        r: Drawable, m: Any, t: Target<Drawable>, d: DataSource, f: Boolean
                    ): Boolean { showImage(); return false }
                    override fun onLoadFailed(
                        e: GlideException?, m: Any, t: Target<Drawable>, f: Boolean
                    ) = false
                })
                .into(imageView)
        }

        when {
            albumUri != null -> Glide.with(ctx).load(albumUri)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        r: Drawable, m: Any, t: Target<Drawable>, d: DataSource, f: Boolean
                    ): Boolean { showImage(); return false }
                    override fun onLoadFailed(
                        e: GlideException?, m: Any, t: Target<Drawable>, f: Boolean
                    ): Boolean {
                        if (coverFile != null) loadFile(coverFile)
                        return false
                    }
                })
                .into(imageView)
            coverFile != null -> loadFile(coverFile)
            // Nessuna sorgente → lettera rimane visibile
        }
    }

    /**
     * Variante per il Player: il placeholder è una View generica (LinearLayout)
     * con un TextView figlio per il carattere.
     * onNoArt viene chiamata quando nessuna sorgente è disponibile
     * (es. per mostrare il bottone download).
     */
    fun loadPlayer(
        imageView: ImageView,
        placeholderView: View,
        tvChar: TextView,
        song: Song,
        onNoArt: () -> Unit = {}
    ) {
        tvChar.text = song.title.firstOrNull()?.uppercaseChar()?.toString() ?: "♪"
        placeholderView.visibility = View.VISIBLE
        imageView.visibility = View.INVISIBLE

        val ctx = imageView.context

        val albumUri: Uri? = if (song.albumId > 0L)
            ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"), song.albumId)
        else null

        val coverFile: File? = song.coverPath
            .takeIf { it.isNotBlank() }
            ?.let { File(it) }
            ?.takeIf { it.exists() }

        fun showImage() {
            imageView.post {
                imageView.visibility   = View.VISIBLE
                placeholderView.visibility = View.GONE
            }
        }

        fun fail() { imageView.post { onNoArt() } }

        fun loadFile(file: File, isFinalFallback: Boolean = false) {
            Glide.with(ctx).load(file)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        r: Drawable, m: Any, t: Target<Drawable>, d: DataSource, f: Boolean
                    ): Boolean { showImage(); return false }
                    override fun onLoadFailed(
                        e: GlideException?, m: Any, t: Target<Drawable>, f: Boolean
                    ): Boolean { if (isFinalFallback) fail(); return false }
                })
                .into(imageView)
        }

        when {
            albumUri != null -> Glide.with(ctx).load(albumUri)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        r: Drawable, m: Any, t: Target<Drawable>, d: DataSource, f: Boolean
                    ): Boolean { showImage(); return false }
                    override fun onLoadFailed(
                        e: GlideException?, m: Any, t: Target<Drawable>, f: Boolean
                    ): Boolean {
                        if (coverFile != null) loadFile(coverFile, isFinalFallback = true)
                        else fail()
                        return false
                    }
                })
                .into(imageView)
            coverFile != null -> loadFile(coverFile, isFinalFallback = true)
            else -> fail()
        }
    }
}
