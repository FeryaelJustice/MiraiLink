package com.feryaeljustice.mirailink.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.domain.model.Anime
import com.feryaeljustice.mirailink.domain.model.Game
import com.feryaeljustice.mirailink.domain.model.User
import com.feryaeljustice.mirailink.ui.utils.extensions.shadow

@Composable
fun UserCard(
    user: User,
    canUndo: Boolean = false,
    modifier: Modifier = Modifier,
    isPreviewMode: Boolean = false,
    onLike: (() -> Unit)? = null,
    onGoBackToLast: (() -> Unit)? = null,
    onDislike: (() -> Unit)? = null,
    onEdit: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .shadow(
                color = MaterialTheme.colorScheme.onSurface,
                alpha = 0.5f,
                offsetX = (4).dp,
                offsetY = (4).dp,
                blurRadius = 4.dp,
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        border = BorderStroke(width = 2.dp, MaterialTheme.colorScheme.onSurface)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                UserPhotoCarousel(photoUrls = user.photos.map { it.url })

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = user.username.uppercase(),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        if (!user.bio.isNullOrBlank()) user.bio else "Aquí para triunfar",
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Secciones: anime y videojuegos
                    user.animes.takeIf { it.isNotEmpty() }?.let { animes ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Animes favoritos:", fontWeight = FontWeight.SemiBold)

                        TagsSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            tags = animes.map { it.title })

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    user.games.takeIf { it.isNotEmpty() }?.let { games ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Videojuegos favoritos:", fontWeight = FontWeight.SemiBold)

                        TagsSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            tags = games.map { it.title })

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(240.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isPreviewMode) {
                    Button(
                        onClick = { onEdit?.invoke() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Editar perfil")
                    }
                } else {
                    OutlinedButton(
                        onClick = { onDislike?.invoke() },
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Blue),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier
                            .size(64.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Descartar",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(32.dp))

                    if (canUndo) {
                        OutlinedButton(
                            onClick = { onGoBackToLast?.invoke() },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Yellow),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, Color.Black),
                            modifier = Modifier
                                .size(64.dp)
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Volver",
                                tint = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(32.dp))
                    }

                    OutlinedButton(
                        onClick = { onLike?.invoke() },
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Red),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier
                            .size(64.dp)
                    ) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Me gusta",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserCardPreview() {
    UserCard(
        user = User(
            id = "1",
            username = "FeryaelJustice",
            bio = "Hola, soy Feryael Justice. Soy un fanático de anime y videojuegos. Me gustan los personajes y la diversidad de temas en estos juegos.",
            phoneNumber = "604892842",
            animes = listOf(
                Anime(
                    title = "Naruto",
                    imageUrl = null,
                    description = "Naruto es un personaje fantástico y el protagonista del mundo de Naruto"
                ),
                Anime(
                    title = "One Punch Man",
                    imageUrl = null,
                    description = "One Punch Man, el protagonista del mundo de One Punch Man"
                ),
                Anime(
                    title = "Dragon Ball Z",
                    imageUrl = null,
                    description = "Dragon Ball Z, el anime por excelencia que ha liderado."
                )
            ),
            games = listOf(
                Game(
                    title = "Final Fantasy VII",
                    imageUrl = null,
                    description = "Una aventura en el mundo de Final Fantasy"
                ),
                Game(
                    title = "Soul Calibur V",
                    imageUrl = null,
                    description = "Soul Calibur V, el videojuego de acción y aventura"
                ),
                Game(
                    title = "The Legend of Zelda: Breath of the Wild",
                    imageUrl = null,
                    description = "Un juego de aventuras en el mundo de The Legend of Zelda"
                )
            ),
            email = "adj@jormail.com",
            gender = "Mujer",
            birthdate = "23-12"
        ),
        isPreviewMode = true,
        onLike = {},
        onDislike = {},
        onEdit = {}
    )
}