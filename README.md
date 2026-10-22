# 🎵 Music-Mood

**Music-Mood** è un'app Android che analizza la libreria musicale locale e trasforma ogni brano in una piccola mappa emotiva: mood, BPM, tonalità, valenza, arousal, profilo musicale personale, BubbleMap e Weekly Mood Report.

L'obiettivo è semplice: **ascoltare la tua musica in modo più intelligente, personale e visivo**.

## ✨ Perché installarla

Music-Mood non è solo un player musicale. È un'app per scoprire **come suona emotivamente la tua libreria**.

Con Music-Mood puoi:

- 🎧 esplorare i tuoi brani locali
- 🧠 classificare i brani in 9 mood musicali, con un mood secondario quando il brano ne ha due
- ℹ️ capire **perché** un brano ha quel mood
- 🤖 usare YAMNet come classificatore avanzato
- 📍 visualizzare i brani nella BubbleMap valenza/arousal
- 📊 generare statistiche aggregate ed esportarle in CSV
- 🧬 costruire il tuo profilo musicale personale
- 🎯 calibrare il classificatore sui tuoi gusti
- 📆 creare un Weekly Mood Report
- 🖼️ condividere immagini del profilo e del report
- 🌍 usare l'app in italiano, inglese o spagnolo
- 🔄 ricaricare e rianalizzare la libreria senza perdere le tue scelte

## 🧠 Il cuore dell'app: mood, valenza e arousal

Music-Mood interpreta ogni brano usando un modello emozionale bidimensionale basato su **valenza** e **arousal**.

### 💜 Valenza

La **valenza** indica la qualità edonica di uno stimolo musicale: quanto un brano tende a essere percepito come positivo, piacevole, luminoso oppure negativo, cupo, malinconico.

In Music-Mood la valenza viene rappresentata come un asse continuo:

```
negativo  ─────────────── positivo
```

### ⚡ Arousal

L'**arousal** rappresenta il livello di attivazione o intensità: quanto un brano è calmo, rilassato, morbido oppure energico, intenso, attivante.

In Music-Mood l'arousal viene rappresentato come un asse continuo:

```
calmo  ─────────────── intenso
```

### 🫧 BubbleMap

La **BubbleMap** usa questi due assi per posizionare i brani in uno spazio emotivo continuo:

```
                    Arousal alto
                         ↑
       intenso/scuro     │   positivo/intenso
                         │
Valenza negativa  ───────┼───────  Valenza positiva
                         │
        triste/calmo     │   positivo/calmo
                         ↓
                    Arousal basso
```

Esempi intuitivi:

- 😔 un brano malinconico tende ad avere **valenza negativa** e **arousal basso**
- 🔥 un brano aggressivo tende ad avere **valenza negativa** e **arousal alto**
- ☀️ un brano positivo tende ad avere **valenza positiva**
- 🌿 un brano rilassato tende ad avere **arousal basso**

Questa impostazione si ispira al modello circomplesso delle emozioni, in cui gli stati emotivi possono essere descritti come combinazione di valenza e arousal. Approfondimenti utili: [Capire le emozioni per vivere meglio - Psicologia Contemporanea](https://www.psicologiacontemporanea.it/blog/capire-le-emozioni-vivere-meglio/), [Arousal - State of Mind](https://www.stateofmind.it/arousal/) e [Circumplex Model of Affect: Valence and Arousal Explained](https://psychologyfanatic.com/circumplex-model-of-arousal-and-valence/).

## 🤖 Classificazione avanzata con YAMNet

Music-Mood integra un approccio ibrido:

- **YAMNet** per supportare la classificazione audio avanzata
- **DSP locale** per BPM, tonalità, valenza, arousal e fallback
- **regole musicali** per i casi difficili: i brani dance non finiscono tra i mood calmi e le ballate lente non vengono lette come energiche
- **tag Last.fm** come secondo parere opzionale
- **calibrazione personalizzata** per adattare il modello alla tua libreria
- **mood manuale** per correggere il risultato quando vuoi decidere tu

YAMNet è una rete neurale pre-addestrata per la classificazione audio. La documentazione TensorFlow descrive YAMNet come un modello in grado di predire 521 classi audio basate sul corpus AudioSet e costruito su architettura MobileNetV1 depthwise-separable convolution. Vedi: [Sound classification with YAMNet - TensorFlow Hub](https://www.tensorflow.org/hub/tutorials/yamnet) e [YAMNet README - TensorFlow Models](https://github.com/tensorflow/models/tree/master/research/audioset/yamnet).

In Music-Mood YAMNet è una parte del motore di classificazione, non l'unica fonte di verità.

## 🎭 I 9 mood supportati

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

## 📱 Funzionalità principali

### 🎧 Libreria musicale locale

- lettura della libreria audio locale
- sezioni per brani, artisti, album, generi, anni e cartelle
- navigazione per cartelle
- ordinamento per titolo, BPM, valenza e arousal
- indice rapido laterale che segue l'ordinamento: lettere, fasce di BPM, valenza o energia
- ricarica manuale della libreria da Impostazioni quando aggiungi nuovi brani

### ▶️ Player e mini-player

- player integrato full-screen
- mini-player persistente
- artwork locale/remoto
- tocco sulla notifica per tornare al brano in riproduzione
- pausa automatica quando parte un altro audio (chiamata, video, altra app) e quando stacchi le cuffie
- menu contestuale per analisi mood e modifica mood manuale

### 🧠 Analisi mood

- classificazione automatica del mood
- mood secondario, quando il brano ne ha due vicini
- valenza e arousal
- BPM stimato
- tonalità e modo maggiore/minore
- supporto a YAMNet e DSP fallback
- **"Perché questo mood?"**: per ogni brano mostra i primi 3 mood in percentuale, i fattori audio che hanno pesato, la posizione nella BubbleMap e l'effetto di Last.fm

### 🏷️ Mood manuale

Puoi correggere un brano manualmente. **Il mood scelto da te ha sempre la precedenza**: statistiche, profilo, BubbleMap e playlist automatiche lo usano subito. Rimuovendo la correzione torni al mood calcolato.

Priorità logica:

1. mood scelto manualmente
2. mood calcolato dall'audio, eventualmente corretto da Last.fm
3. fallback

### 🔄 Rianalisi della libreria

Quando il motore di analisi migliora, l'app propone di rianalizzare la libreria. Puoi farlo subito con **"Aggiorna ora"** o più tardi, anche da Impostazioni con **"Rianalizza la libreria"**.

La rianalisi rifà solo la parte audio: **mood scelti da te e tag Last.fm restano salvati**.

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
- export CSV con mood, mood secondario, mood manuale, punteggi e stato Last.fm

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
- ascolti recenti nel Weekly Report, con aggiornamento manuale
- **tag Last.fm come secondo parere sul mood**: l'app legge i tag della community per ogni brano
  - Last.fm conta solo se il suo mood è già tra i primi 3 indicati dall'audio
  - il suo effetto è limitato: al massimo 6 punti percentuali
  - puoi **interrompere e riprendere** la lettura da Impostazioni o dalla notifica; il lavoro fatto resta salvato
  - i brani nuovi vengono letti automaticamente
  - l'opzione "Usa i tag di Last.fm" si può disattivare in qualsiasi momento

### 🌍 Multilingua

Music-Mood supporta:

- 🇮🇹 Italiano
- 🇬🇧 English
- 🇪🇸 Español

Al primo avvio l'app mostra la scelta lingua **prima della richiesta dei permessi**. Il default è l'italiano.

## 🎯 Calibrazione personalizzata

Music-Mood può adattare il classificatore ai tuoi gusti musicali.

La calibrazione usa la libreria già analizzata per spostare i centroidi del classificatore rispetto al tuo profilo medio.

- 0.0 = nessuna calibrazione
- 0.5 = calibrazione bilanciata
- 1.0 = massima adattabilità

Il **reset della calibrazione** riporta ogni brano al mood originale dell'analisi e aggiorna statistiche, fingerprint, radar, top mood e archetipo. I mood scelti da te restano invariati.

La calibrazione è legata al dispositivo: non viene ripristinata dal backup Android dopo una reinstallazione.

## 🎼 Playlist automatiche mood-safe

Ogni preset usa mood ammessi, mood bloccati e filtri tecnici su arousal, BPM e valenza.

Esempi:

- 🌙 Sleep/Relax esclude Energico, Festivo e Aggressivo
- ⚡ Energy privilegia Energico, Festivo e Positivo
- 🎯 Focus privilegia Concentrazione e Rilassato con arousal contenuto
- 💪 Workout privilegia alta energia, BPM alto o arousal elevato

Se non ci sono abbastanza brani coerenti, il fallback resta comunque limitato ai mood compatibili. Le playlist rispettano i mood scelti da te.

## 🔐 Privacy e dati locali

Music-Mood è progettata per lavorare principalmente sul dispositivo: l'analisi audio avviene in locale e non esiste un backend proprietario.

Gli unici servizi esterni contattati sono:

- **Last.fm**, solo se lo configuri tu
- **Apple iTunes** e **Deezer**, per le copertine mancanti (solo titolo e artista)

La Privacy Policy è disponibile qui: [Music-Mood Privacy Policy](https://lucatrombetta090-sys.github.io/Music-Mood/privacy_policy).

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
- NumPy / Mutagen
- YAMNet / TensorFlow Lite / MediaPipe Tasks Audio
- GitHub Actions
- Keystore stabile via GitHub Secrets

## 🚀 Build

Requisiti consigliati:

- Android Studio recente
- JDK 17
- Android SDK configurato
- Gradle compatibile con il progetto

Build debug:

```
./gradlew assembleDebug
```

## 📦 Stato attuale (1.0.1)

La build include:

- libreria locale con indice rapido
- player e mini-player con gestione dell'audio focus
- analisi mood con YAMNet e DSP
- mood secondario e "Perché questo mood?"
- mood manuale con priorità su tutto
- tag Last.fm come secondo parere, con Interrompi/Riprendi
- rianalisi che conserva le scelte dell'utente
- BubbleMap
- statistiche ed export CSV
- profilo musicale
- calibrazione e reset calibrazione
- playlist automatiche mood-safe
- Weekly Mood Report
- supporto italiano, inglese e spagnolo
- immagini condivisibili profilo/report
- signing stabile tramite GitHub Actions

## 🗺️ Roadmap

- migliorare ulteriormente playlist e raccomandazioni
- affinare la stima del BPM sulle ballate
- rafforzare il matching Last.fm/libreria locale
- ampliare documentazione utente e screenshot store

## 👤 Autore

**Luca Trombetta**

## 📄 Licenza

MIT
