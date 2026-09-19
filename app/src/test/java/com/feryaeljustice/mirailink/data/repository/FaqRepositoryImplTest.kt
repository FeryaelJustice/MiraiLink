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
        assertEquals(4, items.size)
        assertTrue(items.any { it.category == FaqCategory.LOCATION_AND_PRIVACY })
        assertTrue(items.any { it.category == FaqCategory.SEARCH_AND_MATCHING })
    }

    @Test
    fun `filter items by category works as expected`() = runTest {
        val allItems = repository.getFaqItems()
        val privacyItems = allItems.filter { it.category == FaqCategory.LOCATION_AND_PRIVACY }

        assertTrue(privacyItems.isNotEmpty())
        assertTrue(privacyItems.all { it.category == FaqCategory.LOCATION_AND_PRIVACY })
    }
}
