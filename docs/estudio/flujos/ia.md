# Chat IA y estado de conversación

[Guía maestra](../../guia-maestra.md) | [Mapa funcional](../funcionalidades.md)

Revisión: 2026-10-01, basada en código.

## Recorrido

[AiChatViewModel.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatViewModel.kt) rechaza texto vacío o envío durante Loading, añade el prompt recortado a la lista visible y llama GenerateContentUseCase en ioDispatcher. El texto enviado al use case es el prompt original. [GeminiDataSource.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/GeminiDataSource.kt) usa GenerativeModel.generateContent(prompt); no inicia una sesión startChat ni envía mensajes anteriores.

[AiModule.kt](../../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/AiModule.kt) crea el modelo de Firebase AI con GenerativeBackend.googleAI() y el nombre leído de Remote Config. El backend MiraiLink no procesa este prompt ni tiene un endpoint IA en el router revisado. Un cambio de integración debe revisar la guía del servidor para confirmar esta separación.

## Alternativas y conservación

Una respuesta sin text se transforma en cadena vacía. Un error activa AiChatUiState.Error y la acción de reintento, conservando el mensaje ya añadido. Reintentar vuelve a añadir el prompt visible. La lista es memoria del ViewModel, sin persistencia de conversación en PostgreSQL o Room observada en este recorrido.

La configuración del modelo se captura al construir el singleton; no asumir reconstrucción automática del modelo después de cada fetch. Los bindings de AiRepository aparecen también en RepositoryModule, por lo que debe analizarse su orden antes de modificarlos.

## Evidencia

Los tests de AiChatViewModel y AiRepository cubren fakes/mocks. No verifican disponibilidad del modelo, cuotas, políticas ni respuesta de Firebase AI real.
