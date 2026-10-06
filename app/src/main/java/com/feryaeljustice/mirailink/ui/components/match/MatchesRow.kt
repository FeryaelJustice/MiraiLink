package com.feryaeljustice.mirailink.ui.components.match

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme
import com.feryaeljustice.mirailink.ui.viewentries.user.MatchUserViewEntry

@Suppress("ktlint:standard:function-naming")
@Composable
fun MatchesRow(
    modifier: Modifier = Modifier,
    matches: List<MatchUserViewEntry>,
    onNavigateToChat: (String) -> Unit,
) {
    Column(modifier = modifier.padding(PaddingValues(horizontal = 16.dp, vertical = 16.dp))) {
        MiraiLinkText(
            text = stringResource(R.string.matches),
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.titleLarge,
        )

        Row {
            if (matches.isEmpty()) {
                MiraiLinkText(
                    text = stringResource(R.string.matches_empty),
                    style = MaterialTheme.typography.bodySmall,
                )
            } else {
                matches.forEach { user ->
                    MatchCard(
                        modifier =
                            Modifier
                                .padding(horizontal = 8.dp)
                                .onVisibilityChanged(callback = {
                                    Log.d("MatchesRow", "Visibility changed: ${user.username}")
                                }),
                        photoPresentation = user.photoPresentation,
                    userAvatarUrl = user.avatarUrl,
                        userIsBoosted = user.isBoosted,
                        userUsername = user.username,
                        userNickname = user.nickname,
                        onClick = { onNavigateToChat(user.id) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MatchesRowPreview() {
    MiraiLinkTheme {
        MatchesRow(
            matches =
                listOf(
                    MatchUserViewEntry(
                        id = "1",
                        username = "sakura",
                        nickname = "Sakura",
                        avatarUrl = "",
                        isBoosted = true,
                    ),
                    MatchUserViewEntry(
                        id = "2",
                        username = "hinata",
                        nickname = "Hinata",
                        avatarUrl = "",
                    ),
                ),
            onNavigateToChat = {},
        )
    }
}
