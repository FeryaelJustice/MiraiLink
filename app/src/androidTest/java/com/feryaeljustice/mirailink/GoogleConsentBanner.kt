package com.feryaeljustice.mirailink

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until

fun clickConsentBanner(textOfBtnToClick: String) {
    // 1. Obtener la referencia al dispositivo
    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    // 2. Esperar y buscar el botón de consentimiento por su texto (el texto real puede variar, ej. "Aceptar" o "Acepto")
    val buttonText = textOfBtnToClick // O "Aceptar y continuar", debes verificar el texto exacto

    val acceptButton =
        device.wait(
            Until.findObject(By.text(buttonText)),
            10000L, // Esperar hasta 10 segundos
        )

    // 3. Si lo encuentra, hacer click y esperar que desaparezca
    acceptButton?.click()

    // Opcional: esperar a que la UI de tu app se recupere
    device.waitForIdle(1000L)
}
