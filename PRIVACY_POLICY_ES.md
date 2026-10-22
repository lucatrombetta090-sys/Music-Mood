# Política de Privacidad — Music-Mood

Fecha de entrada en vigor: 22/10/2026

Music-Mood es una app Android diseñada para analizar la biblioteca musical local del usuario, clasificar las canciones por mood, generar estadísticas personales, crear un perfil musical y elaborar informes semanales.

Esta política describe qué datos puede tratar la app, con qué finalidades, dónde se conservan y qué controles tiene el usuario.

## 1. Desarrollador

Music-Mood es un proyecto personal desarrollado por Luca Trombetta.

Contacto de privacidad:
lucatrombetta090@mail.com

## 2. Datos tratados por la app

Music-Mood puede tratar datos de la biblioteca musical local del dispositivo, entre ellos:

- título de la canción
- artista
- álbum
- duración
- género, si está disponible
- año, si está disponible
- ruta o carpeta del archivo de audio
- carátula local, si está disponible
- identificador local de la canción

Estos datos se usan para mostrar la biblioteca, organizar las canciones, reproducir música, analizar el audio, generar estadísticas y crear informes musicales.

## 3. Actualización y nuevo análisis de la biblioteca

Music-Mood permite actualizar manualmente la biblioteca musical local desde Ajustes, por ejemplo después de añadir nuevos archivos de audio.

Cuando el motor de análisis se actualiza, la app puede proponer volver a analizar la biblioteca. El nuevo análisis solo empieza con el consentimiento del usuario ("Actualizar ahora" o "Volver a analizar la biblioteca" en Ajustes) y se realiza íntegramente en el dispositivo.

Ni la actualización ni el nuevo análisis borran los moods elegidos manualmente por el usuario ni los datos de Last.fm ya obtenidos.

## 4. Análisis del mood

Music-Mood puede analizar las canciones locales para asignarles un mood musical.

La app puede guardar localmente:

- mood estimado
- confianza de la clasificación
- valencia
- arousal
- BPM estimado
- tonalidad
- modo mayor o menor, si está disponible
- mood cambiado manualmente por el usuario, si lo hay
- puntuaciones de todos los moods, mood secundario y motor de análisis utilizado
- mood deducido de las etiquetas de Last.fm y estado de la lectura de Last.fm, si la opción está activada

Estos datos solo se usan para funciones internas de la app: biblioteca, reproductor, explicación "¿Por qué este mood?", BubbleMap, estadísticas, perfil musical, playlists automáticas y Weekly Mood Report.

## 5. Valencia, arousal y BubbleMap

Music-Mood puede calcular y mostrar métricas emocionales como la valencia y el arousal.

La valencia indica si una canción tiende a percibirse como más positiva o negativa.

El arousal indica el nivel de intensidad o activación de la canción.

Estos valores se pueden usar para colocar las canciones en la BubbleMap, generar estadísticas, crear la huella emocional del perfil y mejorar la coherencia de las playlists automáticas.

## 6. Clasificación con YAMNet y DSP local

Music-Mood puede usar un motor de análisis local basado en DSP y, si está activado, un clasificador avanzado YAMNet/TensorFlow Lite.

Estos procesos se usan para estimar el mood y las características musicales de las canciones.

El análisis de audio se realiza íntegramente en el dispositivo: los archivos de audio nunca se envían al exterior.

## 7. Mood manual

El usuario puede cambiar manualmente el mood de una canción.

El mood elegido por el usuario tiene prioridad sobre el calculado automáticamente y se usa en estadísticas, perfil, BubbleMap, playlists e informes.

El usuario también puede quitar el mood manual y volver al resultado calculado por la app.

## 8. Eventos de escucha y Weekly Mood Report

Music-Mood puede registrar localmente eventos de escucha para generar el Weekly Mood Report.

El informe puede incluir:

- número de canciones escuchadas
- mood dominante
- distribución porcentual de los moods
- comparación con informes anteriores
- datos agregados de la semana

Los informes se guardan localmente en el dispositivo.

## 9. Perfil musical personal

Music-Mood puede generar un perfil musical personal a partir de las canciones analizadas.

El perfil puede incluir:

- arquetipo musical dominante
- huella emocional
- distribución de los moods principales
- artista principal, si está disponible
- número de canciones analizadas
- sugerencias musicales diarias

Estos datos se generan localmente y solo se usan para las funciones de la app.

## 10. Calibración del clasificador

Music-Mood puede usar los datos de análisis disponibles localmente para calibrar el clasificador según la biblioteca del usuario.

La calibración puede usar:

- valencia media de la biblioteca
- arousal medio de la biblioteca
- BPM medio, si está disponible
- número de canciones analizadas

La calibración se realiza localmente en el dispositivo.

El usuario puede aplicar o reiniciar la calibración desde Ajustes. El reinicio restaura los moods originales del análisis y actualiza estadísticas, huella emocional, radar, moods principales y arquetipo del perfil.

Los ajustes de calibración están excluidos de la copia de seguridad de Android y no se restauran tras reinstalar la app.

## 11. Playlists automáticas

Music-Mood puede generar playlists automáticas basadas en mood, valencia, arousal y BPM.

Las playlists se crean localmente con los datos musicales ya presentes en la app.

Las playlists no se envían a ningún backend propio.

## 12. Integración opcional con Last.fm

Music-Mood puede incluir una integración opcional con Last.fm.

Si el usuario configura Last.fm, la app puede guardar localmente:

- nombre de usuario de Last.fm
- API key de Last.fm introducida por el usuario
- resumen local de los últimos datos importados de Last.fm
- estadísticas agregadas de Last.fm mostradas en el Weekly Report
- caché local del resumen de Last.fm

Cuando el usuario pulsa el botón Actualizar Last.fm, Music-Mood puede llamar a la API de Last.fm para obtener las escuchas recientes o las del periodo del informe.

Los datos obtenidos de Last.fm pueden incluir:

- título de la canción
- artista
- álbum
- hora de escucha, si está disponible
- URL de Last.fm de la canción, si está disponible
- URL de la imagen o carátula, si está disponible
- indicación de escucha en curso, si está disponible

La integración con Last.fm es opcional.

Si no hay escuchas de Last.fm en el periodo del Weekly Report, la app puede mostrar las escuchas recientes como información alternativa. Esto no modifica el informe semanal local.

El tratamiento de los datos por parte de Last.fm se rige por sus políticas y condiciones de servicio, disponibles en [https://www.last.fm/legal/privacy](https://www.last.fm/legal/privacy).

## 12-bis. Etiquetas de Last.fm como segunda opinión sobre el mood

Si el usuario ha configurado Last.fm y la opción "Usar etiquetas de Last.fm" está activada (activada por defecto), Music-Mood puede llamar en segundo plano a la API de Last.fm (método track.getTopTags) para cada canción de la biblioteca, enviando el título, el nombre del artista y la API key de Last.fm del usuario.

La app recibe las etiquetas de la comunidad asociadas a la canción (por ejemplo "melancholic", "party") y solo guarda localmente:

- el mood deducido de esas etiquetas
- el estado de la lectura (pendiente, leída, sin etiquetas útiles, error por reintentar), usado para reanudar desde las canciones que faltan

Las etiquetas no se guardan.

El usuario puede detener y reanudar la lectura en cualquier momento, desde Ajustes o desde la notificación de progreso. Al desactivar la opción, la lectura se detiene y se borran los moods deducidos de Last.fm.

## 13. Carátulas de álbum de servicios externos

Para mejorar la experiencia visual en la biblioteca, el reproductor y el mini-reproductor, Music-Mood puede obtener las carátulas de servicios externos públicos cuando no están disponibles en los metadatos de la canción.

Los servicios externos que contacta la app son:

- **Apple iTunes Search API** (endpoint público [https://itunes.apple.com/search](https://itunes.apple.com/search)), como fuente principal
- **Deezer Search API** (endpoint público [https://api.deezer.com/search/track](https://api.deezer.com/search/track)), como fuente alternativa

Al consultar estos servicios, Music-Mood solo envía la información mínima necesaria para buscar la carátula:

- título de la canción
- nombre del artista

No se transmite a estos servicios ningún otro dato personal, identificador del dispositivo, identificador publicitario, dirección IP facilitada voluntariamente, mood estimado, valencia, arousal ni dato de escucha.

Las solicitudes se realizan mediante HTTPS. La app usa la URL de la imagen de la carátula, que se guarda en la caché local para no repetir la búsqueda de la misma canción.

El usuario puede desactivar implícitamente esta función trabajando sin conexión: si el dispositivo no tiene conexión, la app muestra la imagen local por defecto en lugar de la carátula remota.

El tratamiento de los datos por parte de Apple y Deezer se rige por sus respectivas políticas:

- Apple: [https://www.apple.com/legal/privacy/](https://www.apple.com/legal/privacy/)
- Deezer: [https://www.deezer.com/legal/personal-datas](https://www.deezer.com/legal/personal-datas)

Music-Mood no tiene ninguna relación comercial ni contractual con Apple ni con Deezer y usa sus endpoints públicos de búsqueda exclusivamente para mostrar las carátulas.

## 14. Idioma de la app

Music-Mood puede guardar localmente el idioma elegido por el usuario.

Los idiomas disponibles son italiano, inglés y español.

El idioma elegido se guarda en las preferencias locales de la app y se usa para mostrar la interfaz, los textos, los contenidos informativos y la documentación de la app en ese idioma.

## 15. Finalidades del tratamiento

Los datos se tratan para:

- mostrar y organizar la biblioteca musical local
- reproducir música
- clasificar las canciones por mood
- explicar cómo se ha calculado el mood de una canción
- permitir correcciones manuales del mood
- calcular valencia, arousal, BPM y tonalidad
- generar la BubbleMap
- calibrar el clasificador
- generar estadísticas musicales
- generar el perfil musical personal
- generar el Weekly Mood Report
- crear playlists automáticas coherentes con el mood
- integrar opcionalmente datos de Last.fm
- usar las etiquetas de Last.fm como segunda opinión en la clasificación del mood
- obtener carátulas de servicios externos públicos
- mejorar la experiencia de uso
- permitir compartir manualmente el perfil o el informe

## 16. Conservación de los datos

Los datos de la app se guardan principalmente en el dispositivo del usuario.

Pueden guardarse localmente:

- análisis del mood de las canciones
- preferencias del usuario
- idioma elegido
- configuración de Last.fm
- caché del resumen de Last.fm
- mood deducido de las etiquetas de Last.fm y estado de la lectura
- caché de las URL de las carátulas obtenidas de iTunes o Deezer
- informes semanales
- eventos de escucha
- moods cambiados manualmente por el usuario
- datos de calibración
- datos agregados del perfil musical

Los datos permanecen en el dispositivo hasta que el usuario borra los datos de la app, la desinstala o usa las funciones de borrado disponibles.

## 17. Comunicación de datos

Music-Mood no vende los datos personales del usuario.

Music-Mood no usa ningún backend propio para procesar moods, informes, perfil o estadísticas personales.

Algunas funciones de la app pueden implicar solicitudes a servicios externos:

- **Last.fm**, solo si el usuario configura la integración opcional: al pulsar Actualizar Last.fm y, si la opción "Usar etiquetas de Last.fm" está activada, en segundo plano para leer las etiquetas de las canciones (título y artista)
- **Apple iTunes Search API**, para obtener carátulas, enviando título y artista
- **Deezer Search API**, como alternativa cuando iTunes no devuelve resultados, enviando título y artista

El tratamiento de los datos por parte de estos servicios se rige por sus políticas y condiciones.

## 18. API key de Last.fm

La API key de Last.fm introducida por el usuario se guarda localmente en el dispositivo de forma cifrada y se usa para llamar a la API de Last.fm.

El usuario es responsable de usar su API key conforme a las condiciones de Last.fm.

Music-Mood no solicita la contraseña de Last.fm.

## 19. Seguridad

Music-Mood aplica un enfoque de minimización de datos.

Las funciones principales se ejecutan localmente en el dispositivo.

Las comunicaciones con servicios externos, cuando existen, se realizan mediante HTTPS.

Las credenciales sensibles guardadas localmente, como la API key de Last.fm, se cifran mediante las API de seguridad de Android y se excluyen de la copia de seguridad de Android.

## 20. Permisos de Android

Music-Mood puede solicitar los permisos necesarios para acceder a los archivos de audio o a la biblioteca multimedia del dispositivo y para mostrar notificaciones, según la versión de Android y las funciones usadas.

Los permisos se usan para:

- leer las canciones del dispositivo
- mostrar la biblioteca musical
- reproducir música
- analizar las canciones
- mostrar en las notificaciones el progreso de los análisis y el Weekly Mood Report
- generar estadísticas e informes

El usuario puede revocar los permisos desde los ajustes de Android.

## 21. Compartir el perfil, el informe o la exportación

El usuario puede compartir manualmente una imagen o un resumen del perfil musical o del Weekly Mood Report, o exportar los análisis a un archivo CSV, usando las funciones del dispositivo.

Compartir y exportar solo ocurre tras una acción explícita del usuario.

## 22. Menores

Music-Mood no está diseñada específicamente para niños y no pretende recoger conscientemente datos personales de menores.

## 23. Control del usuario

El usuario puede controlar sus datos mediante los ajustes del dispositivo y las funciones de la app.

El usuario puede:

- revocar los permisos de Android concedidos a la app
- borrar los datos de la app desde los ajustes de Android
- desinstalar la app
- no configurar Last.fm
- quitar o cambiar las credenciales de Last.fm
- borrar la caché de Last.fm
- detener y reanudar la lectura de las etiquetas de Last.fm
- desactivar la opción "Usar etiquetas de Last.fm", borrando los moods ya deducidos
- volver a analizar la biblioteca
- reiniciar la calibración del clasificador
- no usar la función de compartir el perfil o el informe
- trabajar sin conexión para evitar la descarga de carátulas de servicios externos

## 24. Política de Privacidad en línea

La Política de Privacidad oficial de Music-Mood está disponible en línea: [Privacy Policy online](https://lucatrombetta090-sys.github.io/Music-Mood/privacy_policy)

También se puede consultar una copia en la pantalla Info de la app.

## 25. Cambios en esta política

Esta Política de Privacidad puede actualizarse para reflejar cambios en la app, en sus funciones o en los requisitos legales.

En caso de cambios relevantes, se actualizará la fecha de entrada en vigor.

**Versión 1.0.1:** se ha eliminado el análisis de las letras de las canciones. La app ya no contacta con el servicio LRCLIB y, con la actualización, las puntuaciones de las letras calculadas por versiones anteriores se borran del dispositivo.

## 26. Contacto

Para preguntas sobre esta Política de Privacidad o sobre el tratamiento de los datos:
lucatrombetta090@mail.com
