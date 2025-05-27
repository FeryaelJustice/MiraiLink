package com.feryaeljustice.mirailink.ui.screens.auth.verification

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VerificationScreen(
    viewModel: VerificationViewModel,
    userId: String,
    onFinish: () -> Unit,
) {
    val uiState by viewModel.state.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        when (uiState.step) {
            1 -> {
                Text("Solicitar verificación a email")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { viewModel.requestCode(userId) }) { Text("Enviar código") }
            }

            2 -> {
                OutlinedTextField(
                    value = uiState.token,
                    onValueChange = viewModel::onTokenChanged,
                    label = { Text("Código") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    viewModel.confirmCode(
                        userId,
                        onFinish = onFinish
                    )
                }) { Text("Verificar") }
            }
        }

        if (uiState.error != null) Text(uiState.error!!, color = Color.Red)
    }

    BackHandler(enabled = true) { Log.i("OnBack", "Clicked back on Verification Screen") }
}