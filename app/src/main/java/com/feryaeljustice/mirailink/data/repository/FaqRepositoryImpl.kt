package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.faq.FaqCategory
import com.feryaeljustice.mirailink.domain.model.faq.FaqItem
import com.feryaeljustice.mirailink.domain.repository.FaqRepository

class FaqRepositoryImpl : FaqRepository {

    override fun getFaqItems(): List<FaqItem> = listOf(
        FaqItem(
            id = "faq_what_is_mirailink",
            category = FaqCategory.ABOUT_MIRAILINK,
            questionRes = R.string.faq_what_is_mirailink_q,
            answerRes = R.string.faq_what_is_mirailink_a,
        ),
        FaqItem(
            id = "faq_50_points",
            category = FaqCategory.LOCATION_AND_PRIVACY,
            questionRes = R.string.faq_50_points_q,
            answerRes = R.string.faq_50_points_a,
        ),
        FaqItem(
            id = "faq_residence_vs_travelers",
            category = FaqCategory.SEARCH_AND_MATCHING,
            questionRes = R.string.faq_residence_vs_travelers_q,
            answerRes = R.string.faq_residence_vs_travelers_a,
        ),
        FaqItem(
            id = "faq_premium",
            category = FaqCategory.PREMIUM_FEATURES,
            questionRes = R.string.faq_premium_q,
            answerRes = R.string.faq_premium_a,
        ),
        FaqItem(
            id = "faq_studio_what_is",
            category = FaqCategory.PHOTOS_AND_STUDIO,
            questionRes = R.string.faq_studio_what_is_q,
            answerRes = R.string.faq_studio_what_is_a,
        ),
        FaqItem(
            id = "faq_studio_standards",
            category = FaqCategory.PHOTOS_AND_STUDIO,
            questionRes = R.string.faq_studio_standards_q,
            answerRes = R.string.faq_studio_standards_a,
        ),
        FaqItem(
            id = "faq_studio_screenshots",
            category = FaqCategory.PHOTOS_AND_STUDIO,
            questionRes = R.string.faq_studio_screenshots_q,
            answerRes = R.string.faq_studio_screenshots_a,
        ),
        FaqItem(
            id = "faq_studio_anime_cosplay",
            category = FaqCategory.PHOTOS_AND_STUDIO,
            questionRes = R.string.faq_studio_anime_cosplay_q,
            answerRes = R.string.faq_studio_anime_cosplay_a,
        ),
    )
}
