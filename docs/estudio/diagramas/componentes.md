# Contexto, componentes y dependencias del cliente

[Índice](indice.md) | [Guía maestra](../../guia-maestra.md)

Tipo: contexto/componentes; representación estructural. Revisión: 2026-10-01. Fuentes: [app/src/main/java/com/feryaeljustice/mirailink/MiraiLinkApp.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/MiraiLinkApp.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

```mermaid
flowchart LR
    Persona --> Compose
    subgraph ClienteAndroid
        Compose --> ViewModel
        ViewModel --> CasosDeUso
        CasosDeUso --> ContratosRepositorio
        ContratosRepositorio --> Delegado
        Koin -. construye .-> ViewModel
        Koin -. construye .-> Delegado
        Delegado -->|remoto| Retrofit
        Delegado -->|demo| Room
        ViewModel --> SessionManager
        SessionManager --> DataStoreCifrado
        DataStoreCifrado --> Keystore
        Coil --> ClienteImagenes
        Studio --> CameraX
        Studio --> MLKit
    end
    Retrofit -->|HTTP JSON y multipart| Backend
    ClienteImagenes -->|allowlist| MediaPublica
    FirebaseAI --> Firebase
    BillingClient --> GooglePlay
    UMPyAds --> AdMob
```

Los repositorios delegados no cubren todos los contratos. Las flechas son dependencias y llamadas, no una garantía de aislamiento de capas. Backend, Firebase y Google Play son servicios distintos. SocketService existe como infraestructura y no describe el polling visible.
