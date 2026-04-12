package com.musicmood

import android.content.ContentUris
import android.content.Context
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
 *
 * FIRMA CRITICA in Glide 4.16 — RequestListener<Drawable>:
 *   onLoadFailed    → model: Any?   (@Nullable Object in Java)
 *   onResourceReady → model: Any    (@NonNull Object in Java)  ← NON nullable
 */
object ArtLoader {

    private val options = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()

    // ── Caricamento standard (adapter lista) ──────────────────────────────────

    fun load(
        context: Context,
        song: Song,
        ivArt: ImageView,
        tvLetter: TextView
    ) {
        val uri: Any? = when {
            song.albumId > 0L -> ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"), song.albumId
            )
            song.coverPath.isNotBlank() -> song.coverPath
            else -> null
        }

        if (uri == null) {
            showPlaceholder(ivArt, tvLetter, song)
            return
        }

        Glide.with(context)
            .load(uri)
            .apply(options)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    if (song.albumId > 0L && song.coverPath.isNotBlank()) {
                        Glide.with(context).load(song.coverPath).apply(options).into(ivArt)
                        ivArt.visibility    = View.VISIBLE
                        tvLetter.visibility = View.GONE
                    } else {
                        showPlaceholder(ivArt, tvLetter, song)
                    }
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    ivArt.visibility    = View.VISIBLE
                    tvLetter.visibility = View.GONE
                    return false
                }
            })
            .into(ivArt)
    }

    // ── Caricamento player (grande, con callback per placeholder) ─────────────

    fun loadPlayer(
        context: Context,
        song: Song,
        ivArtwork: ImageView,
        letterLayout: View,
        onMissingCover: () -> Unit
    ) {
        val bigOptions = options.clone().override(560, 560)

        val uri: Any? = when {
            song.albumId > 0L -> ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"), song.albumId
            )
            song.coverPath.isNotBlank() -> song.coverPath
            else -> null
        }

        if (uri == null) {
            ivArtwork.visibility    = View.INVISIBLE
            letterLayout.visibility = View.VISIBLE
            onMissingCover()
            return
        }

        Glide.with(context)
            .load(uri)
            .apply(bigOptions)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    if (song.albumId > 0L && song.coverPath.isNotBlank()) {
                        Glide.with(context)
                            .load(song.coverPath)
                            .apply(bigOptions)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    ivArtwork.visibility    = View.INVISIBLE
                                    letterLayout.visibility = View.VISIBLE
                                    onMissingCover()
                                    return true
                                }

                                override fun onResourceReady(
                                    resource: Drawable,
                                    model: Any,
                                    target: Target<Drawable>,
                                    dataSource: DataSource,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    ivArtwork.visibility    = View.VISIBLE
                                    letterLayout.visibility = View.GONE
                                    return false
                                }
                            })
                            .into(ivArtwork)
                    } else {
                        ivArtwork.visibility    = View.INVISIBLE
                        letterLayout.visibility = View.VISIBLE
                        onMissingCover()
                    }
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    ivArtwork.visibility    = View.VISIBLE
                    letterLayout.visibility = View.GONE
                    return false
                }
            })
            .into(ivArtwork)
    }

    // ── Utilità ───────────────────────────────────────────────────────────────

    private fun showPlaceholder(ivArt: ImageView, tvLetter: TextView, song: Song) {
        ivArt.visibility    = View.INVISIBLE
        tvLetter.visibility = View.VISIBLE
        tvLetter.text = song.title.firstOrNull()?.uppercaseChar()?.toString() ?: "♪"
    }

    fun cancel(context: Context, ivArt: ImageView) {
        try { Glide.with(context).clear(ivArt) } catch (_: Exception) {}
    }
}
