# 🎵 Music-Mood

**Music-Mood** is an Android app that analyzes your local music library and turns every song into a personal emotional map: mood, BPM, key, valence, arousal, music profile, BubbleMap and Weekly Mood Report.

The goal is simple: **listen to your music in a smarter, more personal and more visual way**.

## ✨ Why install Music-Mood

Music-Mood is not just a music player. It helps you discover **how your music feels**.

With Music-Mood you can:

- 🎧 explore your local music library
- 🧠 classify songs into 9 musical moods, with a secondary mood when a song has two
- ℹ️ understand **why** a song has its mood
- 🤖 use YAMNet as an advanced audio classifier
- 📍 visualize songs on the valence/arousal BubbleMap
- 📊 generate aggregate statistics and export them to CSV
- 🧬 build your personal music profile
- 🎯 calibrate the classifier on your own taste
- 📆 create a Weekly Mood Report
- 🖼️ share profile and report images
- 🌍 use the app in Italian, English or Spanish
- 🔄 refresh and re-analyze your library without losing your choices

## 🧠 The emotional engine: mood, valence and arousal

Music-Mood interprets each song through a two-dimensional emotional model based on **valence** and **arousal**.

### 💜 Valence

**Valence** describes the hedonic quality of a musical stimulus: how positive, pleasant, bright, negative, dark or melancholic a song may feel.

In Music-Mood, valence is represented as a continuous axis:

```
negative  ─────────────── positive
```

### ⚡ Arousal

**Arousal** represents the activation level or intensity: how calm, soft and relaxed a song is, or how energetic, intense and activating it feels.

In Music-Mood, arousal is represented as a continuous axis:

```
calm  ─────────────── intense
```

### 🫧 BubbleMap

The **BubbleMap** uses these two axes to position songs in a continuous emotional space:

```
                    High arousal
                         ↑
        intense/dark     │   positive/intense
                         │
Negative valence  ───────┼───────  Positive valence
                         │
         sad/calm        │   positive/calm
                         ↓
                    Low arousal
```

Intuitive examples:

- 😔 a melancholic song tends to have **negative valence** and **low arousal**
- 🔥 an aggressive song tends to have **negative valence** and **high arousal**
- ☀️ a positive song tends to have **positive valence**
- 🌿 a relaxed song tends to have **low arousal**

This approach is inspired by the circumplex model of emotions, where emotional states are represented as combinations of valence and arousal. Useful references: [Understanding emotions to live better - Psicologia Contemporanea](https://www.psicologiacontemporanea.it/blog/capire-le-emozioni-vivere-meglio/), [Arousal - State of Mind](https://www.stateofmind.it/arousal/) and [Circumplex Model of Affect: Valence and Arousal Explained](https://psychologyfanatic.com/circumplex-model-of-arousal-and-valence/).

## 🤖 Advanced classification with YAMNet

Music-Mood uses a hybrid approach:

- **YAMNet** to support advanced audio classification
- **local DSP** for BPM, key, valence, arousal and fallback
- **music rules** for tricky cases: dance tracks don't end up in calm moods and slow ballads are not read as energetic
- **Last.fm tags** as an optional second opinion
- **personalized calibration** to adapt the model to your library
- **manual mood override** when you want to decide the mood yourself

YAMNet is a pre-trained neural network for audio classification. TensorFlow documentation describes YAMNet as a model able to predict 521 audio event classes based on the AudioSet corpus and built on a MobileNetV1 depthwise-separable convolution architecture. See: [Sound classification with YAMNet - TensorFlow Hub](https://www.tensorflow.org/hub/tutorials/yamnet) and [YAMNet README - TensorFlow Models](https://github.com/tensorflow/models/tree/master/research/audioset/yamnet).

In Music-Mood, YAMNet is part of the classification engine, not the only source of truth.

## 🎭 Supported moods

| Mood | Meaning |
|---|---|
| ⚡ Energetic | high activation, energy, movement |
| ☀️ Positive | bright, pleasant, good vibes |
| 🔥 Aggressive | intense, strong, impactful |
| 🌧️ Melancholic | deep, dark, delicate |
| 🕯️ Romantic | warm, intimate, emotional |
| 🌿 Relaxed | calm, soft, soothing |
| 📼 Nostalgic | memories, nuance, reflection |
| 🎯 Focus | concentration, balance, order |
| 🎉 Party | celebration, excitement, lightness |

## 📱 Main features

### 🎧 Local music library

- local audio library scanning
- sections for songs, artists, albums, genres, years and folders
- folder-based navigation
- sorting by title, BPM, valence and arousal
- side fast-scroll index that follows the sorting: letters, BPM, valence or energy ranges
- manual library refresh from Settings when you add new songs

### ▶️ Player and mini-player

- integrated full-screen player
- persistent mini-player
- local and remote artwork
- tap the notification to return to the playing song
- automatic pause when another audio starts (call, video, other app) and when you unplug headphones
- contextual menu for mood analysis and manual mood editing

### 🧠 Mood analysis

- automatic mood classification
- secondary mood, when a song has two close moods
- valence and arousal
- estimated BPM
- key and major/minor mode
- YAMNet support and DSP fallback
- **"Why this mood?"**: for each song it shows the top 3 moods in percentages, the audio factors that mattered, its position on the BubbleMap and the effect of Last.fm

### 🏷️ Manual mood

You can manually correct the mood of a song. **Your choice always comes first**: statistics, profile, BubbleMap and automatic playlists use it immediately. Removing the correction brings back the calculated mood.

Logical priority:

1. user-selected manual mood
2. mood calculated from audio, possibly adjusted by Last.fm
3. fallback

### 🔄 Library re-analysis

When the analysis engine improves, the app offers to re-analyze your library. You can do it right away with **"Update now"** or later, also from Settings with **"Re-analyze library"**.

Re-analysis only redoes the audio part: **your manual moods and Last.fm tags are kept**.

### 🫧 BubbleMap

- visual map of songs in the valence/arousal space
- positivity/intensity axes
- localized mood labels
- tap bubbles to view title, artist and mood

### 📊 Stats

- mood distribution
- aggregate metrics
- average BPM
- average valence
- average arousal
- most frequent key
- top artists
- CSV export with mood, secondary mood, manual mood, scores and Last.fm status

### 🧬 Personal music profile

- dominant musical archetype
- emotional fingerprint radar
- top moods
- top artist
- daily music suggestion
- shareable profile image

### 📆 Weekly Mood Report

- weekly listening summary
- dominant mood of the week
- weekly mood distribution
- comparison with previous week
- shareable report image

### 🌐 Optional Last.fm integration

- manual username/API key configuration
- recent tracks in the Weekly Report, with manual refresh
- **Last.fm tags as a second opinion on mood**: the app reads community tags for each song
  - Last.fm only counts if its mood is already among the top 3 suggested by the audio
  - its effect is limited: at most 6 percentage points
  - you can **stop and resume** the lookup from Settings or from the notification; progress is saved
  - new songs are looked up automatically
  - the "Use Last.fm tags" option can be turned off at any time

### 🌍 Multilingual support

Music-Mood supports:

- 🇮🇹 Italian
- 🇬🇧 English
- 🇪🇸 Spanish

At first launch, the app asks the user to choose a language **before requesting permissions**. The default language is Italian.

## 🎯 Personalized calibration

Music-Mood can adapt the classifier to your music taste.

Calibration uses the already analyzed library to shift classifier centroids based on your average profile.

- 0.0 = no calibration
- 0.5 = balanced calibration
- 1.0 = maximum adaptability

**Reset calibration** brings every song back to its original analysis mood and updates statistics, fingerprint, radar, top moods and archetype. Your manual moods stay unchanged.

Calibration is tied to the device: it is not restored from Android backup after a reinstall.

## 🎼 Mood-safe automatic playlists

Each preset uses allowed moods, blocked moods and technical filters based on arousal, BPM and valence.

Examples:

- 🌙 Sleep/Relax excludes Energetic, Party and Aggressive
- ⚡ Energy prioritizes Energetic, Party and Positive
- 🎯 Focus prioritizes Focus and Relaxed with controlled arousal
- 💪 Workout prioritizes high energy, high BPM or high arousal

If there are not enough matching songs, the fallback still remains limited to compatible moods. Playlists respect your manual moods.

## 🔐 Privacy and local data

Music-Mood is designed to work mainly on-device: audio analysis runs locally and there is no proprietary backend.

The only external services contacted are:

- **Last.fm**, only if you configure it
- **Apple iTunes** and **Deezer**, for missing artwork (title and artist only)

The Privacy Policy is available here: [Music-Mood Privacy Policy](https://lucatrombetta090-sys.github.io/Music-Mood/privacy_policy).

## 🛠️ Technical stack

- Kotlin
- Android
- Media3
- Room
- WorkManager
- Material Components
- AppCompat for per-app language
- Chaquopy
- Python
- NumPy / Mutagen
- YAMNet / TensorFlow Lite / MediaPipe Tasks Audio
- GitHub Actions
- Stable keystore through GitHub Secrets

## 🚀 Build

Recommended requirements:

- recent Android Studio
- JDK 17
- configured Android SDK
- Gradle compatible with the project

Debug build:

```
./gradlew assembleDebug
```

## 📦 Current status (1.0.1)

The build includes:

- local library with fast-scroll index
- player and mini-player with audio focus handling
- mood analysis with YAMNet and DSP
- secondary mood and "Why this mood?"
- manual mood with top priority
- Last.fm tags as a second opinion, with Stop/Resume
- re-analysis that keeps user choices
- BubbleMap
- stats and CSV export
- music profile
- calibration and reset calibration
- mood-safe automatic playlists
- Weekly Mood Report
- Italian, English and Spanish support
- shareable profile/report images
- stable signing through GitHub Actions

## 🗺️ Roadmap

- further improve playlists and recommendations
- refine BPM estimation on ballads
- strengthen Last.fm/local library matching
- expand user documentation and store screenshots

## 👤 Author

**Luca Trombetta**

## 📄 License

MIT
