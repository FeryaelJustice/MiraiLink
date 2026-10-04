package com.feryaeljustice.mirailink.domain.usecase.chat.gesture

import com.feryaeljustice.mirailink.domain.model.chat.gesture.GestureChallengeSummary

class CalculateGestureCompatibilityUseCase {

    operator fun invoke(
        score: Int,
        totalGestures: Int = 4,
        completionTimeSeconds: Float,
    ): GestureChallengeSummary {
        val safeScore = score.coerceIn(0, totalGestures)
        val (percentage, title, description) = when (safeScore) {
            4 -> when {
                completionTimeSeconds <= 7.5f -> Triple(
                    99,
                    "¡Chispa Electrica!",
                    "¡Reflejos nivel dios! Conexion y quimica instantanea comprobada.",
                )
                completionTimeSeconds <= 12.0f -> Triple(
                    96,
                    "¡Quimica Instantanea!",
                    "Completaron la ruleta con gran agilidad y sincronia.",
                )
                else -> Triple(
                    92,
                    "¡Sintonia Perfecta!",
                    "¡Reto superado justo a tiempo! Buena vibra compartida.",
                )
            }
            3 -> Triple(
                88,
                "¡Casi Telepatia!",
                "3 de 4 gestos completados. ¡A un pestañeo de la perfeccion!",
            )
            2 -> Triple(
                82,
                "¡Risas Aseguradas!",
                "La diversion rompio el hielo. ¿Listos para una revancha?",
            )
            1 -> Triple(
                78,
                "¡Buen Intento!",
                "La camara capturo el espiritu del juego. ¡A practicar esos guiños!",
            )
            else -> Triple(
                73,
                "¡El Tiempo Volo!",
                "Se agoto el temporizador pero sobro la diversion para empezar a charlar.",
            )
        }

        return GestureChallengeSummary(
            score = safeScore,
            totalGestures = totalGestures,
            completionTimeSeconds = completionTimeSeconds,
            compatibilityPercentage = percentage,
            funTitle = title,
            description = description,
        )
    }
}
