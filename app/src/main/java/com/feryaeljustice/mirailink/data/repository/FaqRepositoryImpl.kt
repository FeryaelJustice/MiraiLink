package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.faq.FaqCategory
import com.feryaeljustice.mirailink.domain.model.faq.FaqItem
import com.feryaeljustice.mirailink.domain.repository.FaqRepository

class FaqRepositoryImpl : FaqRepository {

    override fun getFaqItems(): List<FaqItem> = listOf(
        // About MiraiLink
        FaqItem(
            id = "faq_what_is_mirailink",
            category = FaqCategory.ABOUT_MIRAILINK,
            questionRes = R.string.faq_what_is_mirailink_q,
            answerRes = R.string.faq_what_is_mirailink_a,
        ),
        FaqItem(
            id = "faq_haptic_heartbeat",
            category = FaqCategory.ABOUT_MIRAILINK,
            questionRes = R.string.faq_haptic_heartbeat_q,
            answerRes = R.string.faq_haptic_heartbeat_a,
        ),
        FaqItem(
            id = "faq_affinity_calculation",
            category = FaqCategory.ABOUT_MIRAILINK,
            questionRes = R.string.faq_affinity_calculation_q,
            answerRes = R.string.faq_affinity_calculation_a,
        ),

        // Cards & Search
        FaqItem(
            id = "faq_cards_gestures",
            category = FaqCategory.CARDS_AND_MATCHING,
            questionRes = R.string.faq_cards_gestures_q,
            answerRes = R.string.faq_cards_gestures_a,
        ),
        FaqItem(
            id = "faq_explore_categories",
            category = FaqCategory.CARDS_AND_MATCHING,
            questionRes = R.string.faq_explore_categories_q,
            answerRes = R.string.faq_explore_categories_a,
        ),
        FaqItem(
            id = "faq_gender_filter",
            category = FaqCategory.CARDS_AND_MATCHING,
            questionRes = R.string.faq_gender_filter_q,
            answerRes = R.string.faq_gender_filter_a,
        ),
        FaqItem(
            id = "faq_no_more_profiles",
            category = FaqCategory.CARDS_AND_MATCHING,
            questionRes = R.string.faq_no_more_profiles_q,
            answerRes = R.string.faq_no_more_profiles_a,
        ),
        FaqItem(
            id = "faq_matches_and_chat",
            category = FaqCategory.CARDS_AND_MATCHING,
            questionRes = R.string.faq_matches_and_chat_q,
            answerRes = R.string.faq_matches_and_chat_a,
        ),
        // Gesture Roulette
        FaqItem(
            id = "faq_gesture_roulette",
            category = FaqCategory.GESTURE_ROULETTE,
            questionRes = R.string.faq_gesture_roulette_q,
            answerRes = R.string.faq_gesture_roulette_a,
        ),
        FaqItem(
            id = "faq_gesture_roulette_privacy",
            category = FaqCategory.GESTURE_ROULETTE,
            questionRes = R.string.faq_gesture_roulette_privacy_q,
            answerRes = R.string.faq_gesture_roulette_privacy_a,
        ),

        // Location & Privacy
        FaqItem(
            id = "faq_50_points",
            category = FaqCategory.LOCATION_AND_PRIVACY,
            questionRes = R.string.faq_50_points_q,
            answerRes = R.string.faq_50_points_a,
        ),
        FaqItem(
            id = "faq_residence_vs_travelers",
            category = FaqCategory.LOCATION_AND_PRIVACY,
            questionRes = R.string.faq_residence_vs_travelers_q,
            answerRes = R.string.faq_residence_vs_travelers_a,
        ),
        FaqItem(
            id = "faq_exact_location_privacy",
            category = FaqCategory.LOCATION_AND_PRIVACY,
            questionRes = R.string.faq_exact_location_privacy_q,
            answerRes = R.string.faq_exact_location_privacy_a,
        ),

        // Subscriptions & Payments
        FaqItem(
            id = "faq_subscription_tiers",
            category = FaqCategory.SUBSCRIPTIONS_AND_PAYMENTS,
            questionRes = R.string.faq_subscription_tiers_q,
            answerRes = R.string.faq_subscription_tiers_a,
        ),
        FaqItem(
            id = "faq_google_play_billing",
            category = FaqCategory.SUBSCRIPTIONS_AND_PAYMENTS,
            questionRes = R.string.faq_google_play_billing_q,
            answerRes = R.string.faq_google_play_billing_a,
        ),
        FaqItem(
            id = "faq_subscription_cancellation",
            category = FaqCategory.SUBSCRIPTIONS_AND_PAYMENTS,
            questionRes = R.string.faq_subscription_cancellation_q,
            answerRes = R.string.faq_subscription_cancellation_a,
        ),
        FaqItem(
            id = "faq_restore_purchases",
            category = FaqCategory.SUBSCRIPTIONS_AND_PAYMENTS,
            questionRes = R.string.faq_restore_purchases_q,
            answerRes = R.string.faq_restore_purchases_a,
        ),
        FaqItem(
            id = "faq_premium",
            category = FaqCategory.SUBSCRIPTIONS_AND_PAYMENTS,
            questionRes = R.string.faq_premium_q,
            answerRes = R.string.faq_premium_a,
        ),

        // Photos & Quality
        FaqItem(
            id = "faq_studio_what_is",
            category = FaqCategory.PHOTOS_AND_QUALITY,
            questionRes = R.string.faq_studio_what_is_q,
            answerRes = R.string.faq_studio_what_is_a,
        ),
        FaqItem(
            id = "faq_studio_anime_cosplay",
            category = FaqCategory.PHOTOS_AND_QUALITY,
            questionRes = R.string.faq_studio_anime_cosplay_q,
            answerRes = R.string.faq_studio_anime_cosplay_a,
        ),
        FaqItem(
            id = "faq_studio_standards",
            category = FaqCategory.PHOTOS_AND_QUALITY,
            questionRes = R.string.faq_studio_standards_q,
            answerRes = R.string.faq_studio_standards_a,
        ),
        FaqItem(
            id = "faq_studio_screenshots",
            category = FaqCategory.PHOTOS_AND_QUALITY,
            questionRes = R.string.faq_studio_screenshots_q,
            answerRes = R.string.faq_studio_screenshots_a,
        ),

        // Account & Security
        FaqItem(
            id = "faq_username_permanent",
            category = FaqCategory.ACCOUNT_AND_SECURITY,
            questionRes = R.string.faq_username_permanent_q,
            answerRes = R.string.faq_username_permanent_a,
        ),
        FaqItem(
            id = "faq_two_factor_auth",
            category = FaqCategory.ACCOUNT_AND_SECURITY,
            questionRes = R.string.faq_two_factor_auth_q,
            answerRes = R.string.faq_two_factor_auth_a,
        ),
        FaqItem(
            id = "faq_delete_account",
            category = FaqCategory.ACCOUNT_AND_SECURITY,
            questionRes = R.string.faq_delete_account_q,
            answerRes = R.string.faq_delete_account_a,
        ),
    )
}
