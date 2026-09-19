package com.feryaeljustice.mirailink.domain.model.faq

import androidx.annotation.StringRes

import com.feryaeljustice.mirailink.R

enum class FaqCategory(@StringRes val titleRes: Int) {
    ABOUT_MIRAILINK(R.string.faq_category_general),
    LOCATION_AND_PRIVACY(R.string.faq_category_privacy),
    SEARCH_AND_MATCHING(R.string.faq_category_matching),
    PREMIUM_FEATURES(R.string.faq_category_premium),
}

data class FaqItem(
    val id: String,
    val category: FaqCategory,
    @StringRes val questionRes: Int,
    @StringRes val answerRes: Int,
)
