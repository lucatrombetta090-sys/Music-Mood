package com.musicmood

import android.content.ContentUris
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

/**
 * Helper per il caricamento asincrono delle copertine tramite Glide 4.
 * Usato in alternativa al caricamento manuale via BitmapFactory quando
 * si vuole sfruttare il caching e il caricamento asincrono di Glide.
 */
object ArtLoader {

    private val defaultOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()

    /**
     * Carica la copertina da MediaStore (albumId) in una ImageView.
     * Se non disponibile, mostra il placeholder.
     */
    fun loadAlbumArt(
        context: Context,
        albumId: Long,
        into: ImageView,
        onSuccess: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    ) {
        val uri = ContentUris.withAppendedId(
            Uri.parse("content://media/external/audio/albumart"), albumId
        )
        Glide.with(context)
            .load(uri)
            .apply(defaultOptions)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    onFailure?.invoke()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any?,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    onSuccess?.invoke()
                    return false
                }
            })
            .into(into)
    }

    /**
     * Carica la copertina da un path locale (coverPath salvato in cache).
     */
    fun loadFromFile(
        context: Context,
        filePath: String,
        into: ImageView,
        onSuccess: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    ) {
        Glide.with(context)
            .load(filePath)
            .apply(defaultOptions)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    onFailure?.invoke()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any?,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    onSuccess?.invoke()
                    return false
                }
            })
            .into(into)
    }

    /**
     * Carica la copertina di un brano scegliendo automaticamente la sorgente:
     * prima MediaStore (albumId), poi coverPath locale.
     * Chiama onFailure se nessuna sorgente è disponibile.
     */
    fun loadSongArt(
        context: Context,
        song: Song,
        into: ImageView,
        onSuccess: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    ) {
        if (song.albumId > 0L) {
            val uri = ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"), song.albumId
            )
            Glide.with(context)
                .load(uri)
                .apply(defaultOptions)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        // Fallback su coverPath se MediaStore fallisce
                        if (song.coverPath.isNotBlank()) {
                            loadFromFile(context, song.coverPath, into, onSuccess, onFailure)
                        } else {
                            onFailure?.invoke()
                        }
                        return true // intercettiamo per gestire il fallback
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any?,
                        target: Target<Drawable>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        onSuccess?.invoke()
                        return false
                    }
                })
                .into(into)
        } else if (song.coverPath.isNotBlank()) {
            loadFromFile(context, song.coverPath, into, onSuccess, onFailure)
        } else {
            onFailure?.invoke()
        }
    }

    /**
     * Annulla eventuali richieste pendenti su una ImageView.
     * Da chiamare in onRecycled() dell'adapter.
     */
    fun cancel(context: Context, into: ImageView) {
        Glide.with(context).clear(into)
    }
}
