package com.feryaeljustice.mirailink.data.local.demo

import com.feryaeljustice.mirailink.data.local.demo.entity.DemoChatEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoFeedUserEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoMatchEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoUserProfileEntity
import com.feryaeljustice.mirailink.domain.model.catalog.Anime
import com.feryaeljustice.mirailink.domain.model.catalog.Game
import com.feryaeljustice.mirailink.domain.model.user.UserPhoto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

class DemoDataSeeder(
    private val database: MiraiLinkDemoDatabase,
    private val json: Json = Json { ignoreUnknownKeys = true },
) {
    companion object {
        const val DEMO_USER_ID = "demo_user_id"
    }

    suspend fun seedInitialDataIfEmpty() {
        val existingProfile = database.userDao().getUserProfile(DEMO_USER_ID)
        if (existingProfile == null) {
            resetDemoData()
        }
    }

    suspend fun resetDemoData() {
        // 1. Limpiar todas las tablas demo
        database.userDao().clearUserProfile()
        database.userDao().clearFeedUsers()
        database.matchDao().clearMatches()
        database.chatDao().clearChats()
        database.chatDao().clearMessages()

        // 2. Sembrar Perfil del Usuario Demo
        val userAnimes = listOf(
            Anime("a1", "Frieren: Beyond Journey's End", "https://cdn.myanimelist.net/images/anime/1015/138006.jpg"),
            Anime("a2", "Steins;Gate", "https://cdn.myanimelist.net/images/anime/1935/127974.jpg"),
            Anime("a3", "Fullmetal Alchemist: Brotherhood", "https://cdn.myanimelist.net/images/anime/1208/94745.jpg"),
        )
        val userGames = listOf(
            Game("g1", "Persona 5 Royal", "https://images.igdb.com/igdb/image/upload/t_cover_big/co1r7f.png"),
            Game("g2", "The Legend of Zelda: Tears of the Kingdom", "https://images.igdb.com/igdb/image/upload/t_cover_big/co5vmg.png"),
            Game("g3", "Elden Ring", "https://images.igdb.com/igdb/image/upload/t_cover_big/co4jni.png"),
        )
        val userPhotos = listOf(
            UserPhoto(DEMO_USER_ID, "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=500", 1),
            UserPhoto(DEMO_USER_ID, "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=500", 2),
        )

        val profile = DemoUserProfileEntity(
            id = DEMO_USER_ID,
            username = "hikari_demo",
            nickname = "Hikari",
            email = "hikari.demo@mirailink.local",
            phoneNumber = null,
            bio = "¡Hola! Me encantan los JRPGs, el anime de fantasía y escuchar openings a todo volumen. Buscando gente para jugar y charlar sobre la nueva temporada de anime ✨",
            gender = "Mujer",
            birthdate = "2003-05-14",
            animesJson = json.encodeToString(userAnimes),
            gamesJson = json.encodeToString(userGames),
            photosJson = json.encodeToString(userPhotos),
        )
        database.userDao().insertUserProfile(profile)

        // 3. Sembrar Usuarios de Feed
        val feedUsers = listOf(
            DemoFeedUserEntity(
                id = "demo_user_1",
                username = "aoi_val",
                nickname = "Aoi",
                bio = "Streamer ocasional y apasionada de la ciencia ficción. Si te gusta Steins;Gate o Evangelion ya me caes bien. ¿Dúo en Valorant?",
                gender = "Mujer",
                birthdate = "2004-11-20",
                animesJson = json.encodeToString(listOf(
                    Anime("a2", "Steins;Gate", "https://cdn.myanimelist.net/images/anime/1935/127974.jpg"),
                    Anime("a4", "Neon Genesis Evangelion", "https://cdn.myanimelist.net/images/anime/1314/108941.jpg"),
                    Anime("a5", "Cyberpunk: Edgerunners", "https://cdn.myanimelist.net/images/anime/1814/127814.jpg"),
                )),
                gamesJson = json.encodeToString(listOf(
                    Game("g4", "Valorant", "https://images.igdb.com/igdb/image/upload/t_cover_big/co2mvt.png"),
                    Game("g5", "Overwatch 2", "https://images.igdb.com/igdb/image/upload/t_cover_big/co517j.png"),
                )),
                photosJson = json.encodeToString(listOf(
                    UserPhoto("demo_user_1", "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=500", 1),
                    UserPhoto("demo_user_1", "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?w=500", 2),
                )),
                willMatch = true,
            ),
            DemoFeedUserEntity(
                id = "demo_user_2",
                username = "kenji_manga",
                nickname = "Kenji",
                bio = "Coleccionista de manga y fan de los soulslike. Me encanta pasar horas explorando mundos oscuros y debatiendo finales de shonen.",
                gender = "Hombre",
                birthdate = "2001-08-12",
                animesJson = json.encodeToString(listOf(
                    Anime("a6", "Jujutsu Kaisen", "https://cdn.myanimelist.net/images/anime/1171/109222.jpg"),
                    Anime("a7", "Chainsaw Man", "https://cdn.myanimelist.net/images/anime/1806/126216.jpg"),
                    Anime("a8", "Vinland Saga", "https://cdn.myanimelist.net/images/anime/1500/103006.jpg"),
                )),
                gamesJson = json.encodeToString(listOf(
                    Game("g3", "Elden Ring", "https://images.igdb.com/igdb/image/upload/t_cover_big/co4jni.png"),
                    Game("g6", "Dark Souls III", "https://images.igdb.com/igdb/image/upload/t_cover_big/co1x77.png"),
                )),
                photosJson = json.encodeToString(listOf(
                    UserPhoto("demo_user_2", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=500", 1),
                )),
                willMatch = true,
            ),
            DemoFeedUserEntity(
                id = "demo_user_3",
                username = "sakura_cos",
                nickname = "Sakura",
                bio = "Cosplayer y diseñadora gráfica. Siempre buscando eventos de anime y amigos para sesiones de fotos temáticas. ¡Bocchi the Rock es mi religión!",
                gender = "Mujer",
                birthdate = "2003-03-25",
                animesJson = json.encodeToString(listOf(
                    Anime("a1", "Frieren: Beyond Journey's End", "https://cdn.myanimelist.net/images/anime/1015/138006.jpg"),
                    Anime("a9", "Bocchi the Rock!", "https://cdn.myanimelist.net/images/anime/1448/127956.jpg"),
                    Anime("a10", "Kaguya-sama: Love is War", "https://cdn.myanimelist.net/images/anime/1295/106551.jpg"),
                )),
                gamesJson = json.encodeToString(listOf(
                    Game("g7", "Genshin Impact", "https://images.igdb.com/igdb/image/upload/t_cover_big/co2k05.png"),
                    Game("g8", "Honkai: Star Rail", "https://images.igdb.com/igdb/image/upload/t_cover_big/co6qg8.png"),
                )),
                photosJson = json.encodeToString(listOf(
                    UserPhoto("demo_user_3", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=500", 1),
                )),
                willMatch = true,
            ),
            DemoFeedUserEntity(
                id = "demo_user_4",
                username = "hiro_indie",
                nickname = "Hiroshi",
                bio = "Desarrollador indie de videojuegos en mis ratos libres y amante del pixel art y la música lo-fi. Fan de NieR y Monster Hunter.",
                gender = "Hombre",
                birthdate = "2000-09-30",
                animesJson = json.encodeToString(listOf(
                    Anime("a5", "Cyberpunk: Edgerunners", "https://cdn.myanimelist.net/images/anime/1814/127814.jpg"),
                    Anime("a11", "Psycho-Pass", "https://cdn.myanimelist.net/images/anime/4/42603.jpg"),
                )),
                gamesJson = json.encodeToString(listOf(
                    Game("g9", "NieR: Automata", "https://images.igdb.com/igdb/image/upload/t_cover_big/co1r0c.png"),
                    Game("g10", "Monster Hunter: World", "https://images.igdb.com/igdb/image/upload/t_cover_big/co1r7c.png"),
                    Game("g11", "Hollow Knight", "https://images.igdb.com/igdb/image/upload/t_cover_big/co1r0a.png"),
                )),
                photosJson = json.encodeToString(listOf(
                    UserPhoto("demo_user_4", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=500", 1),
                )),
                willMatch = true,
            ),
            DemoFeedUserEntity(
                id = "demo_user_5",
                username = "yuki_art",
                nickname = "Yuki",
                bio = "Estudiante de ilustración digital, fan de Studio Ghibli y adicta a los doramas y al ramen. ¿Cuál es tu anime de confort favorito?",
                gender = "Mujer",
                birthdate = "2005-01-18",
                animesJson = json.encodeToString(listOf(
                    Anime("a12", "Spy x Family", "https://cdn.myanimelist.net/images/anime/1441/122795.jpg"),
                    Anime("a13", "Demon Slayer", "https://cdn.myanimelist.net/images/anime/1286/99889.jpg"),
                )),
                gamesJson = json.encodeToString(listOf(
                    Game("g12", "Animal Crossing: New Horizons", "https://images.igdb.com/igdb/image/upload/t_cover_big/co1x7e.png"),
                    Game("g1", "Persona 5 Royal", "https://images.igdb.com/igdb/image/upload/t_cover_big/co1r7f.png"),
                )),
                photosJson = json.encodeToString(listOf(
                    UserPhoto("demo_user_5", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=500", 1),
                )),
                willMatch = true,
            ),
            DemoFeedUserEntity(
                id = "demo_user_6",
                username = "ren_blade",
                nickname = "Ren",
                bio = "Fanático de los torneos de fighting games y guitarrista en una banda de rock. Shonen de los 90 y bandas sonoras épicas.",
                gender = "Hombre",
                birthdate = "2002-06-15",
                animesJson = json.encodeToString(listOf(
                    Anime("a14", "Attack on Titan", "https://cdn.myanimelist.net/images/anime/10/47347.jpg"),
                    Anime("a15", "Hunter x Hunter", "https://cdn.myanimelist.net/images/anime/1337/99013.jpg"),
                )),
                gamesJson = json.encodeToString(listOf(
                    Game("g13", "Street Fighter 6", "https://images.igdb.com/igdb/image/upload/t_cover_big/co5pms.png"),
                    Game("g14", "Final Fantasy VII Rebirth", "https://images.igdb.com/igdb/image/upload/t_cover_big/co6qg7.png"),
                )),
                photosJson = json.encodeToString(listOf(
                    UserPhoto("demo_user_6", "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=500", 1),
                )),
                willMatch = true,
            ),
        )
        database.userDao().insertFeedUsers(feedUsers)

        // 4. Sembrar Matches y Conversaciones Iniciales (Sakura y Kenji)
        val now = System.currentTimeMillis()
        val matchSakura = DemoMatchEntity(
            userId = "demo_user_3",
            matchedAt = now - 3600000,
            isSeen = true,
        )
        val matchKenji = DemoMatchEntity(
            userId = "demo_user_2",
            matchedAt = now - 7200000,
            isSeen = false,
        )
        database.matchDao().insertMatch(matchSakura)
        database.matchDao().insertMatch(matchKenji)

        // Chat con Sakura
        val chatSakuraId = "chat_sakura_demo"
        val chatSakura = DemoChatEntity(
            id = chatSakuraId,
            otherUserId = "demo_user_3",
            lastMessageText = "¡Hola! Vi que también te encanta Frieren, ¿has visto el último episodio?",
            lastMessageSenderId = "demo_user_3",
            lastMessageTimestamp = now - 1800000,
            unreadCount = 1,
        )
        database.chatDao().insertOrUpdateChat(chatSakura)

        val msgSakura1 = DemoMessageEntity(
            id = UUID.randomUUID().toString(),
            chatId = chatSakuraId,
            senderId = "demo_user_3",
            receiverId = DEMO_USER_ID,
            content = "¡Hola! Vi que también te encanta Frieren, ¿has visto el último episodio?",
            timestamp = now - 1800000,
            isRead = false,
        )
        database.chatDao().insertMessage(msgSakura1)

        // Chat con Kenji
        val chatKenjiId = "chat_kenji_demo"
        val chatKenji = DemoChatEntity(
            id = chatKenjiId,
            otherUserId = "demo_user_2",
            lastMessageText = "¡Buenas! Qué buen gusto con Persona 5 y Elden Ring ⚔️",
            lastMessageSenderId = "demo_user_2",
            lastMessageTimestamp = now - 3600000,
            unreadCount = 0,
        )
        database.chatDao().insertOrUpdateChat(chatKenji)

        val msgKenji1 = DemoMessageEntity(
            id = UUID.randomUUID().toString(),
            chatId = chatKenjiId,
            senderId = "demo_user_2",
            receiverId = DEMO_USER_ID,
            content = "¡Buenas! Qué buen gusto con Persona 5 y Elden Ring ⚔️",
            timestamp = now - 3600000,
            isRead = true,
        )
        database.chatDao().insertMessage(msgKenji1)
    }
}
