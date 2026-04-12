package com.musicmood

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.PowerManager
import android.provider.MediaStore
import androidx.lifecycle.*
import com.chaquo.python.Python
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

enum class ScanState { IDLE, SCANNING, ANALYZING, DONE, ERROR }

data class Filters(
    val mood: String = "Tutti",
    val genre: String = "Tutti",
    val year: String = "Tutti",
    val searchQuery: String = ""
)

class SongViewModel : ViewModel() {

    // ── Songs & UI state ──────────────────────────────────────────────────────

    private val _songs        = MutableLiveData<List<Song>>(emptyList())
    private val _scanState    = MutableLiveData(ScanState.IDLE)
    private val _scanProgress = MutableLiveData(0 to 0)
    private val _scanError    = MutableLiveData("")
    private val _filters      = MutableLiveData(Filters())
    private val _currentSong  = MutableLiveData<Song?>(null)
    private val _isPlaying    = MutableLiveData(false)
    private val _scanFolder   = MutableLiveData<String?>(null)

    val songs:        LiveData<List<Song>>    = _songs
    val scanState:    LiveData<ScanState>     = _scanState
    val scanProgress: LiveData<Pair<Int,Int>> = _scanProgress
    val scanError:    LiveData<String>        = _scanError
    val filters:      LiveData<Filters>       = _filters
    val currentSong:  LiveData<Song?>         = _currentSong
    val isPlaying:    LiveData<Boolean>       = _isPlaying
    val scanFolder:   LiveData<String?>       = _scanFolder

    var playlist: List<Song> = emptyList()
    var playlistIndex: Int = 0

    private var analysisJob: Job? = null
    private var wakeLock: PowerManager.WakeLock? = null

    // ── MediaPlayer (unico, vive nel ViewModel — sopravvive alle rotazioni) ───
    //
    //  FIX 2: il player non è più nel Fragment ma qui, così non ci sono
    //  mai due istanze in parallelo anche in caso di rotazione o cambio tema.

    private var player: MediaPlayer? = null
    private var isPreparing = false
    private var currentlyPlayingPath: String? = null

    var isLoop    = false
    var isShuffle = false

    /** Duration in ms, emessa quando il player è pronto */
    private val _playerDuration = MutableLiveData(0)
    val playerDuration: LiveData<Int> = _playerDuration

    // ── Accessor stato player ─────────────────────────────────────────────────

    fun getCurrentlyPlayingPath() = currentlyPlayingPath
    fun isPlayerPreparing()        = isPreparing
    fun isPlayerPlaying()          = try { player?.isPlaying == true } catch (_: Exception) { false }
    fun getPlayerPosition()        = try { player?.currentPosition ?: 0 } catch (_: Exception) { 0 }
    fun getPlayerDuration()        = try { player?.duration ?: 0 } catch (_: Exception) { 0 }
    fun getProgressPercent(): Int {
        val dur = getPlayerDuration()
        return if (dur <= 0) 0 else (getPlayerPosition() * 100 / dur)
    }

    // ── Playback ──────────────────────────────────────────────────────────────

    /**
     * Avvia la riproduzione di [song].
     * Guard: stesso brano già in play/preparing → non riavvia.
     * [volValue] in 0–1.
     */
    fun playSong(song: Song, volValue: Float = 0.8f) {
        if (song.path == currentlyPlayingPath && (player != null || isPreparing)) return
        currentlyPlayingPath = song.path
        stopPlayer(keepPath = true)
        isPreparing = true
        _isPlaying.postValue(false)

        try {
            player = MediaPlayer().apply {
                setDataSource(song.path)
                setVolume(volValue, volValue)
                isLooping = isLoop
                setOnPreparedListener { mp ->
                    isPreparing = false
                    _playerDuration.postValue(mp.duration)
                    mp.start()
                    _isPlaying.postValue(true)
                }
                setOnErrorListener { _, _, _ ->
                    isPreparing = false
                    _isPlaying.postValue(false)
                    true
                }
                setOnCompletionListener {
                    _isPlaying.postValue(false)
                    if (!isLoop) { if (isShuffle) playRandomSong() else playNextSong() }
                }
                prepareAsync()
            }
        } catch (_: Exception) {
            isPreparing = false
        }
    }

    fun togglePlay() {
        if (isPreparing) return
        val p = player ?: run {
            _currentSong.value?.let { playSong(it) }
            return
        }
        try {
            if (p.isPlaying) { p.pause(); _isPlaying.postValue(false) }
            else             { p.start(); _isPlaying.postValue(true)  }
        } catch (_: Exception) {}
    }

    fun seekTo(ms: Int)          { try { player?.seekTo(ms)          } catch (_: Exception) {} }
    fun setPlayerVolume(v: Float) { try { player?.setVolume(v, v)    } catch (_: Exception) {} }

    fun stopPlayer(keepPath: Boolean = false) {
        if (!keepPath) currentlyPlayingPath = null
        isPreparing = false
        try { player?.reset()   } catch (_: Exception) {}
        try { player?.release() } catch (_: Exception) {}
        player = null
        _isPlaying.postValue(false)
        _playerDuration.postValue(0)
    }

    fun playNextSong() {
        val pl = playlist; if (pl.isEmpty()) return
        playlistIndex = (playlistIndex + 1) % pl.size
        val next = pl[playlistIndex]
        setCurrentSong(next); playSong(next)
    }

    fun playPrevSong() {
        val pl = playlist; if (pl.isEmpty()) return
        playlistIndex = (playlistIndex - 1 + pl.size) % pl.size
        val prev = pl[playlistIndex]
        setCurrentSong(prev); playSong(prev)
    }

    fun playRandomSong() {
        val pl = playlist; if (pl.isEmpty()) return
        playlistIndex = (0 until pl.size).random()
        val rand = pl[playlistIndex]
        setCurrentSong(rand); playSong(rand)
    }

    // ── Songs CRUD ────────────────────────────────────────────────────────────

    fun setSongs(list: List<Song>)          { _songs.postValue(list) }
    fun setScanState(s: ScanState)          { _scanState.postValue(s) }
    fun setScanProgress(cur: Int, tot: Int) { _scanProgress.postValue(cur to tot) }
    fun setScanError(msg: String)           { _scanError.postValue(msg) }
    fun setCurrentSong(s: Song?)            { _currentSong.postValue(s) }
    fun setIsPlaying(v: Boolean)            { _isPlaying.postValue(v) }
    fun setScanFolder(path: String?)        { _scanFolder.postValue(path) }

    fun setFilter(mood: String? = null, genre: String? = null,
                  year: String? = null, search: String? = null) {
        val cur = _filters.value ?: Filters()
        _filters.postValue(cur.copy(
            mood        = mood   ?: cur.mood,
            genre       = genre  ?: cur.genre,
            year        = year   ?: cur.year,
            searchQuery = search ?: cur.searchQuery
        ))
    }

    fun updateSong(updated: Song) {
        val list = _songs.value?.toMutableList() ?: return
        val idx  = list.indexOfFirst { it.path == updated.path }
        if (idx >= 0) { list[idx] = updated; _songs.postValue(list) }
    }

    fun setMoodOverride(context: Context, song: Song, newMood: String) {
        song.moodOverride = newMood; updateSong(song)
        viewModelScope.launch(Dispatchers.IO) { SongCache.save(context, _songs.value ?: return@launch) }
    }

    fun clearMoodOverride(context: Context, song: Song) {
        song.moodOverride = ""; updateSong(song)
        viewModelScope.launch(Dispatchers.IO) { SongCache.save(context, _songs.value ?: return@launch) }
    }

    fun getAnalyzedSongs() = _songs.value?.filter { it.analyzed } ?: emptyList()

    fun getFilteredSongs(): List<Song> {
        val f = _filters.value ?: Filters()
        return _songs.value?.filter { s ->
            val moodOk   = f.mood  == "Tutti" || s.effectiveMood == f.mood
            val genreOk  = f.genre == "Tutti" ||
                (s.genreResolved.isNotBlank() && s.genreResolved == f.genre)
            val yearOk   = f.year  == "Tutti" || s.year == f.year
            val searchOk = f.searchQuery.isBlank() ||
                s.title.contains(f.searchQuery, ignoreCase = true) ||
                s.artist.contains(f.searchQuery, ignoreCase = true)
            moodOk && genreOk && yearOk && searchOk
        } ?: emptyList()
    }

    fun availableGenres() = listOf("Tutti") + (_songs.value
        ?.filter { it.analyzed && it.genreResolved.isNotBlank() }
        ?.map { it.genreResolved }?.distinct()?.sorted() ?: emptyList())

    // FIX 3: accetta anni provenienti sia da MediaStore sia dai tag ID3
    fun availableYears() = listOf("Tutti") + (_songs.value
        ?.filter { it.year.length == 4 && it.year.all { c -> c.isDigit() } }
        ?.map { it.year }?.distinct()?.sortedDescending() ?: emptyList())

    // ── Cache ─────────────────────────────────────────────────────────────────

    fun loadCache(context: Context) {
        if (_songs.value?.isNotEmpty() == true) return
        viewModelScope.launch(Dispatchers.IO) {
            val cached = SongCache.load(context)
            val folder = SongCache.loadScanFolder(context)
            if (cached.isNotEmpty()) {
                _songs.postValue(cached)
                _scanFolder.postValue(folder)
                val list = cached.toMutableList()
                var any = false
                list.forEach { s ->
                    val ov = SongCache.getMoodOverride(context, s.path)
                    if (ov != null) { s.mood = ov; any = true }
                }
                if (any) _songs.postValue(list)
                _scanState.postValue(ScanState.DONE)
            }
        }
    }

    fun applyMoodOverrides(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val songs = _songs.value?.toMutableList() ?: return@launch
            var changed = false
            songs.forEach { s ->
                val ov = SongCache.getMoodOverride(context, s.path)
                if (ov != null && ov != s.mood) { s.mood = ov; changed = true }
            }
            if (changed) _songs.postValue(songs)
        }
    }

    // ── Album art helpers ─────────────────────────────────────────────────────

    fun getAlbumArt(context: Context, albumId: Long): Bitmap? {
        if (albumId <= 0L) return null
        return try {
            val uri = ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"), albumId)
            context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) }
        } catch (_: Exception) { null }
    }

    /**
     * FIX 1: scarica automaticamente la copertina tramite iTunes Search API
     * se non presente né in MediaStore né già in cache locale.
     * Salva il file .jpg in filesDir/covers/ e aggiorna song.coverPath.
     */
    fun fetchCoverArtIfMissing(song: Song, context: Context) {
        if (song.coverPath.isNotBlank() && File(song.coverPath).exists()) return
        if (song.albumId > 0L) return   // copertina locale già presente in MediaStore
        if (song.title.isBlank() && song.artist.isBlank()) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val query   = "${song.artist.trim()} ${song.title.trim()}".trim()
                val encoded = URLEncoder.encode(query, "UTF-8")

                val searchConn = (URL(
                    "https://itunes.apple.com/search?term=$encoded&media=music&limit=1&entity=song"
                ).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 7000
                    readTimeout    = 7000
                    setRequestProperty("User-Agent", "MusicMood/1.0")
                }
                val responseText = try {
                    searchConn.inputStream.bufferedReader().readText()
                } finally { searchConn.disconnect() }

                val results = JSONObject(responseText).getJSONArray("results")
                if (results.length() == 0) return@launch

                val artworkUrl = results.getJSONObject(0)
                    .optString("artworkUrl100", "")
                    .replace("100x100bb", "300x300bb")
                if (artworkUrl.isBlank()) return@launch

                val coverDir  = File(context.filesDir, "covers").also { it.mkdirs() }
                val coverFile = File(coverDir, "${song.path.hashCode()}.jpg")

                val imgConn = (URL(artworkUrl).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 7000; readTimeout = 7000
                }
                try {
                    imgConn.inputStream.use { i ->
                        coverFile.outputStream().use { o -> i.copyTo(o) }
                    }
                } finally { imgConn.disconnect() }

                if (coverFile.length() > 1024) {
                    // IMPORTANTE: usare .copy() per creare un nuovo oggetto,
                    // altrimenti DiffUtil confronta lo stesso riferimento e non aggiorna la UI
                    val updatedSong = song.copy(coverPath = coverFile.absolutePath)
                    // Aggiorna anche il song originale per coerenza
                    song.coverPath = coverFile.absolutePath
                    updateSong(updatedSong)
                    // Notifica il player se è il brano corrente
                    if (_currentSong.value?.path == song.path) _currentSong.postValue(updatedSong)
                    val snapshot = _songs.value ?: return@launch
                    SongCache.save(context, snapshot)
                }
            } catch (_: Exception) { /* Fallisce silenziosamente, nessun crash */ }
        }
    }

    // ── Analisi ───────────────────────────────────────────────────────────────

    fun startAnalysis(context: Context, folderFilter: String?) {
        if (_scanState.value == ScanState.SCANNING ||
            _scanState.value == ScanState.ANALYZING) return

        analysisJob?.cancel()
        _scanState.postValue(ScanState.SCANNING)
        _scanError.postValue("")

        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK, "MusicMood::Analysis"
        ).apply { acquire(60 * 60 * 1000L) }

        analysisJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                // ── 1. MediaStore ──────────────────────────────────────────────
                val songs = mutableListOf<Song>()
                val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                val projection = arrayOf(
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.YEAR,
                    MediaStore.Audio.Media.MIME_TYPE,
                )
                var sel = "${MediaStore.Audio.Media.IS_MUSIC} != 0 " +
                        "AND ${MediaStore.Audio.Media.DURATION} > 30000"
                if (!folderFilter.isNullOrBlank())
                    sel += " AND ${MediaStore.Audio.Media.DATA} LIKE '${folderFilter.replace("'","''")}%'"

                context.contentResolver.query(
                    uri, projection, sel, null, "${MediaStore.Audio.Media.TITLE} ASC"
                )?.use { c ->
                    val pathCol    = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                    val titleCol   = c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                    val artistCol  = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                    val albumCol   = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                    val albumIdCol = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                    val durCol     = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                    val yearCol    = c.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)
                    val mimeCol    = c.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)

                    while (c.moveToNext()) {
                        val mime = c.getString(mimeCol) ?: ""
                        if (!mime.startsWith("audio/")) continue
                        val path = c.getString(pathCol) ?: continue
                        // FIX 3: range anno 1900-2100; se 0 → verrà recuperato dai tag
                        val yr = c.getInt(yearCol).let { if (it in 1900..2100) it.toString() else "" }
                        songs.add(Song(
                            path     = path,
                            title    = c.getString(titleCol)?.ifBlank { null }
                                        ?: path.substringAfterLast("/").substringBeforeLast("."),
                            artist   = c.getString(artistCol)?.let {
                                if (it == "<unknown>") "" else it } ?: "",
                            album    = c.getString(albumCol) ?: "",
                            genre    = "",
                            year     = yr,
                            duration = c.getLong(durCol) / 1000f,
                            albumId  = c.getLong(albumIdCol),
                        ))
                    }
                }

                if (songs.isEmpty()) {
                    _scanState.postValue(ScanState.ERROR)
                    _scanError.postValue(
                        "Nessun file audio trovato. Controlla i permessi e il percorso selezionato.")
                    releaseWakeLock(); return@launch
                }

                _songs.postValue(songs)
                _scanState.postValue(ScanState.ANALYZING)
                setScanProgress(0, songs.size)

                // ── 2. DSP parallela (4 slot) ──────────────────────────────────
                val py  = Python.getInstance()
                val mod = py.getModule("music_analyzer")
                val sem = Semaphore(4)
                var done = 0

                coroutineScope {
                    songs.map { song ->
                        async(Dispatchers.IO) {
                            sem.withPermit {
                                try {
                                    val tag      = JSONObject(mod.callAttr("read_tags", song.path).toString())
                                    val genreTag = tag.optString("genre", "")

                                    // FIX 3: recupera anno dal tag ID3 se MediaStore non lo aveva
                                    if (song.year.isBlank()) {
                                        val yt = tag.optString("year", "").take(4).filter { it.isDigit() }
                                        if (yt.length == 4) song.year = yt
                                    }

                                    val decoded = AudioDecoder.decode(song.path)
                                    if (decoded != null) {
                                        val (pcm, sr) = decoded
                                        val res = JSONObject(mod.callAttr("analyze_pcm", pcm, sr).toString())
                                        song.tempo         = res.optDouble("tempo", 120.0).toFloat()
                                        song.energy        = res.optDouble("energy", 0.0).toFloat()
                                        song.mood          = res.optString("mood", "Positivo")
                                        song.genreResolved = genreTag.ifBlank {
                                            res.optString("genre_hint", "Pop") }
                                    } else {
                                        song.mood          = "Positivo"
                                        song.genreResolved = genreTag.ifBlank { "Pop" }
                                    }
                                    song.analyzed = true
                                } catch (_: Exception) {
                                    song.analyzed      = true
                                    song.mood          = "Positivo"
                                    song.genreResolved = "Pop"
                                }
                                withContext(Dispatchers.Main) {
                                    updateSong(song)
                                    done++
                                    setScanProgress(done, songs.size)
                                    if (done % 50 == 0) {
                                        SongCache.save(context, _songs.value ?: emptyList())
                                    }
                                }
                            }
                        }
                    }.awaitAll()
                }

                SongCache.save(context, _songs.value ?: emptyList())
                SongCache.saveScanFolder(context, folderFilter)

                val finalList = _songs.value?.toMutableList() ?: mutableListOf()
                var anyOverride = false
                finalList.forEach { s ->
                    val ov = SongCache.getMoodOverride(context, s.path)
                    if (ov != null) { s.mood = ov; anyOverride = true }
                }
                if (anyOverride) _songs.postValue(finalList)

                _scanState.postValue(ScanState.DONE)

            } catch (e: CancellationException) {
                _scanState.postValue(ScanState.IDLE)
            } catch (e: Exception) {
                _scanState.postValue(ScanState.ERROR)
                _scanError.postValue("Errore: ${e.message?.take(120)}")
            } finally {
                releaseWakeLock()
            }
        }
    }

    fun cancelAnalysis() {
        analysisJob?.cancel()
        releaseWakeLock()
    }

    private fun releaseWakeLock() {
        try { wakeLock?.takeIf { it.isHeld }?.release() } catch (_: Exception) {}
        wakeLock = null
    }

    override fun onCleared() {
        super.onCleared()
        cancelAnalysis()
        stopPlayer()
    }
}
