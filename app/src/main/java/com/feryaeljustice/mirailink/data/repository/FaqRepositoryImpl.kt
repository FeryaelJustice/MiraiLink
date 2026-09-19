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
    )
}
