package com.feryaeljustice.mirailink.data.repository.demo

import com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder
import com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase
import com.feryaeljustice.mirailink.data.local.demo.entity.DemoCategoryPreferenceEntity
import com.feryaeljustice.mirailink.data.local.demo.toDomainUser
import com.feryaeljustice.mirailink.domain.model.explore.CategoryPreference
import com.feryaeljustice.mirailink.domain.model.explore.ExploreCategory
import com.feryaeljustice.mirailink.domain.model.explore.ExploreSection
import com.feryaeljustice.mirailink.domain.model.explore.ExploreSectionGroup
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.ExploreHubData
import com.feryaeljustice.mirailink.domain.repository.ExploreRepository
import com.feryaeljustice.mirailink.domain.util.GeoUtils
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class DemoExploreRepositoryImpl(
    private val database: MiraiLinkDemoDatabase,
    private val seeder: DemoDataSeeder,
) : ExploreRepository {

    private val staticCategories = listOf(
        // Otaku & Anime
        ExploreCategory(
            id = "cat-anime-marathon",
            code = "anime_marathon",
            sectionGroup = ExploreSectionGroup.OTAKU,
            iconKey = "tv",
            title = "Maratón de series",
            description = "Encuentra gente para maratonear tus series y animes favoritos",
            activeCount = 0,
            radiusKm = 40,
        ),
        ExploreCategory(
            id = "cat-cosplay-events",
            code = "cosplay_events",
            sectionGroup = ExploreSectionGroup.OTAKU,
            iconKey = "theater",
            title = "Cosplay & Eventos",
            description = "Compañeros para convenciones, cosplay y sesiones de fotos",
            activeCount = 0,
            radiusKm = 40,
        ),
        ExploreCategory(
            id = "cat-manga-lovers",
            code = "manga_lovers",
            sectionGroup = ExploreSectionGroup.OTAKU,
            iconKey = "book",
            title = "Manga & Lectura",
            description = "Lectores de manga, novelas ligeras y cómics",
            activeCount = 0,
            radiusKm = 40,
        ),
        // Videojuegos & Gaming
        ExploreCategory(
            id = "cat-gaming-coop",
            code = "gaming_coop",
            sectionGroup = ExploreSectionGroup.GAMING,
            iconKey = "controller",
            title = "Gamers & Co-op",
            description = "Tu dúo ideal para juegos cooperativos y multijugador",
            activeCount = 0,
            radiusKm = 40,
        ),
        ExploreCategory(
            id = "cat-esports-comp",
            code = "esports_competitive",
            sectionGroup = ExploreSectionGroup.GAMING,
            iconKey = "trophy",
            title = "Competitivo & E-Sports",
            description = "Sube de rango y compite al máximo nivel",
            activeCount = 0,
            radiusKm = 40,
        ),
        ExploreCategory(
            id = "cat-rpg-fantasy",
            code = "rpg_fantasy",
            sectionGroup = ExploreSectionGroup.GAMING,
            iconKey = "sword",
            title = "RPG & Fantasía",
            description = "Aventuras épicas, mundos abiertos e historias profundas",
            activeCount = 0,
            radiusKm = 40,
        ),
        ExploreCategory(
            id = "cat-casual-gaming",
            code = "casual_gaming",
            sectionGroup = ExploreSectionGroup.GAMING,
            iconKey = "coffee",
            title = "Casual & Chill",
            description = "Jugar de forma relajada y pasar un buen rato",
            activeCount = 0,
            radiusKm = 40,
        ),
        // Conexiones & Metas
        ExploreCategory(
            id = "cat-long-term",
            code = "long_term_relationship",
            sectionGroup = ExploreSectionGroup.CONNECTIONS,
            iconKey = "rose",
            title = "Relación estable",
            description = "Buscando una conexión duradera y auténtica",
            activeCount = 0,
            radiusKm = 40,
        ),
        ExploreCategory(
            id = "cat-casual-dating",
            code = "casual_dating",
            sectionGroup = ExploreSectionGroup.CONNECTIONS,
            iconKey = "cocktail",
            title = "Noche de cita",
            description = "Planes divertidos y citas para conocerse sin presiones",
            activeCount = 0,
            radiusKm = 40,
        ),
        ExploreCategory(
            id = "cat-new-friends",
            code = "new_friends",
            sectionGroup = ExploreSectionGroup.CONNECTIONS,
            iconKey = "puzzle",
            title = "Nuevas amistades",
            description = "Conoce gente afín y amplía tu grupo de amigos",
            activeCount = 0,
            radiusKm = 40,
        ),
    )

    override suspend fun getExploreHubData(): MiraiLinkResult<ExploreHubData> {
        seeder.seedInitialDataIfEmpty()
        val demoProfile = database.userDao().getUserProfile(DemoDataSeeder.DEMO_USER_ID)
        val userLat = demoProfile?.residenceLatitude ?: demoProfile?.currentLatitude
        val userLon = demoProfile?.residenceLongitude ?: demoProfile?.currentLongitude

        val feedUsers = database.userDao().getFeedUsers().map { it.toDomainUser() }

        val resolvedCategories = staticCategories.map { category ->
            val pref = database.categoryDao().getPreference(DemoDataSeeder.DEMO_USER_ID, category.id)
            val radius = pref?.radiusKm ?: 40

            val count = feedUsers.count { user ->
                val distance = GeoUtils.calculateDistanceKm(
                    userLat, userLon,
                    user.residenceLatitude ?: user.currentLatitude,
                    user.residenceLongitude ?: user.currentLongitude,
                )
                val matchesDistance = distance == null || distance <= radius
                val matchesCategory = matchesCategoryFilter(user, category.code)
                matchesDistance && matchesCategory
            }

            category.copy(
                radiusKm = radius,
                activeCount = count,
            )
        }

        val grouped = resolvedCategories.groupBy { it.sectionGroup }
        val sections = listOf(
            ExploreSection(
                group = ExploreSectionGroup.OTAKU,
                title = "Otaku & Anime",
                categories = grouped[ExploreSectionGroup.OTAKU] ?: emptyList(),
            ),
            ExploreSection(
                group = ExploreSectionGroup.GAMING,
                title = "Videojuegos & Gaming",
                categories = grouped[ExploreSectionGroup.GAMING] ?: emptyList(),
            ),
            ExploreSection(
                group = ExploreSectionGroup.CONNECTIONS,
                title = "Conexiones & Metas",
                categories = grouped[ExploreSectionGroup.CONNECTIONS] ?: emptyList(),
            ),
        )

        val recommendations = resolvedCategories.take(4)

        return MiraiLinkResult.Success(
            ExploreHubData(
                recommendations = recommendations,
                sections = sections,
            ),
        )
    }

    override suspend fun getCategoryFeed(
        categoryId: String,
        limit: Int,
        offset: Int,
    ): MiraiLinkResult<List<User>> {
        seeder.seedInitialDataIfEmpty()
        val category = staticCategories.find { it.id == categoryId || it.code == categoryId }
            ?: staticCategories.first()

        val pref = database.categoryDao().getPreference(DemoDataSeeder.DEMO_USER_ID, category.id)
        val radius = pref?.radiusKm ?: 40

        val demoProfile = database.userDao().getUserProfile(DemoDataSeeder.DEMO_USER_ID)
        val userLat = demoProfile?.residenceLatitude ?: demoProfile?.currentLatitude
        val userLon = demoProfile?.residenceLongitude ?: demoProfile?.currentLongitude

        var feedEntities = database.userDao().getFeedUsers()
        if (feedEntities.isEmpty()) {
            val all = database.userDao().getAllFeedUsers()
            if (all.isNotEmpty()) {
                database.userDao().insertFeedUsers(all.map { it.copy(isLiked = false, isDisliked = false) })
                feedEntities = database.userDao().getFeedUsers()
            }
        }

        val filteredUsers = feedEntities.map { entity ->
            val user = entity.toDomainUser()
            val distance = GeoUtils.calculateDistanceKm(
                userLat, userLon,
                user.residenceLatitude ?: user.currentLatitude,
                user.residenceLongitude ?: user.currentLongitude,
            )
            user.copy(distanceKm = distance)
        }.filter { user ->
            val matchesDistance = user.distanceKm == null || user.distanceKm <= radius
            val matchesCategory = matchesCategoryFilter(user, category.code)
            matchesDistance && matchesCategory
        }.drop(offset).take(limit)

        return MiraiLinkResult.Success(filteredUsers)
    }

    override suspend fun getCategoryPreferences(categoryId: String): MiraiLinkResult<CategoryPreference> {
        val pref = database.categoryDao().getPreference(DemoDataSeeder.DEMO_USER_ID, categoryId)
        return MiraiLinkResult.Success(
            CategoryPreference(
                categoryId = categoryId,
                radiusKm = pref?.radiusKm ?: 40,
            ),
        )
    }

    override suspend fun updateCategoryPreferences(
        categoryId: String,
        radiusKm: Int,
    ): MiraiLinkResult<CategoryPreference> {
        val entity = DemoCategoryPreferenceEntity(
            userId = DemoDataSeeder.DEMO_USER_ID,
            categoryId = categoryId,
            radiusKm = radiusKm,
            updatedAt = System.currentTimeMillis(),
        )
        database.categoryDao().insertOrUpdate(entity)
        return MiraiLinkResult.Success(
            CategoryPreference(
                categoryId = categoryId,
                radiusKm = radiusKm,
            ),
        )
    }

    private fun matchesCategoryFilter(user: User, categoryCode: String): Boolean =
        when (categoryCode) {
            "anime_marathon", "cosplay_events", "manga_lovers" -> user.animes.isNotEmpty()
            "gaming_coop", "esports_competitive", "rpg_fantasy", "casual_gaming" -> user.games.isNotEmpty()
            else -> true
        }
}
