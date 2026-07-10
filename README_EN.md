# 🎵 Music-Mood

**Music-Mood** is an Android app that analyzes your local music library and turns every song into a personal emotional map: mood, confidence, BPM, key, valence, arousal, music profile, BubbleMap and Weekly Mood Report.

The goal is simple: **listen to your music in a smarter, more personal and more visual way**.

---

## ✨ Why install Music-Mood

Music-Mood is not just a music player. It helps you discover **how your music feels**.

With Music-Mood you can:

- 🎧 explore your local music library
- 🧠 classify songs into 9 musical moods
- 🤖 use YAMNet as an advanced audio classifier
- 📍 visualize songs on the valence/arousal BubbleMap
- 📊 generate aggregate statistics
- 🧬 build your personal music profile
- 🎯 calibrate the classifier on your own taste
- 📆 create a Weekly Mood Report
- 🖼️ share profile and report images
- 🌍 use the app in Italian or English
- 🔄 refresh your library when you add new songs

---

## 🧠 The emotional engine: mood, valence and arousal

Music-Mood interprets each song through a two-dimensional emotional model based on **valence** and **arousal**.

### 💜 Valence

**Valence** describes the hedonic quality of a musical stimulus: how positive, pleasant, bright, negative, dark or melancholic a song may feel.

In Music-Mood, valence is represented as a continuous axis:

```text
negative  ─────────────── positive
```

### ⚡ Arousal

**Arousal** represents the activation level or intensity of the response: how calm, soft and relaxed a song is, or how energetic, intense and activating it feels.

In Music-Mood, arousal is represented as a continuous axis:

```text
calm  ─────────────── intense
```

### 🫧 BubbleMap

The **BubbleMap** uses these two axes to position songs in a continuous emotional space:

```text
                    High arousal
                        ↑
        intense/dark    │   positive/intense
                        │
Negative valence  ──────┼──────  Positive valence
                        │
       sad/calm         │   positive/calm
                        ↓
                    Low arousal
```

Intuitive examples:

- 😔 a melancholic song tends to have **negative valence** and **low arousal**
- 🔥 an aggressive song tends to have **negative valence** and **high arousal**
- ☀️ a positive song tends to have **positive valence**
- 🌿 a relaxed song tends to have **low arousal**

This approach is inspired by the circumplex model of emotions, where emotional states are represented as combinations of valence and arousal. Useful references: [Understanding emotions to live better - Psicologia Contemporanea](https://www.psicologiacontemporanea.it/blog/capire-le-emozioni-vivere-meglio/), [Arousal - State of Mind](https://www.stateofmind.it/arousal/) and [Circumplex Model of Affect: Valence and Arousal Explained](https://psychologyfanatic.com/circumplex-model-of-arousal-and-valence/).

---

## 🤖 Advanced classification with YAMNet

Music-Mood uses a hybrid approach:

- **YAMNet** to support advanced audio classification
- **local DSP** for music features and fallback logic
- **personalized calibration** to adapt the model to your library
- **manual mood override** when you want to decide the mood yourself

YAMNet is a pre-trained neural network for audio classification. TensorFlow documentation describes YAMNet as a model able to predict 521 audio event classes based on the AudioSet corpus and built on a MobileNetV1 depthwise-separable convolution architecture. See: [Sound classification with YAMNet - TensorFlow Hub](https://www.tensorflow.org/hub/tutorials/yamnet) and [YAMNet README - TensorFlow Models](https://github.com/tensorflow/models/tree/master/research/audioset/yamnet).

In Music-Mood, YAMNet is part of the classification engine, not the only source of truth. The app combines audio model results with metadata, DSP features, calibration and manual corrections.

---

## 🎭 Supported moods

Music-Mood classifies songs into 9 main moods:

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

---

## 📱 Main features

### 🎧 Local music library

- local audio library scanning
- sections for songs, artists, albums, genres, years and folders
- folder-based navigation
- sorting by title, BPM, valence and arousal
- manual library refresh from Settings when you add new songs

### ▶️ Player and mini-player

- integrated full-screen player
- persistent mini-player
- local and remote artwork
- contextual menu for mood analysis and manual mood editing

### 🧠 Mood analysis

- automatic mood classification
- confidence score
- valence and arousal
- estimated BPM
- key and major/minor mode
- YAMNet support and DSP fallback

### 🏷️ Manual mood

You can manually correct the mood of a song. Manual mood has priority over calculated mood.

Logical priority:

1. user-selected manual mood
2. locally calculated mood
3. optional external data
4. fallback

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
- CSV export

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
- manual refresh
- local Last.fm summary cache
- integration in the Weekly Mood Report

### 🌍 Multilingual support

Music-Mood supports:

- 🇮🇹 Italian
- 🇬🇧 English

At first launch, the app asks the user to choose a language **before requesting permissions**. The default language is Italian. Automatic/system language selection has been removed to make the behavior clearer and more predictable.

---

## 🎯 Personalized calibration

Music-Mood can adapt the classifier to your music taste.

Calibration uses the already analyzed library to shift classifier centroids based on your average profile.

- `0.0` = no calibration
- `0.5` = balanced calibration
- `1.0` = maximum adaptability

Reset calibration restores the original classification and updates fingerprint, radar, top moods and archetype.

---

## 🎼 Mood-safe automatic playlists

Automatic playlists are now stricter: each preset uses allowed moods, blocked moods and technical filters based on arousal/BPM/valence.

Examples:

- 🌙 Sleep/Relax excludes Energetic, Party and Aggressive
- ⚡ Energy prioritizes Energetic, Party and Positive
- 🎯 Focus prioritizes Focus and Relaxed with controlled arousal
- 💪 Workout prioritizes high energy, high BPM or high arousal

If there are not enough technically matching songs, the fallback still remains limited to compatible moods.

---

## 🔐 Privacy and local data

Music-Mood is designed to work mainly on-device.

The app may process locally:

- title, artist, album, duration
- file path or folder
- artwork
- estimated mood
- confidence
- BPM
- valence/arousal
- key
- manual mood
- app preferences
- weekly reports
- optional Last.fm cache

The Privacy Policy is available here: [Music-Mood Privacy Policy](https://lucatrombetta090-sys.github.io/Music-Mood/privacy_policy).

---

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
- NumPy / SciPy / Mutagen
- YAMNet / TensorFlow Lite / MediaPipe Tasks Audio
- GitHub Actions
- Stable keystore through GitHub Secrets

---

## 🚀 Build

Recommended requirements:

- recent Android Studio
- JDK 17
- configured Android SDK
- Gradle compatible with the project

Debug build:

```bash
./gradlew assembleDebug
```

---

## 📦 Current status

The build includes:

- local library
- player and mini-player
- mood analysis
- optional YAMNet
- DSP fallback
- advanced sorting
- BubbleMap
- stats
- music profile
- calibration and reset calibration
- mood-safe automatic playlists
- Weekly Mood Report
- optional Last.fm
- library refresh from Settings
- first-launch language selection
- Italian/English support
- shareable profile/report images
- stable signing through GitHub Actions

---

## 🗺️ Roadmap

- complete Play Store pre-release validation
- further improve playlists and recommendations
- strengthen Last.fm/local library matching
- evaluate release build/AAB for Play Console
- expand user documentation and store screenshots

---

## 👤 Author

**Luca Trombetta**

---

## 📄 License

MIT
