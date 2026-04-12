package com.musicmood

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class PlayerFragment : Fragment() {

    private val vm: SongViewModel by activityViewModels()

    private lateinit var tvTitle: TextView
    private lateinit var tvArtist: TextView
    private lateinit var tvMood: TextView
    private lateinit var tvGenre: TextView
    private lateinit var tvTempo: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvDuration: TextView
    private lateinit var slider: Slider
    private lateinit var btnPlay: MaterialButton
    private lateinit var btnPrev: MaterialButton
    private lateinit var btnNext: MaterialButton
    private lateinit var btnLoop: MaterialButton
    private lateinit var btnShuffle: MaterialButton
    private lateinit var btnDownloadCover: MaterialButton
    private lateinit var volSlider: Slider
    private lateinit var tvNoSong: TextView
    private lateinit var playerContent: View
    private lateinit var ivArtwork: ImageView
    private lateinit var artworkLetterLayout: View
    private lateinit var tvArtworkLetterChar: TextView
    private lateinit var artworkBg: View

    private val handler = Handler(Looper.getMainLooper())
    private var isSeeking = false
    private var currentSong: Song? = null

    private val updateProgress = object : Runnable {
        override fun run() {
            if (!isSeeking) {
                try {
                    val pos = vm.getPlayerPosition()
                    val valueTo = slider.valueTo
                    if (valueTo > 1f) {
                        slider.value = pos.toFloat().coerceIn(0f, valueTo)
                        tvTime.text = formatMs(pos)
                    }
                } catch (_: Exception) {}
            }
            handler.postDelayed(this, 500)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_player, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tvTitle            = view.findViewById(R.id.tvPlayerTitle)
        tvArtist           = view.findViewById(R.id.tvPlayerArtist)
        tvMood             = view.findViewById(R.id.tvPlayerMood)
        tvGenre            = view.findViewById(R.id.tvPlayerGenre)
        tvTempo            = view.findViewById(R.id.tvPlayerTempo)
        tvTime             = view.findViewById(R.id.tvTime)
        tvDuration         = view.findViewById(R.id.tvDuration)
        slider             = view.findViewById(R.id.seekSlider)
        btnPlay            = view.findViewById(R.id.btnPlay)
        btnPrev            = view.findViewById(R.id.btnPrev)
        btnNext            = view.findViewById(R.id.btnNext)
        btnLoop            = view.findViewById(R.id.btnLoop)
        btnShuffle         = view.findViewById(R.id.btnShuffle)
        btnDownloadCover   = view.findViewById(R.id.btnDownloadCover)
        volSlider          = view.findViewById(R.id.volSlider)
        tvNoSong           = view.findViewById(R.id.tvNoSong)
        playerContent      = view.findViewById(R.id.playerContent)
        ivArtwork          = view.findViewById(R.id.ivArtwork)
        artworkLetterLayout = view.findViewById(R.id.tvArtworkLetter)
        tvArtworkLetterChar = view.findViewById(R.id.tvArtworkLetterChar)
        artworkBg          = view.findViewById(R.id.artworkBg)

        btnPlay.setOnClickListener { vm.togglePlay() }
        btnPrev.setOnClickListener { vm.playPrevSong() }
        btnNext.setOnClickListener { vm.playNextSong() }

        btnLoop.setOnClickListener {
            vm.isLoop = !vm.isLoop
            btnLoop.alpha = if (vm.isLoop) 1f else 0.35f
        }
        btnShuffle.setOnClickListener {
            vm.isShuffle = !vm.isShuffle
            btnShuffle.alpha = if (vm.isShuffle) 1f else 0.35f
        }

        btnDownloadCover.setOnClickListener {
            val song = currentSong ?: return@setOnClickListener
            downloadCoverToGallery(song)
        }

        slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(s: Slider) { isSeeking = true }
            override fun onStopTrackingTouch(s: Slider) {
                vm.seekTo(s.value.toInt())
                tvTime.text = formatMs(s.value.toInt())
                isSeeking = false
            }
        })

        volSlider.value = 80f
        volSlider.addOnChangeListener { _, value, _ ->
            vm.setPlayerVolume(value / 100f)
        }

        vm.playerDuration.observe(viewLifecycleOwner) { dur ->
            if (dur > 0) {
                slider.valueTo = dur.toFloat()
                slider.value = 0f
                tvDuration.text = formatMs(dur)
                tvTime.text = "0:00"
            } else {
                try { slider.valueTo = 1f; slider.value = 0f } catch (_: Exception) {}
                tvDuration.text = "0:00"
                tvTime.text = "0:00"
            }
        }

        vm.currentSong.observe(viewLifecycleOwner) { song ->
            currentSong = song
            if (song != null) {
                playerContent.visibility = View.VISIBLE
                tvNoSong.visibility = View.GONE
                updateUI(song)
                if (song.path != vm.getCurrentlyPlayingPath()) {
                    vm.playSong(song, volSlider.value / 100f)
                }
            } else {
                playerContent.visibility = View.GONE
                tvNoSong.visibility = View.VISIBLE
                vm.stopPlayer()
            }
        }

        vm.isPlaying.observe(viewLifecycleOwner) { playing ->
            btnPlay.text = if (playing) "⏸" else "▶"
            if (playing) handler.post(updateProgress)
        }
    }

    private fun updateUI(song: Song) {
        tvTitle.text = song.title.ifBlank { "Sconosciuto" }
        tvArtist.text = buildString {
            append(song.artist.ifBlank { "Artista sconosciuto" })
            if (song.year.isNotBlank()) append("  ·  ${song.year}")
        }

        // ── Copertina ──────────────────────────────────────────────────────────
        val bmpLocal = if (song.albumId > 0L) {
            try {
                val artUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"), song.albumId)
                requireContext().contentResolver.openInputStream(artUri)?.use {
                    BitmapFactory.decodeStream(it)
                }
            } catch (_: Exception) { null }
        } else null

        val bmpCached = if (bmpLocal == null && song.coverPath.isNotBlank()) {
            try { BitmapFactory.decodeFile(song.coverPath) } catch (_: Exception) { null }
        } else null

        val bmp = bmpLocal ?: bmpCached

        if (bmp != null) {
            ivArtwork.setImageBitmap(bmp)
            ivArtwork.visibility = View.VISIBLE
            artworkLetterLayout.visibility = View.GONE
            btnDownloadCover.visibility = View.GONE
        } else {
            ivArtwork.visibility = View.INVISIBLE
            artworkLetterLayout.visibility = View.VISIBLE
            tvArtworkLetterChar.text =
                song.title.firstOrNull()?.uppercaseChar()?.toString() ?: "♪"
            btnDownloadCover.visibility = View.VISIBLE
            vm.fetchCoverArtIfMissing(song, requireContext())
        }

        // ── Mood / genere / BPM ──────────────────────────────────────────────
        if (song.analyzed && song.effectiveMood.isNotBlank()) {
            tvMood.text = if (song.hasManualMood) "✏ ${song.effectiveMood}" else song.effectiveMood
            tvGenre.text = song.genreResolved.ifBlank { "—" }
            tvTempo.text = "${song.tempo.toInt()} BPM"
            tvMood.visibility = View.VISIBLE
        } else {
            tvMood.visibility = View.GONE
            tvGenre.text = "—"
            tvTempo.text = "—"
        }
    }

    // ── Download copertina nella galleria ─────────────────────────────────────

    private fun downloadCoverToGallery(song: Song) {
        val ctx = requireContext()
        btnDownloadCover.isEnabled = false
        btnDownloadCover.text = "⏳"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val cachedBmp: Bitmap? = if (song.coverPath.isNotBlank()) {
                    try { BitmapFactory.decodeFile(song.coverPath) } catch (_: Exception) { null }
                } else null

                val bmp: Bitmap? = cachedBmp ?: run {
                    val query = "${song.artist.trim()} ${song.title.trim()}".trim()
                    val encoded = java.net.URLEncoder.encode(query, "UTF-8")
                    val conn = (java.net.URL(
                        "https://itunes.apple.com/search?term=$encoded&media=music&limit=1&entity=song"
                    ).openConnection() as java.net.HttpURLConnection).apply {
                        connectTimeout = 8000; readTimeout = 8000
                        setRequestProperty("User-Agent", "MusicMood/1.0")
                    }
                    val json = try { conn.inputStream.bufferedReader().readText() }
                              finally { conn.disconnect() }
                    val results = org.json.JSONObject(json).getJSONArray("results")
                    if (results.length() == 0) return@run null
                    val url = results.getJSONObject(0)
                        .optString("artworkUrl100", "")
                        .replace("100x100bb", "600x600bb")
                    if (url.isBlank()) return@run null
                    val imgConn = (java.net.URL(url).openConnection() as java.net.HttpURLConnection).apply {
                        connectTimeout = 8000; readTimeout = 8000
                    }
                    try { BitmapFactory.decodeStream(imgConn.inputStream) }
                    finally { imgConn.disconnect() }
                }

                if (bmp == null) {
                    withContext(Dispatchers.Main) {
                        showSnack("❌ Copertina non trovata")
                        resetDownloadBtn()
                    }
                    return@launch
                }

                val filename = buildString {
                    val safe = song.artist.replace(Regex("[^A-Za-z0-9_\\- ]"), "").trim()
                    val safeT = song.title.replace(Regex("[^A-Za-z0-9_\\- ]"), "").trim()
                    if (safe.isNotBlank()) { append(safe); append(" - ") }
                    append(safeT.ifBlank { "copertina" })
                    append(".jpg")
                }

                val resolver = ctx.contentResolver
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH,
                        "${Environment.DIRECTORY_PICTURES}/MusicMood")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
                val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { out ->
                        bmp.compress(Bitmap.CompressFormat.JPEG, 92, out)
                    }
                    values.clear()
                    values.put(MediaStore.Images.Media.IS_PENDING, 0)
                    resolver.update(uri, values, null, null)
                    withContext(Dispatchers.Main) {
                        showSnack("✅ Copertina salvata in Galleria")
                        btnDownloadCover.visibility = View.GONE
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showSnack("❌ Impossibile salvare")
                        resetDownloadBtn()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showSnack("❌ Errore: ${e.message?.take(50)}")
                    resetDownloadBtn()
                }
            }
        }
    }

    private fun showSnack(msg: String) {
        view?.let { Snackbar.make(it, msg, Snackbar.LENGTH_SHORT).show() }
    }

    private fun resetDownloadBtn() {
        btnDownloadCover.text = "⬇"
        btnDownloadCover.isEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(updateProgress)
    }

    fun getProgressPercent(): Int = vm.getProgressPercent()
    fun togglePlayFromMini() { vm.togglePlay() }
    fun nextFromMini() { vm.playNextSong() }

    private fun formatMs(ms: Int): String {
        val s = ms / 1000; return "%d:%02d".format(s / 60, s % 60)
    }
}
