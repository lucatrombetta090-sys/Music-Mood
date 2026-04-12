package com.musicmood

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class LibraryFragment : Fragment() {

    private val vm: SongViewModel by activityViewModels()

    // Views
    private lateinit var rv: RecyclerView
    private lateinit var btnScan: MaterialButton
    private lateinit var btnFilter: MaterialButton
    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var tvStatus: TextView
    private lateinit var tvCount: TextView
    private lateinit var tvEmpty: TextView
    private lateinit var etSearch: TextInputEditText
    private lateinit var chipMoodRow: ChipGroup
    private lateinit var moodChipScroll: View
    private lateinit var tabIndicator: View

    // Tabs
    private lateinit var tabBrani: TextView
    private lateinit var tabCartelle: TextView
    private lateinit var tabArtisti: TextView
    private lateinit var tabAnno: TextView

    // Adapters
    private lateinit var songAdapter: SongAdapter
    private lateinit var groupedAdapter: GroupedAdapter

    enum class Tab { BRANI, CARTELLE, ARTISTI, ANNO }
    private var currentTab = Tab.BRANI

    private val permLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        if (grants.values.any { it }) showFolderDialog()
        else Snackbar.make(requireView(),
            "Permesso negato. Vai in Impostazioni → App → Autorizzazioni.",
            Snackbar.LENGTH_LONG).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_library, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv            = view.findViewById(R.id.rvSongs)
        btnScan       = view.findViewById(R.id.btnScan)
        btnFilter     = view.findViewById(R.id.btnFilter)
        progressBar   = view.findViewById(R.id.progressScan)
        tvStatus      = view.findViewById(R.id.tvStatus)
        tvCount       = view.findViewById(R.id.tvCount)
        tvEmpty       = view.findViewById(R.id.tvEmpty)
        etSearch      = view.findViewById(R.id.etSearch)
        chipMoodRow   = view.findViewById(R.id.chipMoodRow)
        moodChipScroll = view.findViewById(R.id.moodChipScroll)
        tabIndicator  = view.findViewById(R.id.tabIndicator)
        tabBrani      = view.findViewById(R.id.tabBrani)
        tabCartelle   = view.findViewById(R.id.tabCartelle)
        tabArtisti    = view.findViewById(R.id.tabArtisti)
        tabAnno       = view.findViewById(R.id.tabAnno)

        // ── Song adapter (tab Brani) ──────────────────────────────────────────
        songAdapter = SongAdapter(
            onClick = { song ->
                vm.playlist = vm.getFilteredSongs()
                vm.playlistIndex = vm.playlist.indexOfFirst { it.path == song.path }.coerceAtLeast(0)
                vm.setCurrentSong(song)
                (activity as? MainActivity)?.goToPlayer()
            },
            onLongClick = { song -> showMoodPicker(song) }
        )

        // ── Grouped adapter (Cartelle / Artisti / Anno) ───────────────────────
        groupedAdapter = GroupedAdapter(
            onSongClick = { song ->
                // Costruisci la playlist dal gruppo corrente
                vm.playlist = getCurrentGroupSongs()
                vm.playlistIndex = vm.playlist.indexOfFirst { it.path == song.path }.coerceAtLeast(0)
                vm.setCurrentSong(song)
                (activity as? MainActivity)?.goToPlayer()
            },
            onSongLongClick = { song -> showMoodPicker(song) }
        )

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator().apply {
            addDuration = 150; removeDuration = 100; changeDuration = 120
        }

        // ── Mood chips ────────────────────────────────────────────────────────
        listOf("Tutti", "Energico", "Positivo", "Aggressivo", "Malinconico",
            "Romantico", "Rilassato", "Nostalgico", "Concentrazione", "Festivo"
        ).forEachIndexed { i, mood ->
            chipMoodRow.addView(Chip(requireContext()).apply {
                text = mood; isCheckable = true; isChecked = i == 0
                chipBackgroundColor = resources.getColorStateList(R.color.chip_bg_selector, null)
                setTextColor(resources.getColorStateList(R.color.chip_text_selector, null))
                setOnClickListener { vm.setFilter(mood = mood) }
            })
        }

        // ── Tab click listeners ───────────────────────────────────────────────
        tabBrani.setOnClickListener    { selectTab(Tab.BRANI) }
        tabCartelle.setOnClickListener { selectTab(Tab.CARTELLE) }
        tabArtisti.setOnClickListener  { selectTab(Tab.ARTISTI) }
        tabAnno.setOnClickListener     { selectTab(Tab.ANNO) }

        btnScan.setOnClickListener    { checkPermsAndScan() }
        btnFilter.setOnClickListener  { showFilterSheet() }
        view.findViewById<MaterialButton>(R.id.btnToggleView)
            ?.setOnClickListener { (activity as? MainActivity)?.switchToBubbleMap() }
        etSearch.doAfterTextChanged { vm.setFilter(search = it?.toString() ?: "") }

        // ── Observers ─────────────────────────────────────────────────────────
        vm.songs.observe(viewLifecycleOwner) { refreshCurrentTab() }
        vm.filters.observe(viewLifecycleOwner) { refreshCurrentTab() }

        vm.scanState.observe(viewLifecycleOwner) { state ->
            btnScan.isEnabled = state != ScanState.SCANNING && state != ScanState.ANALYZING
            progressBar.visibility = if (
                state == ScanState.SCANNING || state == ScanState.ANALYZING
            ) View.VISIBLE else View.GONE
            when (state) {
                ScanState.IDLE -> tvStatus.text = "Premi SCANSIONA per iniziare"
                ScanState.DONE -> tvStatus.text = "✓ ${vm.getAnalyzedSongs().size} brani analizzati"
                else -> {}
            }
        }
        vm.scanProgress.observe(viewLifecycleOwner) { (cur, tot) ->
            if (tot > 0) {
                progressBar.max = tot; progressBar.progress = cur
                tvStatus.text = when (vm.scanState.value) {
                    ScanState.ANALYZING -> "Analisi mood $cur/$tot…"
                    else                -> "Lettura file $cur/$tot…"
                }
            }
        }
        vm.scanError.observe(viewLifecycleOwner) { err ->
            if (err.isNotBlank()) Snackbar.make(requireView(), err, Snackbar.LENGTH_LONG).show()
        }
        vm.scanFolder.observe(viewLifecycleOwner) { folder ->
            val label = folder?.substringAfterLast("/") ?: "Tutto"
            btnScan.text = "📂 $label"
        }

        // Stato iniziale: tab Brani
        selectTab(Tab.BRANI)
    }

    // ── Tab management ────────────────────────────────────────────────────────

    private fun selectTab(tab: Tab) {
        currentTab = tab
        updateTabStyles()
        refreshCurrentTab()
    }

    private fun updateTabStyles() {
        val active   = (0xFFFFFFFF).toInt()
        val inactive = (0xFF555570).toInt()
        tabBrani.setTextColor(if (currentTab == Tab.BRANI)     active else inactive)
        tabCartelle.setTextColor(if (currentTab == Tab.CARTELLE) active else inactive)
        tabArtisti.setTextColor(if (currentTab == Tab.ARTISTI)  active else inactive)
        tabAnno.setTextColor(if (currentTab == Tab.ANNO)    active else inactive)

        // Sposta indicatore sotto il tab attivo
        val targetTab = when (currentTab) {
            Tab.BRANI    -> tabBrani
            Tab.CARTELLE -> tabCartelle
            Tab.ARTISTI  -> tabArtisti
            Tab.ANNO     -> tabAnno
        }
        targetTab.post {
            val lp = tabIndicator.layoutParams as? ViewGroup.MarginLayoutParams ?: return@post
            lp.marginStart = (targetTab.left + 16 * resources.displayMetrics.density).toInt()
            lp.width = targetTab.width - (8 * resources.displayMetrics.density).toInt()
            tabIndicator.layoutParams = lp
        }

        // Mood chips visibili solo in tab Brani
        moodChipScroll.visibility = if (currentTab == Tab.BRANI) View.VISIBLE else View.GONE
    }

    private fun refreshCurrentTab() {
        when (currentTab) {
            Tab.BRANI    -> showBrani()
            Tab.CARTELLE -> showCartelle()
            Tab.ARTISTI  -> showArtisti()
            Tab.ANNO     -> showAnno()
        }
    }

    // ── Brani (lista flat filtrata) ───────────────────────────────────────────

    private fun showBrani() {
        rv.adapter = songAdapter
        val filtered = vm.getFilteredSongs()
        songAdapter.submitList(filtered.toList())
        val total = vm.songs.value?.size ?: 0
        tvCount.text = "${filtered.size} di $total brani"
        tvEmpty.visibility = if (filtered.isEmpty() && total > 0) View.VISIBLE else View.GONE
    }

    // ── Cartelle ──────────────────────────────────────────────────────────────

    private fun showCartelle() {
        rv.adapter = groupedAdapter
        val songs = vm.songs.value?.filter { it.analyzed } ?: emptyList()
        val q = etSearch.text?.toString() ?: ""

        val grouped = songs
            .filter { q.isBlank() || it.title.contains(q, true) || it.artist.contains(q, true) }
            .groupBy { song ->
                // Folder = parent directory name
                song.path.substringBeforeLast("/").substringAfterLast("/")
            }
            .entries
            .sortedBy { it.key }
            .map { (folder, list) ->
                Triple(folder, "${list.size} brani", list.sortedBy { it.title })
            }

        groupedAdapter.submitGroups(grouped)
        tvCount.text = "${grouped.size} cartelle"
        tvEmpty.visibility = if (grouped.isEmpty()) View.VISIBLE else View.GONE
    }

    // ── Artisti ───────────────────────────────────────────────────────────────

    private fun showArtisti() {
        rv.adapter = groupedAdapter
        val songs = vm.songs.value?.filter { it.analyzed } ?: emptyList()
        val q = etSearch.text?.toString() ?: ""

        val grouped = songs
            .filter { q.isBlank() || it.artist.contains(q, true) || it.title.contains(q, true) }
            .groupBy { it.artist.ifBlank { "Artista sconosciuto" } }
            .entries
            .sortedBy { it.key }
            .map { (artist, list) ->
                Triple(artist, "${list.size} brani", list.sortedBy { it.title })
            }

        groupedAdapter.submitGroups(grouped)
        tvCount.text = "${grouped.size} artisti"
        tvEmpty.visibility = if (grouped.isEmpty()) View.VISIBLE else View.GONE
    }

    // ── Anno ─────────────────────────────────────────────────────────────────

    private fun showAnno() {
        rv.adapter = groupedAdapter
        val songs = vm.songs.value?.filter { it.analyzed } ?: emptyList()
        val q = etSearch.text?.toString() ?: ""

        val grouped = songs
            .filter { q.isBlank() || it.title.contains(q, true) || it.artist.contains(q, true) }
            .groupBy { it.year.ifBlank { "Sconosciuto" } }
            .entries
            .sortedByDescending { it.key }
            .map { (year, list) ->
                Triple(year, "${list.size} brani", list.sortedBy { it.title })
            }

        groupedAdapter.submitGroups(grouped)
        tvCount.text = "${grouped.size} anni"
        tvEmpty.visibility = if (grouped.isEmpty()) View.VISIBLE else View.GONE
    }

    // ── Helper: lista piatta dei brani nella tab corrente (per playlist) ─────

    private fun getCurrentGroupSongs(): List<Song> {
        val songs = vm.songs.value?.filter { it.analyzed } ?: emptyList()
        return when (currentTab) {
            Tab.BRANI    -> vm.getFilteredSongs()
            else         -> songs
        }
    }

    // ── Selezione cartella ────────────────────────────────────────────────────

    private fun checkPermsAndScan() {
        val perms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            arrayOf(Manifest.permission.READ_MEDIA_AUDIO)
        else
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        val ok = perms.all {
            ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
        }
        if (ok) showFolderDialog() else permLauncher.launch(perms)
    }

    private fun showFolderDialog() {
        val base = Environment.getExternalStorageDirectory().absolutePath
        val cur  = vm.scanFolder.value
        val optLabels = mutableListOf(
            "🌐  Tutto il dispositivo",
            "🎵  Music",
            "⬇️  Download",
            "💬  WhatsApp Audio",
            "✈️  Telegram Audio",
            "📁  Percorso personalizzato…"
        )
        val optPaths = mutableListOf<String?>(
            null,
            "$base/Music",
            "$base/Download",
            "$base/WhatsApp/Media/WhatsApp Audio",
            "$base/Telegram/Telegram Audio",
            "__custom__"
        )
        if (cur != null && !optPaths.contains(cur)) {
            optLabels.add(0, "📂  $cur")
            optPaths.add(0, cur)
        }
        val checkedIdx = optPaths.indexOf(cur).coerceAtLeast(0)
        AlertDialog.Builder(requireContext())
            .setTitle("Dove cercare la musica?")
            .setSingleChoiceItems(optLabels.toTypedArray(), checkedIdx) { dialog, which ->
                val chosen = optPaths[which]
                if (chosen == "__custom__") {
                    dialog.dismiss(); showCustomPathDialog()
                } else {
                    vm.setScanFolder(chosen)
                    SongCache.saveScanFolder(requireContext(), chosen)
                    dialog.dismiss()
                    startAnalysis(chosen)
                }
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    private fun showCustomPathDialog() {
        val input = EditText(requireContext()).apply {
            setText(vm.scanFolder.value?.takeIf { it != "__custom__" }
                ?: "/storage/emulated/0/Music")
            setPadding(56, 32, 56, 32)
        }
        AlertDialog.Builder(requireContext())
            .setTitle("Percorso cartella")
            .setView(input)
            .setPositiveButton("Scansiona") { _, _ ->
                val path = input.text.toString().trim().ifBlank { null }
                vm.setScanFolder(path)
                SongCache.saveScanFolder(requireContext(), path)
                startAnalysis(path)
            }
            .setNegativeButton("Annulla", null)
            .show()
    }

    private fun startAnalysis(folder: String?) {
        val where = folder?.let { "in ${it.substringAfterLast("/")}" } ?: "su tutto il dispositivo"
        tvStatus.text = "Ricerca audio $where…"
        vm.startAnalysis(requireContext().applicationContext, folder)
    }

    // ── Filtri (bottom sheet) ─────────────────────────────────────────────────

    private fun showFilterSheet() {
        val ctx   = requireContext()
        val sheet = BottomSheetDialog(ctx, R.style.BottomSheetStyle)
        val sv    = layoutInflater.inflate(R.layout.sheet_filters, null)
        sheet.setContentView(sv)
        val chipGenre: ChipGroup     = sv.findViewById(R.id.chipGenre)
        val chipYear: ChipGroup      = sv.findViewById(R.id.chipYear)
        val btnApply: MaterialButton = sv.findViewById(R.id.btnApplyFilters)
        val btnReset: MaterialButton = sv.findViewById(R.id.btnResetFilters)
        val cur = vm.filters.value ?: return

        vm.availableGenres().forEach { g ->
            chipGenre.addView(Chip(ctx).apply {
                text = g; isCheckable = true; isChecked = g == cur.genre
                chipBackgroundColor = resources.getColorStateList(R.color.chip_bg_selector, null)
                setTextColor(resources.getColorStateList(R.color.chip_text_selector, null))
            })
        }
        vm.availableYears().forEach { y ->
            chipYear.addView(Chip(ctx).apply {
                text = y; isCheckable = true; isChecked = y == cur.year
                chipBackgroundColor = resources.getColorStateList(R.color.chip_bg_selector, null)
                setTextColor(resources.getColorStateList(R.color.chip_text_selector, null))
            })
        }

        btnApply.setOnClickListener {
            val g = (0 until chipGenre.childCount).map { chipGenre.getChildAt(it) as Chip }
                .firstOrNull { it.isChecked }?.text?.toString() ?: "Tutti"
            val y = (0 until chipYear.childCount).map { chipYear.getChildAt(it) as Chip }
                .firstOrNull { it.isChecked }?.text?.toString() ?: "Tutti"
            vm.setFilter(genre = g, year = y)
            val active = listOf(g, y).count { it != "Tutti" }
            btnFilter.text = if (active > 0) "Filtri ($active)" else "Filtri"
            sheet.dismiss()
        }
        btnReset.setOnClickListener {
            vm.setFilter(genre = "Tutti", year = "Tutti")
            btnFilter.text = "Filtri"
            sheet.dismiss()
        }
        sheet.show()
    }

    // ── Mood picker (long press) ──────────────────────────────────────────────

    private fun showMoodPicker(song: Song) {
        val ctx   = requireContext()
        val sheet = BottomSheetDialog(ctx, R.style.BottomSheetStyle)
        val v     = layoutInflater.inflate(R.layout.sheet_mood_picker, null)
        sheet.setContentView(v)

        v.findViewById<TextView>(R.id.tvPickerSong).text =
            "${song.title} — ${song.artist.ifBlank { "Artista sconosciuto" }}"

        val moods = listOf(
            "Energico"    to R.id.rowEnergicoContainer,
            "Positivo"    to R.id.rowPositivoContainer,
            "Aggressivo"  to R.id.rowAggressivoContainer,
            "Malinconico" to R.id.rowMalinconatoContainer
        )
        val checks = mapOf(
            "Energico"    to R.id.checkEnergicoBtn,
            "Positivo"    to R.id.checkPositivoBtn,
            "Aggressivo"  to R.id.checkAggressivoBtn,
            "Malinconico" to R.id.checkMalinconatoBtn
        )
        checks[song.effectiveMood]?.let { id ->
            v.findViewById<TextView>(id).visibility = View.VISIBLE
        }
        moods.forEach { (moodName, rowId) ->
            v.findViewById<View>(rowId).setOnClickListener {
                vm.setMoodOverride(requireContext().applicationContext, song, moodName)
                sheet.dismiss()
                Snackbar.make(requireView(), "Mood: $moodName", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(0xFF6C63FF.toInt()).setTextColor(0xFFFFFFFF.toInt()).show()
            }
        }
        v.findViewById<View>(R.id.rowResetContainer).setOnClickListener {
            vm.clearMoodOverride(requireContext().applicationContext, song)
            sheet.dismiss()
            Snackbar.make(requireView(), "Mood ripristinato", Snackbar.LENGTH_SHORT).show()
        }
        sheet.show()
    }
}
