# 🎵 Music-Mood

**Music-Mood** es una app Android que analiza tu biblioteca musical local y convierte cada canción en un pequeño mapa emocional: mood, BPM, tonalidad, valencia, arousal, perfil musical personal, BubbleMap y Weekly Mood Report.

El objetivo es sencillo: **escuchar tu música de forma más inteligente, personal y visual**.

## ✨ Por qué instalarla

Music-Mood no es solo un reproductor. Te ayuda a descubrir **cómo suena emocionalmente tu música**.

Con Music-Mood puedes:

- 🎧 explorar tu biblioteca musical local
- 🧠 clasificar las canciones en 9 moods, con un mood secundario cuando una canción tiene dos
- ℹ️ entender **por qué** una canción tiene ese mood
- 🤖 usar YAMNet como clasificador de audio avanzado
- 📍 ver las canciones en la BubbleMap de valencia/arousal
- 📊 generar estadísticas y exportarlas en CSV
- 🧬 crear tu perfil musical personal
- 🎯 calibrar el clasificador según tus gustos
- 📆 crear un Weekly Mood Report
- 🖼️ compartir imágenes del perfil y del informe
- 🌍 usar la app en italiano, inglés o español
- 🔄 actualizar y volver a analizar la biblioteca sin perder tus elecciones

## 🧠 El motor emocional: mood, valencia y arousal

Music-Mood interpreta cada canción con un modelo emocional de dos dimensiones basado en **valencia** y **arousal**.

### 💜 Valencia

La **valencia** describe la calidad hedónica de un estímulo musical: lo positiva, agradable y luminosa, o negativa, oscura y melancólica, que puede sentirse una canción.

En Music-Mood la valencia se representa como un eje continuo:

```
negativa  ─────────────── positiva
```

### ⚡ Arousal

El **arousal** representa el nivel de activación o intensidad: lo tranquila, suave y relajada, o enérgica, intensa y activadora, que es una canción.

En Music-Mood el arousal se representa como un eje continuo:

```
tranquila  ─────────────── intensa
```

### 🫧 BubbleMap

La **BubbleMap** usa estos dos ejes para colocar las canciones en un espacio emocional continuo:

```
                    Arousal alto
                         ↑
      intensa/oscura     │   positiva/intensa
                         │
Valencia negativa ───────┼───────  Valencia positiva
                         │
     triste/tranquila    │   positiva/tranquila
                         ↓
                    Arousal bajo
```

Ejemplos intuitivos:

- 😔 una canción melancólica tiende a tener **valencia negativa** y **arousal bajo**
- 🔥 una canción agresiva tiende a tener **valencia negativa** y **arousal alto**
- ☀️ una canción positiva tiende a tener **valencia positiva**
- 🌿 una canción relajada tiende a tener **arousal bajo**

Este enfoque se inspira en el modelo circumplejo de las emociones, en el que los estados emocionales se describen como combinación de valencia y arousal. Referencias útiles: [Capire le emozioni per vivere meglio - Psicologia Contemporanea](https://www.psicologiacontemporanea.it/blog/capire-le-emozioni-vivere-meglio/), [Arousal - State of Mind](https://www.stateofmind.it/arousal/) y [Circumplex Model of Affect: Valence and Arousal Explained](https://psychologyfanatic.com/circumplex-model-of-arousal-and-valence/).

## 🤖 Clasificación avanzada con YAMNet

Music-Mood usa un enfoque híbrido:

- **YAMNet** para la clasificación de audio avanzada
- **DSP local** para BPM, tonalidad, valencia, arousal y como alternativa
- **reglas musicales** para los casos difíciles: los temas dance no terminan en moods tranquilos y las baladas lentas no se leen como enérgicas
- **etiquetas de Last.fm** como segunda opinión opcional
- **calibración personalizada** para adaptar el modelo a tu biblioteca
- **mood manual** para corregir el resultado cuando quieras decidir tú

YAMNet es una red neuronal preentrenada para la clasificación de audio. La documentación de TensorFlow describe YAMNet como un modelo capaz de predecir 521 clases de eventos sonoros basadas en el corpus AudioSet, construido sobre la arquitectura MobileNetV1 depthwise-separable convolution. Ver: [Sound classification with YAMNet - TensorFlow Hub](https://www.tensorflow.org/hub/tutorials/yamnet) y [YAMNet README - TensorFlow Models](https://github.com/tensorflow/models/tree/master/research/audioset/yamnet).

En Music-Mood, YAMNet es una parte del motor de clasificación, no la única fuente de verdad.

## 🎭 Los 9 moods

| Mood | Significado |
|---|---|
| ⚡ Enérgico | alta activación, energía, movimiento |
| ☀️ Positivo | luminoso, agradable, buen rollo |
| 🔥 Agresivo | intenso, fuerte, impactante |
| 🌧️ Melancólico | profundo, oscuro, delicado |
| 🕯️ Romántico | cálido, íntimo, emotivo |
| 🌿 Relajado | tranquilo, suave, sereno |
| 📼 Nostálgico | recuerdos, matices, reflexión |
| 🎯 Concentración | foco, equilibrio, orden |
| 🎉 Festivo | fiesta, entusiasmo, ligereza |

## 📱 Funciones principales

### 🎧 Biblioteca musical local

- lectura de la biblioteca de audio local
- secciones de canciones, artistas, álbumes, géneros, años y carpetas
- navegación por carpetas
- ordenación por título, BPM, valencia y arousal
- índice rápido lateral que sigue la ordenación: letras o rangos de BPM, valencia o energía
- actualización manual de la biblioteca desde Ajustes cuando añades canciones

### ▶️ Reproductor y mini-reproductor

- reproductor integrado a pantalla completa
- mini-reproductor persistente
- carátulas locales y remotas
- toque en la notificación para volver a la canción en reproducción
- pausa automática cuando empieza otro audio (llamada, vídeo, otra app) y al desconectar los auriculares
- menú contextual para analizar el mood y cambiarlo manualmente

### 🧠 Análisis del mood

- clasificación automática del mood
- mood secundario, cuando la canción tiene dos moods cercanos
- valencia y arousal
- BPM estimado
- tonalidad y modo mayor/menor
- YAMNet y DSP como alternativa
- **"¿Por qué este mood?"**: para cada canción muestra los 3 primeros moods en porcentaje, los factores de audio que han influido, su posición en la BubbleMap y el efecto de Last.fm

### 🏷️ Mood manual

Puedes corregir el mood de una canción. **Tu elección siempre tiene prioridad**: estadísticas, perfil, BubbleMap y playlists automáticas la usan al instante. Al quitar la corrección vuelve el mood calculado.

Prioridad:

1. mood elegido manualmente
2. mood calculado a partir del audio, ajustado por Last.fm si procede
3. alternativa por defecto

### 🔄 Nuevo análisis de la biblioteca

Cuando el motor de análisis mejora, la app propone volver a analizar la biblioteca. Puedes hacerlo al momento con **"Actualizar ahora"** o más tarde, también desde Ajustes con **"Volver a analizar la biblioteca"**.

El nuevo análisis solo rehace la parte de audio: **tus moods manuales y las etiquetas de Last.fm se conservan**.

### 🫧 BubbleMap

- mapa visual de las canciones en el espacio valencia/arousal
- ejes de positividad/intensidad
- etiquetas de mood traducidas
- toca una burbuja para ver título, artista y mood

### 📊 Estadísticas

- distribución de moods
- métricas agregadas
- BPM medio
- valencia media
- arousal medio
- tonalidad más frecuente
- artistas principales
- exportación CSV con mood, mood secundario, mood manual, puntuaciones y estado de Last.fm

### 🧬 Perfil musical personal

- arquetipo musical dominante
- huella emocional en radar
- moods principales
- artista principal
- sugerencia diaria
- imagen del perfil para compartir

### 📆 Weekly Mood Report

- resumen semanal de escuchas
- mood dominante de la semana
- distribución semanal
- comparación con la semana anterior
- imagen del informe para compartir

### 🌐 Last.fm opcional

- configuración manual de usuario y API key
- escuchas recientes en el Weekly Report, con actualización manual
- **etiquetas de Last.fm como segunda opinión sobre el mood**: la app lee las etiquetas de la comunidad de cada canción
  - Last.fm solo cuenta si su mood ya está entre los 3 primeros indicados por el audio
  - su efecto está limitado: como máximo 6 puntos porcentuales
  - puedes **detener y reanudar** la lectura desde Ajustes o desde la notificación; el progreso se guarda
  - las canciones nuevas se leen automáticamente
  - la opción "Usar etiquetas de Last.fm" se puede desactivar en cualquier momento

### 🌍 Multilingüe

Music-Mood está disponible en:

- 🇮🇹 Italiano
- 🇬🇧 English
- 🇪🇸 Español

En el primer inicio, la app pide elegir el idioma **antes de solicitar los permisos**. El idioma por defecto es el italiano.

## 🎯 Calibración personalizada

Music-Mood puede adaptar el clasificador a tus gustos musicales.

La calibración usa la biblioteca ya analizada para desplazar los centroides del clasificador según tu perfil medio.

- 0.0 = sin calibración
- 0.5 = calibración equilibrada
- 1.0 = máxima adaptación

El **reinicio de la calibración** devuelve cada canción a su mood original y actualiza estadísticas, huella emocional, radar, moods principales y arquetipo. Tus moods manuales no cambian.

La calibración está ligada al dispositivo: no se restaura desde la copia de seguridad de Android tras reinstalar la app.

## 🎼 Playlists automáticas coherentes con el mood

Cada preset usa moods permitidos, moods bloqueados y filtros técnicos de arousal, BPM y valencia.

Ejemplos:

- 🌙 Sleep/Relax excluye Enérgico, Festivo y Agresivo
- ⚡ Energy prioriza Enérgico, Festivo y Positivo
- 🎯 Focus prioriza Concentración y Relajado con arousal moderado
- 💪 Workout prioriza energía alta, BPM alto o arousal elevado

Si no hay suficientes canciones coherentes, la alternativa sigue limitada a moods compatibles. Las playlists respetan tus moods manuales.

## 🔐 Privacidad y datos locales

Music-Mood está diseñada para funcionar principalmente en el dispositivo: el análisis de audio es local y no existe un backend propio.

Los únicos servicios externos que se contactan son:

- **Last.fm**, solo si lo configuras tú
- **Apple iTunes** y **Deezer**, para las carátulas que faltan (solo título y artista)

La Política de Privacidad está disponible aquí: [Music-Mood Privacy Policy](https://lucatrombetta090-sys.github.io/Music-Mood/privacy_policy).

## 🛠️ Tecnologías

- Kotlin
- Android
- Media3
- Room
- WorkManager
- Material Components
- AppCompat para el idioma por app
- Chaquopy
- Python
- NumPy / Mutagen
- YAMNet / TensorFlow Lite / MediaPipe Tasks Audio
- GitHub Actions
- Keystore estable mediante GitHub Secrets

## 🚀 Compilación

Requisitos recomendados:

- Android Studio reciente
- JDK 17
- Android SDK configurado
- Gradle compatible con el proyecto

Compilación debug:

```
./gradlew assembleDebug
```

## 📦 Estado actual (1.0.1)

La versión incluye:

- biblioteca local con índice rápido
- reproductor y mini-reproductor con gestión del foco de audio
- análisis del mood con YAMNet y DSP
- mood secundario y "¿Por qué este mood?"
- mood manual con prioridad absoluta
- etiquetas de Last.fm como segunda opinión, con Detener/Reanudar
- nuevo análisis que conserva las elecciones del usuario
- BubbleMap
- estadísticas y exportación CSV
- perfil musical
- calibración y reinicio de la calibración
- playlists automáticas coherentes con el mood
- Weekly Mood Report
- italiano, inglés y español
- imágenes de perfil e informe para compartir
- firma estable mediante GitHub Actions

## 🗺️ Próximos pasos

- mejorar las playlists y las recomendaciones
- afinar la estimación del BPM en las baladas
- reforzar la correspondencia entre Last.fm y la biblioteca local
- ampliar la documentación y las capturas de la tienda

## 👤 Autor

**Luca Trombetta**

## 📄 Licencia

MIT
