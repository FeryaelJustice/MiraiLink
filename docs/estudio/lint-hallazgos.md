# Resultado de lint Android

[Guía maestra](../guia-maestra.md) | [Verificación](desarrollo-y-verificacion.md). Ejecución: 2026-10-01.

Resultado: 78 errores y 167 warnings, por lo que lintDebug falla. Snapshot del XML local generado por Gradle, sin copiar valores privados. No se ha creado una baseline ni modificado código para ocultarlos.

El manifest y recursos no se han modificado en esta rama. La comparación de fuentes Kotlin acredita cambios solo de comentarios, no una nueva ejecución de lint sobre HEAD previo. Por ello se registran como hallazgos existentes en el código inspeccionado, sin atribuirlos a comentarios ni afirmar comparación de dos ejecuciones.

| Severidad | ID | Apariciones |
| --- | --- | --- |
| Warning | UnusedResources | 67 |
| Error | MissingTranslation | 62 |
| Warning | Typos | 41 |
| Warning | TypographyEllipsis | 16 |
| Warning | GradleDependency | 9 |
| Error | LocalContextGetResourceValueCall | 8 |
| Warning | NewerVersionAvailable | 7 |
| Warning | PrivateResource | 5 |
| Error | StringFormatInvalid | 5 |
| Warning | UseKtx | 5 |
| Warning | PluralsCandidate | 4 |
| Warning | IconLocation | 4 |
| Warning | ObsoleteSdkInt | 3 |
| Warning | UnknownIssueId | 2 |
| Error | NonObservableLocale | 2 |
| Warning | ModifierParameter | 2 |
| Error | CredManMissingDal | 1 |
| Warning | CredentialManagerMisuse | 1 |
| Warning | DiscouragedApi | 1 |

## Ubicaciones de errores

| ID | Archivo y línea |
| --- | --- |
| CredManMissingDal | [app/src/main/AndroidManifest.xml:20](../../app/src/main/AndroidManifest.xml) |
| NonObservableLocale | [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt:256](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt) |
| NonObservableLocale | [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt:587](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt) |
| LocalContextGetResourceValueCall | [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt:196](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt) |
| LocalContextGetResourceValueCall | [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt:513](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt) |
| LocalContextGetResourceValueCall | [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt:517](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt) |
| LocalContextGetResourceValueCall | [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesScreen.kt:73](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesScreen.kt) |
| LocalContextGetResourceValueCall | [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesScreen.kt:178](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesScreen.kt) |
| LocalContextGetResourceValueCall | [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallScreen.kt:84](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallScreen.kt) |
| LocalContextGetResourceValueCall | [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallScreen.kt:93](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallScreen.kt) |
| LocalContextGetResourceValueCall | [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt:113](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt) |
| StringFormatInvalid | [app/src/main/res/values-es/strings.xml:306](../../app/src/main/res/values-es/strings.xml) |
| StringFormatInvalid | [app/src/main/res/values-en/strings.xml:309](../../app/src/main/res/values-en/strings.xml) |
| StringFormatInvalid | [app/src/main/res/values/strings.xml:312](../../app/src/main/res/values/strings.xml) |
| StringFormatInvalid | [app/src/main/res/values/strings.xml:312](../../app/src/main/res/values/strings.xml) |
| StringFormatInvalid | [app/src/main/res/values/strings.xml:312](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:74](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:272](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:273](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:291](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:316](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:317](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:374](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:375](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:376](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:377](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:378](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:475](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:476](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:477](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:478](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:479](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:480](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:481](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:482](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:483](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:486](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:487](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:488](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:489](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:490](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:491](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:492](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:493](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:494](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:495](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:496](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:497](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:498](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:499](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:502](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:503](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:504](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:505](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:506](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:507](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:508](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:509](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:510](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:511](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:512](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:513](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:514](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:515](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:516](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:517](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:518](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:519](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:520](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:521](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:522](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:523](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:524](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:525](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:526](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:527](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:528](../../app/src/main/res/values/strings.xml) |
| MissingTranslation | [app/src/main/res/values/strings.xml:529](../../app/src/main/res/values/strings.xml) |

El informe HTML completo se genera en app/build/reports/lint-results-debug.html y no se versiona. Los tests JVM que pasan no sustituyen esta comprobación.
