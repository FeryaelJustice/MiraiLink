# [APROBADO] Especificación Funcional: Búsqueda y Filtrado Geográfico, Gestión de Ubicaciones, Minimapa y FAQ

- **Fecha**: 2026-09-19
- **Estado**: [APROBADO]
- **Autor / Responsable**: Arquitecto de Software Mobile Senior & Backend (SDMD)
- **Módulos Afectados**: `:app` (`com.feryaeljustice.mirailink`) y `MiraiLink-Backend` (`Express 5 + PostgreSQL`)

- - -

## 1. Problema y Objetivo

- **Problema que resuelve**:
  MiraiLink carece de un sistema de descubrimiento geográfico realista. Actualmente no distingue entre el lugar donde reside un usuario y su posición física al abrir la app (modo viaje), no permite ajustar el alcance de búsqueda, ni informa con transparencia al usuario sobre la privacidad y el tratamiento de sus coordenadas históricas.
  
- **Objetivo**:
  1. **Separación Clara de Residencia vs Ubicación Activa**:
     - *Lugar de Residencia*: Ciudad, región y país que el usuario define en su perfil y que ven los demás ("Vive en Palma de Mallorca").
     - *Ubicación Activa de Sesión*: Coordenadas GPS en tiempo real captadas al abrir la aplicación para calcular distancias exactas.
     - *Histórico de Ubicaciones*: Almacenamiento optimizado de un máximo de 50 puntos históricos por usuario con reglas de espaciado (> 5 km o > 24 h).
  2. **Configuración de Alcance en Ajustes (Guardado Explícito)**:
     - *Radio Local*: Slider continuo de 5 km a 100 km (por defecto 40 km) sincronizado con minimapa interactivo.
     - *Minimapa con doble control*: Permite ajustar el circulo mediante gestos tactiles/scroll o mediante el slider de respaldo.
     - *Modos de Alcance*: Radio Local en Km, Todo mi País, Todo el Mundo y Pasaporte por País Específico (preparado para Premium).
     - *Modo Viajeros (Futuro Premium)*: Switch para matchear por ubicación actual física en vez de por residencia habitual.
     - *Guardado Explícito*: Las preferencias no se guardan al vuelo mientras se prueba el minimapa, sino al pulsar "Guardar Ajustes".
  3. **Transparencia y Preguntas Frecuentes (FAQ)**:
     - Nueva pantalla en Ajustes con diseño en acordeón respondiendo qué es MiraiLink, cómo funciona el algoritmo y cómo se gestiona la privacidad de los últimos 50 puntos.
  4. **Tarjeta de Usuario (`UserCard`)**:
     - Muestra siempre el lugar de residencia habitual y la distancia relativa en kilometros (ocultando los km en modo Todo el Mundo).

- - -

## 2. Decisiones de Arquitectura Consolidadas

1. **Doble Ubicación en Base de Datos**:
   - `users`: Columnas de residencia (`residence_city`, `residence_region`, `residence_country_code`, `residence_latitude`, `residence_longitude`) separadas de las columnas de sesión activa (`current_latitude`, `current_longitude`, `last_location_updated_at`).
2. **Tabla `user_location_history`**:
   - Almacena hasta 50 registros por usuario. Se inserta solo si el usuario se desplazó más de 5 km del último punto o pasaron más de 24 horas. Al llegar a 51, se poda automáticamente el registro más antiguo del usuario.
3. **Flujo de Permisos**:
   - Opcional en el registro (con selección manual de ciudad de residencia).
   - En el Swipe: si no hay permiso ni coordenadas activas, muestra una pantalla vacía elegante invitando a activar la ubicación o a buscar en todo el mundo.
   - En Modo Demo: 100% simulado en Palma de Mallorca sin solicitar permisos de Android.
4. **Algoritmo de Feed Ponderado**:
   - Filtro duro según el alcance elegido.
   - Ordenación dinámica: score compuesto por cercania, intereses en común (animes/juegos), actividad reciente y factor aleatorio para renovar el mazo.
5. **Minimapa en Ajustes**:
   - Basado en OpenStreetMap y Compose Canvas (sin coste de Google Maps). Circulo dinámico con auto-zoom al cambiar el radio.
6. **Pantalla FAQ**:
   - Acordeones colapsables con Material 3 explicando MiraiLink, la política de los 50 puntos y las funciones Premium futuras.
