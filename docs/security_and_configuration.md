# Guia de Configuracion y Seguridad de Red (MiraiLink Android)

Este documento detalla como configurar de manera sencilla y centralizada los dominios permitidos, la conexion al backend y la seguridad de red en la aplicacion Android.

---

## 1. Archivo de Configuracion Local: `local.properties`

En la raiz del proyecto `MiraiLink`, el archivo `local.properties` (que no se sube a control de versiones Git para preservar secretos locales) contiene las variables principales:

```properties
# URL base del backend de MiraiLink (puede ser localhost, IP local, tunel Cloudflare o dominio de produccion)
mirailink.baseUrl=http://192.168.1.137:3000

# Dominios autorizados para descarga de imagenes remotas en Coil (separados por comas)
mirailink.imageAllowedDomains=mirailink.xyz,cdn.myanimelist.net,media.rawg.io,images.igdb.com,images.unsplash.com,10.0.2.2,10.0.3.2,localhost,127.0.0.1,192.168.1.137,trycloudflare.com
```

### ¿Donde tocar para anadir un nuevo dominio de imagenes?
Si el backend o la app necesitan mostrar imagenes de un nuevo proveedor CDN (por ejemplo Cloudinary, Imgur, AWS S3 o un nuevo dominio VPS):
1. Abre `local.properties`.
2. Anade el dominio a la lista en `mirailink.imageAllowedDomains`, separado por comas (ej. `,mi-servidor.com`).
3. Recompila la aplicacion. `app/build.gradle.kts` inyecta automaticamente este valor en `BuildConfig.IMAGE_ALLOWED_DOMAINS`.

---

## 2. Whitelist e Interceptor de Seguridad de Imagenes: `ImageDomainSecurityInterceptor`

Ubicado en:
`app/src/main/java/com/feryaeljustice/mirailink/data/remote/interceptor/ImageDomainSecurityInterceptor.kt`

- **Funcionamiento**: Intercepta cada peticion de descarga de imagen realizada por `Coil` (`ImageLoader`).
- **Validacion**: Comprueba si el host de la imagen coincide exactamente con alguno de los dominios permitidos o es un subdominio valido (ej. `api.mirailink.xyz` o `img.trycloudflare.com`).
- **Fallback**: Si una URL apunta a un dominio no autorizado, la peticion se rechaza con una excepcion de seguridad controlada, provocando que Coil active el fallback visual (`InterestImageFallback`).
- **Inyeccion de dependencias**: Registrado en `NetworkModule.kt` y suministrado al `ImageLoaderFactory` en `MiraiLinkApp.kt`.

---

## 3. Politica de Red del Sistema: `network_security_config.xml`

Ubicado en:
`app/src/main/res/xml/network_security_config.xml`

- **HTTPS Estricto por Defecto**:
  `<base-config cleartextTrafficPermitted="false">`: Toda conexion externa de la aplicacion debe ser cifrada via HTTPS.
- **Excepciones para Desarrollo Local**:
  `<domain-config cleartextTrafficPermitted="true">`:
  - `localhost`
  - `127.0.0.1`
  - `10.0.2.2` (emulador oficial de Android)
  - `10.0.3.2` (emulador Genymotion)
  - `192.168.1.137` (IP de depuracion en dispositivo fisico sobre red WiFi local)

---

## 4. Tests y Verificacion

- Tests unitarios del interceptor:
  `app/src/test/java/com/feryaeljustice/mirailink/data/remote/interceptor/ImageDomainSecurityInterceptorTest.kt`
  - Verifica que dominios exactos y subdominios autorizados se cargan correctamente.
  - Verifica que dominios no autorizados son bloqueados de forma segura.
