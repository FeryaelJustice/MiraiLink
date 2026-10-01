package com.feryaeljustice.mirailink.domain.model.faq

import androidx.annotation.StringRes

import com.feryaeljustice.mirailink.R

enum class FaqCategory(@StringRes val titleRes: Int) {
    ABOUT_MIRAILINK(R.string.faq_category_about_mirailink),
    CARDS_AND_MATCHING(R.string.faq_category_cards_and_matching),
    LOCATION_AND_PRIVACY(R.string.faq_category_location_and_privacy),
    SUBSCRIPTIONS_AND_PAYMENTS(R.string.faq_category_subscriptions_and_payments),
    PHOTOS_AND_QUALITY(R.string.faq_category_photos_and_quality),
    ACCOUNT_AND_SECURITY(R.string.faq_category_account_and_security),
}

data class FaqItem(
    val id: String,
    val category: FaqCategory,
    @StringRes val questionRes: Int,
    @StringRes val answerRes: Int,
)
