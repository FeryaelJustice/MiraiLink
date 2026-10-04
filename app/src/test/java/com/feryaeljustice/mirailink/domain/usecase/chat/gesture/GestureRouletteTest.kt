package com.feryaeljustice.mirailink.domain.usecase.chat.gesture

import com.feryaeljustice.mirailink.domain.model.chat.gesture.FaceGestureType
import com.feryaeljustice.mirailink.domain.model.chat.gesture.GestureChallengeSummary
import com.feryaeljustice.mirailink.domain.model.chat.gesture.GestureMessageParsed
import com.feryaeljustice.mirailink.domain.model.chat.gesture.GestureMessagePayload
import com.feryaeljustice.mirailink.domain.model.studio.FaceBiometrics
import com.feryaeljustice.mirailink.domain.model.studio.NormalizedRect
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GestureRouletteTest {

    private val evaluateUseCase = EvaluateFaceGestureUseCase()
    private val compatibilityUseCase = CalculateGestureCompatibilityUseCase()

    private fun dummyFace(
        leftEyeOpen: Float = 1.0f,
        rightEyeOpen: Float = 1.0f,
        smile: Float = 0.0f,
        eulerZ: Float = 0.0f,
        eulerY: Float = 0.0f,
    ) = FaceBiometrics(
        boundingBox = NormalizedRect(0.2f, 0.2f, 0.8f, 0.8f),
        headEulerAngleY = eulerY,
        headEulerAngleZ = eulerZ,
        headEulerAngleX = 0f,
        smilingProbability = smile,
        leftEyeOpenProbability = leftEyeOpen,
        rightEyeOpenProbability = rightEyeOpen,
    )

    @Test
    fun evaluateFaceGesture_nullFace_returnsFalse() {
        assertFalse(evaluateUseCase(null, FaceGestureType.BIG_SMILE))
        assertFalse(evaluateUseCase(null, FaceGestureType.WINK_LEFT))
    }

    @Test
    fun evaluateFaceGesture_winkLeft_detectedCorrectly() {
        val winkingLeft = dummyFace(leftEyeOpen = 0.1f, rightEyeOpen = 0.9f)
        assertTrue(evaluateUseCase(winkingLeft, FaceGestureType.WINK_LEFT))

        val bothOpen = dummyFace(leftEyeOpen = 0.8f, rightEyeOpen = 0.8f)
        assertFalse(evaluateUseCase(bothOpen, FaceGestureType.WINK_LEFT))
    }

    @Test
    fun evaluateFaceGesture_winkRight_detectedCorrectly() {
        val winkingRight = dummyFace(leftEyeOpen = 0.85f, rightEyeOpen = 0.15f)
        assertTrue(evaluateUseCase(winkingRight, FaceGestureType.WINK_RIGHT))

        val bothClosed = dummyFace(leftEyeOpen = 0.1f, rightEyeOpen = 0.1f)
        assertFalse(evaluateUseCase(bothClosed, FaceGestureType.WINK_RIGHT))
    }

    @Test
    fun evaluateFaceGesture_bigSmile_detectedCorrectly() {
        val smiling = dummyFace(smile = 0.85f)
        assertTrue(evaluateUseCase(smiling, FaceGestureType.BIG_SMILE))

        val neutral = dummyFace(smile = 0.2f)
        assertFalse(evaluateUseCase(neutral, FaceGestureType.BIG_SMILE))
    }

    @Test
    fun evaluateFaceGesture_tiltHead_detectedCorrectly() {
        val tiltedLeft = dummyFace(eulerZ = 22f)
        assertTrue(evaluateUseCase(tiltedLeft, FaceGestureType.TILT_HEAD))

        val tiltedRight = dummyFace(eulerZ = -20f)
        assertTrue(evaluateUseCase(tiltedRight, FaceGestureType.TILT_HEAD))

        val straight = dummyFace(eulerZ = 5f)
        assertFalse(evaluateUseCase(straight, FaceGestureType.TILT_HEAD))
    }

    @Test
    fun calculateGestureCompatibility_fastFullScore_returnsHighCompatibility() {
        val summary = compatibilityUseCase(score = 4, totalGestures = 4, completionTimeSeconds = 6.2f)
        assertEquals(99, summary.compatibilityPercentage)
        assertEquals("¡Chispa Electrica!", summary.funTitle)
        assertEquals(4, summary.score)
    }

    @Test
    fun calculateGestureCompatibility_partialScore_returnsExpectedRating() {
        val summary = compatibilityUseCase(score = 2, totalGestures = 4, completionTimeSeconds = 15.0f)
        assertEquals(82, summary.compatibilityPercentage)
        assertEquals("¡Risas Aseguradas!", summary.funTitle)
    }

    @Test
    fun gestureMessagePayload_formatAndParseInvite() {
        val inviteText = GestureMessagePayload.formatInvite()
        val parsed = GestureMessagePayload.parse(inviteText)
        assertTrue(parsed is GestureMessageParsed.Invite)
    }

    @Test
    fun gestureMessagePayload_formatAndParseResult() {
        val summary = GestureChallengeSummary(
            score = 4,
            totalGestures = 4,
            completionTimeSeconds = 8.5f,
            compatibilityPercentage = 96,
            funTitle = "¡Quimica Instantanea!",
            description = "Gran sincronia de gestos",
        )
        val resultText = GestureMessagePayload.formatResult(summary)
        val parsed = GestureMessagePayload.parse(resultText)

        assertTrue(parsed is GestureMessageParsed.Result)
        val parsedSummary = (parsed as GestureMessageParsed.Result).summary
        assertEquals(4, parsedSummary.score)
        assertEquals(96, parsedSummary.compatibilityPercentage)
        assertEquals("¡Quimica Instantanea!", parsedSummary.funTitle)
    }

    @Test
    fun gestureMessagePayload_regularMessage_returnsRegular() {
        val parsed = GestureMessagePayload.parse("Hola! Como estas?")
        assertTrue(parsed is GestureMessageParsed.Regular)
    }
}
