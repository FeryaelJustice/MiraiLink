# Guía de Configuración y Seguridad de Red (MiraiLink Android)

Este documento detalla como configurar de manera sencilla y centralizada los dominios permitidos, la conexión al backend y la seguridad de red en la aplicación Android.

---

## 1. Archivo de Configuración Local: `local.properties`

En la raíz del proyecto `MiraiLink`, el archivo `local.properties` (que no se sube a control de versiones Git para preservar secretos locales) contiene las variables principales:

```properties
# URL base del backend de MiraiLink (puede ser localhost, IP local, túnel Cloudflare o dominio de producción)
mirailink.baseUrl=http://192.168.1.137:3000

# Dominios autorizados para descarga de imágenes remotas en Coil (separados por comas)
mirailink.imageAllowedDomains=mirailink.xyz,cdn.myanimelist.net,media.rawg.io,images.igdb.com,images.unsplash.com,alphacoders.com,openstreetmap.org,10.0.2.2,10.0.3.2,localhost,127.0.0.1,192.168.1.137,trycloudflare.com
```

### ¿Donde tocar para añadir un nuevo dominio de imágenes?
Si el backend o la app necesitan mostrar imágenes de un nuevo proveedor CDN (por ejemplo Cloudinary, Imgur, AWS S3 o un nuevo dominio VPS):
1. Abre `local.properties`.
2. Añade el dominio a la lista en `mirailink.imageAllowedDomains`, separado por comas (ej. `,mi-servidor.com`).
3. Recompila la aplicación. `app/build.gradle.kts` inyecta automáticamente este valor en `BuildConfig.IMAGE_ALLOWED_DOMAINS`.

---

## 2. Whitelist e Interceptor de Seguridad de Imágenes: `ImageDomainSecurityInterceptor`

Ubicado en:
`app/src/main/java/com/feryaeljustice/mirailink/data/remote/interceptor/ImageDomainSecurityInterceptor.kt`

- **Funcionamiento**: Intercepta cada petición de descarga de imagen realizada por `Coil` (`ImageLoader`).
- **Validación**: Comprueba si el host de la imagen coincide exactamente con alguno de los dominios permitidos o es un subdominio válido (ej. `api.mirailink.xyz` o `img.trycloudflare.com`).
- **Fallback**: Si una URL apunta a un dominio no autorizado, la petición se rechaza con una excepción de seguridad controlada, provocando que Coil active el fallback visual (`InterestImageFallback`).
- **Inyección de dependencias**: Registrado en `NetworkModule.kt` y suministrado al `ImageLoaderFactory` en `MiraiLinkApp.kt`.

---

## 3. Política de Red del Sistema: `network_security_config.xml`

Ubicado en:
`app/src/main/res/xml/network_security_config.xml`

- **HTTPS Estricto por Defecto**:
  `<base-config cleartextTrafficPermitted="false">`: Toda conexión externa de la aplicación debe ser cifrada vía HTTPS.
- **Excepciones para Desarrollo Local**:
  `<domain-config cleartextTrafficPermitted="true">`:
  - `localhost`
  - `127.0.0.1`
  - `10.0.2.2` (emulador oficial de Android)
  - `10.0.3.2` (emulador Genymotion)
  - `192.168.1.137` (IP de depuración en dispositivo físico sobre red WiFi local)

---

## 4. Tests y Verificación

- Tests unitarios del interceptor:
  `app/src/test/java/com/feryaeljustice/mirailink/data/remote/interceptor/ImageDomainSecurityInterceptorTest.kt`
  - Verifica que dominios exactos y subdominios autorizados se cargan correctamente.
  - Verifica que dominios no autorizados son bloqueados de forma segura.
