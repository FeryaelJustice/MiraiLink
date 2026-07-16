# Análisis del proyecto de referencia

Proyecto observado: MiCursoTestingAndroidAppcademy.

Regla de trabajo: se inspeccionaron archivos y configuración en solo lectura. No se modificó, creó ni eliminó ningún archivo y no se ejecutó ninguna tarea Gradle dentro del proyecto de referencia.

## Inventario observado

El proyecto de referencia contiene 59 archivos Kotlin de soporte o test repartidos entre src/test, src/androidTest y src/sharedTest. Su sourceSets añade src/sharedTest/kotlin a tests JVM e instrumentados.

## Librerías y herramientas

- JUnit 4
- MockK
- Turbine
- kotlinx-coroutines-test
- Compose UI Test
- MockWebServer
- Room en memoria
- Hilt Test
- Kover

MiraiLink mantiene JUnit 4 y usa Koin en lugar de Hilt. No se copió Hilt ni Room porque no pertenecen a su arquitectura actual. Se conservaron MockK, Turbine, coroutines-test, Compose, MockWebServer, Robolectric, Truth y Koin Test, que ya estaban versionados.

## Patrones trasladados

### Scheduler único

La regla del proyecto de referencia comparte un TestCoroutineScheduler entre dispatcher estándar y unconfined. MiraiLink ahora aplica el mismo principio en MainCoroutineRule.

### Datos de test reutilizables

El proyecto de referencia usa builders, object mothers, fakes y stubs. En MiraiLink se priorizan constructores mínimos locales cuando un modelo solo aparece en una clase, dobles compartidos para sesión y tema Compose, MockK cuando se debe verificar delegación y flujos reales para estado observable.

No se creó una object mother genérica para todos los DTO porque la suite actual ya contiene fixtures específicos por mapper, endpoint y repositorio. Centralizarlos sin una necesidad común aumentaría el acoplamiento.

### ViewModels

El patrón observado prueba estado inicial, loading, success, error, secuencia con Turbine, interacciones exactas, reintento y scheduler compartido.

Este patrón se aplicó al ViewModel de AI y a la infraestructura de errores. Los ViewModels existentes ya cubrían sus ramas principales.

### Integración

El proyecto de referencia combina MockWebServer, datasource real, repositorio real, base de datos en memoria y ViewModel real.

MiraiLink ya tenía pruebas de servicios Retrofit, datasources, repositorios, DataStore y Koin. La ampliación evita duplicar esos contratos y se centra en los huecos detectados.

### Compose

El proyecto de referencia usa ComponentActivity, regla Compose v2, pantalla aislada, estado controlado, tags o semántica estable y Robot Pattern cuando una pantalla acumula varios escenarios.

MiraiLink adopta la regla v2 y pantallas aisladas por grupos funcionales. Los helpers comunes evitan repetir tema y sesión. Se mantiene el E2E separado.

## Diferencias justificadas

| Referencia | MiraiLink | Motivo |
| --- | --- | --- |
| Hilt Test | Koin Test o parámetros directos | DI real del proyecto |
| Room en memoria | DataStore instrumentado | No existe Room |
| Kover | Sin cambio de plugin | Añadir un umbral no ejecutado ni validado sería engañoso |
| Robot por pantalla compleja | Helpers compartidos y tests funcionales agrupados | Los escenarios añadidos son cortos |
| Fakes de repositorio | MockK en huecos nuevos | Se verifica delegación puntual, no un almacén con comportamiento |

## Resultado de la adaptación

Se conservaron las ideas, no las dependencias ni clases que no corresponden:

- Infraestructura compartida.
- Corrutinas deterministas.
- Test doubles herméticos.
- Tests unitarios de capas.
- Pruebas Compose aisladas.
- E2E separado.
- Documentación de cobertura, límites y uso.
