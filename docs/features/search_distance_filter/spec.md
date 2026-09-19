# [APROBADO] Especificacion Funcional: Busqueda y Filtrado Geografico, Gestion de Ubicaciones, Minimapa y FAQ

- **Fecha**: 2026-09-19
- **Estado**: [APROBADO]
- **Autor / Responsable**: Arquitecto de Software Mobile Senior & Backend (SDMD)
- **Modulos Afectados**: `:app` (`com.feryaeljustice.mirailink`) y `MiraiLink-Backend` (`Express 5 + PostgreSQL`)

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**:
  MiraiLink carece de un sistema de descubrimiento geografico realista. Actualmente no distingue entre el lugar donde reside un usuario y su posicion fisica al abrir la app (modo viaje), no permite ajustar el alcance de busqueda, ni informa con transparencia al usuario sobre la privacidad y el tratamiento de sus coordenadas historicas.
  
- **Objetivo**:
  1. **Separacion Clara de Residencia vs Ubicacion Activa**:
     - *Lugar de Residencia*: Ciudad, region y pais que el usuario define en su perfil y que ven los demas ("Vive en Palma de Mallorca").
     - *Ubicacion Activa de Sesion*: Coordenadas GPS en tiempo real captadas al abrir la aplicacion para calcular distancias exactas.
     - *Historico de Ubicaciones*: Almacenamiento optimizado de un maximo de 50 puntos historicos por usuario con reglas de espaciado (> 5 km o > 24 h).
  2. **Configuracion de Alcance en Ajustes (Guardado Explicito)**:
     - *Radio Local*: Slider continuo de 5 km a 100 km (por defecto 40 km) sincronizado con minimapa interactivo.
     - *Minimapa con doble control*: Permite ajustar el circulo mediante gestos tactiles/scroll o mediante el slider de respaldo.
     - *Modos de Alcance*: Radio Local en Km, Todo mi Pais, Todo el Mundo y Pasaporte por Pais Especifico (preparado para Premium).
     - *Modo Viajeros (Futuro Premium)*: Switch para matchear por ubicacion actual fisica en vez de por residencia habitual.
     - *Guardado Explicito*: Las preferencias no se guardan al vuelo mientras se prueba el minimapa, sino al pulsar "Guardar Ajustes".
  3. **Transparencia y Preguntas Frecuentes (FAQ)**:
     - Nueva pantalla en Ajustes con diseno en acordeon respondiendo que es MiraiLink, como funciona el algoritmo y como se gestiona la privacidad de los ultimos 50 puntos.
  4. **Tarjeta de Usuario (`UserCard`)**:
     - Muestra siempre el lugar de residencia habitual y la distancia relativa en kilometros (ocultando los km en modo Todo el Mundo).

- - -

## 2. Decisiones de Arquitectura Consolidadas

1. **Doble Ubicacion en Base de Datos**:
   - `users`: Columnas de residencia (`residence_city`, `residence_region`, `residence_country_code`, `residence_latitude`, `residence_longitude`) separadas de las columnas de sesion activa (`current_latitude`, `current_longitude`, `last_location_updated_at`).
2. **Tabla `user_location_history`**:
   - Almacena hasta 50 registros por usuario. Se inserta solo si el usuario se desplazo mas de 5 km del ultimo punto o pasaron mas de 24 horas. Al llegar a 51, se poda automaticamente el registro mas antiguo del usuario.
3. **Flujo de Permisos**:
   - Opcional en el registro (con seleccion manual de ciudad de residencia).
   - En el Swipe: si no hay permiso ni coordenadas activas, muestra una pantalla vacia elegante invitando a activar la ubicacion o a buscar en todo el mundo.
   - En Modo Demo: 100% simulado en Palma de Mallorca sin solicitar permisos de Android.
4. **Algoritmo de Feed Ponderado**:
   - Filtro duro segun el alcance elegido.
   - Ordenacion dinamica: score compuesto por cercania, intereses en comun (animes/juegos), actividad reciente y factor aleatorio para renovar el mazo.
5. **Minimapa en Ajustes**:
   - Basado en OpenStreetMap y Compose Canvas (sin coste de Google Maps). Circulo dinamico con auto-zoom al cambiar el radio.
6. **Pantalla FAQ**:
   - Acordeones colapsables con Material 3 explicando MiraiLink, la politica de los 50 puntos y las funciones Premium futuras.
