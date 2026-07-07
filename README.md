# Music-Mood

Music-Mood è un'app Android che analizza la libreria musicale locale e classifica ogni brano in mood musicali.

L'app combina analisi audio locale, statistiche personali, player integrato, report settimanali e integrazione opzionale con Last.fm.

## Funzionalità principali

- Libreria musicale locale con brani, artisti, album, generi, anni e cartelle
- Player musicale integrato con mini-player persistente
- Classificazione mood dei brani
- Supporto a mood manuale modificabile dall'utente
- Analisi audio locale tramite motore DSP
- Supporto opzionale a classificazione avanzata YAMNet
- Ordinamento per titolo, BPM, valenza e arousal
- BubbleMap interattiva nello spazio valenza/arousal
- Statistiche aggregate della libreria
- Profilo musicale personale
- Weekly Mood Report
- Integrazione opzionale Last.fm
- Export CSV
- Condivisione profilo e report

## Mood supportati

Music-Mood classifica i brani nei seguenti mood:

- Energico
- Positivo
- Aggressivo
- Malinconico
- Romantico
- Rilassato
- Nostalgico
- Concentrazione
- Festivo

## Motore di analisi

Il motore di analisi lavora principalmente sul dispositivo.

L'app può calcolare o salvare:

- mood stimato
- confidenza della classificazione
- valenza
- arousal
- BPM stimato
- tonalità musicale
- modalità maggiore o minore
- eventuale mood manuale impostato dall'utente

## YAMNet e DSP

Music-Mood può usare YAMNet come classificatore avanzato per il mood.

Il DSP resta importante per calcolare metriche tecniche musicali, come BPM e tonalità.

La logica corrente prevede che:

- YAMNet possa determinare il mood principale
- DSP possa completare le metriche tecniche
- BPM e tonalità non vengano sostituiti da valori fittizi se non disponibili

## Libreria e ordinamenti

Nella tab Brani sono disponibili ordinamenti per:

- A verso Z
- Z verso A
- BPM crescente
- BPM decrescente
- Valenza crescente
- Valenza decrescente
- Arousal crescente
- Arousal decrescente

Nelle viste aggregate, come artisti, album, generi, anni e cartelle, vengono mantenuti ordinamenti testuali per una navigazione più coerente.

## Copertine

Music-Mood mostra le copertine disponibili localmente e può recuperare copertine remote tramite servizi esterni usati dall'app.

Le copertine vengono mostrate in:

- libreria
- player
- mini-player

La cache delle copertine evita richieste ripetute quando possibile.

## Weekly Mood Report

Il Weekly Mood Report mostra una sintesi degli ascolti della settimana.

Il report può includere:

- numero di brani ascoltati
- mood dominante
- distribuzione mood
- confronto con report precedenti
- immagine condivisibile del report

Il report può essere rigenerato manualmente.

Quando viene rigenerato più volte nella stessa settimana, l'app mostra l'ultima versione generata.

## Integrazione Last.fm

L'integrazione Last.fm è opzionale.

Se configurata, l'app può importare ascolti recenti tramite API Last.fm.

Il refresh Last.fm è manuale e viene avviato tramite il pulsante Aggiorna Last.fm.

Music-Mood può usare i dati Last.fm per mostrare una sintesi nella schermata Weekly Report.

L'app salva localmente una cache del riepilogo Last.fm, così i dati restano visibili quando l'utente esce e rientra nella schermata.

Se non ci sono ascolti Last.fm nella settimana corrente, l'app può mostrare gli ultimi ascolti recenti come fallback informativo, senza usarli per alterare il report locale della settimana.

## Privacy

Music-Mood è progettata per funzionare principalmente in locale sul dispositivo.

I dati principali vengono conservati localmente, tra cui:

- libreria musicale letta dal dispositivo
- analisi mood
- report settimanali
- eventi di ascolto locali
- preferenze utente
- eventuale configurazione Last.fm
- cache del riepilogo Last.fm

Music-Mood non usa un backend proprietario per elaborare mood, report o statistiche personali nella configurazione attuale.

La Privacy Policy completa è disponibile nella schermata Info app e nel file:

PRIVACY_POLICY.md

## Permessi

L'app può richiedere permessi per leggere la libreria audio del dispositivo.

Questi permessi servono per:

- mostrare i brani locali
- riprodurre musica
- analizzare i brani
- generare statistiche
- generare report

## Stack tecnico

- Kotlin
- Android
- Media3
- Room
- WorkManager
- Material Components
- Chaquopy
- Python
- NumPy
- SciPy
- Mutagen
- YAMNet tramite MediaPipe Tasks Audio

## Build

Requisiti consigliati:

- Android Studio recente
- JDK 17
- Android SDK configurato
- Gradle compatibile con il progetto

Comando build debug:

```bash
./gradlew assembleDebug

Licenza
MIT

Autore
Luca Trombetta
