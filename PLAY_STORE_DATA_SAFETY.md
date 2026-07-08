# Music-Mood — Google Play Data Safety Draft

Data aggiornamento: 8 luglio 2026

Privacy Policy ufficiale:

https://lucatrombetta090-sys.github.io/Music-Mood/privacy_policy

Questo documento è una bozza operativa per compilare la sezione Data Safety di Google Play Console.

Non è consulenza legale. Le dichiarazioni finali devono essere verificate rispetto alla build pubblicata, alle librerie effettivamente incluse e alle funzionalità abilitate.

## 1. Panoramica app

Music-Mood è un'app Android che analizza la libreria musicale locale dell'utente, classifica i brani per mood e genera statistiche personali e Weekly Mood Report.

L'app funziona principalmente in locale sul dispositivo.

Funzionalità principali:

- lettura libreria audio locale
- analisi mood dei brani
- player musicale
- statistiche
- BubbleMap
- profilo musicale
- Weekly Mood Report
- integrazione opzionale Last.fm
- export CSV
- condivisione manuale di immagini/report

## 2. Privacy Policy URL

Da inserire in Google Play Console:

https://lucatrombetta090-sys.github.io/Music-Mood/privacy_policy

## 3. Raccolta dati: impostazione generale

Risposta consigliata prudenziale:

L'app raccoglie o condivide alcuni tipi di dati?

Sì, se nella build pubblicata è inclusa e configurabile l'integrazione Last.fm, perché l'app può inviare username e API key verso l'API Last.fm quando l'utente preme manualmente Aggiorna Last.fm.

No, per i dati che restano esclusivamente locali sul dispositivo e non vengono inviati a un server dello sviluppatore o a terze parti.

## 4. Dati locali trattati ma non inviati a backend proprietario

Questi dati sono usati localmente dall'app:

- titolo brano
- artista
- album
- durata
- genere, se disponibile
- anno, se disponibile
- cartella o percorso locale
- copertina locale
- identificativo locale del brano
- mood stimato
- confidenza
- BPM
- valenza
- arousal
- tonalità
- mood manuale impostato dall'utente
- eventi di ascolto locali
- Weekly Report locale
- preferenze utente

Questi dati sono conservati localmente nel dispositivo e sono usati per funzionalità dell'app.

## 5. Integrazione opzionale Last.fm

L'integrazione Last.fm è opzionale.

Se l'utente configura Last.fm, l'app può salvare localmente:

- username Last.fm
- API key Last.fm
- riepilogo locale Last.fm
- cache locale del riepilogo Last.fm

Quando l'utente preme Aggiorna Last.fm, l'app può chiamare l'API Last.fm.

Dati Last.fm recuperati o trattati dall'app:

- titolo del brano
- artista
- album
- timestamp di ascolto, se disponibile
- URL Last.fm del brano, se disponibile
- immagine o copertina, se disponibile
- stato now playing, se disponibile

## 6. Data types — bozza Play Console

### Personal info

Possibile dichiarazione prudenziale:

- User IDs

Motivo:

- username Last.fm, se configurato dall'utente

Purpose:

- App functionality

Optional?

- Sì, perché Last.fm è opzionale

Shared?

- Sì, prudenzialmente, verso Last.fm quando l'utente usa il pulsante Aggiorna Last.fm

Encrypted in transit?

- Sì, se le chiamate avvengono tramite HTTPS

Note:

- L'app non vende questi dati.
- L'app non usa backend proprietario per questi dati.

### App activity

Possibile dichiarazione prudenziale:

- Other user-generated content oppure Other actions, se Play Console richiede una categoria per gli ascolti recenti Last.fm o gli eventi di ascolto

Purpose:

- App functionality
- Personalization

Optional?

- Per Last.fm: sì
- Per eventi locali necessari al Weekly Report: dipende dalla funzionalità usata dall'utente

Shared?

- Eventi locali: no, se restano sul dispositivo
- Last.fm: sì, prudenzialmente, solo per la chiamata Last.fm avviata dall'utente

### Audio

Possibile impostazione:

- Non dichiarare come collected se i file audio e la libreria restano esclusivamente sul dispositivo e non sono inviati a server esterni.

Nota:

- L'app richiede accesso alla libreria audio per funzionare.
- L'app analizza localmente le caratteristiche musicali.
- L'app non carica i file audio su backend proprietario nella configurazione attuale.

### Photos and videos

Possibile impostazione:

- Non dichiarare, se l'app non accede alla galleria foto/video.
- Le copertine album fanno parte dei metadati musicali o vengono caricate come immagini associate ai brani, non come raccolta della galleria foto dell'utente.

### Device or other IDs

Possibile impostazione:

- Non dichiarare, se la build non include SDK analytics/ads/crash reporting che leggono identificativi dispositivo.

Da verificare prima della pubblicazione:

- Firebase Analytics
- Crashlytics
- AdMob
- altri SDK di analytics o advertising
- SDK che leggono Android ID o advertising ID

### Location

Impostazione consigliata:

- No, se l'app non accede alla posizione.

### Contacts

Impostazione consigliata:

- No, se l'app non accede ai contatti.

### Financial info

Impostazione consigliata:

- No, se l'app non gestisce pagamenti o dati finanziari.

### Health and fitness

Impostazione consigliata:

- No, se l'app non tratta dati salute/fitness.

### Messages

Impostazione consigliata:

- No, se l'app non accede a SMS, email o messaggi.

### Files and docs

Possibile riflessione:

- L'app accede ai file audio tramite permessi media/audio.
- Se Play Console distingue specificamente file audio da documenti generici, usare la voce più specifica relativa ad audio/media.
- Non dichiarare documenti generici se l'app non legge documenti dell'utente.

## 7. Security practices

Risposte consigliate:

### Is all user data collected by your app encrypted in transit?

Sì, per le chiamate Last.fm, se effettuate tramite HTTPS.

### Do you provide a way for users to request that their data is deleted?

Risposta prudenziale:

Sì, se la privacy policy indica che l'utente può cancellare i dati app dalle impostazioni Android o rimuovere configurazioni locali.

Valutazione:

- Se Play Console richiede un meccanismo specifico dentro l'app, aggiungere in futuro un pulsante per cancellare dati Last.fm e cache locale.
- Attualmente è già possibile cancellare dati app via impostazioni Android.
- L'app dovrebbe prevedere o mantenere funzioni per cancellare analisi locali, ove disponibili.

## 8. Data sharing

Possibile dichiarazione:

L'app non vende dati utente.

L'app può trasferire dati a Last.fm solo quando l'utente configura Last.fm e preme manualmente Aggiorna Last.fm.

Dati potenzialmente trasferiti a Last.fm:

- username Last.fm
- API key Last.fm
- parametri necessari alla richiesta API

Dati ricevuti da Last.fm:

- ascolti recenti
- metadati brani
- immagini/copertine, se disponibili
- tag musicali, se richiesti dalla funzione di classificazione

## 9. Retention and deletion

Dati conservati localmente:

- analisi mood
- eventi di ascolto
- report settimanali
- preferenze
- configurazione Last.fm
- cache riepilogo Last.fm

Durata:

- fino a cancellazione dati app
- fino a disinstallazione
- fino a rimozione manuale, se la funzione è disponibile

Suggerimento futuro:

- aggiungere un pulsante dedicato in Settings:
  - Cancella cache Last.fm
  - Rimuovi configurazione Last.fm
  - Cancella Weekly Report
  - Cancella eventi di ascolto

## 10. Checklist prima della pubblicazione

- Privacy Policy online accessibile
- Privacy Policy linkata dentro app
- Repository tecnico privato
- Repository pubblico solo documentale
- Data Safety coerente con la build
- Nessun SDK Ads se dichiari no ads
- Nessun SDK Analytics se dichiari no analytics
- Last.fm opzionale e manuale
- API key Last.fm salvata localmente
- Cache Last.fm locale
- File audio non caricati su server
- Report locale indipendente da fallback Last.fm
- Permessi Android coerenti con funzionalità audio
- Email di contatto privacy compilata nella policy

## 11. Risposte sintetiche consigliate

Privacy Policy:

https://lucatrombetta090-sys.github.io/Music-Mood/privacy_policy

Ads:

No, se non hai SDK pubblicitari.

Data collected:

Sì, prudentemente per username/API key Last.fm e dati Last.fm opzionali, se l'integrazione è nella build pubblicata.

Data shared:

Sì, prudentemente verso Last.fm, solo quando l'utente usa l'integrazione opzionale.

Data encrypted in transit:

Sì, per chiamate Last.fm via HTTPS.

User deletion:

Sì, tramite cancellazione dati app e funzionalità locali disponibili. Valutare aggiunta di pulsante dedicato per maggiore chiarezza.

Purpose:

- App functionality
- Personalization

Optional data:

Sì per Last.fm.

Required data:

Accesso alla libreria audio locale necessario per le funzionalità principali dell'app.
`
