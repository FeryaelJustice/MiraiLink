# [APROBADO] Especificacion Funcional: Catalogo localizado de animes y juegos

- **Fecha**: 2026-09-20
- **Estado**: [APROBADO]
- **Autor / Responsable**: Codex y equipo MiraiLink
- **Modulos afectados**: Android `:app` y MiraiLink Backend

## 1. Problema y objetivo

- **Problema que resuelve**: Los intereses de anime y juegos dependen hoy del nombre visible. En Android, la seleccion se resuelve buscando una coincidencia exacta de `name`; el backend conserva una tabla de catalogo sin localizacion y devuelve esos nombres directamente. Esto es fragil ante traducciones, correcciones editoriales y diferencias de idioma.
- **Objetivo**: Mantener intereses por identificadores estables, presentar los datos del catalogo en el idioma de la aplicacion y dejar los modelos independientes de anime y juego preparados para recursos traducibles futuros sin columnas por idioma. La experiencia de tarjeta de descubrimiento y edicion de perfil quedara jerarquizada sin alterar los datos que el usuario puede editar.

## 2. Situacion actual comprobada

- El backend tiene `animes` y `games` con `id`, `name`, `description` e `image_url`; las relaciones `user_anime_interests` y `user_game_interests` ya persisten el UUID del catalogo.
- El endpoint de catalogo devuelve `id`, `name` e `image_url`. El endpoint de perfil devuelve los mismos objetos, pero la pantalla Android vuelve a emparejar la seleccion comparando el nombre mostrado.
- La base de datos inicial inserta catalogos en `db_inserts.sql` y el seed Node los conserva de forma idempotente. Hay una ruta de reseteo separada.
- Las tarjetas muestran campos de perfil sin una agrupacion formal y el formulario de edicion concentra los controles en una sola jerarquia visual.
- Las respuestas actuales exponen `username` en varios perfiles. Es un identificador de inicio de sesion y el requisito nuevo exige que los perfiles de aplicacion usen `nickname` y no lo envien.

## 3. Alcance

### 3.1 Dentro del alcance

- Migracion PostgreSQL compatible para las entidades independientes `animes` y `games`, idiomas soportados y tablas de traducción separadas por tipo de entidad mediante claves compuestas.
- Animes y juegos con identificadores estables independientes del texto localizado y relaciones de intereses por identificador. No se introducirá una tabla común que mezcle ambas entidades.
- Modelo extensible para biografías localizadas. Las filas iniciales de biografías se crearán vacías para cada entidad e idioma soportado, sin interfaz de edición ni presentación en esta entrega.
- Campo de imagen no traducible y anulable en cada tabla principal de anime y juego. Almacenará una URL absoluta o una ruta relativa completa, incluido nombre y extensión del archivo. No se almacenarán binarios en PostgreSQL.
- Seeds idempotentes de idiomas, catalogos y traducciones, junto con una ruta de despliegue segura para bases ya existentes.
- Contratos, validacion, controladores, DTOs y documentacion del backend actualizados para devolver solo datos de perfil autorizados y no sensibles.
- Modelos, DTOs, mappers, Room demo, repositorios, casos de uso, estado, selector y persistencia Android adaptados a la identidad por ID.
- Reorganizacion visual de la tarjeta de descubrimiento y de editar perfil en secciones coherentes, manteniendo las funciones actuales.
- Inventario y decision documentada para cada campo de usuario: editable por el propietario, visible a terceros, solo para logica o nunca expuesto.

### 3.2 Fuera del alcance

- Añadir contenido editorial real, pantallas de detalle, miniaturas, edición o presentación de biografías e imágenes del catálogo. La API y los modelos Android recibirán dichos campos preparados, pero la UI no los consumirá todavía.
- Importar catalogos desde servicios externos, moderacion editorial nueva, busqueda avanzada o cambios del algoritmo de matching no necesarios para la migracion.
- Cambiar credenciales, autenticacion, 2FA, almacenamiento de tokens, permisos de ubicacion o el flujo de fotos.
- Eliminar datos existentes o esconder un reinicio de base de datos detrás de un comando de seed.

## 4. Reglas funcionales propuestas

- Cada anime y juego tendrá un identificador interno estable. El cliente envía y compara esos IDs, nunca el nombre localizado.
- Anime y juego conservarán tablas principales, tablas de nombre localizado y tablas de biografía localizada independientes. Las claves compuestas serán respectivamente `(anime_id, language_id)` y `(game_id, language_id)`.
- El idioma procede de una tabla de idiomas soportados y se identifica por un código único. La API intentará resolver el locale solicitado por la aplicación y, si no lo soporta o no encuentra la traducción, devolverá `es`. Los idiomas iniciales son `es` y `en`.
- La respuesta de catálogo y los intereses de un perfil contendrán el ID estable, el nombre localizado, la biografía localizada preparada y la referencia de imagen no traducible. En esta entrega Android almacenará esos últimos datos en sus modelos, pero no los renderizará ni permitirá editarlos.
- Las referencias de imagen de catálogo admiten URL absoluta o ruta relativa. El backend aplicará una única normalización al serializarlas, usando la misma configuración de origen que las fotografías, para que Android siempre reciba una URL cargable sin lógica de dominio duplicada.
- La migracion conservará todas las relaciones existentes de usuarios. El seed será repetible y no duplicará filas ni borrará datos.
- La tarjeta de descubrimiento tendrá como mínimo las secciones "Información básica" y "Gustos", que será una categoría independiente. Editar perfil mantendrá las mismas operaciones, agrupando residencia, fotos y gustos con etiquetas accesibles y localizadas. La residencia puede figurar como subsección de información básica si encaja mejor con la composición final.
- `username` seguirá siendo inmutable tras el registro y podrá viajar solo en la respuesta del perfil autenticado para validaciones internas de Android. Nunca se mostrará en UI ni se incluirá en tarjetas, perfiles de terceros, descubrimiento, matches o chat. Los DTOs públicos no expondrán correo, teléfono, hashes, secretos 2FA, tokens, coordenadas exactas, historial de ubicación ni opciones privadas de búsqueda. La decisión final de los demás campos se documentará antes de implementar.

## 5. Casuisticas mobile

- **Online y demo**: En modo online el catálogo localizado procede de la API. En demo, Room debe usar el mismo identificador y las mismas etiquetas localizadas disponibles en la app. La falta de catálogo no debe deseleccionar intereses guardados.
- **Sin conexión y errores**: La pantalla conservará la selección ya cargada. Si no puede refrescar el catálogo, mostrará el error localizado y una acción de reintento sin borrar cambios no enviados.
- **Ciclo de vida**: Las selecciones y el estado de formularios sobrevivirán a recreación y rotación mediante el estado existente y la persistencia apropiada. No se harán solicitudes desde composición sin una clave de ciclo de vida controlada.
- **Accesibilidad**: Secciones semánticas, objetivos táctiles de 48 dp, etiquetas localizadas, soporte claro y oscuro, `imePadding()` cuando corresponda y márgenes edge-to-edge.

## 6. Criterios de aceptación

### CA1: Identidad estable de intereses

- **Dado que**: un usuario tiene un anime o juego seleccionado.
- **Cuando**: el nombre localizado cambia o la aplicación usa otro idioma.
- **Entonces**: el interés sigue seleccionado y apunta al mismo identificador del catálogo.

### CA2: Catálogo localizado y extensible

- **Dado que**: existe un anime o un juego, con varias traducciones en idiomas soportados.
- **Cuando**: la API solicita un locale compatible.
- **Entonces**: devuelve la traducción correcta desde la tabla específica de su entidad, sin columnas específicas por idioma y sin mezclar anime y juego. Puede añadirse un idioma sin alterar ninguna tabla principal.

### CA3: Metadatos de catálogo sin presentación prematura

- **Dado que**: un anime o juego tiene biografías vacías y una ruta de imagen absoluta o relativa.
- **Cuando**: API y Android intercambian el catálogo.
- **Entonces**: los modelos conservan esos campos y la ruta queda normalizada a una URL cargable, sin crear pantalla, miniatura, edición ni texto visible de metadatos de catálogo.

### CA4: Migración de producción conservadora

- **Dado que**: una base desplegada contiene catálogo e intereses heredados.
- **Cuando**: se ejecuta la migración y después el seed.
- **Entonces**: no se pierde ninguna relación, el seed puede repetirse y no se ejecuta ningún reset implícito.

### CA5: Perfil seguro

- **Dado que**: un usuario consulta su perfil o ve el de otra persona.
- **Cuando**: la API construye la respuesta.
- **Entonces**: solo emite la proyección autorizada para ese contexto. `username` solo aparece para el propietario autenticado y nunca en perfiles de terceros, junto a la exclusión de datos sensibles o de ubicación exacta.

### CA6: Jerarquía de perfil

- **Dado que**: se muestra una tarjeta de descubrimiento o editar perfil.
- **Cuando**: el usuario la recorre con vista o lector de pantalla.
- **Entonces**: la información básica, residencia, fotos y gustos aparecen en secciones comprensibles sin perder funciones actuales.

## 7. Decisiones pendientes

- [x] Idiomas iniciales: `es` y `en`, con fallback a `es`.
- [x] Identidad de catálogo: `catalog_key` propio e inmutable. Los proveedores externos se resolverán en una tabla futura, sin acoplar el núcleo.
- [x] `username`: inmutable, incluido únicamente en el perfil autenticado para validaciones internas, y jamás mostrado ni enviado a proyecciones de terceros.
- [x] Despliegue: contrato nuevo reformado, sin capa de aceptación del formato heredado. La migración de datos existentes será parte obligatoria del despliegue.
- [x] Jerarquía visual: Información básica, Residencia y Gustos como categoría separada. Fotos dentro de Información básica; residencia podrá presentarse como subsección si mejora la composición.
- [x] Entidades y traducciones: anime y juego tienen tablas y claves compuestas separadas. No existe una tabla de catálogo común.
- [x] Metadatos de catálogo: biografías localizadas inicialmente vacías e imágenes no traducibles como URL absoluta o ruta relativa. Llegan a modelos Android, sin interfaz ni edición en esta entrega.
- [x] Especificación aprobada para crear el plan técnico el 2026-09-20.
