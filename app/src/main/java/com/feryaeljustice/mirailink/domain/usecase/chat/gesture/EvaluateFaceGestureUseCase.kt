package com.feryaeljustice.mirailink.domain.usecase.chat.gesture

import com.feryaeljustice.mirailink.domain.model.chat.gesture.FaceGestureType
import com.feryaeljustice.mirailink.domain.model.studio.FaceBiometrics
import kotlin.math.abs

class EvaluateFaceGestureUseCase {

    operator fun invoke(face: FaceBiometrics?, gesture: FaceGestureType): Boolean {
        if (face == null) return false

        return when (gesture) {
            FaceGestureType.WINK_LEFT -> {
                val leftOpen = face.leftEyeOpenProbability ?: 1f
                val rightOpen = face.rightEyeOpenProbability ?: 0f
                leftOpen <= 0.30f && rightOpen >= 0.65f
            }

            FaceGestureType.BIG_SMILE -> {
                val smile = face.smilingProbability ?: 0f
                smile >= 0.65f
            }

            FaceGestureType.WINK_RIGHT -> {
                val rightOpen = face.rightEyeOpenProbability ?: 1f
                val leftOpen = face.leftEyeOpenProbability ?: 0f
                rightOpen <= 0.30f && leftOpen >= 0.65f
            }

            FaceGestureType.TILT_HEAD -> {
                abs(face.headEulerAngleZ) >= 16f
            }
        }
    }
}
