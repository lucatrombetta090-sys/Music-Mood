# Music-Mood — Bozza Google Play Data Safety

Data aggiornamento: 10 luglio 2026
Privacy Policy ufficiale: [Music-Mood Privacy Policy](https://lucatrombetta090-sys.github.io/Music-Mood/privacy_policy)
Questo documento è una bozza operativa per compilare la sezione **Data Safety** di Google Play Console.
Non è consulenza legale. Le dichiarazioni finali devono essere verificate rispetto alla build effettivamente pubblicata, alle librerie incluse, agli SDK attivi e alle funzionalità abilitate.

## 1. Panoramica app

Music-Mood è un'app Android che analizza la libreria musicale locale dell'utente, classifica i brani per mood e genera statistiche personali, BubbleMap, profilo musicale e Weekly Mood Report.
L'app funziona principalmente in locale sul dispositivo.
Funzionalità principali:
- lettura della libreria audio locale
- ricarica manuale della libreria da Impostazioni
- player musicale e mini-player
- analisi mood dei brani
- classificazione opzionale YAMNet / TensorFlow Lite
- analisi DSP locale
- metriche valenza, arousal, BPM e tonalità
- BubbleMap valenza/arousal
- statistiche aggregate
- profilo musicale personale
- calibrazione e reset calibrazione
- playlist automatiche mood-safe
- Weekly Mood Report
- integrazione opzionale Last.fm
- recupero copertine album da iTunes/Deezer
- export CSV
- condivisione manuale di immagini profilo/report
- supporto lingua Italiano/Inglese

## 2. Privacy Policy URL

Da inserire in Google Play Console:
[Music-Mood Privacy Policy](https://lucatrombetta090-sys.github.io/Music-Mood/privacy_policy)

## 3. Raccolta dati: impostazione generale

Risposta consigliata prudenziale:
**L'app raccoglie o condivide alcuni tipi di dati?**
**Sì**, per due categorie di funzionalità distinte:
- se nella build pubblicata è inclusa e configurabile l'integrazione Last.fm, perché l'app può inviare username, API key e parametri necessari verso l'API Last.fm quando l'utente preme manualmente **Aggiorna Last.fm**
- se nella build pubblicata è attivo il recupero delle copertine album, perché l'app invia titolo e artista del brano ad Apple iTunes Search API e, come fallback, a Deezer Search API

**No**, per i dati che restano esclusivamente locali sul dispositivo e non vengono inviati a server dello sviluppatore o a terze parti.

Nota importante: Google Play richiede che il Data Safety form rifletta in modo accurato raccolta, condivisione e gestione dei dati dell'app, inclusi eventuali SDK o terze parti usate dall'app.

## 4. Dati locali trattati ma non inviati a backend proprietario

Questi dati possono essere usati localmente dall'app:
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
- modalità maggiore/minore, se disponibile
- mood manuale impostato dall'utente
- eventi di ascolto locali
- Weekly Report locale
- profilo musicale personale
- fingerprint emotivo
- dati di calibrazione
- preferenze utente
- lingua scelta dall'utente

Questi dati sono conservati localmente nel dispositivo e sono usati per funzionalità dell'app.

## 5. Ricarica libreria

L'app include un pulsante **Ricarica libreria** nelle Impostazioni.
La funzione esegue una nuova lettura della libreria audio locale tramite MediaStore per rilevare nuovi brani aggiunti dopo l'installazione.
La funzione non ha lo scopo di cancellare:
- analisi mood esistenti
- mood manuali
- cache Last.fm
- report settimanali
- dati di calibrazione

Data Safety: questa funzione non introduce una nuova categoria di dati rispetto all'accesso alla libreria audio locale. Rafforza solo l'esigenza di dichiarare correttamente l'accesso ai file/audio locali se richiesto dalla Play Console.

## 6. Analisi mood, YAMNet, DSP, valenza e arousal

L'app può analizzare localmente i brani per stimare:
- mood
- confidenza
- valenza
- arousal
- BPM
- tonalità
- modo maggiore/minore

L'app può usare:
- motore DSP locale
- classificatore opzionale YAMNet/TensorFlow Lite
- calibrazione personalizzata
- mood manuale impostato dall'utente

I file audio non vengono caricati su un backend proprietario nella configurazione attuale.
Data Safety: non dichiarare upload di audio se la build pubblicata analizza i file esclusivamente sul dispositivo e non li invia a server esterni.

## 7. Integrazione opzionale Last.fm

L'integrazione Last.fm è opzionale.
Se l'utente configura Last.fm, l'app può salvare localmente:
- username Last.fm
- API key Last.fm (in forma cifrata tramite Android EncryptedSharedPreferences)
- riepilogo locale Last.fm
- cache locale del riepilogo Last.fm

Quando l'utente preme **Aggiorna Last.fm**, l'app può chiamare l'API Last.fm.
Dati Last.fm recuperati o trattati dall'app:
- titolo del brano
- artista
- album
- timestamp di ascolto, se disponibile
- URL Last.fm del brano, se disponibile
- immagine o copertina, se disponibile
- stato now playing, se disponibile
- tag musicali, se usati dalla classificazione

Dati potenzialmente inviati a Last.fm:
- username Last.fm
- API key Last.fm
- parametri necessari alla richiesta API

Data Safety: dichiarare prudenzialmente la condivisione verso Last.fm se l'integrazione è inclusa nella build pubblicata e utilizzabile dall'utente.

## 7-bis. Recupero copertine album da Apple iTunes e Deezer

Music-Mood include una funzione automatica di recupero copertine album da servizi esterni pubblici quando la copertina non è disponibile localmente nei metadati del brano.

Servizi contattati:
- **Apple iTunes Search API** (endpoint pubblico https://itunes.apple.com/search), usato come fonte primaria
- **Deezer Search API** (endpoint pubblico https://api.deezer.com/search/track), usato come fonte di fallback

Dati inviati a questi servizi:
- titolo del brano
- nome dell'artista

Dati **non** inviati:
- API key personali, non richieste da questi endpoint pubblici
- identificativi dispositivo
- identificativi pubblicitari
- mood, valenza, arousal, BPM, tonalità
- dati di ascolto
- credenziali Last.fm
- lingua utente
- preferenze utente

Dati ricevuti da iTunes/Deezer:
- URL dell'immagine di copertina
- (opzionalmente) altri metadati del brano, ignorati dall'app

Persistenza locale:
- l'URL della copertina risolto viene conservato in cache Room locale per evitare richieste ripetute per lo stesso brano
- la cache può essere cancellata dall'utente svuotando i dati dell'app

Trasporto:
- richieste effettuate tramite HTTPS

Iniziativa della chiamata:
- automatica, quando l'utente scorre la libreria/player e serve una copertina non presente nei metadati locali
- disabilitabile implicitamente lavorando in modalità offline

Data Safety: dichiarare prudenzialmente la condivisione verso Apple iTunes e Deezer perché l'app trasmette titolo e artista del brano a questi servizi esterni.

## 8. Data types — bozza Play Console

### Personal info

Possibile dichiarazione prudenziale:
- **User IDs**
Motivo:
- username Last.fm, se configurato dall'utente

Purpose:
- App functionality
- Personalization, se usato per mostrare report o riepiloghi personalizzati

Optional?
- Sì, perché Last.fm è opzionale

Shared?
- Sì, prudenzialmente, verso Last.fm quando l'utente usa manualmente Aggiorna Last.fm

Encrypted in transit?
- Sì, se le chiamate avvengono tramite HTTPS

Note:
- L'app non vende questi dati.
- L'app non usa backend proprietario per questi dati.

### App activity

Possibile dichiarazione prudenziale:
- Other actions oppure altra categoria equivalente, se Play Console richiede di dichiarare eventi di ascolto, report, interazioni o attività musicali.

Purpose:
- App functionality
- Personalization

Optional?
- Last.fm: sì
- Eventi locali necessari al Weekly Report: dipende da come la funzionalità è presentata nella build pubblicata

Shared?
- Eventi locali: no, se restano sul dispositivo
- Last.fm: sì, prudenzialmente, solo per chiamate Last.fm avviate dall'utente

### Audio

Possibile impostazione:
- Non dichiarare come collected/uploaded se i file audio e la libreria restano esclusivamente sul dispositivo e non sono inviati a server esterni.

Nota:
- L'app richiede accesso alla libreria audio per funzionare.
- L'app analizza localmente le caratteristiche musicali.
- L'app non carica file audio su backend proprietario nella configurazione attuale.
- L'app trasmette a Apple iTunes e Deezer solo metadati testuali (titolo e artista), non contenuto audio.

### Music files and info

Possibile dichiarazione prudenziale:
- **Sì**, se Play Console distingue specificamente i metadati musicali (titolo brano, artista, album) come categoria separata

Motivo:
- titolo brano e artista vengono inviati a Apple iTunes Search API e Deezer Search API per recuperare le copertine album

Purpose:
- App functionality (visualizzazione copertine album)

Optional?
- No dichiarato, la funzione è attiva di default; disabilitabile solo lavorando in modalità offline

Shared?
- Sì, verso Apple iTunes Search API e Deezer Search API

Encrypted in transit?
- Sì, tramite HTTPS

Note:
- L'app non invia audio, mood, valenza, arousal o altri dati personali a questi servizi.
- L'app invia solo titolo e artista del brano.

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
- altri SDK analytics o advertising
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
- Se Play Console distingue specificamente audio/media da documenti generici, usare la voce più specifica relativa ad audio/media.
- Non dichiarare documenti generici se l'app non legge documenti dell'utente.

## 9. Security practices

### Is all user data collected by your app encrypted in transit?

Risposta consigliata:
- Sì, per le chiamate Last.fm, iTunes e Deezer, tutte effettuate tramite HTTPS.

### Do you provide a way for users to request that their data is deleted?

Risposta prudenziale:
- Sì, se la Privacy Policy indica che l'utente può cancellare i dati app dalle impostazioni Android o rimuovere configurazioni locali.

Funzioni disponibili o rilevanti:
- rimozione configurazione Last.fm
- rimozione cache Last.fm
- cancellazione analisi mood locali, se disponibile nella build
- reset calibrazione
- svuotamento cache copertine album (via cancellazione dati app)
- cancellazione dati app dalle impostazioni Android
- disinstallazione app

Suggerimento futuro:
- se Play Console richiede un meccanismo più esplicito, valutare un pulsante unico "Cancella dati app locali" o una sezione dedicata alla cancellazione dei dati locali.

### Are user credentials encrypted at rest?

Risposta consigliata:
- Sì. L'API key Last.fm è conservata in `EncryptedSharedPreferences` tramite Android Keystore (AES256-GCM).

## 10. Data sharing

Possibile dichiarazione:
Music-Mood non vende dati utente.

L'app trasferisce dati a servizi esterni nei seguenti casi:

1. **Last.fm** — solo quando l'utente configura Last.fm e preme manualmente **Aggiorna Last.fm**.
   Dati potenzialmente trasferiti:
   - username Last.fm
   - API key Last.fm
   - parametri necessari alla richiesta API
   
   Dati ricevuti:
   - ascolti recenti
   - metadati brani
   - immagini/copertine, se disponibili
   - tag musicali, se richiesti dalla funzione di classificazione

2. **Apple iTunes Search API** — automaticamente, quando serve recuperare la copertina di un brano non presente nei metadati locali.
   Dati trasferiti:
   - titolo del brano
   - nome dell'artista
   
   Dati ricevuti:
   - URL della copertina album

3. **Deezer Search API** — automaticamente, come fallback quando iTunes non restituisce risultati.
   Dati trasferiti:
   - titolo del brano
   - nome dell'artista
   
   Dati ricevuti:
   - URL della copertina album

## 11. Retention and deletion

Dati conservati localmente:
- analisi mood
- eventi di ascolto
- report settimanali
- preferenze
- lingua selezionata
- configurazione Last.fm
- cache riepilogo Last.fm
- cache URL copertine album da iTunes/Deezer
- dati di calibrazione
- dati aggregati del profilo musicale

Durata:
- fino a cancellazione dati app
- fino a disinstallazione
- fino a rimozione manuale, se la funzione è disponibile

## 12. Checklist prima della pubblicazione

- Privacy Policy online accessibile
- Privacy Policy linkata dentro app
- Repository tecnico privato, se necessario
- Repository pubblico solo documentale, se scelto
- Data Safety coerente con la build
- Nessun SDK Ads se dichiari no ads
- Nessun SDK Analytics se dichiari no analytics
- Nessun SDK che legge identificativi dispositivo se dichiari no Device IDs
- Last.fm opzionale e manuale
- API key Last.fm salvata localmente in forma cifrata
- Cache Last.fm locale
- iTunes/Deezer dichiarati come destinatari esterni per il recupero copertine
- Cache URL copertine locale
- File audio non caricati su server
- Analisi mood eseguita localmente
- YAMNet/TFLite eseguito localmente
- Report locale indipendente dal fallback Last.fm
- Permessi Android coerenti con funzionalità audio
- Email di contatto privacy compilata nella policy
- Privacy Policy italiana e inglese coerenti
- README/CHANGELOG aggiornati

## 13. Risposte sintetiche consigliate

### Privacy Policy

[Music-Mood Privacy Policy](https://lucatrombetta090-sys.github.io/Music-Mood/privacy_policy)

### Ads

No, se non hai SDK pubblicitari.

### Data collected

Sì, prudentemente per:
- username/API key Last.fm e dati Last.fm opzionali, se l'integrazione è nella build pubblicata
- titolo brano e artista, inviati a Apple iTunes e Deezer per recupero copertine album

### Data shared

Sì, prudentemente verso:
- Last.fm, solo quando l'utente usa l'integrazione opzionale
- Apple iTunes Search API, automaticamente per recupero copertine
- Deezer Search API, automaticamente come fallback per recupero copertine

### Data encrypted in transit

Sì, per tutte le chiamate esterne (Last.fm, iTunes, Deezer) via HTTPS.

### User deletion

Sì, tramite cancellazione dati app, rimozione configurazioni locali e funzionalità disponibili nell'app.

### Purpose

- App functionality
- Personalization

### Optional data

Sì per Last.fm.

### Required data

Accesso alla libreria audio locale necessario per le funzionalità principali dell'app.
Recupero copertine album da servizi esterni attivo di default, disabilitabile implicitamente lavorando in modalità offline.
