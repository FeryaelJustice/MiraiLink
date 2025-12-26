package com.feryaeljustice.mirailink.di.koin

import com.feryaeljustice.mirailink.core.remoteconfig.RemoteConfigManager
import com.feryaeljustice.mirailink.data.datasource.GeminiDataSource
import com.feryaeljustice.mirailink.data.repository.AiRepositoryImpl
import com.feryaeljustice.mirailink.domain.repository.AiRepository
import com.feryaeljustice.mirailink.domain.usecase.ai.GenerateContentUseCase
import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import org.koin.dsl.module

val aiModule =
    module {
        // Proporciona la instancia del modelo GenerativeModel de Gemini
        single<GenerativeModel> {
            // Obtenemos la instancia de RemoteConfigManager inyectada por Koin
            val remoteConfigManager: RemoteConfigManager = get()
            // Obtenemos el nombre del modelo dinámicamente
            val modelName = remoteConfigManager.getGeminiModelName()

            Firebase
                .ai(backend = GenerativeBackend.googleAI())
                .generativeModel(modelName = modelName)
        }

        // Datasource
        single { GeminiDataSource(get()) }

        // Repository
        single<AiRepository> { AiRepositoryImpl(get()) }

        // UseCase
        factory { GenerateContentUseCase(get()) }
    }
