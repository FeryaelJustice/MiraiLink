package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.domain.model.faq.FaqCategory
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FaqRepositoryImplTest {

    private lateinit var repository: FaqRepositoryImpl

    @Before
    fun setUp() {
        repository = FaqRepositoryImpl()
    }

    @Test
    fun `getFaqItems returns all items across categories`() = runTest {
        val items = repository.getFaqItems()

        assertTrue(items.isNotEmpty())
        assertEquals(30, items.size)
        val holoItems = items.filter { it.id.startsWith("faq_holo_") }
        assertEquals(5, holoItems.size)
        assertTrue(holoItems.all { it.category == FaqCategory.CARDS_AND_MATCHING })
        assertEquals(items.size, items.map { it.id }.distinct().size)
        assertTrue(items.any { it.category == FaqCategory.ABOUT_MIRAILINK })
        assertTrue(items.any { it.category == FaqCategory.CARDS_AND_MATCHING })
        assertTrue(items.any { it.category == FaqCategory.GESTURE_ROULETTE })
        assertTrue(items.any { it.category == FaqCategory.LOCATION_AND_PRIVACY })
        assertTrue(items.any { it.category == FaqCategory.SUBSCRIPTIONS_AND_PAYMENTS })
        assertTrue(items.any { it.category == FaqCategory.PHOTOS_AND_QUALITY })
        assertTrue(items.any { it.category == FaqCategory.ACCOUNT_AND_SECURITY })
    }

    @Test
    fun `filter items by category works as expected`() = runTest {
        val allItems = repository.getFaqItems()
        val privacyItems = allItems.filter { it.category == FaqCategory.LOCATION_AND_PRIVACY }
        val gestureItems = allItems.filter { it.category == FaqCategory.GESTURE_ROULETTE }

        assertTrue(privacyItems.isNotEmpty())
        assertTrue(privacyItems.all { it.category == FaqCategory.LOCATION_AND_PRIVACY })
        assertEquals(2, gestureItems.size)
        assertTrue(gestureItems.all { it.category == FaqCategory.GESTURE_ROULETTE })
    }
}
