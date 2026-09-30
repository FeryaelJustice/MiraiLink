# Checklist de Tareas: Rediseño Inmersivo del Onboarding MiraiLink

- [x] **Fase 1: Limpieza de Recursos Físicos y Actualización de Strings**
  - [x] Eliminar físicamente los archivos `app/src/main/res/drawable/onboarding_1.webp`, `onboarding_2.webp` y `onboarding_3.webp`.
  - [x] Actualizar `app/src/main/res/values/strings.xml` con los nuevos títulos, descripciones, "Omitir" y "¡Comenzar Aventura!".
  - [x] Actualizar `app/src/main/res/values-es/strings.xml` con las mismas claves y traducciones en español.
  - [x] Actualizar `app/src/main/res/values-en/strings.xml` con las mismas claves y traducciones en inglés.

- [x] **Fase 2: Componentes Visuales Nativos y Átomos de UI**
  - [x] Crear `OnboardingPillIndicator.kt` en `ui/screens/onboarding/components/` con animación suave de píldora expandida.
  - [x] Crear `OnboardingIllustrations.kt` en `ui/screens/onboarding/components/` implementando las 3 ilustraciones vectoriales nativas en Compose (`RADAR_CONNECTION`, `PROFILE_SHOWCASE`, `INSTANT_CHAT`) con Canvas, gradientes y micro-detalles anime/gaming.

- [x] **Fase 3: Refactorización Integral de `OnboardingScreen.kt`**
  - [x] Definir el modelo de datos de pasos (`OnboardingStep`).
  - [x] Añadir barra superior con botón "Omitir" / "Skip" que invoque `onFinish()`.
  - [x] Integrar el `HorizontalPager` con las ilustraciones nativas y la nueva jerarquía tipográfica (Headline + Body).
  - [x] Implementar la barra inferior de navegación con `OnboardingPillIndicator`, botón "Anterior" y botón principal de avance/comenzar.
  - [x] Añadir previews para modo claro, modo oscuro y dispositivos compactos/tablets.

- [x] **Fase 4: Verificación, Tests y Calidad de Código**
  - [x] Ejecutar tests unitarios existentes (`OnboardingRepositoryImplTest`, `CheckOnboardingIsCompletedTest`, etc.).
  - [x] Compilar el proyecto con `./gradlew assembleDebug` y verificar ausencia total de advertencias o errores.
  - [x] Comprobar formato y lint con `./gradlew ktlintCheck` o análisis estático.
