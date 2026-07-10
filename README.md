# 🎵 Music-Mood

**Music-Mood** è un'app Android che analizza la libreria musicale locale e trasforma ogni brano in una piccola mappa emotiva: mood, confidenza, BPM, tonalità, valenza, arousal, profilo musicale personale, BubbleMap e Weekly Mood Report.

L'obiettivo è semplice: **ascoltare la tua musica in modo più intelligente, personale e visivo**.

---

## ✨ Perché installarla

Music-Mood non è solo un player musicale. È un'app per scoprire **come suona emotivamente la tua libreria**.

Con Music-Mood puoi:

- 🎧 esplorare i tuoi brani locali
- 🧠 classificare i brani in 9 mood musicali
- 🤖 usare YAMNet come classificatore avanzato
- 📍 visualizzare i brani nella BubbleMap valenza/arousal
- 📊 generare statistiche aggregate
- 🧬 costruire il tuo profilo musicale personale
- 🎯 calibrare il classificatore sui tuoi gusti
- 📆 creare un Weekly Mood Report
- 🖼️ condividere immagini del profilo e del report
- 🌍 usare l'app in Italiano o Inglese
- 🔄 ricaricare la libreria quando aggiungi nuovi brani

---

## 🧠 Il cuore dell'app: mood, valenza e arousal

Music-Mood interpreta ogni brano usando un modello emozionale bidimensionale basato su **valenza** e **arousal**.

### 💜 Valenza

La **valenza** indica la qualità edonica di uno stimolo musicale: quanto un brano tende a essere percepito come positivo, piacevole, luminoso oppure negativo, cupo, malinconico.

In Music-Mood la valenza viene rappresentata come un asse continuo:

```text
negativo  ─────────────── positivo
```

### ⚡ Arousal

L'**arousal** rappresenta il livello di attivazione o intensità: quanto un brano è calmo, rilassato, morbido oppure energico, intenso, attivante.

In Music-Mood l'arousal viene rappresentato come un asse continuo:

```text
calmo  ─────────────── intenso
```

### 🫧 BubbleMap

La **BubbleMap** usa questi due assi per posizionare i brani in uno spazio emotivo continuo:

```text
                    Arousal alto
                       ↑
        intenso/scuro  │  positivo/intenso
                       │
Valenza negativa  ─────┼─────  Valenza positiva
                       │
      triste/calmo     │  positivo/calmo
                       ↓
                    Arousal basso
```

Esempi intuitivi:

- 😔 un brano malinconico tende ad avere **valenza negativa** e **arousal basso**
- 🔥 un brano aggressivo tende ad avere **valenza negativa** e **arousal alto**
- ☀️ un brano positivo tende ad avere **valenza positiva**
- 🌿 un brano rilassato tende ad avere **arousal basso**

Questa impostazione si ispira al modello circomplesso delle emozioni, in cui gli stati emotivi possono essere descritti come combinazione di valenza e arousal. Approfondimenti utili: [Capire le emozioni per vivere meglio - Psicologia Contemporanea](https://www.psicologiacontemporanea.it/blog/capire-le-emozioni-vivere-meglio/), [Arousal - State of Mind](https://www.stateofmind.it/arousal/) e [Circumplex Model of Affect: Valence and Arousal Explained](https://psychologyfanatic.com/circumplex-model-of-arousal-and-valence/).

---

## 🤖 Classificazione avanzata con YAMNet

Music-Mood integra un approccio ibrido:

- **YAMNet** per supportare la classificazione audio avanzata
- **DSP locale** per feature musicali e fallback
- **calibrazione personalizzata** per adattare il modello alla tua libreria
- **mood manuale** per correggere il risultato quando vuoi decidere tu

YAMNet è una rete neurale pre-addestrata per la classificazione audio. La documentazione TensorFlow descrive YAMNet come un modello in grado di predire 521 classi audio basate sul corpus AudioSet e costruito su architettura MobileNetV1 depthwise-separable convolution. Vedi: [Sound classification with YAMNet - TensorFlow Hub](https://www.tensorflow.org/hub/tutorials/yamnet) e [YAMNet README - TensorFlow Models](https://github.com/tensorflow/models/tree/master/research/audioset/yamnet).

In Music-Mood YAMNet viene usato come componente del motore di classificazione, non come unica fonte di verità. La logica dell'app combina il risultato dei modelli audio con metadati, feature DSP, calibrazione e correzioni manuali.

---

## 🎭 I 9 mood supportati

Music-Mood classifica i brani in 9 mood principali:

| Mood | Significato |
|---|---|
| ⚡ Energico | alta attivazione, carica, movimento |
| ☀️ Positivo | luminoso, piacevole, good vibes |
| 🔥 Aggressivo | intenso, forte, impattante |
| 🌧️ Malinconico | cupo, delicato, introspettivo |
| 🕯️ Romantico | caldo, intimo, emotivo |
| 🌿 Rilassato | calmo, morbido, distensivo |
| 📼 Nostalgico | ricordi, memoria, sfumature |
| 🎯 Concentrazione | focus, equilibrio, ordine |
| 🎉 Festivo | party, entusiasmo, leggerezza |

---

## 📱 Funzionalità principali

### 🎧 Libreria musicale locale

- lettura della libreria audio locale
- sezioni per brani, artisti, album, generi, anni e cartelle
- navigazione per cartelle
- sorting per titolo, BPM, valenza e arousal
- ricarica manuale della libreria da Impostazioni quando aggiungi nuovi brani

### ▶️ Player e mini-player

- player integrato full-screen
- mini-player persistente
- artwork locale/remoto
- menu contestuale per analisi mood e modifica mood manuale

### 🧠 Analisi mood

- classificazione automatica del mood
- confidenza del risultato
- valenza e arousal
- BPM stimato
- tonalità e modo maggiore/minore
- supporto a YAMNet e DSP fallback

### 🏷️ Mood manuale

Puoi correggere un brano manualmente. Il mood manuale ha priorità sul mood calcolato.

Priorità logica:

1. mood scelto manualmente
2. mood calcolato localmente
3. dati esterni opzionali
4. fallback

### 🫧 BubbleMap

- mappa visuale dei brani nello spazio valenza/arousal
- assi positività/intensità
- label mood localizzate
- tap sulle bolle per vedere titolo, artista e mood

### 📊 Stats

- distribuzione mood
- metriche aggregate
- BPM medio
- valenza media
- arousal medio
- tonalità più frequente
- top artisti
- export CSV

### 🧬 Profilo musicale personale

- archetipo musicale dominante
- fingerprint emotivo radar
- top mood
- top artist
- suggerimento giornaliero
- condivisione immagine profilo

### 📆 Weekly Mood Report

- riepilogo settimanale degli ascolti
- mood dominante della settimana
- distribuzione settimanale
- confronto con settimana precedente
- condivisione immagine report

### 🌐 Last.fm opzionale

- configurazione manuale username/API key
- refresh manuale
- cache locale del riepilogo Last.fm
- integrazione nel Weekly Report

### 🌍 Multilingua

Music-Mood supporta:

- 🇮🇹 Italiano
- 🇬🇧 English

Al primo avvio l'app mostra la scelta lingua **prima della richiesta dei permessi**. Il default è Italiano. L'opzione automatica/sistema è stata rimossa per rendere il comportamento più chiaro e controllato.

---

## 🎯 Calibrazione personalizzata

Music-Mood può adattare il classificatore ai tuoi gusti musicali.

La calibrazione usa la libreria già analizzata per spostare i centroidi del classificatore rispetto al tuo profilo medio.

- `0.0` = nessuna calibrazione
- `0.5` = calibrazione bilanciata
- `1.0` = massima adattabilità

Il reset della calibrazione ripristina la classificazione originale e aggiorna fingerprint, radar, top mood e archetipo.

---

## 🎼 Playlist automatiche mood-safe

Le playlist automatiche sono state rese più rigorose: ogni preset usa mood ammessi, mood bloccati e filtri tecnici su arousal/BPM/valenza.

Esempi:

- 🌙 Sleep/Relax esclude Energico, Festivo e Aggressivo
- ⚡ Energy privilegia Energico, Festivo e Positivo
- 🎯 Focus privilegia Concentrazione e Rilassato con arousal contenuto
- 💪 Workout privilegia alta energia, BPM alto o arousal elevato

Se non ci sono abbastanza brani coerenti, il fallback resta comunque limitato ai mood compatibili.

---

## 🔐 Privacy e dati locali

Music-Mood è progettata per lavorare principalmente sul dispositivo.

L'app può trattare localmente:

- titolo, artista, album, durata
- percorso/cartella del file
- artwork
- mood stimato
- confidenza
- BPM
- valenza/arousal
- tonalità
- mood manuale
- preferenze app
- report settimanali
- cache Last.fm opzionale

La Privacy Policy è disponibile qui: [Music-Mood Privacy Policy](https://lucatrombetta090-sys.github.io/Music-Mood/privacy_policy).

---

## 🛠️ Stack tecnico

- Kotlin
- Android
- Media3
- Room
- WorkManager
- Material Components
- AppCompat per per-app language
- Chaquopy
- Python
- NumPy / SciPy / Mutagen
- YAMNet / TensorFlow Lite / MediaPipe Tasks Audio
- GitHub Actions
- Keystore stabile via GitHub Secrets

---

## 🚀 Build

Requisiti consigliati:

- Android Studio recente
- JDK 17
- Android SDK configurato
- Gradle compatibile con il progetto

Build debug:

```bash
./gradlew assembleDebug
```

---

## 📦 Stato attuale

La build include:

- libreria locale
- player e mini-player
- analisi mood
- YAMNet opzionale
- DSP fallback
- sorting avanzato
- BubbleMap
- statistiche
- profilo musicale
- calibrazione e reset calibrazione
- playlist automatiche mood-safe
- Weekly Mood Report
- Last.fm opzionale
- ricarica libreria da Impostazioni
- scelta lingua al primo avvio
- supporto Italiano/Inglese
- immagini condivisibili profilo/report
- signing stabile tramite GitHub Actions

---

## 🗺️ Roadmap

- completare verifica Play Store pre-release
- migliorare ulteriormente playlist e raccomandazioni
- rafforzare matching Last.fm/local library
- valutare release build/AAB per Play Console
- ampliare documentazione utente e screenshot store

---

## 👤 Autore

**Luca Trombetta**

---

## 📄 Licenza

MIT
