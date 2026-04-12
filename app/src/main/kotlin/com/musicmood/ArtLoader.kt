package com.musicmood

import android.content.ContentUris
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

/**
 * Caricamento copertine tramite Glide 4.16.
 * Il Context viene ricavato dalla View — i caller non lo passano.
 *
 * Firme CRITICHE di RequestListener<Drawable> in Glide 4.16:
 *   onLoadFailed    → model: Any?  (@Nullable)
 *   onResourceReady → model: Any   (@NonNull) ← NON nullable
 */
object ArtLoader {

    private val OPTIONS = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()

    // ── load(ivArt, tvLetter, song) ───────────────────────────────────────────
    // Usato in: SongAdapter, GroupedAdapter, MoodSectionsFragment, MainActivity

    fun load(ivArt: ImageView, tvLetter: TextView, song: Song) {
        val ctx = ivArt.context

        val uri: Any? = when {
            song.albumId > 0L -> ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"), song.albumId
            )
            song.coverPath.isNotBlank() -> song.coverPath
            else -> null
        }

        if (uri == null) { showPlaceholder(ivArt, tvLetter, song); return }

        Glide.with(ctx).load(uri).apply(OPTIONS)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any?,
                    target: Target<Drawable>, isFirstResource: Boolean
                ): Boolean {
                    if (song.albumId > 0L && song.coverPath.isNotBlank()) {
                        Glide.with(ctx).load(song.coverPath).apply(OPTIONS).into(ivArt)
                        ivArt.visibility = View.VISIBLE; tvLetter.visibility = View.GONE
                    } else showPlaceholder(ivArt, tvLetter, song)
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable, model: Any,
                    target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean
                ): Boolean {
                    ivArt.visibility = View.VISIBLE; tvLetter.visibility = View.GONE
                    return false
                }
            }).into(ivArt)
    }

    // ── loadPlayer(imageView, placeholderView, tvChar, song, onNoArt) ─────────
    // Usato in: PlayerFragment (parametri nominati)

    fun loadPlayer(
        imageView: ImageView,
        placeholderView: View,
        tvChar: TextView,
        song: Song,
        onNoArt: () -> Unit
    ) {
        val ctx = imageView.context
        val bigOptions = OPTIONS.clone().override(560, 560)

        val uri: Any? = when {
            song.albumId > 0L -> ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"), song.albumId
            )
            song.coverPath.isNotBlank() -> song.coverPath
            else -> null
        }

        if (uri == null) {
            imageView.visibility = View.INVISIBLE
            placeholderView.visibility = View.VISIBLE
            tvChar.visibility = View.VISIBLE
            onNoArt(); return
        }

        Glide.with(ctx).load(uri).apply(bigOptions)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any?,
                    target: Target<Drawable>, isFirstResource: Boolean
                ): Boolean {
                    if (song.albumId > 0L && song.coverPath.isNotBlank()) {
                        Glide.with(ctx).load(song.coverPath).apply(bigOptions)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?, model: Any?,
                                    target: Target<Drawable>, isFirstResource: Boolean
                                ): Boolean {
                                    imageView.visibility = View.INVISIBLE
                                    placeholderView.visibility = View.VISIBLE
                                    tvChar.visibility = View.VISIBLE
                                    onNoArt(); return true
                                }
                                override fun onResourceReady(
                                    resource: Drawable, model: Any,
                                    target: Target<Drawable>, dataSource: DataSource,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    imageView.visibility = View.VISIBLE
                                    placeholderView.visibility = View.GONE
                                    tvChar.visibility = View.GONE; return false
                                }
                            }).into(imageView)
                    } else {
                        imageView.visibility = View.INVISIBLE
                        placeholderView.visibility = View.VISIBLE
                        tvChar.visibility = View.VISIBLE
                        onNoArt()
                    }
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable, model: Any,
                    target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean
                ): Boolean {
                    imageView.visibility = View.VISIBLE
                    placeholderView.visibility = View.GONE
                    tvChar.visibility = View.GONE; return false
                }
            }).into(imageView)
    }

    // ── Utilità ───────────────────────────────────────────────────────────────

    private fun showPlaceholder(ivArt: ImageView, tvLetter: TextView, song: Song) {
        ivArt.visibility = View.INVISIBLE
        tvLetter.visibility = View.VISIBLE
        tvLetter.text = song.title.firstOrNull()?.uppercaseChar()?.toString() ?: "♪"
    }

    fun cancel(ivArt: ImageView) {
        try { Glide.with(ivArt.context).clear(ivArt) } catch (_: Exception) {}
    }
}
