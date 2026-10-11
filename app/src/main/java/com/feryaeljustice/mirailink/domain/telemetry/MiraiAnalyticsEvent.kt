package com.feryaeljustice.mirailink.domain.telemetry

sealed interface MiraiAnalyticsEvent {
    val eventName: String
    val params: Map<String, Any?>

    sealed interface SubscriptionEvent : MiraiAnalyticsEvent {
        data class PaywallViewed(val triggerSource: String) : SubscriptionEvent {
            override val eventName: String = "paywall_viewed"
            override val params: Map<String, Any?> = mapOf("trigger_source" to triggerSource)
        }

        data class PlanSelected(
            val tier: String,
            val duration: String,
            val price: String? = null,
        ) : SubscriptionEvent {
            override val eventName: String = "paywall_plan_selected"
            override val params: Map<String, Any?> =
                buildMap {
                    put("tier", tier)
                    put("duration", duration)
                    price?.let { put("price", it) }
                }
        }

        data class CheckoutInitiated(
            val tier: String,
            val duration: String,
            val price: String? = null,
        ) : SubscriptionEvent {
            override val eventName: String = "subscription_checkout_initiated"
            override val params: Map<String, Any?> =
                buildMap {
                    put("tier", tier)
                    put("duration", duration)
                    price?.let { put("price", it) }
                }
        }

        data class PurchaseSuccess(
            val tier: String,
            val duration: String,
        ) : SubscriptionEvent {
            override val eventName: String = "subscription_purchase_success"
            override val params: Map<String, Any?> = mapOf("tier" to tier, "duration" to duration)
        }

        data class PurchaseCancelled(
            val tier: String,
            val duration: String,
        ) : SubscriptionEvent {
            override val eventName: String = "subscription_purchase_cancelled"
            override val params: Map<String, Any?> = mapOf("tier" to tier, "duration" to duration)
        }

        data class PurchaseFailed(
            val tier: String,
            val duration: String,
            val errorMessage: String? = null,
        ) : SubscriptionEvent {
            override val eventName: String = "subscription_purchase_failed"
            override val params: Map<String, Any?> =
                buildMap {
                    put("tier", tier)
                    put("duration", duration)
                    errorMessage?.let { put("error_message", it) }
                }
        }

        data object RestorePurchasesClicked : SubscriptionEvent {
            override val eventName: String = "subscription_restore_clicked"
            override val params: Map<String, Any?> = emptyMap()
        }

        data class RestorePurchasesResult(
            val success: Boolean,
            val restoredCount: Int = 0,
        ) : SubscriptionEvent {
            override val eventName: String = "subscription_restore_result"
            override val params: Map<String, Any?> =
                mapOf("success" to success, "restored_count" to restoredCount)
        }
    }

    sealed interface DiscoveryEvent : MiraiAnalyticsEvent {
        data class CardViewed(
            val targetUserId: String,
            val position: Int,
        ) : DiscoveryEvent {
            override val eventName: String = "profile_card_viewed"
            override val params: Map<String, Any?> =
                mapOf("target_user_id" to targetUserId, "position" to position)
        }

        data class CardSwiped(
            val action: String,
            val targetUserId: String,
            val isDemoMode: Boolean = false,
        ) : DiscoveryEvent {
            override val eventName: String = "card_swiped"
            override val params: Map<String, Any?> =
                mapOf(
                    "action" to action,
                    "target_user_id" to targetUserId,
                    "is_demo_mode" to isDemoMode,
                )
        }

        data class CardFlipped(val cardSide: String) : DiscoveryEvent {
            override val eventName: String = "profile_card_flipped"
            override val params: Map<String, Any?> = mapOf("card_side" to cardSide)
        }

        data class MatchCreated(val matchedUserId: String) : DiscoveryEvent {
            override val eventName: String = "match_dialog_shown"
            override val params: Map<String, Any?> = mapOf("matched_user_id" to matchedUserId)
        }

        data class DailyLimitReached(val limitType: String) : DiscoveryEvent {
            override val eventName: String = "daily_limit_reached"
            override val params: Map<String, Any?> = mapOf("limit_type" to limitType)
        }
    }

    sealed interface ProfileEvent : MiraiAnalyticsEvent {
        data class ProfileViewed(
            val isSelf: Boolean,
            val targetUserId: String? = null,
        ) : ProfileEvent {
            override val eventName: String = "profile_viewed"
            override val params: Map<String, Any?> =
                buildMap {
                    put("is_self", isSelf)
                    targetUserId?.let { put("target_user_id", it) }
                }
        }

        data object ProfileEditStarted : ProfileEvent {
            override val eventName: String = "profile_edit_started"
            override val params: Map<String, Any?> = emptyMap()
        }

        data class ProfileFieldUpdated(val fieldName: String) : ProfileEvent {
            override val eventName: String = "profile_field_updated"
            override val params: Map<String, Any?> = mapOf("field_name" to fieldName)
        }

        data class ProfilePhotosReordered(val photosCount: Int) : ProfileEvent {
            override val eventName: String = "profile_photos_reordered"
            override val params: Map<String, Any?> = mapOf("photos_count" to photosCount)
        }

        data class ProfilePhotoAdded(val source: String) : ProfileEvent {
            override val eventName: String = "profile_photo_added"
            override val params: Map<String, Any?> = mapOf("source" to source)
        }

        data class ProfileEditSaved(
            val success: Boolean,
            val fieldsModifiedCount: Int = 0,
        ) : ProfileEvent {
            override val eventName: String = "profile_edit_saved"
            override val params: Map<String, Any?> =
                mapOf("success" to success, "fields_modified_count" to fieldsModifiedCount)
        }

        data class VerificationCompleted(val success: Boolean) : ProfileEvent {
            override val eventName: String = "profile_verification_completed"
            override val params: Map<String, Any?> = mapOf("success" to success)
        }
    }

    sealed interface ExploreEvent : MiraiAnalyticsEvent {
        data class CategorySelected(
            val categoryId: String,
            val categoryName: String,
        ) : ExploreEvent {
            override val eventName: String = "explore_category_selected"
            override val params: Map<String, Any?> =
                mapOf("category_id" to categoryId, "category_name" to categoryName)
        }

        data class FilterApplied(
            val distanceKm: Int? = null,
            val gender: String? = null,
            val ageMin: Int? = null,
            val ageMax: Int? = null,
        ) : ExploreEvent {
            override val eventName: String = "explore_filter_applied"
            override val params: Map<String, Any?> =
                buildMap {
                    distanceKm?.let { put("distance_km", it) }
                    gender?.let { put("gender", it) }
                    ageMin?.let { put("age_min", it) }
                    ageMax?.let { put("age_max", it) }
                }
        }

        data class FeedScrolled(val depth: Int) : ExploreEvent {
            override val eventName: String = "explore_feed_scrolled"
            override val params: Map<String, Any?> = mapOf("depth" to depth)
        }
    }

    sealed interface LikesEvent : MiraiAnalyticsEvent {
        data class ReceivedLikesViewed(
            val totalCount: Int,
            val blurredCount: Int,
        ) : LikesEvent {
            override val eventName: String = "received_likes_viewed"
            override val params: Map<String, Any?> =
                mapOf("total_count" to totalCount, "blurred_count" to blurredCount)
        }

        data object BlurredLikeClicked : LikesEvent {
            override val eventName: String = "blurred_like_clicked"
            override val params: Map<String, Any?> = emptyMap()
        }

        data class ReceivedLikeMatched(val targetUserId: String) : LikesEvent {
            override val eventName: String = "received_like_matched"
            override val params: Map<String, Any?> = mapOf("target_user_id" to targetUserId)
        }
    }

    sealed interface ChatEvent : MiraiAnalyticsEvent {
        data class ChatListOpened(val activeChatsCount: Int) : ChatEvent {
            override val eventName: String = "chat_list_opened"
            override val params: Map<String, Any?> = mapOf("active_chats_count" to activeChatsCount)
        }

        data class ConversationOpened(val partnerId: String) : ChatEvent {
            override val eventName: String = "conversation_opened"
            override val params: Map<String, Any?> = mapOf("partner_id" to partnerId)
        }

        data class MessageSent(val messageType: String) : ChatEvent {
            override val eventName: String = "message_sent"
            override val params: Map<String, Any?> = mapOf("message_type" to messageType)
        }

        data class CapsuleUnlocked(
            val level: Int,
            val topic: String,
        ) : ChatEvent {
            override val eventName: String = "affinity_capsule_unlocked"
            override val params: Map<String, Any?> = mapOf("level" to level, "topic" to topic)
        }
    }

    sealed interface AuthEvent : MiraiAnalyticsEvent {
        data class OnboardingStepCompleted(val stepIndex: Int) : AuthEvent {
            override val eventName: String = "onboarding_step_completed"
            override val params: Map<String, Any?> = mapOf("step_index" to stepIndex)
        }

        data class LoginSuccess(val method: String = "password") : AuthEvent {
            override val eventName: String = "login_success"
            override val params: Map<String, Any?> = mapOf("method" to method)
        }

        data class LoginFailed(val reason: String) : AuthEvent {
            override val eventName: String = "login_failed"
            override val params: Map<String, Any?> = mapOf("reason" to reason)
        }

        data object RegisterSuccess : AuthEvent {
            override val eventName: String = "register_success"
            override val params: Map<String, Any?> = emptyMap()
        }

        data class RegisterFailed(val reason: String) : AuthEvent {
            override val eventName: String = "register_failed"
            override val params: Map<String, Any?> = mapOf("reason" to reason)
        }

        data object Logout : AuthEvent {
            override val eventName: String = "user_logout"
            override val params: Map<String, Any?> = emptyMap()
        }
    }

    sealed interface SettingsEvent : MiraiAnalyticsEvent {
        data class ThemeChanged(val theme: String) : SettingsEvent {
            override val eventName: String = "theme_changed"
            override val params: Map<String, Any?> = mapOf("theme" to theme)
        }

        data class NotificationToggled(
            val channel: String,
            val enabled: Boolean,
        ) : SettingsEvent {
            override val eventName: String = "notification_preference_toggled"
            override val params: Map<String, Any?> = mapOf("channel" to channel, "enabled" to enabled)
        }
    }

    sealed interface NavigationEvent : MiraiAnalyticsEvent {
        data class ScreenView(val screenName: String) : NavigationEvent {
            override val eventName: String = "screen_view"
            override val params: Map<String, Any?> = mapOf("screen_name" to screenName)
        }

        data class DeepLinkOpened(val uri: String) : NavigationEvent {
            override val eventName: String = "deeplink_opened"
            override val params: Map<String, Any?> = mapOf("uri" to uri)
        }
    }

    sealed interface SystemEvent : MiraiAnalyticsEvent {
        data class AppException(
            val exceptionType: String,
            val reason: String,
            val screen: String? = null,
            val isFatal: Boolean = false,
            val metadata: Map<String, Any?> = emptyMap(),
        ) : SystemEvent {
            override val eventName: String = "app_exception"
            override val params: Map<String, Any?> =
                buildMap {
                    put("exception_type", exceptionType)
                    put("reason", reason)
                    put("is_fatal", isFatal)
                    screen?.let { put("screen_name", it) }
                    putAll(metadata)
                }
        }
    }
}
