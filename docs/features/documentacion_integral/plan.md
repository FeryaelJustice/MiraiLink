# [APROBADO] Plan técnico de documentación integral Android

- Fecha: 2026-10-01.
- Estado: APROBADO por el usuario el 2026-10-01.
- Especificación: [spec.md](spec.md), aprobada.
- Rama: `codex/documentacion-integral`.
- Módulo: `:app`; documentación, fuentes propias y configuración versionada.

## 1. Hechos verificados

Las versiones siguientes son declaraciones del repositorio, no una comprobación de dependencias resueltas ni de compatibilidad de compilación.

| Fuente | Declaraciones actuales |
| --- | --- |
| `gradle/libs.versions.toml` | Kotlin 2.4.10, AGP 9.4.1, KSP 2.3.8, Compose BOM 2026.09.00, Koin BOM/annotations 4.2.2 |
| Mismo catálogo | Navigation 3 1.1.7, Room 2.8.5, DataStore 1.2.1, Retrofit 3.0.0, OkHttp 5.5.0, Serialization 1.11.0, Socket.IO client 2.1.2 |
| Mismo catálogo | Firebase BOM 34.19.0, Billing 9.1.0, CameraX 1.6.2, ML Kit face detection 16.1.7 |
| `gradle/wrapper/gradle-wrapper.properties` | Gradle 9.7.1 |
| `app/build.gradle.kts` | Java 17, min SDK 30, compile/target SDK 37, versión 3.0.0, código 34 |
| Fuentes inspeccionadas | Demo Room, DataStore cifrado, DI Koin, consulta REST del chat cada 3 segundos |

Las plantillas y documentos antiguos no sustituyen estas fuentes. Durante el inventario se revisarán también las dependencias utilizadas que no aparecen en esta selección.

## 2. Diseño documental y recorrido de lectura

La entrada para estudiar el cliente será `docs/guia-maestra.md`, enlazada desde el README. Mantendrá un índice temático, un recorrido de lectura y referencias al backend. `docs/ai/README.md` seguirá siendo la entrada específica para asistentes y enlazará la guía humana.

Los documentos nuevos se agruparán en `docs/estudio/`. Cuando un documento existente sea la referencia adecuada se actualizará y enlazará desde el maestro, evitando mantener dos explicaciones incompatibles.

| Documento previsto | Contenido |
| --- | --- |
| `arquitectura.md` | Arranque, paquetes, límites reales, DI, flujo UI/StateFlow/casos de uso/repositorios, dependencias hacia frameworks |
| `tecnologias.md` | Versiones declaradas, propósito, archivos consumidores y uso concreto de cada tecnología |
| `autenticacion-y-seguridad.md` | Entrada de credenciales, envío, verificación, 2FA, sesión, almacenamiento cifrado y límites del cliente |
| `funcionalidades.md` | Inventario completo con enlaces a recorridos detallados por dominio |
| `flujos/` | Recorridos individuales de funcionalidades, consumidores, estados, errores y alternativas |
| `persistencia-y-demo.md` | Room demo, DataStore, repositorios delegados, estado en memoria, límites de sincronización |
| `integraciones.md` | Red, Firebase, FCM, IA, imágenes, anuncios, consentimiento, Billing, cámara y análisis según implementación |
| `configuracion.md` | Nombre de opción, lector, fase de uso, obligatoriedad, fallback y ausencia/invalidez; ejemplos sin secretos |
| `ciclo-de-vida-y-errores.md` | Corrutinas, cancelación, navegación, rotación, proceso, conectividad y reintento real |
| `desarrollo-y-verificacion.md` | Compilación, pruebas, herramientas, evidencia obtenida y operaciones pendientes |
| `cobertura.md` | Matriz dominio/tecnología -> fuente -> documento -> diagrama -> evidencia -> límites |
| `hallazgos.md` | Contradicciones documentales corregidas y defectos funcionales registrados sin modificar comportamiento |

Cada documento incluirá fuentes relativas navegables y distinguirá inspección estática de ejecución. Los enlaces al backend usarán rutas del repositorio remoto y avisarán si la rama todavía no está publicada. No se afirmará que una URL de rama local es accesible antes de publicarla.

## 3. Inventario y contratos por capas

- Revisar archivos propios de `core`, `data`, `domain`, `state`, `di/koin`, `ui`, recursos/configuración y pruebas relevantes.
- Trazar cada funcionalidad desde pantalla y ViewModel hasta caso de uso, repositorio, datasource y API o almacenamiento demo.
- Documentar firmas reales de contratos, `Flow`/`StateFlow`, `MiraiLinkResult`, transformaciones, efectos y consumidores. No introducir contratos nuevos.
- Registrar rutas HTTP, DTO, formatos, traducciones y errores que Android conoce; enlazar procesamiento SQL, hashes y servicios internos al backend.
- Separar casos implementados, rutas declaradas sin consumidor visible y objetivos escritos solo en especificaciones anteriores.
- Revisar uso real de permisos, restauración de estado, segundo plano, cancelación y demo sin inferir garantías desde las directrices.

No hay cambios previstos en entidades, DAOs, navegación, DI, firmas, dependencias ni comportamiento.

## 4. Comentarios de código

Revisar funciones, clases, propiedades y bloques no evidentes en fuentes propias. Añadir KDoc para contratos y comentarios locales para decisiones, orden de efectos, concurrencia, seguridad y algoritmos. Traducir comentarios explicativos en inglés conservando identificadores y términos técnicos. No comentar trivialidades ni atribuir garantías inexistentes.

Los comentarios largos apuntarán a la guía correspondiente. Se revisará el diff de cada grupo para asegurar que solo cambian comentarios o documentos y que ninguna directiva funcional de herramientas se traduce accidentalmente.

## 5. Diagramas

Ubicación: `docs/estudio/diagramas/`, con índice enlazado al maestro. Usar Markdown con Mermaid; PlantUML para representaciones UML que lo requieran. Incluir título, tipo, alcance, fuente y explicación de lectura.

Representaciones previstas: contexto del cliente, componentes/paquetes, modelos y persistencia demo, secuencias de acceso/2FA/verificación, sesión y navegación, actividades de perfil/fotos/búsqueda/match/chat, estados de errores y demo. El diagrama ER de PostgreSQL se enlaza al backend.

La lista definitiva se ajusta al inventario: cada funcionalidad debe quedar explicada; no crear una variante UML sin significado en el código. No instalar herramientas nuevas para renderizar sin necesidad.

## 6. Secuencia de trabajo tras aprobación

1. Generar `tasks.md` coordinado con cobertura por dominios y tecnologías.
2. Completar inventario y matriz de fuentes antes de redactar afirmaciones.
3. Redactar guía maestra y actualizar referencias existentes.
4. Completar documentos por dominio, configuración y casuística.
5. Crear y contrastar diagramas con fuentes y contratos del backend.
6. Revisar y traducir comentarios por grupo de archivos, sin alterar comportamiento.
7. Revisar enlaces, cobertura, diff y verificaciones; registrar resultados y límites.

## 7. Verificación

- Revisar que toda funcionalidad y tecnología inventariada tenga documento o exclusión justificada.
- Comprobar destinos locales y anclas de enlaces; revisar manualmente referencias cruzadas remotas y publicación pendiente.
- Contrastar diagramas, estados y configuración con código; comprobar sintaxis con herramientas disponibles cuando sea posible. Una inspección de sintaxis no equivale a renderizado.
- Revisar cambios semánticos accidentales mediante diff y `git diff --check`.
- Tras cambios de comentarios en fuentes: `assembleDebug`, `testDebugUnitTest`, `lintDebug`, sin repetir suites ya correctas si no cambian las fuentes.
- No añadir tests que solo comprueben comentarios. No ejecutar pruebas instrumentadas que modifiquen backend real ni afirmar validación visual sin dispositivo.
- Registrar fecha, comando, resultado, fallos previos y limitaciones. Ante bloqueos, distinguir fallo del entorno, del código o comprobación no realizada.

## 8. Riesgos y mitigaciones

- Cobertura superficial: matriz por dominio y tecnología, fuentes y consumidores trazados.
- Divergencia entre guía y código: citas a archivos, fecha de revisión y distinción entre objetivo e implementación.
- Duplicación cliente/servidor: propiedad del tema y enlaces explícitos al responsable.
- Exposición de secretos: extraer semántica de lectores versionados; no leer configuración privada.
- Regresión por comentarios: revisar diff, directivas de herramientas y ejecutar checks pertinentes.
- Diagramas ilegibles: separar dominios y enlazar vistas de detalle desde un índice común.

## 9. Aprobación y ampliación de mantenimiento

Plan aprobado explícitamente el 2026-10-01. El usuario añade un disclaimer obligatorio en el maestro y un protocolo de mantenimiento conectado entre frontends/apps y backend para temas cruzados.
