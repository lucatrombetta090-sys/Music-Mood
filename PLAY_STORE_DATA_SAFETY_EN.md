# Music-Mood — Google Play Data Safety Draft

Last updated: July 10, 2026
Official Privacy Policy: [Music-Mood Privacy Policy](https://lucatrombetta090-sys.github.io/Music-Mood/privacy_policy)
This document is an operational draft for completing the **Data Safety** section in Google Play Console.
This is not legal advice. Final declarations must be verified against the actually published build, included libraries, active SDKs and enabled features.

## 1. App overview

Music-Mood is an Android app that analyzes the user's local music library, classifies songs by mood and generates personal statistics, BubbleMap, music profile and Weekly Mood Report.
The app works mainly locally on the device.
Main features:
- local audio library access
- manual library refresh from Settings
- music player and mini-player
- song mood analysis
- optional YAMNet / TensorFlow Lite classification
- local DSP analysis
- valence, arousal, BPM and key metrics
- valence/arousal BubbleMap
- aggregate statistics
- personal music profile
- calibration and reset calibration
- mood-safe automatic playlists
- Weekly Mood Report
- optional Last.fm integration
- album artwork retrieval from iTunes/Deezer
- CSV export
- manual sharing of profile/report images
- Italian/English language support

## 2. Privacy Policy URL

To be entered in Google Play Console:
[Music-Mood Privacy Policy](https://lucatrombetta090-sys.github.io/Music-Mood/privacy_policy)

## 3. Data collection: general setting

Recommended cautious answer:
**Does the app collect or share any required user data types?**
**Yes**, for two distinct feature categories:
- if the published build includes configurable Last.fm integration, because the app may send username, API key and required request parameters to the Last.fm API when the user manually presses **Refresh Last.fm**
- if the published build has album artwork retrieval enabled, because the app sends song title and artist to the Apple iTunes Search API and, as a fallback, to the Deezer Search API

**No**, for data that remains exclusively local on the device and is not sent to the developer's server or third parties.

Important note: Google Play requires the Data Safety form to accurately reflect the app's data collection, sharing and handling practices, including any third-party SDKs or services used by the app.

## 4. Local data processed but not sent to a proprietary backend

The following data may be used locally by the app:
- song title
- artist
- album
- duration
- genre, if available
- year, if available
- local folder or path
- local artwork
- local song identifier
- estimated mood
- confidence
- BPM
- valence
- arousal
- key
- major/minor mode, if available
- manual mood selected by the user
- local listening events
- local Weekly Report
- personal music profile
- emotional fingerprint
- calibration data
- user preferences
- selected app language

This data is stored locally on the device and is used for app functionality.

## 5. Library refresh

The app includes a **Refresh library** button in Settings.
This feature performs a new scan of the local audio library through MediaStore to detect songs added after installation.
This feature is not intended to delete:
- existing mood analysis
- manual moods
- Last.fm cache
- weekly reports
- calibration data

Data Safety: this feature does not introduce a new data category compared with local audio library access. It reinforces the need to correctly disclose access to local audio/media files if required by Play Console.

## 6. Mood analysis, YAMNet, DSP, valence and arousal

The app may locally analyze songs to estimate:
- mood
- confidence
- valence
- arousal
- BPM
- key
- major/minor mode

The app may use:
- local DSP engine
- optional YAMNet/TensorFlow Lite classifier
- personalized calibration
- manual mood selected by the user

Audio files are not uploaded to a proprietary backend in the current configuration.
Data Safety: do not declare audio upload if the published build analyzes files exclusively on-device and does not send them to external servers.

## 7. Optional Last.fm integration

Last.fm integration is optional.
If the user configures Last.fm, the app may locally store:
- Last.fm username
- Last.fm API key (encrypted through Android EncryptedSharedPreferences)
- local Last.fm summary
- local Last.fm summary cache

When the user presses **Refresh Last.fm**, the app may call the Last.fm API.
Last.fm data retrieved or processed by the app:
- track title
- artist
- album
- listening timestamp, if available
- Last.fm track URL, if available
- image or artwork, if available
- now playing status, if available
- music tags, if used by classification

Data potentially sent to Last.fm:
- Last.fm username
- Last.fm API key
- request parameters required by the API

Data Safety: cautiously declare sharing with Last.fm if the integration is included in the published build and available to the user.

## 7-bis. Album artwork retrieval from Apple iTunes and Deezer

Music-Mood includes an automatic album artwork retrieval feature from public external services when artwork is not available locally in the song metadata.

Services contacted:
- **Apple iTunes Search API** (public endpoint https://itunes.apple.com/search), used as the primary source
- **Deezer Search API** (public endpoint https://api.deezer.com/search/track), used as fallback source

Data sent to these services:
- song title
- artist name

Data **not** sent:
- personal API keys, not required by these public endpoints
- device identifiers
- advertising identifiers
- mood, valence, arousal, BPM, key
- listening data
- Last.fm credentials
- user language
- user preferences

Data received from iTunes/Deezer:
- album artwork image URL
- (optionally) other song metadata, ignored by the app

Local persistence:
- the resolved artwork URL is stored in a local Room cache to avoid repeated requests for the same song
- the cache can be cleared by the user by clearing app data

Transport:
- requests made over HTTPS

Call initiation:
- automatic, when the user scrolls through library/player and artwork not present in local metadata is needed
- can be implicitly disabled by working in offline mode

Data Safety: cautiously declare sharing with Apple iTunes and Deezer because the app transmits song title and artist to these external services.

## 8. Data types — Play Console draft

### Personal info

Possible cautious declaration:
- **User IDs**
Reason:
- Last.fm username, if configured by the user

Purpose:
- App functionality
- Personalization, if used to show personalized reports or summaries

Optional?
- Yes, because Last.fm is optional

Shared?
- Yes, cautiously, with Last.fm when the user manually uses Refresh Last.fm

Encrypted in transit?
- Yes, if calls are made over HTTPS

Notes:
- The app does not sell this data.
- The app does not use a proprietary backend for this data.

### App activity

Possible cautious declaration:
- Other actions or equivalent category, if Play Console requires listening events, reports, interactions or music activity to be declared.

Purpose:
- App functionality
- Personalization

Optional?
- Last.fm: yes
- Local events required for Weekly Report: depends on how the feature is presented in the published build

Shared?
- Local events: no, if they remain on the device
- Last.fm: yes, cautiously, only for Last.fm calls initiated by the user

### Audio

Possible setting:
- Do not declare as collected/uploaded if audio files and library remain exclusively on-device and are not sent to external servers.

Note:
- The app requires access to the audio library to work.
- The app locally analyzes music features.
- The app does not upload audio files to a proprietary backend in the current configuration.
- The app transmits only textual metadata (title and artist) to Apple iTunes and Deezer, not audio content.

### Music files and info

Possible cautious declaration:
- **Yes**, if Play Console specifically distinguishes music metadata (song title, artist, album) as a separate category

Reason:
- song title and artist are sent to Apple iTunes Search API and Deezer Search API to retrieve album artwork

Purpose:
- App functionality (album artwork display)

Optional?
- Not declared optional; the feature is active by default, can be disabled only by working in offline mode

Shared?
- Yes, with Apple iTunes Search API and Deezer Search API

Encrypted in transit?
- Yes, over HTTPS

Notes:
- The app does not send audio, mood, valence, arousal or other personal data to these services.
- The app sends only song title and artist.

### Photos and videos

Possible setting:
- Do not declare if the app does not access the user's photo/video gallery.
- Album artwork is part of music metadata or song-associated images, not general photo gallery collection.

### Device or other IDs

Possible setting:
- Do not declare if the build does not include analytics/ads/crash reporting SDKs that read device identifiers.

Verify before publication:
- Firebase Analytics
- Crashlytics
- AdMob
- other analytics or advertising SDKs
- SDKs that read Android ID or advertising ID

### Location

Recommended setting:
- No, if the app does not access location.

### Contacts

Recommended setting:
- No, if the app does not access contacts.

### Financial info

Recommended setting:
- No, if the app does not handle payments or financial data.

### Health and fitness

Recommended setting:
- No, if the app does not process health/fitness data.

### Messages

Recommended setting:
- No, if the app does not access SMS, email or messages.

### Files and docs

Possible consideration:
- The app accesses audio files through media/audio permissions.
- If Play Console distinguishes audio/media from generic documents, use the more specific audio/media entry.
- Do not declare generic documents if the app does not read user documents.

## 9. Security practices

### Is all user data collected by your app encrypted in transit?

Recommended answer:
- Yes, for Last.fm, iTunes and Deezer calls, all made over HTTPS.

### Do you provide a way for users to request that their data is deleted?

Cautious answer:
- Yes, if the Privacy Policy states that users can clear app data from Android settings or remove local configurations.

Available or relevant functions:
- remove Last.fm configuration
- clear Last.fm cache
- clear local mood analysis, if available in the build
- reset calibration
- clear album artwork URL cache (via app data clear)
- clear app data from Android settings
- uninstall app

Future suggestion:
- If Play Console requires a more explicit mechanism, consider a single "Delete local app data" button or a dedicated local data deletion section.

### Are user credentials encrypted at rest?

Recommended answer:
- Yes. The Last.fm API key is stored in `EncryptedSharedPreferences` through Android Keystore (AES256-GCM).

## 10. Data sharing

Possible declaration:
Music-Mood does not sell user data.

The app transfers data to external services in the following cases:

1. **Last.fm** — only when the user configures Last.fm and manually presses **Refresh Last.fm**.
   Data potentially transferred:
   - Last.fm username
   - Last.fm API key
   - request parameters required by the API
   
   Data received:
   - recent listens
   - song metadata
   - images/artwork, if available
   - music tags, if requested by the classification feature

2. **Apple iTunes Search API** — automatically, when artwork retrieval is needed for a song not present in local metadata.
   Data transferred:
   - song title
   - artist name
   
   Data received:
   - album artwork URL

3. **Deezer Search API** — automatically, as fallback when iTunes returns no results.
   Data transferred:
   - song title
   - artist name
   
   Data received:
   - album artwork URL

## 11. Retention and deletion

Data stored locally:
- mood analysis
- listening events
- weekly reports
- preferences
- selected language
- Last.fm configuration
- Last.fm summary cache
- album artwork URL cache from iTunes/Deezer
- calibration data
- aggregated music profile data

Retention:
- until app data is cleared
- until uninstall
- until manual removal, if the feature is available

## 12. Pre-publication checklist

- Online Privacy Policy accessible
- Privacy Policy linked inside the app
- Private technical repository, if needed
- Public documentation-only repository, if chosen
- Data Safety aligned with the published build
- No Ads SDK if declaring no ads
- No Analytics SDK if declaring no analytics
- No SDK reading device identifiers if declaring no Device IDs
- Last.fm optional and manual
- Last.fm API key stored locally in encrypted form
- Local Last.fm cache
- iTunes/Deezer declared as external recipients for artwork retrieval
- Local artwork URL cache
- Audio files not uploaded to server
- Mood analysis performed locally
- YAMNet/TFLite performed locally
- Local report independent from Last.fm fallback
- Android permissions aligned with audio features
- Privacy contact email included in the policy
- Italian and English Privacy Policies aligned
- README/CHANGELOG updated

## 13. Recommended short answers

### Privacy Policy

[Music-Mood Privacy Policy](https://lucatrombetta090-sys.github.io/Music-Mood/privacy_policy)

### Ads

No, if no advertising SDK is included.

### Data collected

Yes, cautiously for:
- Last.fm username/API key and optional Last.fm data, if the integration is included in the published build
- song title and artist, sent to Apple iTunes and Deezer for album artwork retrieval

### Data shared

Yes, cautiously with:
- Last.fm, only when the user uses the optional integration
- Apple iTunes Search API, automatically for artwork retrieval
- Deezer Search API, automatically as fallback for artwork retrieval

### Data encrypted in transit

Yes, for all external calls (Last.fm, iTunes, Deezer) over HTTPS.

### User deletion

Yes, through app data deletion, removal of local configuration and available in-app functions.

### Purpose

- App functionality
- Personalization

### Optional data

Yes for Last.fm.

### Required data

Access to the local audio library is required for the app's core features.
Album artwork retrieval from external services is active by default, can be implicitly disabled by working in offline mode.
