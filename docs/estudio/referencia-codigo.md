# Referencia navegable de fuentes propias

Anexo obtenido por inspección estática. Las declaraciones y consumidores son ayudas de navegación, no un análisis completo de ejecución. Revisar el flujo en los documentos temáticos y en el código. Los enlaces de archivo evitan números de línea que quedarían obsoletos al editar comentarios.

[Volver a la guía maestra](../guia-maestra.md). Regeneración: `node scripts/documentacion-inventario.mjs android`.

## app/src/main/java/com/feryaeljustice/mirailink/MiraiLinkApp.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/MiraiLinkApp.kt](../../app/src/main/java/com/feryaeljustice/mirailink/MiraiLinkApp.kt).

Declaraciones: `MiraiLinkApp`, `onCreate`, `newImageLoader`, `initKoin`.

Dependencias importadas: `android.app.Application`, `com.feryaeljustice.mirailink.di.koin.aiModule`, `com.feryaeljustice.mirailink.di.koin.appModule`, `com.feryaeljustice.mirailink.di.koin.cryptoModule`, `com.feryaeljustice.mirailink.di.koin.dataModule`, `com.feryaeljustice.mirailink.di.koin.dataStoreModule`, `com.feryaeljustice.mirailink.di.koin.demoModule`, `com.feryaeljustice.mirailink.di.koin.dispatchersModule`, `com.feryaeljustice.mirailink.di.koin.featureFlagModule`, `com.feryaeljustice.mirailink.di.koin.loggerModule`, `com.feryaeljustice.mirailink.di.koin.networkModule`, `com.feryaeljustice.mirailink.di.koin.repositoryModule`, `com.feryaeljustice.mirailink.di.koin.serializationModule`, `com.feryaeljustice.mirailink.di.koin.socketModule`, `com.feryaeljustice.mirailink.di.koin.telemetryModule`, `com.feryaeljustice.mirailink.di.koin.useCaseModule`, `com.feryaeljustice.mirailink.di.koin.viewModelModule`, `io.kotzilla.sdk.analytics.koin.analytics`, `org.koin.android.ext.koin.androidContext`, `org.koin.android.ext.koin.androidLogger`, `org.koin.core.context.startKoin`, `org.koin.core.logger.Level`, `org.koin.core.module.Module`, `org.koin.dsl.KoinAppDeclaration`, `coil.ImageLoader`, `coil.ImageLoaderFactory`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.ImageOkHttpClient`, `okhttp3.OkHttpClient`, `org.koin.android.ext.android.inject`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/core/featureflags/FeatureFlag.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/core/featureflags/FeatureFlag.kt](../../app/src/main/java/com/feryaeljustice/mirailink/core/featureflags/FeatureFlag.kt).

Declaraciones: `FeatureFlag`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/FeatureFlagModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/FeatureFlagModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/MainViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/MainViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/MiraiLinkAppRoot.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/MiraiLinkAppRoot.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/theme/AppThemeManager.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/theme/AppThemeManager.kt).

## app/src/main/java/com/feryaeljustice/mirailink/core/featureflags/FeatureFlagStore.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/core/featureflags/FeatureFlagStore.kt](../../app/src/main/java/com/feryaeljustice/mirailink/core/featureflags/FeatureFlagStore.kt).

Declaraciones: `FeatureFlagStore`, `setChristmasEnabled`.

Dependencias importadas: `kotlinx.coroutines.flow.Flow`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/FeatureFlagModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/FeatureFlagModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/MainViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/MainViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/theme/AppThemeManager.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/theme/AppThemeManager.kt).

## app/src/main/java/com/feryaeljustice/mirailink/core/featureflags/FeatureFlagStoreImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/core/featureflags/FeatureFlagStoreImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/core/featureflags/FeatureFlagStoreImpl.kt).

Declaraciones: `FeatureFlagStoreImpl`, `setChristmasEnabled`.

Dependencias importadas: `android.content.Context`, `androidx.datastore.preferences.core.booleanPreferencesKey`, `androidx.datastore.preferences.core.edit`, `androidx.datastore.preferences.preferencesDataStore`, `kotlinx.coroutines.flow.Flow`, `kotlinx.coroutines.flow.map`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/FeatureFlagModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/FeatureFlagModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/core/remoteconfig/RemoteConfigManager.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/core/remoteconfig/RemoteConfigManager.kt](../../app/src/main/java/com/feryaeljustice/mirailink/core/remoteconfig/RemoteConfigManager.kt).

Declaraciones: `RemoteConfigManager`, `initialize`, `getGeminiModelName`, `getIsChristmasMode`, `RemoteConfigManagerImpl`.

Dependencias importadas: `com.feryaeljustice.mirailink.R`, `com.google.firebase.Firebase`, `com.google.firebase.remoteconfig.FirebaseRemoteConfig`, `com.google.firebase.remoteconfig.remoteConfig`, `com.google.firebase.remoteconfig.remoteConfigSettings`, `kotlinx.coroutines.tasks.await`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/AiModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/AiModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/billing/BillingClientManager.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/billing/BillingClientManager.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/billing/BillingClientManager.kt).

Declaraciones: `BillingPurchaseEvent`, `PurchaseSuccess`, `PurchaseFailed`, `PurchaseCanceled`, `BillingClientManager`, `startConnection`, `onBillingSetupFinished`, `onBillingServiceDisconnected`, `querySubscriptionProducts`, `launchBillingFlow`, `onPurchasesUpdated`, `handlePurchase`, `queryActivePurchases`.

Dependencias importadas: `android.app.Activity`, `android.content.Context`, `android.util.Log`, `com.android.billingclient.api.AcknowledgePurchaseParams`, `com.android.billingclient.api.BillingClient`, `com.android.billingclient.api.BillingClientStateListener`, `com.android.billingclient.api.BillingFlowParams`, `com.android.billingclient.api.BillingResult`, `com.android.billingclient.api.PendingPurchasesParams`, `com.android.billingclient.api.ProductDetails`, `com.android.billingclient.api.Purchase`, `com.android.billingclient.api.PurchasesUpdatedListener`, `com.android.billingclient.api.QueryProductDetailsParams`, `com.android.billingclient.api.QueryPurchasesParams`, `com.android.billingclient.api.acknowledgePurchase`, `com.android.billingclient.api.queryProductDetails`, `com.android.billingclient.api.queryPurchasesAsync`, `kotlinx.coroutines.CoroutineScope`, `kotlinx.coroutines.flow.MutableSharedFlow`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.SharedFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asSharedFlow`, `kotlinx.coroutines.flow.asStateFlow`, `kotlinx.coroutines.launch`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SubscriptionRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SubscriptionRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/LaunchBillingFlowUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/LaunchBillingFlowUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/datasource/AppConfigRemoteDataSource.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/AppConfigRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/AppConfigRemoteDataSource.kt).

Declaraciones: `AppConfigRemoteDataSource`, `getVersion`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.mappers.toDomain`, `com.feryaeljustice.mirailink.data.remote.AppConfigApiService`, `com.feryaeljustice.mirailink.data.util.safeApiCall`, `com.feryaeljustice.mirailink.domain.model.AppVersionInfo`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/AppConfigRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/AppConfigRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt).

Declaraciones: `CatalogRemoteDataSource`, `getAnimes`, `getGames`, `getCountries`, `getRegions`, `getCities`, `getProfileOptions`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.AnimeDto`, `com.feryaeljustice.mirailink.data.model.GameDto`, `com.feryaeljustice.mirailink.data.model.GeographicPlaceDto`, `com.feryaeljustice.mirailink.data.remote.CatalogApiService`, `com.feryaeljustice.mirailink.data.util.safeApiCall`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/CatalogRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/CatalogRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt).

Declaraciones: `ChatRemoteDataSource`, `getChatsFromUser`, `markChatAsRead`, `createPrivateChat`, `createGroupChat`, `sendMessage`, `getChatHistory`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.request.chat.ChatRequest`, `com.feryaeljustice.mirailink.data.model.request.chat.CreateGroupChatRequest`, `com.feryaeljustice.mirailink.data.model.response.chat.ChatMessageResponse`, `com.feryaeljustice.mirailink.data.model.response.chat.ChatSummaryResponse`, `com.feryaeljustice.mirailink.data.remote.ChatApiService`, `com.feryaeljustice.mirailink.data.util.NetworkErrorMapper`, `com.feryaeljustice.mirailink.data.util.NetworkOperation`, `com.feryaeljustice.mirailink.data.util.safeApiCall`, `com.feryaeljustice.mirailink.data.util.safeApiCallRecoveringHttp`, `com.feryaeljustice.mirailink.data.util.safeApiUnitResponse`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ChatRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ChatRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ExploreRemoteDataSource.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ExploreRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ExploreRemoteDataSource.kt).

Declaraciones: `ExploreRemoteDataSource`, `getCategories`, `getCategoryFeed`, `getCategorySettings`, `updateCategorySettings`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.UserDto`, `com.feryaeljustice.mirailink.data.model.explore.CategorySettingsResponseDto`, `com.feryaeljustice.mirailink.data.model.explore.ExploreHubResponseDto`, `com.feryaeljustice.mirailink.data.model.explore.UpdateCategorySettingsRequestDto`, `com.feryaeljustice.mirailink.data.remote.ExploreApiService`, `com.feryaeljustice.mirailink.data.util.NetworkOperation`, `com.feryaeljustice.mirailink.data.util.safeApiCall`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/datasource/FeedbackRemoteDatasource.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/FeedbackRemoteDatasource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/FeedbackRemoteDatasource.kt).

Declaraciones: `FeedbackRemoteDatasource`, `sendFeedback`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.request.feedback.SendFeedbackRequest`, `com.feryaeljustice.mirailink.data.remote.FeedbackApiService`, `com.feryaeljustice.mirailink.data.util.NetworkOperation`, `com.feryaeljustice.mirailink.data.util.safeApiUnitResponse`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/FeedbackRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/FeedbackRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/datasource/GeminiDataSource.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/GeminiDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/GeminiDataSource.kt).

Declaraciones: `GeminiDataSource`, `generateContent`.

Dependencias importadas: `com.google.firebase.ai.GenerativeModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/AiRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/AiRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/AiModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/AiModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/datasource/MatchRemoteDataSource.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/MatchRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/MatchRemoteDataSource.kt).

Declaraciones: `MatchRemoteDataSource`, `getMatches`, `getUnseenMatches`, `markMatchAsSeen`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.UserDto`, `com.feryaeljustice.mirailink.data.model.request.match.MarkMatchAsSeenRequest`, `com.feryaeljustice.mirailink.data.remote.MatchApiService`, `com.feryaeljustice.mirailink.data.util.NetworkOperation`, `com.feryaeljustice.mirailink.data.util.safeApiCall`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/MatchRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/MatchRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ReportRemoteDataSource.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ReportRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ReportRemoteDataSource.kt).

Declaraciones: `ReportRemoteDataSource`, `reportUser`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.request.report.ReportUserRequest`, `com.feryaeljustice.mirailink.data.remote.ReportApiService`, `com.feryaeljustice.mirailink.data.util.NetworkOperation`, `com.feryaeljustice.mirailink.data.util.safeApiUnitResponse`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ReportRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ReportRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt).

Declaraciones: `SwipeRemoteDataSource`, `getFeed`, `getReceivedLikes`, `likeUser`, `dislikeUser`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.UserDto`, `com.feryaeljustice.mirailink.data.model.request.swipe.SwipeRequest`, `com.feryaeljustice.mirailink.data.remote.SwipeApiService`, `com.feryaeljustice.mirailink.data.util.NetworkOperation`, `com.feryaeljustice.mirailink.data.util.safeApiCall`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SwipeRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SwipeRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/datasource/TwoFactorRemoteDataSource.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/TwoFactorRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/TwoFactorRemoteDataSource.kt).

Declaraciones: `TwoFactorRemoteDataSource`, `get2FAStatus`, `setup2FA`, `verify2FA`, `disable2FA`, `loginVerifyTwoFactorLastStep`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.response.auth.LoginResponse`, `com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorSetupResponse`, `com.feryaeljustice.mirailink.data.remote.TwoFactorApiService`, `com.feryaeljustice.mirailink.data.util.NetworkOperation`, `com.feryaeljustice.mirailink.data.util.safeApiCall`, `com.feryaeljustice.mirailink.data.util.safeApiUnitResponse`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/TwoFactorRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/TwoFactorRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt).

Declaraciones: `UserRemoteDataSource`, `autologin`, `login`, `logout`, `register`, `deleteAccount`, `deleteUserPhoto`, `requestPasswordReset`, `confirmPasswordReset`, `checkIsVerified`, `requestVerificationCode`, `confirmVerificationCode`, `getCurrentUser`, `getUserById`, `getUserByUsername`, `updateProfile`, `hasProfilePicture`, `uploadUserPhoto`, `saveUserFCM`, `updateSearchSettings`, `pingLocation`, `createPhotoPart`, `PreparedProfileRequest`, `PreparedPhotoUpload`.

Dependencias importadas: `android.content.Context`, `android.net.Uri`, `com.feryaeljustice.mirailink.data.model.ReorderedPhotoDto`, `com.feryaeljustice.mirailink.data.model.UserDto`, `com.feryaeljustice.mirailink.data.model.UserPhotoDto`, `com.feryaeljustice.mirailink.data.model.request.auth.LoginRequest`, `com.feryaeljustice.mirailink.data.model.response.auth.LoginResponse`, `com.feryaeljustice.mirailink.data.model.request.auth.PasswordResetConfirmRequest`, `com.feryaeljustice.mirailink.data.model.request.auth.RegisterRequest`, `com.feryaeljustice.mirailink.data.model.request.generic.ByIdRequest`, `com.feryaeljustice.mirailink.data.model.request.generic.EmailRequest`, `com.feryaeljustice.mirailink.data.model.request.notifications.SaveFCMUserRequest`, `com.feryaeljustice.mirailink.data.model.request.verification.VerificationConfirmRequest`, `com.feryaeljustice.mirailink.data.model.request.verification.VerificationRequest`, `com.feryaeljustice.mirailink.data.remote.UserApiService`, `com.feryaeljustice.mirailink.data.util.InvalidMediaException`, `com.feryaeljustice.mirailink.data.util.NetworkOperation`, `com.feryaeljustice.mirailink.data.util.safeApiCall`, `com.feryaeljustice.mirailink.data.util.safeApiUnitResponse`, `com.feryaeljustice.mirailink.data.util.safeLocalCall`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.Dispatchers`, `kotlinx.serialization.json.Json`, `okhttp3.MediaType.Companion.toMediaType`, `okhttp3.MediaType.Companion.toMediaTypeOrNull`, `okhttp3.MultipartBody`, `okhttp3.RequestBody`, `okhttp3.RequestBody.Companion.toRequestBody`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/LocationRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/LocationRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/UserRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/UserRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UsersRemoteDataSource.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UsersRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UsersRemoteDataSource.kt).

Declaraciones: `UsersRemoteDataSource`, `getUsers`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.UserDto`, `com.feryaeljustice.mirailink.data.remote.UsersApiService`, `com.feryaeljustice.mirailink.data.util.NetworkOperation`, `com.feryaeljustice.mirailink.data.util.safeApiCall`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/UsersRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/UsersRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/datastore/MiraiLinkPrefs.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/datastore/MiraiLinkPrefs.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datastore/MiraiLinkPrefs.kt).

Declaraciones: `MiraiLinkPrefs`, `markOnboardingCompleted`, `isOnboardingCompleted`.

Dependencias importadas: `androidx.datastore.core.DataStore`, `com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs`, `kotlinx.coroutines.flow.first`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/OnboardingRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/OnboardingRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkPrefs.kt](../../app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkPrefs.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/datastore/SessionManager.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/datastore/SessionManager.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datastore/SessionManager.kt).

Declaraciones: `SessionManager`, `getCurrentTokenSync`, `cacheTokenTemporarily`, `clearTemporaryToken`, `updateVerificationSync`, `clearSessionSync`, `saveSession`, `saveIsVerified`, `clearSession`, `getCurrentToken`.

Dependencias importadas: `androidx.datastore.core.DataStore`, `com.feryaeljustice.mirailink.data.model.local.datastore.Session`, `kotlinx.coroutines.CoroutineScope`, `kotlinx.coroutines.flow.Flow`, `kotlinx.coroutines.flow.MutableSharedFlow`, `kotlinx.coroutines.flow.SharedFlow`, `kotlinx.coroutines.flow.asSharedFlow`, `kotlinx.coroutines.flow.combine`, `kotlinx.coroutines.flow.first`, `kotlinx.coroutines.flow.map`, `kotlinx.coroutines.launch`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/demo/DemoModeManager.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/demo/DemoModeManager.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/interceptor/AuthInterceptor.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/interceptor/AuthInterceptor.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/UserRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/UserRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt](../../app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/datastore/crypto/KeystoreAesGcmProvider.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/datastore/crypto/KeystoreAesGcmProvider.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datastore/crypto/KeystoreAesGcmProvider.kt).

Declaraciones: `SecretKeyProvider`, `get`, `KeystoreAesGcmProvider`, `isAtLeastM`.

Dependencias importadas: `android.os.Build`, `android.security.keystore.KeyGenParameterSpec`, `android.security.keystore.KeyProperties`, `androidx.annotation.ChecksSdkIntAtLeast`, `java.security.KeyStore`, `javax.crypto.KeyGenerator`, `javax.crypto.SecretKey`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/CryptoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/CryptoModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/datastore/serializer/EncryptedJsonSerializer.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/datastore/serializer/EncryptedJsonSerializer.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datastore/serializer/EncryptedJsonSerializer.kt).

Declaraciones: `EncryptedJsonSerializer`, `readFrom`, `writeTo`.

Dependencias importadas: `androidx.datastore.core.Serializer`, `com.feryaeljustice.mirailink.data.datastore.crypto.SecretKeyProvider`, `kotlinx.serialization.KSerializer`, `kotlinx.serialization.json.Json`, `java.io.InputStream`, `java.io.OutputStream`, `java.nio.charset.StandardCharsets`, `javax.crypto.Cipher`, `javax.crypto.spec.GCMParameterSpec`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataStoreModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataStoreModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/demo/DemoModeManager.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/demo/DemoModeManager.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/demo/DemoModeManager.kt).

Declaraciones: `DemoModeManager`, `isDemoActive`, `enableDemoMode`, `disableDemoMode`, `resetDemoData`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder`, `com.feryaeljustice.mirailink.data.datastore.SessionManager`, `kotlinx.coroutines.CoroutineScope`, `kotlinx.coroutines.Dispatchers`, `kotlinx.coroutines.Job`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asStateFlow`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/LocationRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/LocationRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingChatRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingChatRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingExploreRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingExploreRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingMatchRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingMatchRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingSwipeRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingSwipeRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingUserRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingUserRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt](../../app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoDataSeeder.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoDataSeeder.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoDataSeeder.kt).

Declaraciones: `DemoDataSeeder`, `seedInitialDataIfEmpty`, `resetDemoData`.

Dependencias importadas: `androidx.room.withTransaction`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoChatEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoFeedUserEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoMatchEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoUserProfileEntity`, `com.feryaeljustice.mirailink.domain.model.catalog.Anime`, `com.feryaeljustice.mirailink.domain.model.catalog.Game`, `com.feryaeljustice.mirailink.domain.model.user.UserPhoto`, `kotlinx.serialization.encodeToString`, `kotlinx.serialization.json.Json`, `java.util.UUID`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/demo/DemoModeManager.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/demo/DemoModeManager.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoChatRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoChatRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoUserRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoUserRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt](../../app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt).

Declaraciones: `DemoUserProfileEntity.toDomainUser`, `DemoUserProfileEntity.toMinimalUserInfo`, `DemoFeedUserEntity.toDomainUser`, `DemoFeedUserEntity.toMinimalUserInfo`, `DemoMessageEntity.toDomainChatMessage`, `DemoChatEntity.toDomainChatSummary`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.local.demo.entity.DemoChatEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoFeedUserEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoUserProfileEntity`, `com.feryaeljustice.mirailink.domain.enums.ChatRole`, `com.feryaeljustice.mirailink.domain.enums.ChatType`, `com.feryaeljustice.mirailink.domain.model.catalog.Anime`, `com.feryaeljustice.mirailink.domain.model.catalog.Game`, `com.feryaeljustice.mirailink.domain.model.chat.ChatMessage`, `com.feryaeljustice.mirailink.domain.model.chat.ChatSummary`, `com.feryaeljustice.mirailink.domain.model.user.MinimalUserInfo`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.model.user.UserPhoto`, `kotlinx.serialization.json.Json`, `java.util.Date`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/MiraiLinkDemoDatabase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/MiraiLinkDemoDatabase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/MiraiLinkDemoDatabase.kt).

Declaraciones: `MiraiLinkDemoDatabase`, `userDao`, `matchDao`, `chatDao`, `categoryDao`.

Dependencias importadas: `androidx.room.Database`, `androidx.room.RoomDatabase`, `com.feryaeljustice.mirailink.data.local.demo.dao.DemoCategoryDao`, `com.feryaeljustice.mirailink.data.local.demo.dao.DemoChatDao`, `com.feryaeljustice.mirailink.data.local.demo.dao.DemoMatchDao`, `com.feryaeljustice.mirailink.data.local.demo.dao.DemoUserDao`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoCategoryPreferenceEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoChatEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoFeedUserEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoMatchEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoUserProfileEntity`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoChatRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoChatRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoMatchRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoMatchRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoUserRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoUserRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/dao/DemoCategoryDao.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/dao/DemoCategoryDao.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/dao/DemoCategoryDao.kt).

Declaraciones: `DemoCategoryDao`, `getPreference`, `insertOrUpdate`, `clearUserPreferences`.

Dependencias importadas: `androidx.room.Dao`, `androidx.room.Insert`, `androidx.room.OnConflictStrategy`, `androidx.room.Query`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoCategoryPreferenceEntity`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/MiraiLinkDemoDatabase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/MiraiLinkDemoDatabase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/dao/DemoDaos.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/dao/DemoDaos.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/dao/DemoDaos.kt).

Declaraciones: `DemoUserDao`, `getUserProfile`, `insertUserProfile`, `updateUserProfile`, `getFeedUsers`, `getFeedUserById`, `getAllFeedUsers`, `insertFeedUsers`, `markLiked`, `markDisliked`, `clearUserProfile`, `clearFeedUsers`, `DemoMatchDao`, `getAllMatches`, `getUnseenMatches`, `getMatchByUserId`, `insertMatch`, `markMatchesAsSeen`, `clearMatches`, `DemoChatDao`, `getAllChats`, `getChatByUserId`, `getChatById`, `insertOrUpdateChat`, `getMessagesBetween`, `insertMessage`, `markChatAsRead`, `clearChats`, `clearMessages`.

Dependencias importadas: `androidx.room.Dao`, `androidx.room.Insert`, `androidx.room.OnConflictStrategy`, `androidx.room.Query`, `androidx.room.Update`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoChatEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoFeedUserEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoMatchEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoUserProfileEntity`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/entity/DemoCategoryPreferenceEntity.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/entity/DemoCategoryPreferenceEntity.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/entity/DemoCategoryPreferenceEntity.kt).

Declaraciones: `DemoCategoryPreferenceEntity`.

Dependencias importadas: `androidx.room.Entity`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/MiraiLinkDemoDatabase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/MiraiLinkDemoDatabase.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/dao/DemoCategoryDao.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/dao/DemoCategoryDao.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/entity/DemoEntities.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/entity/DemoEntities.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/entity/DemoEntities.kt).

Declaraciones: `DemoUserProfileEntity`, `DemoFeedUserEntity`, `DemoMatchEntity`, `DemoChatEntity`, `DemoMessageEntity`.

Dependencias importadas: `androidx.room.Entity`, `androidx.room.PrimaryKey`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/manager/AdMobManager.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/manager/AdMobManager.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/manager/AdMobManager.kt).

Declaraciones: `AdMobManager`, `initialize`, `loadAd`, `onAdFailedToLoad`, `onAdLoaded`, `showInterstitial`, `onAdDismissedFullScreenContent`, `onAdFailedToShowFullScreenContent`, `onAdShowedFullScreenContent`.

Dependencias importadas: `android.app.Activity`, `android.content.Context`, `com.google.android.gms.ads.AdError`, `com.google.android.gms.ads.AdRequest`, `com.google.android.gms.ads.FullScreenContentCallback`, `com.google.android.gms.ads.LoadAdError`, `com.google.android.gms.ads.MobileAds`, `com.google.android.gms.ads.interstitial.InterstitialAd`, `com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback`, `com.feryaeljustice.mirailink.BuildConfig`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/MainActivity.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/MainActivity.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/mappers/AppVersionInfoMapper.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/AppVersionInfoMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/AppVersionInfoMapper.kt).

Declaraciones: `AppVersionInfoDto.toDomain`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.AppVersionInfoDto`, `com.feryaeljustice.mirailink.domain.model.AppVersionInfo`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/mappers/AuthSessionMapper.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/AuthSessionMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/AuthSessionMapper.kt).

Declaraciones: `LoginResponse.toAuthSessionInfo`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.response.auth.LoginResponse`, `com.feryaeljustice.mirailink.domain.model.auth.AuthSessionInfo`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/mappers/CatalogMapper.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/CatalogMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/CatalogMapper.kt).

Declaraciones: `AnimeDto.toDomain`, `GameDto.toDomain`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.AnimeDto`, `com.feryaeljustice.mirailink.data.model.GameDto`, `com.feryaeljustice.mirailink.domain.model.catalog.Anime`, `com.feryaeljustice.mirailink.domain.model.catalog.Game`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ChatMapper.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ChatMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ChatMapper.kt).

Declaraciones: `ChatSummaryResponse.toDomain`, `ChatMessageResponse.toDomain`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.response.chat.ChatMessageResponse`, `com.feryaeljustice.mirailink.data.model.response.chat.ChatSummaryResponse`, `com.feryaeljustice.mirailink.domain.enums.ChatRole`, `com.feryaeljustice.mirailink.domain.enums.ChatType`, `com.feryaeljustice.mirailink.domain.model.chat.ChatMessage`, `com.feryaeljustice.mirailink.domain.model.chat.ChatSummary`, `com.feryaeljustice.mirailink.domain.util.parseDate`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/mappers/MediaMapper.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/MediaMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/MediaMapper.kt).

Declaraciones: `UserPhotoDto.toDomain`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.UserPhotoDto`, `com.feryaeljustice.mirailink.domain.model.user.UserPhoto`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/mappers/TwoFactorAuthMapper.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/TwoFactorAuthMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/TwoFactorAuthMapper.kt).

Declaraciones: `TwoFactorSetupResponse.toTwoFactorAuthInfo`, `TwoFactorStatusResponse.toTwoFactorAuthInfo`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorSetupResponse`, `com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorStatusResponse`, `com.feryaeljustice.mirailink.domain.model.auth.TwoFactorAuthInfo`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/mappers/UserMapper.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/UserMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/UserMapper.kt).

Declaraciones: `UserDto.toDomain`, `MinimalUserInfoResponse.toMinimalUserInfo`, `UserDto.toMinimalUserInfo`, `User.toMinimalUserInfo`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.UserDto`, `com.feryaeljustice.mirailink.data.model.response.user.MinimalUserInfoResponse`, `com.feryaeljustice.mirailink.domain.model.user.MinimalUserInfo`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.model.user.UserPhoto`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/AppConfigMappers.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/AppConfigMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/AppConfigMappers.kt).

Declaraciones: `VersionCheckResult.toVersionCheckResultViewEntry`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.VersionCheckResult`, `com.feryaeljustice.mirailink.ui.viewentries.VersionCheckResultViewEntry`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/CatalogViewEntry.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/CatalogViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/CatalogViewEntry.kt).

Declaraciones: `Anime.toAnimeViewEntry`, `Game.toGameViewEntry`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.catalog.Anime`, `com.feryaeljustice.mirailink.domain.model.catalog.Game`, `com.feryaeljustice.mirailink.ui.viewentries.catalog.AnimeViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.catalog.GameViewEntry`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/ChatMappers.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/ChatMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/ChatMappers.kt).

Declaraciones: `ChatSummary.toChatPreviewViewEntry`, `ChatMessage.toChatMessageViewEntry`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.chat.ChatMessage`, `com.feryaeljustice.mirailink.domain.model.chat.ChatSummary`, `com.feryaeljustice.mirailink.domain.util.getFormattedUrl`, `com.feryaeljustice.mirailink.ui.viewentries.chat.ChatMessageViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.chat.ChatPreviewViewEntry`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/MediaMappers.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/MediaMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/MediaMappers.kt).

Declaraciones: `UserPhotoViewEntry.toPhotoSlotViewEntry`, `UserPhoto.toUserPhotoViewEntry`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.user.UserPhoto`, `com.feryaeljustice.mirailink.ui.viewentries.media.PhotoSlotViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.media.UserPhotoViewEntry`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/UserMappers.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/UserMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/UserMappers.kt).

Declaraciones: `User.toUserViewEntry`, `User.toMatchUserViewEntry`, `MinimalUserInfo.toMinimalUserInfoViewEntry`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.constants.TEMPORAL_PLACEHOLDER_PICTURE_URL`, `com.feryaeljustice.mirailink.domain.model.user.MinimalUserInfo`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.ui.viewentries.user.MatchUserViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.user.MinimalUserInfoViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/model/AnimeDto.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/AnimeDto.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/AnimeDto.kt).

Declaraciones: `AnimeDto`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/CatalogMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/CatalogMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/CatalogApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/CatalogApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/AppVersionInfoDto.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/AppVersionInfoDto.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/AppVersionInfoDto.kt).

Declaraciones: `AppVersionInfoDto`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/AppVersionInfoMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/AppVersionInfoMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/AppConfigApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/AppConfigApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/GameDto.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/GameDto.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/GameDto.kt).

Declaraciones: `GameDto`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/CatalogMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/CatalogMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/CatalogApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/CatalogApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/GeographicPlaceDto.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/GeographicPlaceDto.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/GeographicPlaceDto.kt).

Declaraciones: `GeographicPlaceDto`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/CatalogApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/CatalogApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/CatalogRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/CatalogRepositoryImpl.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/ReorderedPhotoDto.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/ReorderedPhotoDto.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/ReorderedPhotoDto.kt).

Declaraciones: `ReorderedPhotoDto`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/UserDto.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/UserDto.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/UserDto.kt).

Declaraciones: `UserDto`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ExploreRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ExploreRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/MatchRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/MatchRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UsersRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UsersRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/UserMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/UserMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/chat/ChatMessageResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/chat/ChatMessageResponse.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/swipe/ReceivedLikeDto.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/swipe/ReceivedLikeDto.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/ExploreApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ExploreApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/MatchApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/MatchApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UsersApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UsersApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/UserPhotoDto.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/UserPhotoDto.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/UserPhotoDto.kt).

Declaraciones: `UserPhotoDto`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/MediaMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/MediaMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/UserPromptAnswerDto.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/UserPromptAnswerDto.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/UserPromptAnswerDto.kt).

Declaraciones: `UserPromptAnswerDto`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/model/explore/ExploreDtos.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/explore/ExploreDtos.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/explore/ExploreDtos.kt).

Declaraciones: `ExploreCategoryDto`, `ExploreSectionDto`, `ExploreHubResponseDto`, `CategorySettingsResponseDto`, `UpdateCategorySettingsRequestDto`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/model/local/datastore/AppPrefs.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/local/datastore/AppPrefs.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/local/datastore/AppPrefs.kt).

Declaraciones: `AppPrefs`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datastore/MiraiLinkPrefs.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datastore/MiraiLinkPrefs.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataStoreModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataStoreModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/local/datastore/Session.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/local/datastore/Session.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/local/datastore/Session.kt).

Declaraciones: `Session`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datastore/SessionManager.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datastore/SessionManager.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/demo/DemoModeManager.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/demo/DemoModeManager.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/interceptor/AuthInterceptor.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/interceptor/AuthInterceptor.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/UserRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/UserRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataStoreModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataStoreModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt](../../app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/auth/LoginRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/auth/LoginRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/auth/LoginRequest.kt).

Declaraciones: `LoginRequest`.

Dependencias importadas: `kotlinx.serialization.EncodeDefault`, `kotlinx.serialization.ExperimentalSerializationApi`, `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/auth/PasswordResetConfirmRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/auth/PasswordResetConfirmRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/auth/PasswordResetConfirmRequest.kt).

Declaraciones: `PasswordResetConfirmRequest`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/auth/RegisterRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/auth/RegisterRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/auth/RegisterRequest.kt).

Declaraciones: `RegisterRequest`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/chat/ChatRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/chat/ChatRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/chat/ChatRequest.kt).

Declaraciones: `ChatRequest`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/chat/CreateGroupChatRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/chat/CreateGroupChatRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/chat/CreateGroupChatRequest.kt).

Declaraciones: `CreateGroupChatRequest`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/feedback/SendFeedbackRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/feedback/SendFeedbackRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/feedback/SendFeedbackRequest.kt).

Declaraciones: `SendFeedbackRequest`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/FeedbackRemoteDatasource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/FeedbackRemoteDatasource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/FeedbackApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/FeedbackApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/generic/ByIdRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/generic/ByIdRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/generic/ByIdRequest.kt).

Declaraciones: `ByIdRequest`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/generic/EmailRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/generic/EmailRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/generic/EmailRequest.kt).

Declaraciones: `EmailRequest`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/location/LocationPingRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/location/LocationPingRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/location/LocationPingRequest.kt).

Declaraciones: `LocationPingRequest`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/match/MarkMatchAsSeenRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/match/MarkMatchAsSeenRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/match/MarkMatchAsSeenRequest.kt).

Declaraciones: `MarkMatchAsSeenRequest`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/MatchRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/MatchRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/MatchApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/MatchApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/notifications/SaveFCMUserRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/notifications/SaveFCMUserRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/notifications/SaveFCMUserRequest.kt).

Declaraciones: `SaveFCMUserRequest`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/report/ReportUserRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/report/ReportUserRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/report/ReportUserRequest.kt).

Declaraciones: `ReportUserRequest`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ReportRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ReportRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/ReportApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ReportApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/settings/UpdateSearchSettingsRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/settings/UpdateSearchSettingsRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/settings/UpdateSearchSettingsRequest.kt).

Declaraciones: `UpdateSearchSettingsRequest`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/subscription/CancelSubscriptionIntentRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/subscription/CancelSubscriptionIntentRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/subscription/CancelSubscriptionIntentRequest.kt).

Declaraciones: `CancelSubscriptionIntentRequest`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/SubscriptionApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SubscriptionApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/subscription/VerifySubscriptionRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/subscription/VerifySubscriptionRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/subscription/VerifySubscriptionRequest.kt).

Declaraciones: `VerifySubscriptionRequest`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/SubscriptionApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SubscriptionApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/swipe/SwipeRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/swipe/SwipeRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/swipe/SwipeRequest.kt).

Declaraciones: `SwipeRequest`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/verification/VerificationConfirmRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/verification/VerificationConfirmRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/verification/VerificationConfirmRequest.kt).

Declaraciones: `VerificationConfirmRequest`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/request/verification/VerificationRequest.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/request/verification/VerificationRequest.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/request/verification/VerificationRequest.kt).

Declaraciones: `VerificationRequest`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/AutologinResponse.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/AutologinResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/AutologinResponse.kt).

Declaraciones: `AutologinResponse`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/CheckIsVerifiedResponse.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/CheckIsVerifiedResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/CheckIsVerifiedResponse.kt).

Declaraciones: `CheckIsVerifiedResponse`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/LoginResponse.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/LoginResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/LoginResponse.kt).

Declaraciones: `LoginResponse`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/TwoFactorRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/TwoFactorRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/AuthSessionMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/AuthSessionMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/TwoFactorApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/TwoFactorApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/TwoFactorRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/TwoFactorRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/TwoFactorRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/TwoFactorRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/LoginVerifyTwoFactorLastStepUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/LoginVerifyTwoFactorLastStepUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/LogoutResponse.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/LogoutResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/LogoutResponse.kt).

Declaraciones: `LogoutResponse`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/RegisterResponse.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/RegisterResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/RegisterResponse.kt).

Declaraciones: `RegisterResponse`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/two_factor/TwoFactorSetupResponse.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/two_factor/TwoFactorSetupResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/two_factor/TwoFactorSetupResponse.kt).

Declaraciones: `TwoFactorSetupResponse`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/TwoFactorRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/TwoFactorRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/TwoFactorAuthMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/TwoFactorAuthMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/TwoFactorApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/TwoFactorApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/two_factor/TwoFactorStatusResponse.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/two_factor/TwoFactorStatusResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/auth/two_factor/TwoFactorStatusResponse.kt).

Declaraciones: `TwoFactorStatusResponse`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/TwoFactorAuthMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/TwoFactorAuthMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/TwoFactorApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/TwoFactorApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/catalog/ProfileOptionsDto.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/catalog/ProfileOptionsDto.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/catalog/ProfileOptionsDto.kt).

Declaraciones: `CatalogItemOptionDto`, `ProfileOptionsResponseDto`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/chat/ChatIdResponse.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/chat/ChatIdResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/chat/ChatIdResponse.kt).

Declaraciones: `ChatIdResponse`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/chat/ChatMessageResponse.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/chat/ChatMessageResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/chat/ChatMessageResponse.kt).

Declaraciones: `ChatMessageResponse`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.UserDto`, `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ChatMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ChatMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/chat/ChatSummaryResponse.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/chat/ChatSummaryResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/chat/ChatSummaryResponse.kt).

Declaraciones: `ChatSummaryResponse`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.response.user.MinimalUserInfoResponse`, `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ChatMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ChatMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/generic/ApiErrorResponse.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/generic/ApiErrorResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/generic/ApiErrorResponse.kt).

Declaraciones: `ApiErrorResponse`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/util/NetworkErrorMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/util/NetworkErrorMapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/generic/BasicResponse.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/generic/BasicResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/generic/BasicResponse.kt).

Declaraciones: `BasicResponse`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/MatchApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/MatchApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/photo/UploadPhotoResponse.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/photo/UploadPhotoResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/photo/UploadPhotoResponse.kt).

Declaraciones: `UploadPhotoResponse`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/subscription/CancelSubscriptionIntentResponse.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/subscription/CancelSubscriptionIntentResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/subscription/CancelSubscriptionIntentResponse.kt).

Declaraciones: `CancelSubscriptionIntentResponse`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/SubscriptionApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SubscriptionApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/subscription/SubscriptionStatusDto.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/subscription/SubscriptionStatusDto.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/subscription/SubscriptionStatusDto.kt).

Declaraciones: `SubscriptionStatusDto`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/SubscriptionApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SubscriptionApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/swipe/ReceivedLikeDto.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/swipe/ReceivedLikeDto.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/swipe/ReceivedLikeDto.kt).

Declaraciones: `ReceivedLikeDto`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.UserDto`, `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/swipe/SwipeResponse.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/swipe/SwipeResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/swipe/SwipeResponse.kt).

Declaraciones: `SwipeResponse`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/model/response/user/MinimalUserInfoResponse.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/user/MinimalUserInfoResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/user/MinimalUserInfoResponse.kt).

Declaraciones: `MinimalUserInfoResponse`.

Dependencias importadas: `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/UserMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/UserMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/chat/ChatSummaryResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/chat/ChatSummaryResponse.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/remote/AppConfigApiService.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/AppConfigApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/AppConfigApiService.kt).

Declaraciones: `AppConfigApiService`, `getAndroidAppVersion`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.AppVersionInfoDto`, `retrofit2.http.GET`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/AppConfigRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/AppConfigRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/remote/CatalogApiService.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/CatalogApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/CatalogApiService.kt).

Declaraciones: `CatalogApiService`, `getAllAnimes`, `getAllGames`, `getCountries`, `getRegions`, `getCities`, `getProfileOptions`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.AnimeDto`, `com.feryaeljustice.mirailink.data.model.GameDto`, `com.feryaeljustice.mirailink.data.model.GeographicPlaceDto`, `retrofit2.http.GET`, `retrofit2.http.Path`, `retrofit2.http.Query`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt).

Declaraciones: `ChatApiService`, `getChatsFromUser`, `markChatAsRead`, `createPrivateChat`, `createGroupChat`, `sendMessage`, `getChatHistory`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.request.chat.ChatRequest`, `com.feryaeljustice.mirailink.data.model.request.chat.CreateGroupChatRequest`, `com.feryaeljustice.mirailink.data.model.response.chat.ChatIdResponse`, `com.feryaeljustice.mirailink.data.model.response.chat.ChatMessageResponse`, `com.feryaeljustice.mirailink.data.model.response.chat.ChatSummaryResponse`, `retrofit2.Response`, `retrofit2.http.Body`, `retrofit2.http.GET`, `retrofit2.http.PATCH`, `retrofit2.http.POST`, `retrofit2.http.Path`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/remote/ExploreApiService.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/ExploreApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ExploreApiService.kt).

Declaraciones: `ExploreApiService`, `getCategories`, `getCategoryFeed`, `getCategorySettings`, `updateCategorySettings`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.UserDto`, `com.feryaeljustice.mirailink.data.model.explore.CategorySettingsResponseDto`, `com.feryaeljustice.mirailink.data.model.explore.ExploreHubResponseDto`, `com.feryaeljustice.mirailink.data.model.explore.UpdateCategorySettingsRequestDto`, `retrofit2.http.Body`, `retrofit2.http.GET`, `retrofit2.http.PUT`, `retrofit2.http.Path`, `retrofit2.http.Query`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ExploreRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ExploreRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/remote/FeedbackApiService.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/FeedbackApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/FeedbackApiService.kt).

Declaraciones: `FeedbackApiService`, `sendFeeback`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.request.feedback.SendFeedbackRequest`, `retrofit2.Response`, `retrofit2.http.Body`, `retrofit2.http.POST`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/FeedbackRemoteDatasource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/FeedbackRemoteDatasource.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/remote/MatchApiService.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/MatchApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/MatchApiService.kt).

Declaraciones: `MatchApiService`, `getMatches`, `getUnseenMatches`, `markMatchAsSeen`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.UserDto`, `com.feryaeljustice.mirailink.data.model.request.match.MarkMatchAsSeenRequest`, `com.feryaeljustice.mirailink.data.model.response.generic.BasicResponse`, `retrofit2.http.Body`, `retrofit2.http.GET`, `retrofit2.http.POST`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/MatchRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/MatchRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/remote/ReportApiService.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/ReportApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ReportApiService.kt).

Declaraciones: `ReportApiService`, `reportUser`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.request.report.ReportUserRequest`, `retrofit2.Response`, `retrofit2.http.Body`, `retrofit2.http.POST`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ReportRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ReportRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/remote/SubscriptionApiService.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/SubscriptionApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SubscriptionApiService.kt).

Declaraciones: `SubscriptionApiService`, `getSubscriptionStatus`, `verifySubscription`, `cancelSubscriptionIntent`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.request.subscription.CancelSubscriptionIntentRequest`, `com.feryaeljustice.mirailink.data.model.request.subscription.VerifySubscriptionRequest`, `com.feryaeljustice.mirailink.data.model.response.subscription.CancelSubscriptionIntentResponse`, `com.feryaeljustice.mirailink.data.model.response.subscription.SubscriptionStatusDto`, `retrofit2.http.Body`, `retrofit2.http.GET`, `retrofit2.http.POST`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt).

Declaraciones: `SwipeApiService`, `getFeed`, `getReceivedLikes`, `likeUser`, `dislikeUser`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.UserDto`, `com.feryaeljustice.mirailink.data.model.request.swipe.SwipeRequest`, `com.feryaeljustice.mirailink.data.model.response.generic.BasicResponse`, `com.feryaeljustice.mirailink.data.model.response.swipe.SwipeResponse`, `retrofit2.http.Body`, `retrofit2.http.GET`, `retrofit2.http.POST`, `retrofit2.http.Query`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/remote/TwoFactorApiService.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/TwoFactorApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/TwoFactorApiService.kt).

Declaraciones: `TwoFactorApiService`, `getTwoFactorStatus`, `setupTwoFactor`, `verifyTwoFactor`, `disableTwoFactor`, `loginVerifyTwoFactorLastStep`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.response.auth.LoginResponse`, `com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorSetupResponse`, `com.feryaeljustice.mirailink.data.model.response.auth.two_factor.TwoFactorStatusResponse`, `retrofit2.Response`, `retrofit2.http.Body`, `retrofit2.http.POST`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/TwoFactorRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/TwoFactorRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt).

Declaraciones: `UserApiService`, `autologin`, `login`, `logout`, `register`, `requestPasswordReset`, `confirmPasswordReset`, `checkIsVerified`, `requestVerificationCode`, `confirmVerificationCode`, `getCurrentUser`, `deleteAccount`, `deleteUserPhoto`, `getUserPhotos`, `uploadUserPhoto`, `getUserById`, `getUserByUsername`, `updateProfile`, `saveUserFcm`, `updateSearchSettings`, `pingLocation`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.UserDto`, `com.feryaeljustice.mirailink.data.model.UserPhotoDto`, `com.feryaeljustice.mirailink.data.model.request.auth.LoginRequest`, `com.feryaeljustice.mirailink.data.model.request.auth.PasswordResetConfirmRequest`, `com.feryaeljustice.mirailink.data.model.request.auth.RegisterRequest`, `com.feryaeljustice.mirailink.data.model.request.generic.ByIdRequest`, `com.feryaeljustice.mirailink.data.model.request.generic.EmailRequest`, `com.feryaeljustice.mirailink.data.model.request.notifications.SaveFCMUserRequest`, `com.feryaeljustice.mirailink.data.model.request.verification.VerificationConfirmRequest`, `com.feryaeljustice.mirailink.data.model.request.verification.VerificationRequest`, `com.feryaeljustice.mirailink.data.model.response.auth.AutologinResponse`, `com.feryaeljustice.mirailink.data.model.response.auth.CheckIsVerifiedResponse`, `com.feryaeljustice.mirailink.data.model.response.auth.LoginResponse`, `com.feryaeljustice.mirailink.data.model.response.auth.LogoutResponse`, `com.feryaeljustice.mirailink.data.model.response.auth.RegisterResponse`, `com.feryaeljustice.mirailink.data.model.response.generic.BasicResponse`, `com.feryaeljustice.mirailink.data.model.response.photo.UploadPhotoResponse`, `okhttp3.MultipartBody`, `okhttp3.RequestBody`, `retrofit2.Response`, `retrofit2.http.Body`, `retrofit2.http.DELETE`, `retrofit2.http.GET`, `retrofit2.http.Multipart`, `retrofit2.http.POST`, `retrofit2.http.PUT`, `retrofit2.http.Part`, `retrofit2.http.Path`, `retrofit2.http.Query`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/remote/UsersApiService.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UsersApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UsersApiService.kt).

Declaraciones: `UsersApiService`, `getUsers`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.UserDto`, `retrofit2.http.GET`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UsersRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UsersRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/remote/interceptor/AuthInterceptor.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/interceptor/AuthInterceptor.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/interceptor/AuthInterceptor.kt).

Declaraciones: `AuthInterceptor`, `intercept`, `parseJsonBoolean`.

Dependencias importadas: `android.util.Log`, `com.feryaeljustice.mirailink.data.datastore.SessionManager`, `okhttp3.Interceptor`, `okhttp3.Response`, `okhttp3.ResponseBody.Companion.toResponseBody`, `org.json.JSONObject`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/remote/interceptor/ImageDomainSecurityInterceptor.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/interceptor/ImageDomainSecurityInterceptor.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/interceptor/ImageDomainSecurityInterceptor.kt).

Declaraciones: `ImageDomainSecurityInterceptor`, `intercept`.

Dependencias importadas: `com.feryaeljustice.mirailink.BuildConfig`, `okhttp3.Interceptor`, `okhttp3.Response`, `java.io.IOException`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/remote/socket/SocketService.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/remote/socket/SocketService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/socket/SocketService.kt).

Declaraciones: `SocketService`, `initSocket`, `connect`, `disconnect`, `on`, `emit`, `off`.

Dependencias importadas: `io.socket.client.IO`, `io.socket.client.Socket`, `org.json.JSONObject`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ChatRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ChatRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/SocketModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/SocketModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/AiRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/AiRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/AiRepositoryImpl.kt).

Declaraciones: `AiRepositoryImpl`, `generateContent`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datasource.GeminiDataSource`, `com.feryaeljustice.mirailink.domain.repository.AiRepository`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/AiModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/AiModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/AppConfigRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/AppConfigRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/AppConfigRepositoryImpl.kt).

Declaraciones: `AppConfigRepositoryImpl`, `getVersion`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datasource.AppConfigRemoteDataSource`, `com.feryaeljustice.mirailink.domain.repository.AppConfigRepository`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/CatalogRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/CatalogRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/CatalogRepositoryImpl.kt).

Declaraciones: `CatalogRepositoryImpl`, `getAnimes`, `getGames`, `getCountries`, `getRegions`, `getCities`, `getProfileOptions`, `MiraiLinkResult<List<com.feryaeljustice.mirailink.data.model.GeographicPlaceDto>>.toGeographicPlaces`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datasource.CatalogRemoteDataSource`, `com.feryaeljustice.mirailink.data.mappers.toDomain`, `com.feryaeljustice.mirailink.domain.model.catalog.Anime`, `com.feryaeljustice.mirailink.domain.model.catalog.Game`, `com.feryaeljustice.mirailink.domain.model.geography.GeographicPlace`, `com.feryaeljustice.mirailink.domain.repository.CatalogRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/ChatRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ChatRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ChatRepositoryImpl.kt).

Declaraciones: `ChatRepositoryImpl`, `connectSocket`, `disconnectSocket`, `getChatsFromUser`, `markChatAsRead`, `createPrivateChat`, `createGroupChat`, `sendMessageTo`, `getMessagesWith`, `listenForMessages`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datasource.ChatRemoteDataSource`, `com.feryaeljustice.mirailink.data.mappers.toDomain`, `com.feryaeljustice.mirailink.data.remote.socket.SocketService`, `com.feryaeljustice.mirailink.domain.model.chat.ChatMessage`, `com.feryaeljustice.mirailink.domain.model.chat.ChatSummary`, `com.feryaeljustice.mirailink.domain.repository.ChatRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.domain.util.resolvePhotoUrl`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt).

Declaraciones: `ExploreRepositoryImpl`, `getExploreHubData`, `getCategoryFeed`, `getCategoryPreferences`, `updateCategoryPreferences`, `ExploreCategoryDto.toDomain`, `ExploreSectionDto.toDomain`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datasource.ExploreRemoteDataSource`, `com.feryaeljustice.mirailink.data.mappers.toDomain`, `com.feryaeljustice.mirailink.data.model.explore.ExploreCategoryDto`, `com.feryaeljustice.mirailink.data.model.explore.ExploreSectionDto`, `com.feryaeljustice.mirailink.domain.model.explore.CategoryPreference`, `com.feryaeljustice.mirailink.domain.model.explore.ExploreCategory`, `com.feryaeljustice.mirailink.domain.model.explore.ExploreSection`, `com.feryaeljustice.mirailink.domain.model.explore.ExploreSectionGroup`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.ExploreHubData`, `com.feryaeljustice.mirailink.domain.repository.ExploreRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.domain.util.resolvePhotoUrls`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/FaqRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/FaqRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/FaqRepositoryImpl.kt).

Declaraciones: `FaqRepositoryImpl`, `getFaqItems`.

Dependencias importadas: `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.model.faq.FaqCategory`, `com.feryaeljustice.mirailink.domain.model.faq.FaqItem`, `com.feryaeljustice.mirailink.domain.repository.FaqRepository`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/FeedbackRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/FeedbackRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/FeedbackRepositoryImpl.kt).

Declaraciones: `FeedbackRepositoryImpl`, `sendFeedback`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datasource.FeedbackRemoteDatasource`, `com.feryaeljustice.mirailink.domain.repository.FeedbackRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/LocationRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/LocationRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/LocationRepositoryImpl.kt).

Declaraciones: `LocationRepositoryImpl`, `sendLocationPing`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datasource.UserRemoteDataSource`, `com.feryaeljustice.mirailink.data.demo.DemoModeManager`, `com.feryaeljustice.mirailink.domain.repository.LocationRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/MatchRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/MatchRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/MatchRepositoryImpl.kt).

Declaraciones: `MatchRepositoryImpl`, `getMatches`, `getUnseenMatches`, `markMatchAsSeen`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datasource.MatchRemoteDataSource`, `com.feryaeljustice.mirailink.data.mappers.toDomain`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.MatchRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.domain.util.resolvePhotoUrls`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/OnboardingRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/OnboardingRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/OnboardingRepositoryImpl.kt).

Declaraciones: `OnboardingRepositoryImpl`, `checkOnboardingIsCompleted`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datastore.MiraiLinkPrefs`, `com.feryaeljustice.mirailink.domain.error.DataError`, `com.feryaeljustice.mirailink.domain.repository.OnboardingRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `java.util.concurrent.CancellationException`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/ReportRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ReportRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ReportRepositoryImpl.kt).

Declaraciones: `ReportRepositoryImpl`, `reportUser`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datasource.ReportRemoteDataSource`, `com.feryaeljustice.mirailink.domain.repository.ReportRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt).

Declaraciones: `SearchPreferencesRepositoryImpl`, `getSearchPreferences`, `saveSearchPreferences`.

Dependencias importadas: `androidx.datastore.core.DataStore`, `com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs`, `com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences`, `com.feryaeljustice.mirailink.domain.model.settings.SearchScope`, `com.feryaeljustice.mirailink.domain.repository.SearchPreferencesRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `kotlinx.coroutines.flow.Flow`, `kotlinx.coroutines.flow.catch`, `kotlinx.coroutines.flow.map`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt).

Declaraciones: `SubscriptionRepositoryImpl`, `fetchSubscriptionStatus`, `launchBillingFlow`, `verifyPurchase`, `restorePurchases`, `requestCancelIntent`, `SubscriptionStatusDto.toDomain`, `mapOffers`, `buildTierOffers`.

Dependencias importadas: `android.app.Activity`, `com.android.billingclient.api.BillingClient`, `com.feryaeljustice.mirailink.data.billing.BillingClientManager`, `com.feryaeljustice.mirailink.data.billing.BillingPurchaseEvent`, `com.feryaeljustice.mirailink.data.model.request.subscription.CancelSubscriptionIntentRequest`, `com.feryaeljustice.mirailink.data.model.request.subscription.VerifySubscriptionRequest`, `com.feryaeljustice.mirailink.data.model.response.subscription.SubscriptionStatusDto`, `com.feryaeljustice.mirailink.data.remote.SubscriptionApiService`, `com.feryaeljustice.mirailink.data.util.NetworkOperation`, `com.feryaeljustice.mirailink.data.util.safeApiCall`, `com.feryaeljustice.mirailink.domain.error.DataError`, `com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanInfo`, `com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanType`, `com.feryaeljustice.mirailink.domain.repository.SubscriptionRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `kotlinx.coroutines.CoroutineScope`, `kotlinx.coroutines.flow.Flow`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.SharingStarted`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asStateFlow`, `kotlinx.coroutines.flow.map`, `kotlinx.coroutines.flow.stateIn`, `kotlinx.coroutines.launch`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.android.billingclient.api.ProductDetails`, `com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionDuration`, `com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionOfferOption`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/SwipeRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SwipeRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SwipeRepositoryImpl.kt).

Declaraciones: `SwipeRepositoryImpl`, `getFeed`, `getReceivedLikes`, `likeUser`, `dislikeUser`, `String.isCanonicalUuid`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datasource.SwipeRemoteDataSource`, `com.feryaeljustice.mirailink.data.mappers.toDomain`, `com.feryaeljustice.mirailink.domain.error.ValidationError`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.SwipeRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.domain.util.resolvePhotoUrls`, `java.util.UUID`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/TwoFactorRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/TwoFactorRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/TwoFactorRepositoryImpl.kt).

Declaraciones: `TwoFactorRepositoryImpl`, `get2FAStatus`, `setup2FA`, `verify2FA`, `disable2FA`, `loginVerifyTwoFactorLastStep`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datasource.TwoFactorRemoteDataSource`, `com.feryaeljustice.mirailink.data.mappers.toTwoFactorAuthInfo`, `com.feryaeljustice.mirailink.data.model.response.auth.LoginResponse`, `com.feryaeljustice.mirailink.domain.model.auth.TwoFactorAuthInfo`, `com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/UserRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/UserRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/UserRepositoryImpl.kt).

Declaraciones: `UserRepositoryImpl`, `autologin`, `login`, `logout`, `register`, `deleteAccount`, `deleteUserPhoto`, `requestPasswordReset`, `confirmPasswordReset`, `checkIsVerified`, `requestVerificationCode`, `confirmVerificationCode`, `getCurrentUser`, `getUserById`, `getUserByUsername`, `updateProfile`, `hasProfilePicture`, `uploadUserPhoto`, `saveUserFCM`.

Dependencias importadas: `android.net.Uri`, `com.feryaeljustice.mirailink.data.datasource.UserRemoteDataSource`, `com.feryaeljustice.mirailink.data.datastore.SessionManager`, `com.feryaeljustice.mirailink.data.mappers.toAuthSessionInfo`, `com.feryaeljustice.mirailink.data.mappers.toDomain`, `com.feryaeljustice.mirailink.domain.model.auth.AuthSessionInfo`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.domain.util.map`, `com.feryaeljustice.mirailink.domain.util.resolvePhotoUrls`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/UsersRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/UsersRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/UsersRepositoryImpl.kt).

Declaraciones: `UsersRepositoryImpl`, `getUsers`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datasource.UsersRemoteDataSource`, `com.feryaeljustice.mirailink.data.mappers.toDomain`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.UsersRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.domain.util.resolvePhotoUrls`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingChatRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingChatRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingChatRepository.kt).

Declaraciones: `DelegatingChatRepository`, `targetRepo`, `connectSocket`, `disconnectSocket`, `getChatsFromUser`, `markChatAsRead`, `createPrivateChat`, `createGroupChat`, `getMessagesWith`, `sendMessageTo`, `listenForMessages`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.demo.DemoModeManager`, `com.feryaeljustice.mirailink.domain.model.chat.ChatMessage`, `com.feryaeljustice.mirailink.domain.model.chat.ChatSummary`, `com.feryaeljustice.mirailink.domain.repository.ChatRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingExploreRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingExploreRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingExploreRepository.kt).

Declaraciones: `DelegatingExploreRepository`, `targetRepo`, `getExploreHubData`, `getCategoryFeed`, `getCategoryPreferences`, `updateCategoryPreferences`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.demo.DemoModeManager`, `com.feryaeljustice.mirailink.domain.model.explore.CategoryPreference`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.ExploreHubData`, `com.feryaeljustice.mirailink.domain.repository.ExploreRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingMatchRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingMatchRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingMatchRepository.kt).

Declaraciones: `DelegatingMatchRepository`, `targetRepo`, `getMatches`, `getUnseenMatches`, `markMatchAsSeen`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.demo.DemoModeManager`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.MatchRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingSwipeRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingSwipeRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingSwipeRepository.kt).

Declaraciones: `DelegatingSwipeRepository`, `targetRepo`, `getFeed`, `getReceivedLikes`, `likeUser`, `dislikeUser`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.demo.DemoModeManager`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.SwipeRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingUserRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingUserRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingUserRepository.kt).

Declaraciones: `DelegatingUserRepository`, `targetRepo`, `autologin`, `login`, `logout`, `register`, `deleteAccount`, `deleteUserPhoto`, `checkIsVerified`, `requestPasswordReset`, `confirmPasswordReset`, `requestVerificationCode`, `confirmVerificationCode`, `getCurrentUser`, `getUserById`, `getUserByUsername`, `updateProfile`, `hasProfilePicture`, `uploadUserPhoto`, `saveUserFCM`.

Dependencias importadas: `android.net.Uri`, `com.feryaeljustice.mirailink.data.demo.DemoModeManager`, `com.feryaeljustice.mirailink.domain.model.auth.AuthSessionInfo`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoChatRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoChatRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoChatRepositoryImpl.kt).

Declaraciones: `DemoChatRepositoryImpl`, `connectSocket`, `disconnectSocket`, `getChatsFromUser`, `markChatAsRead`, `createPrivateChat`, `createGroupChat`, `getMessagesWith`, `sendMessageTo`, `listenForMessages`, `scheduleSimulatedReply`, `generateBotReply`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder`, `com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoChatEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity`, `com.feryaeljustice.mirailink.data.local.demo.toDomainChatMessage`, `com.feryaeljustice.mirailink.data.local.demo.toDomainChatSummary`, `com.feryaeljustice.mirailink.data.local.demo.toMinimalUserInfo`, `com.feryaeljustice.mirailink.domain.model.chat.ChatMessage`, `com.feryaeljustice.mirailink.domain.model.chat.ChatSummary`, `com.feryaeljustice.mirailink.domain.model.user.MinimalUserInfo`, `com.feryaeljustice.mirailink.domain.repository.ChatRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `kotlinx.coroutines.CoroutineScope`, `kotlinx.coroutines.Dispatchers`, `kotlinx.coroutines.delay`, `kotlinx.coroutines.launch`, `java.util.UUID`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt).

Declaraciones: `DemoExploreRepositoryImpl`, `getExploreHubData`, `getCategoryFeed`, `getCategoryPreferences`, `updateCategoryPreferences`, `matchesCategoryFilter`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder`, `com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoCategoryPreferenceEntity`, `com.feryaeljustice.mirailink.data.local.demo.toDomainUser`, `com.feryaeljustice.mirailink.domain.model.explore.CategoryPreference`, `com.feryaeljustice.mirailink.domain.model.explore.ExploreCategory`, `com.feryaeljustice.mirailink.domain.model.explore.ExploreSection`, `com.feryaeljustice.mirailink.domain.model.explore.ExploreSectionGroup`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.ExploreHubData`, `com.feryaeljustice.mirailink.domain.repository.ExploreRepository`, `com.feryaeljustice.mirailink.domain.util.GeoUtils`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoMatchRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoMatchRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoMatchRepositoryImpl.kt).

Declaraciones: `DemoMatchRepositoryImpl`, `getMatches`, `getUnseenMatches`, `markMatchAsSeen`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase`, `com.feryaeljustice.mirailink.data.local.demo.toDomainUser`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.MatchRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt).

Declaraciones: `DemoSwipeRepositoryImpl`, `getFeed`, `getReceivedLikes`, `likeUser`, `dislikeUser`, `getGreetingForUser`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder`, `com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoChatEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoMatchEntity`, `com.feryaeljustice.mirailink.data.local.demo.entity.DemoMessageEntity`, `com.feryaeljustice.mirailink.data.local.demo.toDomainUser`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.model.settings.SearchScope`, `com.feryaeljustice.mirailink.domain.repository.SearchPreferencesRepository`, `com.feryaeljustice.mirailink.domain.repository.SwipeRepository`, `com.feryaeljustice.mirailink.domain.util.GeoUtils`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.domain.error.LocationError`, `kotlinx.coroutines.flow.first`, `java.util.UUID`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoUserRepositoryImpl.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoUserRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoUserRepositoryImpl.kt).

Declaraciones: `DemoUserRepositoryImpl`, `autologin`, `login`, `logout`, `register`, `deleteAccount`, `deleteUserPhoto`, `checkIsVerified`, `requestPasswordReset`, `confirmPasswordReset`, `requestVerificationCode`, `confirmVerificationCode`, `getCurrentUser`, `getUserById`, `getUserByUsername`, `updateProfile`, `hasProfilePicture`, `uploadUserPhoto`, `saveUserFCM`.

Dependencias importadas: `android.net.Uri`, `com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder`, `com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase`, `com.feryaeljustice.mirailink.data.local.demo.toDomainUser`, `com.feryaeljustice.mirailink.domain.model.auth.AuthSessionInfo`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.model.user.UserPhoto`, `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `kotlinx.serialization.json.Json`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/studio/BitmapOptimizationUtils.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/studio/BitmapOptimizationUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/studio/BitmapOptimizationUtils.kt).

Declaraciones: `BitmapOptimizationUtils`, `loadOptimizedBitmap`.

Dependencias importadas: `android.content.Context`, `android.graphics.Bitmap`, `android.graphics.BitmapFactory`, `android.graphics.Matrix`, `android.net.Uri`, `androidx.exifinterface.media.ExifInterface`, `kotlinx.coroutines.Dispatchers`, `kotlinx.coroutines.withContext`, `kotlin.math.max`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/studio/FaceDetectorDataSource.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/studio/FaceDetectorDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/studio/FaceDetectorDataSource.kt).

Declaraciones: `FaceDetectorDataSource`, `detectInBitmap`, `detectInMediaImage`, `mapToFaceBiometrics`, `Task<T>.awaitTask`.

Dependencias importadas: `android.graphics.Bitmap`, `android.media.Image`, `com.feryaeljustice.mirailink.domain.model.studio.FaceBiometrics`, `com.feryaeljustice.mirailink.domain.model.studio.NormalizedPoint`, `com.feryaeljustice.mirailink.domain.model.studio.NormalizedRect`, `com.google.android.gms.tasks.Task`, `com.google.mlkit.vision.common.InputImage`, `com.google.mlkit.vision.face.Face`, `com.google.mlkit.vision.face.FaceDetection`, `com.google.mlkit.vision.face.FaceDetector`, `com.google.mlkit.vision.face.FaceDetectorOptions`, `com.google.mlkit.vision.face.FaceLandmark`, `kotlinx.coroutines.suspendCancellableCoroutine`, `kotlin.coroutines.resumeWithException`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/studio/QualityMetricsCalculator.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/studio/QualityMetricsCalculator.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/studio/QualityMetricsCalculator.kt).

Declaraciones: `QualityMetricsCalculator`, `calculateFromBitmap`, `detectScreenshot`, `isTopBarUniform`, `calculateLuminanceFromYPlane`.

Dependencias importadas: `android.graphics.Bitmap`, `android.graphics.Color`, `com.feryaeljustice.mirailink.domain.model.studio.ImageQualityMetrics`, `kotlin.math.abs`, `kotlin.math.sqrt`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/studio/StudioPhotoAnalyzer.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/studio/StudioPhotoAnalyzer.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/studio/StudioPhotoAnalyzer.kt).

Declaraciones: `StudioPhotoAnalyzer`, `analyze`.

Dependencias importadas: `androidx.annotation.OptIn`, `androidx.camera.core.ExperimentalGetImage`, `androidx.camera.core.ImageAnalysis`, `androidx.camera.core.ImageProxy`, `com.feryaeljustice.mirailink.domain.model.studio.FaceBiometrics`, `kotlinx.coroutines.CoroutineScope`, `kotlinx.coroutines.launch`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/telemetry/FirebaseAnalyticsTracker.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/telemetry/FirebaseAnalyticsTracker.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/telemetry/FirebaseAnalyticsTracker.kt).

Declaraciones: `FirebaseAnalyticsTracker`, `logEvent`, `setUserId`, `setUserProperty`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker`, `com.google.firebase.analytics.FirebaseAnalytics`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/TelemetryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/TelemetryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/telemetry/FirebaseCrashlyticsReporter.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/telemetry/FirebaseCrashlyticsReporter.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/telemetry/FirebaseCrashlyticsReporter.kt).

Declaraciones: `FirebaseCrashlyticsReporter`, `recordNonFatal`, `setUserId`, `setKey`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.telemetry.CrashReporter`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/TelemetryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/TelemetryModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/util/AndroidLogger.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/util/AndroidLogger.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/util/AndroidLogger.kt).

Declaraciones: `AndroidLogger`, `d`.

Dependencias importadas: `android.util.Log`, `com.feryaeljustice.mirailink.domain.util.Logger`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/LoggerModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/LoggerModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/util/DataMediaUtils.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/util/DataMediaUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/util/DataMediaUtils.kt).

Declaraciones: `createImageUri`, `deleteTempFile`, `isTempFile`.

Dependencias importadas: `android.content.Context`, `android.net.Uri`, `android.util.Log`, `androidx.core.content.FileProvider`, `java.io.File`, `java.net.URI`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/util/NetworkErrorMapper.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/util/NetworkErrorMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/util/NetworkErrorMapper.kt).

Declaraciones: `NetworkErrorMapper`, `map`, `existingChatId`, `mapHttpException`, `parsePayload`, `mapKnownPayload`, `normalizeCode`, `mapStableCode`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.response.generic.ApiErrorResponse`, `com.feryaeljustice.mirailink.domain.error.AppError`, `com.feryaeljustice.mirailink.domain.error.AuthError`, `com.feryaeljustice.mirailink.domain.error.DataError`, `com.feryaeljustice.mirailink.domain.error.UnknownError`, `com.feryaeljustice.mirailink.domain.error.LocationError`, `com.feryaeljustice.mirailink.domain.error.SubscriptionError`, `java.io.IOException`, `java.net.ConnectException`, `java.net.SocketTimeoutException`, `java.net.UnknownHostException`, `java.util.Locale`, `kotlinx.serialization.SerializationException`, `kotlinx.serialization.json.Json`, `retrofit2.HttpException`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/util/NetworkOperation.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/util/NetworkOperation.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/util/NetworkOperation.kt).

Declaraciones: `NetworkOperation`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ExploreRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ExploreRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/FeedbackRemoteDatasource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/FeedbackRemoteDatasource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/MatchRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/MatchRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ReportRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ReportRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/TwoFactorRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/TwoFactorRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UsersRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UsersRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt).

## app/src/main/java/com/feryaeljustice/mirailink/data/util/SafeApiCall.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/util/SafeApiCall.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/util/SafeApiCall.kt).

Declaraciones: `safeApiCall`, `safeApiUnitResponse`, `safeApiCallRecoveringHttp`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `java.util.concurrent.CancellationException`, `retrofit2.HttpException`, `retrofit2.Response`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/data/util/SafeLocalCall.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/data/util/SafeLocalCall.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/util/SafeLocalCall.kt).

Declaraciones: `InvalidMediaException`, `safeLocalCall`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.error.DataError`, `com.feryaeljustice.mirailink.domain.error.ValidationError`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `java.io.FileNotFoundException`, `java.io.IOException`, `java.util.concurrent.CancellationException`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.withContext`, `kotlinx.serialization.SerializationException`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/di/koin/AiModule.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/AiModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/AiModule.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `com.feryaeljustice.mirailink.core.remoteconfig.RemoteConfigManager`, `com.feryaeljustice.mirailink.data.datasource.GeminiDataSource`, `com.feryaeljustice.mirailink.data.repository.AiRepositoryImpl`, `com.feryaeljustice.mirailink.domain.repository.AiRepository`, `com.feryaeljustice.mirailink.domain.usecase.ai.GenerateContentUseCase`, `com.google.firebase.Firebase`, `com.google.firebase.ai.GenerativeModel`, `com.google.firebase.ai.ai`, `com.google.firebase.ai.type.GenerativeBackend`, `org.koin.dsl.module`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `com.feryaeljustice.mirailink.core.remoteconfig.RemoteConfigManager`, `com.feryaeljustice.mirailink.core.remoteconfig.RemoteConfigManagerImpl`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.ApplicationScope`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkPrefs`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.MainViewModel`, `org.koin.android.ext.koin.androidContext`, `org.koin.core.module.dsl.viewModel`, `org.koin.dsl.module`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/di/koin/CryptoModule.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/CryptoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/CryptoModule.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datastore.crypto.KeystoreAesGcmProvider`, `com.feryaeljustice.mirailink.data.datastore.crypto.SecretKeyProvider`, `org.koin.dsl.module`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datasource.AppConfigRemoteDataSource`, `com.feryaeljustice.mirailink.data.datasource.CatalogRemoteDataSource`, `com.feryaeljustice.mirailink.data.datasource.ChatRemoteDataSource`, `com.feryaeljustice.mirailink.data.datasource.FeedbackRemoteDatasource`, `com.feryaeljustice.mirailink.data.datasource.MatchRemoteDataSource`, `com.feryaeljustice.mirailink.data.datasource.ReportRemoteDataSource`, `com.feryaeljustice.mirailink.data.datasource.SwipeRemoteDataSource`, `com.feryaeljustice.mirailink.data.datasource.TwoFactorRemoteDataSource`, `com.feryaeljustice.mirailink.data.datasource.UserRemoteDataSource`, `com.feryaeljustice.mirailink.data.datasource.UsersRemoteDataSource`, `com.feryaeljustice.mirailink.data.datastore.MiraiLinkPrefs`, `com.feryaeljustice.mirailink.data.datastore.SessionManager`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.IoDispatcher`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.PrefsDataStore`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.SessionDataStore`, `com.feryaeljustice.mirailink.domain.util.CredentialHelper`, `kotlinx.coroutines.CoroutineScope`, `org.koin.android.ext.koin.androidContext`, `org.koin.dsl.module`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataStoreModule.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataStoreModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataStoreModule.kt).

Declaraciones: `providePrefsDataStore`, `provideSessionPrefsDataStore`, `shouldMigrate`, `migrate`, `cleanUp`.

Dependencias importadas: `android.content.Context`, `androidx.datastore.core.DataMigration`, `androidx.datastore.core.DataStore`, `androidx.datastore.core.DataStoreFactory`, `androidx.datastore.dataStoreFile`, `androidx.datastore.preferences.core.PreferenceDataStoreFactory`, `androidx.datastore.preferences.core.Preferences`, `androidx.datastore.preferences.core.booleanPreferencesKey`, `androidx.datastore.preferences.core.stringPreferencesKey`, `androidx.datastore.preferences.preferencesDataStoreFile`, `com.feryaeljustice.mirailink.data.datastore.crypto.SecretKeyProvider`, `com.feryaeljustice.mirailink.data.datastore.serializer.EncryptedJsonSerializer`, `com.feryaeljustice.mirailink.data.model.local.datastore.AppPrefs`, `com.feryaeljustice.mirailink.data.model.local.datastore.Session`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.IoDispatcher`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.PrefsDataStore`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.SessionDataStore`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.CoroutineScope`, `kotlinx.coroutines.SupervisorJob`, `kotlinx.coroutines.flow.first`, `kotlinx.serialization.json.Json`, `org.koin.android.ext.koin.androidContext`, `org.koin.dsl.module`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `androidx.room.Room`, `com.feryaeljustice.mirailink.data.demo.DemoModeManager`, `com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder`, `com.feryaeljustice.mirailink.data.local.demo.MiraiLinkDemoDatabase`, `com.feryaeljustice.mirailink.data.repository.demo.DemoChatRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.demo.DemoExploreRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.demo.DemoMatchRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.demo.DemoSwipeRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.demo.DemoUserRepositoryImpl`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.ApplicationScope`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.Demo`, `com.feryaeljustice.mirailink.domain.repository.ChatRepository`, `com.feryaeljustice.mirailink.domain.repository.ExploreRepository`, `com.feryaeljustice.mirailink.domain.repository.MatchRepository`, `com.feryaeljustice.mirailink.domain.repository.SwipeRepository`, `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `org.koin.android.ext.koin.androidContext`, `org.koin.dsl.module`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/di/koin/DispatchersModule.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DispatchersModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DispatchersModule.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `com.feryaeljustice.mirailink.di.koin.Qualifiers.ApplicationScope`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.DefaultDispatcher`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.IoDispatcher`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.MainDispatcher`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.CoroutineScope`, `kotlinx.coroutines.Dispatchers`, `kotlinx.coroutines.SupervisorJob`, `org.koin.dsl.module`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/di/koin/FeatureFlagModule.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/FeatureFlagModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/FeatureFlagModule.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `com.feryaeljustice.mirailink.core.featureflags.FeatureFlagStore`, `com.feryaeljustice.mirailink.core.featureflags.FeatureFlagStoreImpl`, `com.feryaeljustice.mirailink.ui.theme.AppThemeManager`, `org.koin.android.ext.koin.androidContext`, `org.koin.dsl.module`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/di/koin/LoggerModule.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/LoggerModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/LoggerModule.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `com.feryaeljustice.mirailink.data.util.AndroidLogger`, `com.feryaeljustice.mirailink.domain.util.Logger`, `org.koin.dsl.module`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `com.feryaeljustice.mirailink.BuildConfig`, `com.feryaeljustice.mirailink.data.datastore.SessionManager`, `com.feryaeljustice.mirailink.data.remote.AppConfigApiService`, `com.feryaeljustice.mirailink.data.remote.CatalogApiService`, `com.feryaeljustice.mirailink.data.remote.ChatApiService`, `com.feryaeljustice.mirailink.data.remote.ExploreApiService`, `com.feryaeljustice.mirailink.data.remote.FeedbackApiService`, `com.feryaeljustice.mirailink.data.remote.MatchApiService`, `com.feryaeljustice.mirailink.data.remote.ReportApiService`, `com.feryaeljustice.mirailink.data.remote.SubscriptionApiService`, `com.feryaeljustice.mirailink.data.remote.SwipeApiService`, `com.feryaeljustice.mirailink.data.remote.TwoFactorApiService`, `com.feryaeljustice.mirailink.data.remote.UserApiService`, `com.feryaeljustice.mirailink.data.remote.UsersApiService`, `com.feryaeljustice.mirailink.data.remote.interceptor.AuthInterceptor`, `com.feryaeljustice.mirailink.data.remote.interceptor.ImageDomainSecurityInterceptor`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.BaseApiUrl`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.BaseUrl`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.ImageOkHttpClient`, `kotlinx.serialization.json.Json`, `okhttp3.MediaType.Companion.toMediaType`, `okhttp3.OkHttpClient`, `okhttp3.logging.HttpLoggingInterceptor`, `org.koin.dsl.module`, `retrofit2.Retrofit`, `retrofit2.converter.kotlinx.serialization.asConverterFactory`, `java.util.concurrent.TimeUnit`, `java.util.Locale`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/di/koin/Qualifiers.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/Qualifiers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/Qualifiers.kt).

Declaraciones: `Qualifiers`.

Dependencias importadas: `org.koin.core.qualifier.named`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/MiraiLinkApp.kt](../../app/src/main/java/com/feryaeljustice/mirailink/MiraiLinkApp.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataStoreModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataStoreModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DispatchersModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DispatchersModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/SocketModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/SocketModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/MainActivity.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/MainActivity.kt).

## app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datasource.ExploreRemoteDataSource`, `com.feryaeljustice.mirailink.data.repository.AiRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.AppConfigRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.CatalogRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.ChatRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.ExploreRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.FeedbackRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.MatchRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.OnboardingRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.ReportRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.SwipeRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.TwoFactorRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.SubscriptionRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.UserRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.UsersRepositoryImpl`, `com.feryaeljustice.mirailink.data.repository.delegating.DelegatingChatRepository`, `com.feryaeljustice.mirailink.data.repository.delegating.DelegatingExploreRepository`, `com.feryaeljustice.mirailink.data.repository.delegating.DelegatingMatchRepository`, `com.feryaeljustice.mirailink.data.repository.delegating.DelegatingSwipeRepository`, `com.feryaeljustice.mirailink.data.repository.delegating.DelegatingUserRepository`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.ApplicationScope`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.BaseUrl`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.Demo`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.Remote`, `com.feryaeljustice.mirailink.domain.repository.SubscriptionRepository`, `com.feryaeljustice.mirailink.domain.repository.AiRepository`, `com.feryaeljustice.mirailink.domain.repository.AppConfigRepository`, `com.feryaeljustice.mirailink.domain.repository.CatalogRepository`, `com.feryaeljustice.mirailink.domain.repository.ChatRepository`, `com.feryaeljustice.mirailink.domain.repository.ExploreRepository`, `com.feryaeljustice.mirailink.domain.repository.FeedbackRepository`, `com.feryaeljustice.mirailink.domain.repository.MatchRepository`, `com.feryaeljustice.mirailink.domain.repository.OnboardingRepository`, `com.feryaeljustice.mirailink.domain.repository.ReportRepository`, `com.feryaeljustice.mirailink.domain.repository.SwipeRepository`, `com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository`, `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.repository.UsersRepository`, `org.koin.dsl.module`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/di/koin/SerializationModule.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/SerializationModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/SerializationModule.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `kotlinx.serialization.json.Json`, `org.koin.dsl.module`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/di/koin/SocketModule.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/SocketModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/SocketModule.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `com.feryaeljustice.mirailink.data.remote.socket.SocketService`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.BaseUrl`, `org.koin.dsl.module`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/di/koin/TelemetryModule.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/TelemetryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/TelemetryModule.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `com.feryaeljustice.mirailink.data.telemetry.FirebaseAnalyticsTracker`, `com.feryaeljustice.mirailink.data.telemetry.FirebaseCrashlyticsReporter`, `com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker`, `com.feryaeljustice.mirailink.domain.telemetry.CrashReporter`, `com.google.firebase.analytics.FirebaseAnalytics`, `com.google.firebase.crashlytics.FirebaseCrashlytics`, `org.koin.android.ext.koin.androidContext`, `org.koin.dsl.module`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.usecase.CheckAppVersionUseCase`, `com.feryaeljustice.mirailink.domain.usecase.ai.GenerateContentUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.AutologinUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.CheckIsVerifiedUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.LoginUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.LogoutUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.RegisterUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.DisableTwoFactorUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.GetTwoFactorStatusUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.LoginVerifyTwoFactorLastStepUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.SetupTwoFactorUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.VerifyTwoFactorUseCase`, `com.feryaeljustice.mirailink.domain.usecase.catalog.GetAnimesUseCase`, `com.feryaeljustice.mirailink.domain.usecase.catalog.GetGamesUseCase`, `com.feryaeljustice.mirailink.domain.usecase.catalog.GetProfileOptionsUseCase`, `com.feryaeljustice.mirailink.domain.usecase.chat.ChatUseCases`, `com.feryaeljustice.mirailink.domain.usecase.chat.ConnectSocketUseCase`, `com.feryaeljustice.mirailink.domain.usecase.chat.CreateGroupChatUseCase`, `com.feryaeljustice.mirailink.domain.usecase.chat.CreatePrivateChatUseCase`, `com.feryaeljustice.mirailink.domain.usecase.chat.DisconnectSocketUseCase`, `com.feryaeljustice.mirailink.domain.usecase.chat.GetChatMessagesUseCase`, `com.feryaeljustice.mirailink.domain.usecase.chat.GetChatsFromUser`, `com.feryaeljustice.mirailink.domain.usecase.chat.ListenForMessagesUseCase`, `com.feryaeljustice.mirailink.domain.usecase.chat.MarkChatAsReadUseCase`, `com.feryaeljustice.mirailink.domain.usecase.chat.SendMessageUseCase`, `com.feryaeljustice.mirailink.domain.usecase.explore.GetCategoryFeedUseCase`, `com.feryaeljustice.mirailink.domain.usecase.explore.GetCategoryPreferencesUseCase`, `com.feryaeljustice.mirailink.domain.usecase.explore.GetExploreSectionsUseCase`, `com.feryaeljustice.mirailink.domain.usecase.explore.UpdateCategoryPreferencesUseCase`, `com.feryaeljustice.mirailink.domain.usecase.feed.GetFeedUseCase`, `com.feryaeljustice.mirailink.domain.usecase.feedback.SendFeedbackUseCase`, `com.feryaeljustice.mirailink.domain.usecase.match.GetMatchesUseCase`, `com.feryaeljustice.mirailink.domain.usecase.notification.SaveNotificationFCMUseCase`, `com.feryaeljustice.mirailink.domain.usecase.onboarding.CheckOnboardingIsCompleted`, `com.feryaeljustice.mirailink.domain.usecase.photos.CheckProfilePictureUseCase`, `com.feryaeljustice.mirailink.domain.usecase.photos.DeleteUserPhotoUseCase`, `com.feryaeljustice.mirailink.domain.usecase.photos.UploadUserPhotoUseCase`, `com.feryaeljustice.mirailink.domain.usecase.report.ReportUseCase`, `com.feryaeljustice.mirailink.domain.usecase.swipe.DislikeUserUseCase`, `com.feryaeljustice.mirailink.domain.usecase.swipe.GetReceivedLikesUseCase`, `com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.ConfirmPasswordResetUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.ConfirmVerificationCodeUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.DeleteAccountUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.GetUserByIdUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.GetUserProfileByUsernameUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.RequestPasswordResetUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.RequestVerificationCodeUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.UpdateUserProfileUseCase`, `org.koin.dsl.module`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `android.util.Log`, `com.feryaeljustice.mirailink.core.remoteconfig.RemoteConfigManager`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.IoDispatcher`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.MainDispatcher`, `com.feryaeljustice.mirailink.ui.navigation.NavAnalyticsViewModel`, `com.feryaeljustice.mirailink.ui.screens.ai.chat.AiChatViewModel`, `com.feryaeljustice.mirailink.ui.screens.auth.AuthViewModel`, `com.feryaeljustice.mirailink.ui.screens.auth.recover.RecoverPasswordViewModel`, `com.feryaeljustice.mirailink.ui.screens.auth.verification.VerificationViewModel`, `com.feryaeljustice.mirailink.ui.screens.chat.ChatViewModel`, `com.feryaeljustice.mirailink.ui.screens.explore.ExploreViewModel`, `com.feryaeljustice.mirailink.ui.screens.explore.feed.CategoryFeedViewModel`, `com.feryaeljustice.mirailink.ui.screens.home.HomeViewModel`, `com.feryaeljustice.mirailink.ui.screens.home.search.SearchPreferencesViewModel`, `com.feryaeljustice.mirailink.ui.screens.likes.ReceivedLikesViewModel`, `com.feryaeljustice.mirailink.ui.screens.messages.MessagesViewModel`, `com.feryaeljustice.mirailink.ui.screens.photo.ProfilePictureViewModel`, `com.feryaeljustice.mirailink.ui.screens.profile.ProfileViewModel`, `com.feryaeljustice.mirailink.ui.screens.profile.detail.UserProfileDetailViewModel`, `com.feryaeljustice.mirailink.ui.screens.settings.SettingsViewModel`, `com.feryaeljustice.mirailink.ui.screens.settings.feedback.FeedbackViewModel`, `com.feryaeljustice.mirailink.ui.screens.settings.twofactor.configure.ConfigureTwoFactorViewModel`, `com.feryaeljustice.mirailink.ui.screens.splash.SplashScreenViewModel`, `com.feryaeljustice.mirailink.ui.screens.subscription.SubscriptionManageViewModel`, `com.feryaeljustice.mirailink.ui.screens.subscription.SubscriptionPaywallViewModel`, `org.koin.core.module.dsl.viewModel`, `org.koin.core.module.dsl.viewModelOf`, `org.koin.dsl.module`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/domain/constants/Constants.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/constants/Constants.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/constants/Constants.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `com.feryaeljustice.mirailink.BuildConfig`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/domain/core/CoreUtils.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/core/CoreUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/core/CoreUtils.kt).

Declaraciones: `rememberInitializedStateFlow`, `JwtUtils`, `extractUserId`.

Dependencias importadas: `android.util.Base64`, `android.util.Log`, `org.json.JSONObject`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/domain/enums/ChatRole.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/enums/ChatRole.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/enums/ChatRole.kt).

Declaraciones: `ChatRole`, `fromString`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ChatMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ChatMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/model/chat/ChatSummary.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/chat/ChatSummary.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/enums/ChatType.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/enums/ChatType.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/enums/ChatType.kt).

Declaraciones: `ChatType`, `fromString`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ChatMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ChatMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/ChatMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/ChatMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/model/chat/ChatSummary.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/chat/ChatSummary.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/enums/TagType.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/enums/TagType.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/enums/TagType.kt).

Declaraciones: `TagType`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/enums/TextFieldType.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/enums/TextFieldType.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/enums/TextFieldType.kt).

Declaraciones: `TextFieldType`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/ResidenceSelector.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/ResidenceSelector.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/error/AppError.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/error/AppError.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/error/AppError.kt).

Declaraciones: `AppError`, `DataError`, `Network`, `Local`, `AuthError`, `ValidationError`, `LocationError`, `SubscriptionError`, `UnknownError`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/util/NetworkErrorMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/util/NetworkErrorMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/util/MiraiLinkResult.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/MiraiLinkResult.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/error/AppErrorUiMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/error/AppErrorUiMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/AppVersionInfo.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/AppVersionInfo.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/AppVersionInfo.kt).

Declaraciones: `AppVersionInfo`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/AppConfigRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/AppConfigRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/AppVersionInfoMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/AppVersionInfoMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/AppConfigApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/AppConfigApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/AppConfigRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/AppConfigRepository.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/VersionCheckResult.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/VersionCheckResult.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/VersionCheckResult.kt).

Declaraciones: `VersionCheckResult`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/AppConfigMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/AppConfigMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/CheckAppVersionUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/CheckAppVersionUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt](../../app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/auth/AuthSessionInfo.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/auth/AuthSessionInfo.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/auth/AuthSessionInfo.kt).

Declaraciones: `AuthSessionInfo`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/AuthSessionMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/AuthSessionMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/UserRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/UserRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingUserRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingUserRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoUserRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoUserRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/UserRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/UserRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LoginUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LoginUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/auth/TwoFactorAuthInfo.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/auth/TwoFactorAuthInfo.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/auth/TwoFactorAuthInfo.kt).

Declaraciones: `TwoFactorAuthInfo`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/TwoFactorAuthMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/TwoFactorAuthMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/TwoFactorRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/TwoFactorRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/TwoFactorRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/TwoFactorRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/SetupTwoFactorUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/SetupTwoFactorUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/catalog/Anime.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/catalog/Anime.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/catalog/Anime.kt).

Declaraciones: `Anime`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoDataSeeder.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoDataSeeder.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/CatalogMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/CatalogMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/CatalogViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/CatalogViewEntry.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/CatalogApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/CatalogApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/CatalogRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/CatalogRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/model/user/User.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/user/User.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/CatalogRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/CatalogRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetAnimesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetAnimesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestsGrid.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestsGrid.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/UserViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/UserViewEntry.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/catalog/Game.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/catalog/Game.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/catalog/Game.kt).

Declaraciones: `Game`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoDataSeeder.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoDataSeeder.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/CatalogMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/CatalogMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/UserMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/UserMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/CatalogViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/CatalogViewEntry.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/UserMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/UserMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/CatalogApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/CatalogApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/CatalogRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/CatalogRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/model/user/User.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/user/User.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/CatalogRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/CatalogRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetGamesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetGamesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestsGrid.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestsGrid.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/GamerPromptComponents.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/GamerPromptComponents.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/UserViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/UserViewEntry.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/chat/ChatMessage.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/chat/ChatMessage.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/chat/ChatMessage.kt).

Declaraciones: `ChatMessage`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.user.MinimalUserInfo`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ChatMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ChatMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/ChatMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/ChatMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ChatRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ChatRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingChatRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingChatRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoChatRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoChatRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ChatRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ChatRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatMessagesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatMessagesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/chat/ChatSummary.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/chat/ChatSummary.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/chat/ChatSummary.kt).

Declaraciones: `ChatSummary`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.enums.ChatRole`, `com.feryaeljustice.mirailink.domain.enums.ChatType`, `com.feryaeljustice.mirailink.domain.model.user.MinimalUserInfo`, `com.feryaeljustice.mirailink.domain.util.DateSerializer`, `kotlinx.serialization.Serializable`, `java.util.Date`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ChatMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ChatMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/ChatMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/ChatMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ChatApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ChatRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ChatRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingChatRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingChatRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoChatRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoChatRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ChatRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ChatRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatsFromUser.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatsFromUser.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/enum/Gender.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/enum/Gender.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/enum/Gender.kt).

Declaraciones: `Gender`, `toString`, `fromRealValue`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/GenderSelector.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/GenderSelector.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/utils/extensions/DataExtensions.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/utils/extensions/DataExtensions.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/explore/CategoryPreference.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/explore/CategoryPreference.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/explore/CategoryPreference.kt).

Declaraciones: `CategoryPreference`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingExploreRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingExploreRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ExploreRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ExploreRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryPreferencesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryPreferencesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/UpdateCategoryPreferencesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/UpdateCategoryPreferencesUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/explore/ExploreCategory.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/explore/ExploreCategory.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/explore/ExploreCategory.kt).

Declaraciones: `ExploreCategory`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ExploreRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ExploreRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/explore/CategoryCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/explore/CategoryCard.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/explore/ExploreSection.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/explore/ExploreSection.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/explore/ExploreSection.kt).

Declaraciones: `ExploreSection`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ExploreRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ExploreRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/explore/CategoryCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/explore/CategoryCard.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/explore/ExploreSectionGroup.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/explore/ExploreSectionGroup.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/explore/ExploreSectionGroup.kt).

Declaraciones: `ExploreSectionGroup`, `fromRaw`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/explore/CategoryCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/explore/CategoryCard.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/faq/FaqItem.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/faq/FaqItem.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/faq/FaqItem.kt).

Declaraciones: `FaqCategory`, `FaqItem`.

Dependencias importadas: `androidx.annotation.StringRes`, `com.feryaeljustice.mirailink.R`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/FaqRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/FaqRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/FaqRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/FaqRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/faq/GetFaqItemsUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/faq/GetFaqItemsUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/geography/GeographicPlace.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/geography/GeographicPlace.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/geography/GeographicPlace.kt).

Declaraciones: `GeographicPlace`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/CatalogApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/CatalogApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/CatalogRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/CatalogRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/CatalogRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/CatalogRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/ResidenceSelector.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/ResidenceSelector.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/haptics/HeartbeatAffinity.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/haptics/HeartbeatAffinity.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/haptics/HeartbeatAffinity.kt).

Declaraciones: `HeartbeatAffinity`.

Dependencias importadas: `androidx.compose.runtime.Immutable`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/haptics/CalculateHeartbeatAffinityUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/haptics/CalculateHeartbeatAffinityUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/haptics/HapticHeartbeatOverlay.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/haptics/HapticHeartbeatOverlay.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/settings/SearchPreferences.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/settings/SearchPreferences.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/settings/SearchPreferences.kt).

Declaraciones: `SearchScope`, `fromWireValue`, `isRadiusScope`, `SearchPreferences`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SearchPreferencesRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SearchPreferencesRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/settings/GetSearchPreferencesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/settings/GetSearchPreferencesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/settings/SaveSearchPreferencesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/settings/SaveSearchPreferencesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/SearchSettingsSection.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/SearchSettingsSection.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/studio/FaceBiometrics.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/studio/FaceBiometrics.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/studio/FaceBiometrics.kt).

Declaraciones: `NormalizedPoint`, `NormalizedRect`, `FaceBiometrics`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/studio/FaceDetectorDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/studio/FaceDetectorDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/studio/StudioPhotoAnalyzer.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/studio/StudioPhotoAnalyzer.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/studio/AnalyzePhotoQualityUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/studio/AnalyzePhotoQualityUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioUiState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioUiState.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/FaceBoxOverlay.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/FaceBoxOverlay.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/studio/ImageQualityMetrics.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/studio/ImageQualityMetrics.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/studio/ImageQualityMetrics.kt).

Declaraciones: `ImageQualityMetrics`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/studio/QualityMetricsCalculator.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/studio/QualityMetricsCalculator.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/studio/AnalyzePhotoQualityUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/studio/AnalyzePhotoQualityUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/studio/MetricStatus.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/studio/MetricStatus.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/studio/MetricStatus.kt).

Declaraciones: `MetricStatus`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/studio/AnalyzePhotoQualityUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/studio/AnalyzePhotoQualityUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/HudMetricGauge.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/HudMetricGauge.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/studio/MiraiScanResult.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/studio/MiraiScanResult.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/studio/MiraiScanResult.kt).

Declaraciones: `ScanContentType`, `MiraiScanResult`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/studio/AnalyzePhotoQualityUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/studio/AnalyzePhotoQualityUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioUiState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioUiState.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/HudQualityVerdictSheet.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/HudQualityVerdictSheet.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/studio/QualityBadge.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/studio/QualityBadge.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/studio/QualityBadge.kt).

Declaraciones: `QualityBadge`.

Dependencias importadas: `androidx.annotation.StringRes`, `com.feryaeljustice.mirailink.R`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/studio/AnalyzePhotoQualityUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/studio/AnalyzePhotoQualityUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/QualityBadgesRow.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/QualityBadgesRow.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/subscription/SubscriptionPlanInfo.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/subscription/SubscriptionPlanInfo.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/subscription/SubscriptionPlanInfo.kt).

Declaraciones: `SubscriptionDuration`, `SubscriptionOfferOption`, `SubscriptionPlanInfo`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SubscriptionRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SubscriptionRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/GetSubscriptionStatusUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/GetSubscriptionStatusUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/RestorePurchasesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/RestorePurchasesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/subscription/SubscriptionPlanType.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/subscription/SubscriptionPlanType.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/subscription/SubscriptionPlanType.kt).

Declaraciones: `SubscriptionPlanType`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SubscriptionRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SubscriptionRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/swipe/ReceivedLike.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/swipe/ReceivedLike.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/swipe/ReceivedLike.kt).

Declaraciones: `ReceivedLike`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.user.User`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SwipeRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SwipeRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingSwipeRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingSwipeRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SwipeRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SwipeRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/GetReceivedLikesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/GetReceivedLikesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/user/GamerPromptAnswer.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/user/GamerPromptAnswer.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/user/GamerPromptAnswer.kt).

Declaraciones: `GamerPromptAnswer`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/UserMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/UserMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/UserMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/UserMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/GamerPromptComponents.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/GamerPromptComponents.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/user/MinimalUserInfo.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/user/MinimalUserInfo.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/user/MinimalUserInfo.kt).

Declaraciones: `MinimalUserInfo`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/UserMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/UserMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/UserMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/UserMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/chat/ChatSummaryResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/chat/ChatSummaryResponse.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoChatRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoChatRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/model/chat/ChatMessage.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/chat/ChatMessage.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/model/chat/ChatSummary.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/chat/ChatSummary.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/util/UserUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/UserUtils.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/chat/ChatMessageViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/chat/ChatMessageViewEntry.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/user/User.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/user/User.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/user/User.kt).

Declaraciones: `User`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.catalog.Anime`, `com.feryaeljustice.mirailink.domain.model.catalog.Game`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ExploreRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ExploreRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/MatchRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/MatchRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UsersRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UsersRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoDataSeeder.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoDataSeeder.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/MediaMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/MediaMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/UserMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/UserMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/MediaMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/MediaMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/UserMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/UserMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/chat/ChatMessageResponse.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/chat/ChatMessageResponse.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/model/response/swipe/ReceivedLikeDto.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/model/response/swipe/ReceivedLikeDto.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/ExploreApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/ExploreApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/MatchApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/MatchApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/SwipeApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UsersApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UsersApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/LocationRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/LocationRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/MatchRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/MatchRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SwipeRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SwipeRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/UserRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/UserRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/UsersRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/UsersRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingExploreRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingExploreRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingMatchRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingMatchRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingSwipeRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingSwipeRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingUserRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingUserRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoMatchRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoMatchRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoUserRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoUserRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/NetworkModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/model/swipe/ReceivedLike.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/swipe/ReceivedLike.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ExploreRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ExploreRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/MatchRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/MatchRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SwipeRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SwipeRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/UserRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/UserRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/UsersRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/UsersRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/AutologinUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/AutologinUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/CheckIsVerifiedUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/CheckIsVerifiedUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LoginUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LoginUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LogoutUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LogoutUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/RegisterUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/RegisterUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryFeedUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryFeedUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/feed/GetFeedUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/feed/GetFeedUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/haptics/CalculateHeartbeatAffinityUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/haptics/CalculateHeartbeatAffinityUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/match/GetMatchesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/match/GetMatchesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/notification/SaveNotificationFCMUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/notification/SaveNotificationFCMUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/CheckProfilePictureUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/CheckProfilePictureUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/DeleteUserPhotoUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/DeleteUserPhotoUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/UploadUserPhotoUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/UploadUserPhotoUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmPasswordResetUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmPasswordResetUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmVerificationCodeUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmVerificationCodeUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/DeleteAccountUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/DeleteAccountUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetCurrentUserUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetCurrentUserUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserByIdUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserByIdUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserProfileByUsernameUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserProfileByUsernameUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUsersUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUsersUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestPasswordResetUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestPasswordResetUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestVerificationCodeUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestVerificationCodeUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/UpdateUserProfileUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/UpdateUserProfileUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/util/MediaUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/MediaUtils.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/util/UserUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/UserUtils.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/MiraiLinkAppRoot.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/MiraiLinkAppRoot.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/GamerPromptComponents.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/GamerPromptComponents.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/MinimalUserInfoViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/MinimalUserInfoViewEntry.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/UserViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/UserViewEntry.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/model/user/UserPhoto.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/model/user/UserPhoto.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/model/user/UserPhoto.kt).

Declaraciones: `UserPhoto`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoDataSeeder.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoDataSeeder.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/local/demo/DemoMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/MediaMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/MediaMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/UserMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/UserMapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/MediaMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/MediaMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/remote/UserApiService.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoUserRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoUserRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/util/MediaUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/MediaUtils.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/MinimalUserInfoViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/MinimalUserInfoViewEntry.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/UserViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/UserViewEntry.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/repository/AiRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/AiRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/AiRepository.kt).

Declaraciones: `AiRepository`, `generateContent`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/AiRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/AiRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/AiModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/AiModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/ai/GenerateContentUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/ai/GenerateContentUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/repository/AppConfigRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/AppConfigRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/AppConfigRepository.kt).

Declaraciones: `AppConfigRepository`, `getVersion`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.AppVersionInfo`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/AppConfigRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/AppConfigRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/CheckAppVersionUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/CheckAppVersionUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/repository/CatalogRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/CatalogRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/CatalogRepository.kt).

Declaraciones: `CatalogRepository`, `getAnimes`, `getGames`, `getCountries`, `getRegions`, `getCities`, `getProfileOptions`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.catalog.Anime`, `com.feryaeljustice.mirailink.domain.model.catalog.Game`, `com.feryaeljustice.mirailink.domain.model.geography.GeographicPlace`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/CatalogRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/CatalogRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetAnimesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetAnimesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetGamesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetGamesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetProfileOptionsUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetProfileOptionsUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/ResidenceSelector.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/ResidenceSelector.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ChatRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ChatRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ChatRepository.kt).

Declaraciones: `ChatRepository`, `connectSocket`, `disconnectSocket`, `getChatsFromUser`, `markChatAsRead`, `createPrivateChat`, `createGroupChat`, `getMessagesWith`, `sendMessageTo`, `listenForMessages`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.chat.ChatMessage`, `com.feryaeljustice.mirailink.domain.model.chat.ChatSummary`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ChatRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ChatRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingChatRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingChatRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoChatRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoChatRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/ConnectSocketUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/ConnectSocketUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/CreateGroupChatUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/CreateGroupChatUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/CreatePrivateChatUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/CreatePrivateChatUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/DisconnectSocketUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/DisconnectSocketUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatMessagesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatMessagesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatsFromUser.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatsFromUser.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/ListenForMessagesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/ListenForMessagesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/MarkChatAsReadUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/MarkChatAsReadUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/SendMessagesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/SendMessagesUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ExploreRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ExploreRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ExploreRepository.kt).

Declaraciones: `ExploreHubData`, `ExploreRepository`, `getExploreHubData`, `getCategoryFeed`, `getCategoryPreferences`, `updateCategoryPreferences`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.explore.CategoryPreference`, `com.feryaeljustice.mirailink.domain.model.explore.ExploreCategory`, `com.feryaeljustice.mirailink.domain.model.explore.ExploreSection`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingExploreRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingExploreRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryFeedUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryFeedUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryPreferencesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryPreferencesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetExploreSectionsUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetExploreSectionsUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/UpdateCategoryPreferencesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/UpdateCategoryPreferencesUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/repository/FaqRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/FaqRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/FaqRepository.kt).

Declaraciones: `FaqRepository`, `getFaqItems`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.faq.FaqItem`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/FaqRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/FaqRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/faq/GetFaqItemsUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/faq/GetFaqItemsUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/repository/FeedbackRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/FeedbackRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/FeedbackRepository.kt).

Declaraciones: `FeedbackRepository`, `sendFeedback`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/FeedbackRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/FeedbackRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/feedback/SendFeedbackUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/feedback/SendFeedbackUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/repository/LocationRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/LocationRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/LocationRepository.kt).

Declaraciones: `LocationRepository`, `sendLocationPing`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/LocationRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/LocationRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/location/SendLocationPingUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/location/SendLocationPingUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/repository/MatchRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/MatchRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/MatchRepository.kt).

Declaraciones: `MatchRepository`, `getMatches`, `getUnseenMatches`, `markMatchAsSeen`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/MatchRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/MatchRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingMatchRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingMatchRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoMatchRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoMatchRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/match/GetMatchesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/match/GetMatchesUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/repository/OnboardingRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/OnboardingRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/OnboardingRepository.kt).

Declaraciones: `OnboardingRepository`, `checkOnboardingIsCompleted`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/OnboardingRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/OnboardingRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/onboarding/CheckOnboardingIsCompleted.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/onboarding/CheckOnboardingIsCompleted.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ReportRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ReportRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ReportRepository.kt).

Declaraciones: `ReportRepository`, `reportUser`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ReportRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ReportRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/report/ReportUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/report/ReportUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SearchPreferencesRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SearchPreferencesRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SearchPreferencesRepository.kt).

Declaraciones: `SearchPreferencesRepository`, `getSearchPreferences`, `saveSearchPreferences`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `kotlinx.coroutines.flow.Flow`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/settings/GetSearchPreferencesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/settings/GetSearchPreferencesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/settings/SaveSearchPreferencesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/settings/SaveSearchPreferencesUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SubscriptionRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SubscriptionRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SubscriptionRepository.kt).

Declaraciones: `SubscriptionRepository`, `fetchSubscriptionStatus`, `launchBillingFlow`, `verifyPurchase`, `restorePurchases`, `requestCancelIntent`.

Dependencias importadas: `android.app.Activity`, `com.feryaeljustice.mirailink.data.billing.BillingClientManager`, `com.feryaeljustice.mirailink.data.billing.BillingPurchaseEvent`, `com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionOfferOption`, `com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanInfo`, `com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanType`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `kotlinx.coroutines.flow.Flow`, `kotlinx.coroutines.flow.StateFlow`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/CancelSubscriptionIntentUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/CancelSubscriptionIntentUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/GetSubscriptionStatusUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/GetSubscriptionStatusUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/LaunchBillingFlowUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/LaunchBillingFlowUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/RestorePurchasesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/RestorePurchasesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SwipeRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SwipeRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SwipeRepository.kt).

Declaraciones: `SwipeRepository`, `getFeed`, `getReceivedLikes`, `likeUser`, `dislikeUser`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SwipeRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SwipeRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingSwipeRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingSwipeRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/feed/GetFeedUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/feed/GetFeedUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/DislikeUserUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/DislikeUserUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/GetReceivedLikesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/GetReceivedLikesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/LikeUserUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/LikeUserUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/repository/TwoFactorRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/TwoFactorRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/TwoFactorRepository.kt).

Declaraciones: `TwoFactorRepository`, `get2FAStatus`, `setup2FA`, `verify2FA`, `disable2FA`, `loginVerifyTwoFactorLastStep`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.response.auth.LoginResponse`, `com.feryaeljustice.mirailink.domain.model.auth.TwoFactorAuthInfo`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/TwoFactorRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/TwoFactorRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/DisableTwoFactorUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/DisableTwoFactorUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/GetTwoFactorStatusUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/GetTwoFactorStatusUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/LoginVerifyTwoFactorLastStepUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/LoginVerifyTwoFactorLastStepUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/SetupTwoFactorUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/SetupTwoFactorUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/VerifyTwoFactorUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/VerifyTwoFactorUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/repository/UserRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/UserRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/UserRepository.kt).

Declaraciones: `UserRepository`, `autologin`, `login`, `logout`, `register`, `deleteAccount`, `deleteUserPhoto`, `checkIsVerified`, `requestPasswordReset`, `confirmPasswordReset`, `requestVerificationCode`, `confirmVerificationCode`, `getCurrentUser`, `getUserById`, `getUserByUsername`, `updateProfile`, `hasProfilePicture`, `uploadUserPhoto`, `saveUserFCM`.

Dependencias importadas: `android.net.Uri`, `com.feryaeljustice.mirailink.domain.model.auth.AuthSessionInfo`, `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/UserRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/UserRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingUserRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingUserRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoUserRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoUserRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DemoModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/AutologinUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/AutologinUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/CheckIsVerifiedUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/CheckIsVerifiedUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LoginUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LoginUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LogoutUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LogoutUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/RegisterUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/RegisterUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/notification/SaveNotificationFCMUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/notification/SaveNotificationFCMUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/CheckProfilePictureUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/CheckProfilePictureUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/DeleteUserPhotoUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/DeleteUserPhotoUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/UploadUserPhotoUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/UploadUserPhotoUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmPasswordResetUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmPasswordResetUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmVerificationCodeUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmVerificationCodeUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/DeleteAccountUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/DeleteAccountUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetCurrentUserUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetCurrentUserUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserByIdUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserByIdUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserProfileByUsernameUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserProfileByUsernameUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestPasswordResetUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestPasswordResetUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestVerificationCodeUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestVerificationCodeUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/UpdateUserProfileUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/UpdateUserProfileUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/repository/UsersRepository.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/UsersRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/UsersRepository.kt).

Declaraciones: `UsersRepository`, `getUsers`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/UsersRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/UsersRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/RepositoryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUsersUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUsersUseCase.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/telemetry/AnalyticsTracker.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/telemetry/AnalyticsTracker.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/telemetry/AnalyticsTracker.kt).

Declaraciones: `AnalyticsTracker`, `logEvent`, `setUserId`, `setUserProperty`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/telemetry/FirebaseAnalyticsTracker.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/telemetry/FirebaseAnalyticsTracker.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/TelemetryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/TelemetryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavAnalyticsViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavAnalyticsViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/telemetry/CrashReporter.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/telemetry/CrashReporter.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/telemetry/CrashReporter.kt).

Declaraciones: `CrashReporter`, `recordNonFatal`, `setUserId`, `setKey`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/telemetry/FirebaseCrashlyticsReporter.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/telemetry/FirebaseCrashlyticsReporter.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/TelemetryModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/TelemetryModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/CheckAppVersionUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/CheckAppVersionUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/CheckAppVersionUseCase.kt).

Declaraciones: `CheckAppVersionUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.VersionCheckResult`, `com.feryaeljustice.mirailink.domain.repository.AppConfigRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/ai/GenerateContentUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/ai/GenerateContentUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/ai/GenerateContentUseCase.kt).

Declaraciones: `GenerateContentUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.error.UnknownError`, `com.feryaeljustice.mirailink.domain.repository.AiRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `java.util.concurrent.CancellationException`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/AiModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/AiModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/AutologinUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/AutologinUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/AutologinUseCase.kt).

Declaraciones: `AutologinUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/CheckIsVerifiedUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/CheckIsVerifiedUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/CheckIsVerifiedUseCase.kt).

Declaraciones: `CheckIsVerifiedUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LoginUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LoginUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LoginUseCase.kt).

Declaraciones: `LoginUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.auth.AuthSessionInfo`, `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LogoutUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LogoutUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LogoutUseCase.kt).

Declaraciones: `LogoutUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.domain.util.asEmptyResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/RegisterUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/RegisterUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/RegisterUseCase.kt).

Declaraciones: `RegisterUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.error.ValidationError`, `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.domain.util.isAtLeast16YearsOld`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/DisableTwoFactorUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/DisableTwoFactorUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/DisableTwoFactorUseCase.kt).

Declaraciones: `DisableTwoFactorUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/GetTwoFactorStatusUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/GetTwoFactorStatusUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/GetTwoFactorStatusUseCase.kt).

Declaraciones: `GetTwoFactorStatusUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/LoginVerifyTwoFactorLastStepUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/LoginVerifyTwoFactorLastStepUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/LoginVerifyTwoFactorLastStepUseCase.kt).

Declaraciones: `LoginVerifyTwoFactorLastStepUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.response.auth.LoginResponse`, `com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/SetupTwoFactorUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/SetupTwoFactorUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/SetupTwoFactorUseCase.kt).

Declaraciones: `SetupTwoFactorUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.auth.TwoFactorAuthInfo`, `com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/VerifyTwoFactorUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/VerifyTwoFactorUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/VerifyTwoFactorUseCase.kt).

Declaraciones: `VerifyTwoFactorUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.TwoFactorRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetAnimesUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetAnimesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetAnimesUseCase.kt).

Declaraciones: `GetAnimesUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.catalog.Anime`, `com.feryaeljustice.mirailink.domain.repository.CatalogRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetGamesUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetGamesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetGamesUseCase.kt).

Declaraciones: `GetGamesUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.catalog.Game`, `com.feryaeljustice.mirailink.domain.repository.CatalogRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetProfileOptionsUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetProfileOptionsUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetProfileOptionsUseCase.kt).

Declaraciones: `GetProfileOptionsUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.model.response.catalog.ProfileOptionsResponseDto`, `com.feryaeljustice.mirailink.domain.repository.CatalogRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/ChatUseCases.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/ChatUseCases.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/ChatUseCases.kt).

Declaraciones: `ChatUseCases`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/ConnectSocketUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/ConnectSocketUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/ConnectSocketUseCase.kt).

Declaraciones: `ConnectSocketUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.error.DataError`, `com.feryaeljustice.mirailink.domain.repository.ChatRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `java.util.concurrent.CancellationException`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/CreateGroupChatUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/CreateGroupChatUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/CreateGroupChatUseCase.kt).

Declaraciones: `CreateGroupChatUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.ChatRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/CreatePrivateChatUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/CreatePrivateChatUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/CreatePrivateChatUseCase.kt).

Declaraciones: `CreatePrivateChatUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.ChatRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/DisconnectSocketUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/DisconnectSocketUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/DisconnectSocketUseCase.kt).

Declaraciones: `DisconnectSocketUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.error.DataError`, `com.feryaeljustice.mirailink.domain.repository.ChatRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `java.util.concurrent.CancellationException`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatMessagesUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatMessagesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatMessagesUseCase.kt).

Declaraciones: `GetChatMessagesUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.chat.ChatMessage`, `com.feryaeljustice.mirailink.domain.repository.ChatRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatsFromUser.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatsFromUser.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatsFromUser.kt).

Declaraciones: `GetChatsFromUser`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.chat.ChatSummary`, `com.feryaeljustice.mirailink.domain.repository.ChatRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/ListenForMessagesUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/ListenForMessagesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/ListenForMessagesUseCase.kt).

Declaraciones: `ListenForMessagesUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.ChatRepository`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/MarkChatAsReadUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/MarkChatAsReadUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/MarkChatAsReadUseCase.kt).

Declaraciones: `MarkChatAsReadUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.ChatRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/SendMessagesUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/SendMessagesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/SendMessagesUseCase.kt).

Declaraciones: `SendMessageUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.ChatRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryFeedUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryFeedUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryFeedUseCase.kt).

Declaraciones: `GetCategoryFeedUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.ExploreRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryPreferencesUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryPreferencesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryPreferencesUseCase.kt).

Declaraciones: `GetCategoryPreferencesUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.explore.CategoryPreference`, `com.feryaeljustice.mirailink.domain.repository.ExploreRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetExploreSectionsUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetExploreSectionsUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetExploreSectionsUseCase.kt).

Declaraciones: `GetExploreSectionsUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.ExploreHubData`, `com.feryaeljustice.mirailink.domain.repository.ExploreRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/UpdateCategoryPreferencesUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/UpdateCategoryPreferencesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/UpdateCategoryPreferencesUseCase.kt).

Declaraciones: `UpdateCategoryPreferencesUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.explore.CategoryPreference`, `com.feryaeljustice.mirailink.domain.repository.ExploreRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/faq/GetFaqItemsUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/faq/GetFaqItemsUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/faq/GetFaqItemsUseCase.kt).

Declaraciones: `GetFaqItemsUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.faq.FaqItem`, `com.feryaeljustice.mirailink.domain.repository.FaqRepository`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/feed/GetFeedUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/feed/GetFeedUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/feed/GetFeedUseCase.kt).

Declaraciones: `GetFeedUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.SwipeRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/feedback/SendFeedbackUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/feedback/SendFeedbackUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/feedback/SendFeedbackUseCase.kt).

Declaraciones: `SendFeedbackUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.FeedbackRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/haptics/CalculateHeartbeatAffinityUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/haptics/CalculateHeartbeatAffinityUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/haptics/CalculateHeartbeatAffinityUseCase.kt).

Declaraciones: `CalculateHeartbeatAffinityUseCase`, `calculateCategoryScore`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.haptics.HeartbeatAffinity`, `com.feryaeljustice.mirailink.domain.model.user.User`, `kotlin.math.roundToInt`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/location/SendLocationPingUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/location/SendLocationPingUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/location/SendLocationPingUseCase.kt).

Declaraciones: `SendLocationPingUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.LocationRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/match/GetMatchesUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/match/GetMatchesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/match/GetMatchesUseCase.kt).

Declaraciones: `GetMatchesUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.MatchRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/notification/SaveNotificationFCMUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/notification/SaveNotificationFCMUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/notification/SaveNotificationFCMUseCase.kt).

Declaraciones: `SaveNotificationFCMUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/service/FcmService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/service/FcmService.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/MainActivity.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/MainActivity.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/onboarding/CheckOnboardingIsCompleted.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/onboarding/CheckOnboardingIsCompleted.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/onboarding/CheckOnboardingIsCompleted.kt).

Declaraciones: `CheckOnboardingIsCompleted`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.OnboardingRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/CheckProfilePictureUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/CheckProfilePictureUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/CheckProfilePictureUseCase.kt).

Declaraciones: `CheckProfilePictureUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt](../../app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/DeleteUserPhotoUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/DeleteUserPhotoUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/DeleteUserPhotoUseCase.kt).

Declaraciones: `DeleteUserPhotoUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/UploadUserPhotoUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/UploadUserPhotoUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/UploadUserPhotoUseCase.kt).

Declaraciones: `UploadUserPhotoUseCase`.

Dependencias importadas: `android.net.Uri`, `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/report/ReportUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/report/ReportUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/report/ReportUseCase.kt).

Declaraciones: `ReportUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.ReportRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/settings/GetSearchPreferencesUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/settings/GetSearchPreferencesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/settings/GetSearchPreferencesUseCase.kt).

Declaraciones: `GetSearchPreferencesUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences`, `com.feryaeljustice.mirailink.domain.repository.SearchPreferencesRepository`, `kotlinx.coroutines.flow.Flow`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/settings/SaveSearchPreferencesUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/settings/SaveSearchPreferencesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/settings/SaveSearchPreferencesUseCase.kt).

Declaraciones: `SaveSearchPreferencesUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences`, `com.feryaeljustice.mirailink.domain.repository.SearchPreferencesRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/studio/AnalyzePhotoQualityUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/studio/AnalyzePhotoQualityUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/studio/AnalyzePhotoQualityUseCase.kt).

Declaraciones: `AnalyzePhotoQualityUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.studio.FaceBiometrics`, `com.feryaeljustice.mirailink.domain.model.studio.ImageQualityMetrics`, `com.feryaeljustice.mirailink.domain.model.studio.MetricStatus`, `com.feryaeljustice.mirailink.domain.model.studio.MiraiScanResult`, `com.feryaeljustice.mirailink.domain.model.studio.QualityBadge`, `com.feryaeljustice.mirailink.domain.model.studio.ScanContentType`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/CancelSubscriptionIntentUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/CancelSubscriptionIntentUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/CancelSubscriptionIntentUseCase.kt).

Declaraciones: `CancelSubscriptionIntentUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.SubscriptionRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/GetSubscriptionStatusUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/GetSubscriptionStatusUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/GetSubscriptionStatusUseCase.kt).

Declaraciones: `GetSubscriptionStatusUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanInfo`, `com.feryaeljustice.mirailink.domain.repository.SubscriptionRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `kotlinx.coroutines.flow.StateFlow`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/LaunchBillingFlowUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/LaunchBillingFlowUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/LaunchBillingFlowUseCase.kt).

Declaraciones: `LaunchBillingFlowUseCase`.

Dependencias importadas: `android.app.Activity`, `com.feryaeljustice.mirailink.data.billing.BillingClientManager`, `com.feryaeljustice.mirailink.domain.repository.SubscriptionRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/RestorePurchasesUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/RestorePurchasesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/RestorePurchasesUseCase.kt).

Declaraciones: `RestorePurchasesUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanInfo`, `com.feryaeljustice.mirailink.domain.repository.SubscriptionRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/DislikeUserUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/DislikeUserUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/DislikeUserUseCase.kt).

Declaraciones: `DislikeUserUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.SwipeRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/GetReceivedLikesUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/GetReceivedLikesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/GetReceivedLikesUseCase.kt).

Declaraciones: `GetReceivedLikesUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.swipe.ReceivedLike`, `com.feryaeljustice.mirailink.domain.repository.SwipeRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/LikeUserUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/LikeUserUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/LikeUserUseCase.kt).

Declaraciones: `LikeUserUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.SwipeRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmPasswordResetUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmPasswordResetUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmPasswordResetUseCase.kt).

Declaraciones: `ConfirmPasswordResetUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmVerificationCodeUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmVerificationCodeUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmVerificationCodeUseCase.kt).

Declaraciones: `ConfirmVerificationCodeUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/DeleteAccountUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/DeleteAccountUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/DeleteAccountUseCase.kt).

Declaraciones: `DeleteAccountUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetCurrentUserUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetCurrentUserUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetCurrentUserUseCase.kt).

Declaraciones: `GetCurrentUserUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserByIdUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserByIdUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserByIdUseCase.kt).

Declaraciones: `GetUserByIdUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserProfileByUsernameUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserProfileByUsernameUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserProfileByUsernameUseCase.kt).

Declaraciones: `GetUserProfileByUsernameUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUsersUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUsersUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUsersUseCase.kt).

Declaraciones: `GetUsersUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.user.User`, `com.feryaeljustice.mirailink.domain.repository.UsersRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestPasswordResetUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestPasswordResetUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestPasswordResetUseCase.kt).

Declaraciones: `RequestPasswordResetUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestVerificationCodeUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestVerificationCodeUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestVerificationCodeUseCase.kt).

Declaraciones: `RequestVerificationCodeUseCase`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/UpdateUserProfileUseCase.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/UpdateUserProfileUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/UpdateUserProfileUseCase.kt).

Declaraciones: `UpdateUserProfileUseCase`.

Dependencias importadas: `android.net.Uri`, `com.feryaeljustice.mirailink.domain.repository.UserRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/UseCaseModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/util/CredentialHelper.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/util/CredentialHelper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/CredentialHelper.kt).

Declaraciones: `CredentialHelper`, `savePasswordCredential`, `getSavedPasswordCredential`.

Dependencias importadas: `android.content.Context`, `androidx.credentials.CreatePasswordRequest`, `androidx.credentials.CredentialManager`, `androidx.credentials.GetCredentialRequest`, `androidx.credentials.GetPasswordOption`, `androidx.credentials.PasswordCredential`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/DataModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/util/DateUtils.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/util/DateUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/DateUtils.kt).

Declaraciones: `parseDate`, `formatTimestamp`, `formatDateSeparator`, `DateSerializer`, `serialize`, `deserialize`, `millisToBackendDate`, `toBackendDate`, `backendDateToMillis`, `String?.toAgeOrNull`.

Dependencias importadas: `android.util.Log`, `kotlinx.serialization.KSerializer`, `kotlinx.serialization.descriptors.PrimitiveKind`, `kotlinx.serialization.descriptors.PrimitiveSerialDescriptor`, `kotlinx.serialization.descriptors.SerialDescriptor`, `kotlinx.serialization.encoding.Decoder`, `kotlinx.serialization.encoding.Encoder`, `java.text.ParseException`, `java.text.SimpleDateFormat`, `java.time.Instant`, `java.time.LocalDate`, `java.time.Period`, `java.time.ZoneId`, `java.time.format.DateTimeFormatter`, `java.time.format.DateTimeParseException`, `java.time.format.FormatStyle`, `java.util.Date`, `java.util.Locale`, `java.util.TimeZone`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/domain/util/FirebaseUtils.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/util/FirebaseUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/FirebaseUtils.kt).

Declaraciones: `applyTelemetryConsent`.

Dependencias importadas: `android.content.Context`, `com.google.firebase.analytics.FirebaseAnalytics`, `com.google.firebase.crashlytics.FirebaseCrashlytics`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/domain/util/GenericUtils.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/util/GenericUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/GenericUtils.kt).

Declaraciones: `List<T>.mapNotNullIndexed`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/domain/util/GeoUtils.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/util/GeoUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/GeoUtils.kt).

Declaraciones: `GeoUtils`, `calculateDistanceKm`, `formatDistance`.

Dependencias importadas: `kotlin.math.atan2`, `kotlin.math.cos`, `kotlin.math.sin`, `kotlin.math.sqrt`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/map/SearchRadiusMinimap.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/map/SearchRadiusMinimap.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/util/Logger.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/util/Logger.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/Logger.kt).

Declaraciones: `Logger`, `d`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/util/AndroidLogger.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/util/AndroidLogger.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/LoggerModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/LoggerModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/util/MediaUtils.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/util/MediaUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/MediaUtils.kt).

Declaraciones: `resolvePhotoUrls`, `resolvePhotoUrl`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.constants.HTTP_REGEX`, `com.feryaeljustice.mirailink.domain.model.user.UserPhoto`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/domain/util/MiraiLinkResult.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/util/MiraiLinkResult.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/MiraiLinkResult.kt).

Declaraciones: `MiraiLinkResult`, `Success`, `Error`, `success`, `error`, `MiraiLinkResult<T>.map`, `MiraiLinkResult<T>.mapError`, `MiraiLinkResult<T>.onSuccess`, `MiraiLinkResult<T>.onError`, `MiraiLinkResult<T>.asEmptyResult`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.error.AppError`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/AppConfigRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/AppConfigRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/CatalogRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ChatRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ExploreRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ExploreRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/FeedbackRemoteDatasource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/FeedbackRemoteDatasource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/MatchRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/MatchRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ReportRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/ReportRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/SwipeRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/TwoFactorRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/TwoFactorRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UserRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UsersRemoteDataSource.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/datasource/UsersRemoteDataSource.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/CatalogRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/CatalogRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ChatRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ChatRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/FeedbackRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/FeedbackRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/LocationRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/LocationRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/MatchRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/MatchRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/OnboardingRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/OnboardingRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/ReportRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/ReportRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SearchPreferencesRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SwipeRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SwipeRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/TwoFactorRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/TwoFactorRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/UserRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/UserRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/UsersRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/UsersRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingChatRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingChatRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingExploreRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingExploreRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingMatchRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingMatchRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingSwipeRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingSwipeRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingUserRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/delegating/DelegatingUserRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoChatRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoChatRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoExploreRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoMatchRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoMatchRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoSwipeRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoUserRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/demo/DemoUserRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/util/SafeApiCall.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/util/SafeApiCall.kt), [app/src/main/java/com/feryaeljustice/mirailink/data/util/SafeLocalCall.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/util/SafeLocalCall.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/AppConfigRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/AppConfigRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/CatalogRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/CatalogRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ChatRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ChatRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ExploreRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ExploreRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/FeedbackRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/FeedbackRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/LocationRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/LocationRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/MatchRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/MatchRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/OnboardingRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/OnboardingRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ReportRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/ReportRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SearchPreferencesRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SearchPreferencesRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SubscriptionRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SubscriptionRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SwipeRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/SwipeRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/TwoFactorRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/TwoFactorRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/UserRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/UserRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/repository/UsersRepository.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/repository/UsersRepository.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/CheckAppVersionUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/CheckAppVersionUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/ai/GenerateContentUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/ai/GenerateContentUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/AutologinUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/AutologinUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/CheckIsVerifiedUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/CheckIsVerifiedUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LoginUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LoginUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LogoutUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/LogoutUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/RegisterUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/RegisterUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/DisableTwoFactorUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/DisableTwoFactorUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/GetTwoFactorStatusUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/GetTwoFactorStatusUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/LoginVerifyTwoFactorLastStepUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/LoginVerifyTwoFactorLastStepUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/SetupTwoFactorUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/SetupTwoFactorUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/VerifyTwoFactorUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/auth/two_factor/VerifyTwoFactorUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetAnimesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetAnimesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetGamesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetGamesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetProfileOptionsUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/catalog/GetProfileOptionsUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/ConnectSocketUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/ConnectSocketUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/CreateGroupChatUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/CreateGroupChatUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/CreatePrivateChatUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/CreatePrivateChatUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/DisconnectSocketUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/DisconnectSocketUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatMessagesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatMessagesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatsFromUser.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/GetChatsFromUser.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/MarkChatAsReadUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/MarkChatAsReadUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/SendMessagesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/chat/SendMessagesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryFeedUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryFeedUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryPreferencesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetCategoryPreferencesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetExploreSectionsUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/GetExploreSectionsUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/UpdateCategoryPreferencesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/explore/UpdateCategoryPreferencesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/feed/GetFeedUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/feed/GetFeedUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/feedback/SendFeedbackUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/feedback/SendFeedbackUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/location/SendLocationPingUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/location/SendLocationPingUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/match/GetMatchesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/match/GetMatchesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/notification/SaveNotificationFCMUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/notification/SaveNotificationFCMUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/onboarding/CheckOnboardingIsCompleted.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/onboarding/CheckOnboardingIsCompleted.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/CheckProfilePictureUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/CheckProfilePictureUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/DeleteUserPhotoUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/DeleteUserPhotoUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/UploadUserPhotoUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/photos/UploadUserPhotoUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/report/ReportUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/report/ReportUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/settings/SaveSearchPreferencesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/settings/SaveSearchPreferencesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/CancelSubscriptionIntentUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/CancelSubscriptionIntentUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/GetSubscriptionStatusUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/GetSubscriptionStatusUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/LaunchBillingFlowUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/LaunchBillingFlowUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/RestorePurchasesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/subscription/RestorePurchasesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/DislikeUserUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/DislikeUserUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/GetReceivedLikesUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/GetReceivedLikesUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/LikeUserUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/swipe/LikeUserUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmPasswordResetUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmPasswordResetUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmVerificationCodeUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/ConfirmVerificationCodeUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/DeleteAccountUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/DeleteAccountUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetCurrentUserUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetCurrentUserUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserByIdUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserByIdUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserProfileByUsernameUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUserProfileByUsernameUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUsersUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/GetUsersUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestPasswordResetUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestPasswordResetUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestVerificationCodeUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/RequestVerificationCodeUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/UpdateUserProfileUseCase.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/usecase/users/UpdateUserProfileUseCase.kt), [app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt](../../app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/ResidenceSelector.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/ResidenceSelector.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/domain/util/StringUtils.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/util/StringUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/StringUtils.kt).

Declaraciones: `String.isValidUrl`, `String?.getFormattedUrl`, `String.superCapitalize`, `String.isEmailValid`, `String.isPhoneNumberValid`, `String.isCountryCodeValid`, `String.isPasswordValid`, `String.isNotTrivialPassword`, `String?.isStringNotEmpty`, `String.isSafeSqlInput`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.constants.TEMPORAL_PLACEHOLDER_PICTURE_URL`, `com.feryaeljustice.mirailink.domain.constants.URL_REGEX`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/domain/util/UserUtils.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/domain/util/UserUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/UserUtils.kt).

Declaraciones: `UserViewEntry.nicknameElseUsername`, `MinimalUserInfoViewEntry.nicknameElseUsername`, `calculateAge`, `isAtLeast16YearsOld`.

Dependencias importadas: `com.feryaeljustice.mirailink.ui.viewentries.user.MinimalUserInfoViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/kotzilla/KotzillaJson.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/kotzilla/KotzillaJson.kt](../../app/src/main/java/com/feryaeljustice/mirailink/kotzilla/KotzillaJson.kt).

Declaraciones: `KotzillaJson`, `KotzillaKey`, `KotzillaConfigLoader`, `loadForThisApp`.

Dependencias importadas: `kotlinx.serialization.Serializable`, `kotlinx.serialization.json.Json`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/notification/MiraiLinkNotificationUtils.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/notification/MiraiLinkNotificationUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/notification/MiraiLinkNotificationUtils.kt).

Declaraciones: `createNotificationChannel`.

Dependencias importadas: `android.app.NotificationChannel`, `android.app.NotificationManager`, `com.feryaeljustice.mirailink.service.FcmService.Companion.NOTIFICATION_CHANNEL_DESCRIPTION`, `com.feryaeljustice.mirailink.service.FcmService.Companion.NOTIFICATION_CHANNEL_ID`, `com.feryaeljustice.mirailink.service.FcmService.Companion.NOTIFICATION_CHANNEL_NAME`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/service/FcmService.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/service/FcmService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/service/FcmService.kt).

Declaraciones: `FcmService`, `onMessageReceived`, `onNewToken`, `showChatNotification`, `showNotification`.

Dependencias importadas: `android.app.NotificationManager`, `android.app.PendingIntent`, `android.app.PendingIntent.FLAG_IMMUTABLE`, `android.content.Intent`, `android.util.Log`, `androidx.core.app.NotificationCompat`, `androidx.core.app.Person`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.usecase.notification.SaveNotificationFCMUseCase`, `com.feryaeljustice.mirailink.notification.createNotificationChannel`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.MainActivity`, `com.google.firebase.messaging.FirebaseMessagingService`, `com.google.firebase.messaging.RemoteMessage`, `kotlinx.coroutines.CoroutineScope`, `kotlinx.coroutines.flow.first`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withTimeoutOrNull`, `org.koin.core.component.KoinComponent`, `org.koin.core.component.inject`, `kotlin.random.Random`, `kotlin.time.Duration.Companion.milliseconds`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/notification/MiraiLinkNotificationUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/notification/MiraiLinkNotificationUtils.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/MainActivity.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/MainActivity.kt).

## app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkPrefs.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkPrefs.kt](../../app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkPrefs.kt).

Declaraciones: `GlobalMiraiLinkPrefs`, `markOnboardingCompleted`, `isOnboardingCompleted`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datastore.MiraiLinkPrefs`, `kotlinx.coroutines.CoroutineScope`, `kotlinx.coroutines.launch`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt](../../app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt).

Declaraciones: `GlobalMiraiLinkSession`, `currentAuth`, `setPremium`, `setPlus`, `setSubscriptionState`, `handleDeepLink`, `clearPendingDeepLink`, `setPendingStudioPhoto`, `consumePendingStudioPhoto`, `setForceUpdateBlocking`, `clearForceUpdateBlocking`, `setForcedUpdate`, `clearForcedUpdate`, `clearSession`, `saveSession`, `enterDemoMode`, `resetDemoData`, `saveIsVerified`, `disableBars`, `enableBars`, `hideBars`, `showBars`, `showHideTopBar`, `showHideBottomBar`, `enableDisableTopBar`, `enableDisableBottomBar`, `hideTopBarSettingsIcon`, `showTopBarSettingsIcon`, `startObservingHasProfilePicture`, `stopObservingHasProfilePicture`, `refreshHasProfilePicture`.

Dependencias importadas: `com.feryaeljustice.mirailink.data.datastore.SessionManager`, `com.feryaeljustice.mirailink.data.demo.DemoModeManager`, `com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder`, `com.feryaeljustice.mirailink.domain.usecase.photos.CheckProfilePictureUseCase`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.ui.components.topbars.TopBarConfig`, `kotlinx.coroutines.CoroutineScope`, `kotlinx.coroutines.Job`, `kotlinx.coroutines.channels.BufferOverflow`, `kotlinx.coroutines.delay`, `kotlinx.coroutines.flow.MutableSharedFlow`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.SharedFlow`, `kotlinx.coroutines.flow.SharingStarted`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asSharedFlow`, `kotlinx.coroutines.flow.collectLatest`, `kotlinx.coroutines.flow.distinctUntilChanged`, `kotlinx.coroutines.flow.stateIn`, `kotlinx.coroutines.flow.update`, `kotlinx.coroutines.isActive`, `kotlinx.coroutines.launch`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/repository/SubscriptionRepositoryImpl.kt), [app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/service/FcmService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/service/FcmService.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/MainActivity.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/MainActivity.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/MainActivity.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/MainActivity.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/MainActivity.kt).

Declaraciones: `MainActivity`, `onCreate`, `onNewIntent`, `initializeAds`, `firebaseInitialize`, `newToken`.

Dependencias importadas: `android.app.NotificationManager`, `android.content.Context`, `android.os.Build`, `android.os.Bundle`, `android.util.Log`, `androidx.activity.ComponentActivity`, `androidx.activity.compose.setContent`, `androidx.activity.enableEdgeToEdge`, `androidx.annotation.RequiresApi`, `androidx.compose.runtime.getValue`, `androidx.lifecycle.Lifecycle`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `androidx.lifecycle.lifecycleScope`, `com.feryaeljustice.mirailink.BuildConfig`, `com.feryaeljustice.mirailink.data.manager.AdMobManager`, `com.feryaeljustice.mirailink.di.koin.Qualifiers.ApplicationScope`, `com.feryaeljustice.mirailink.domain.usecase.notification.SaveNotificationFCMUseCase`, `com.feryaeljustice.mirailink.notification.createNotificationChannel`, `com.feryaeljustice.mirailink.service.FcmService`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.theme.AppThemeManager`, `com.google.firebase.Firebase`, `com.google.firebase.appcheck.appCheck`, `com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory`, `com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory`, `com.google.firebase.initialize`, `com.google.firebase.messaging.messaging`, `kotlinx.coroutines.CoroutineScope`, `kotlinx.coroutines.delay`, `kotlinx.coroutines.flow.first`, `kotlinx.coroutines.isActive`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withTimeoutOrNull`, `org.koin.android.ext.android.inject`, `org.koin.androidx.viewmodel.ext.android.viewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/service/FcmService.kt](../../app/src/main/java/com/feryaeljustice/mirailink/service/FcmService.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/MainViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/MainViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/MainViewModel.kt).

Declaraciones: `MainViewModel`.

Dependencias importadas: `androidx.lifecycle.ViewModel`, `com.feryaeljustice.mirailink.core.featureflags.FeatureFlagStore`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/MiraiLinkAppRoot.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/MiraiLinkAppRoot.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/MiraiLinkAppRoot.kt).

Declaraciones: `MiraiLinkAppRoot`, `EnableTransparentStatusBar`.

Dependencias importadas: `android.Manifest`, `android.app.Activity`, `android.content.pm.PackageManager`, `android.os.Build`, `android.util.Log`, `androidx.activity.compose.rememberLauncherForActivityResult`, `androidx.activity.result.contract.ActivityResultContracts`, `androidx.annotation.RequiresApi`, `androidx.compose.foundation.isSystemInDarkTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.saveable.rememberSaveable`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.platform.LocalContext`, `androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale`, `androidx.core.content.ContextCompat`, `com.feryaeljustice.mirailink.core.featureflags.FeatureFlag`, `com.feryaeljustice.mirailink.domain.util.applyTelemetryConsent`, `com.feryaeljustice.mirailink.ui.components.notifications.NotificationRationaleDialog`, `com.feryaeljustice.mirailink.ui.navigation.NavWrapper`, `com.feryaeljustice.mirailink.ui.theme.AppThemeManager`, `com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme`, `com.feryaeljustice.mirailink.ui.utils.findActivity`, `com.google.android.gms.ads.MobileAds`, `com.google.android.ump.ConsentInformation`, `com.google.android.ump.ConsentRequestParameters`, `com.google.android.ump.UserMessagingPlatform`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/appconfig/UpdateGate.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/appconfig/UpdateGate.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/appconfig/UpdateGate.kt).

Declaraciones: `UpdateGate`.

Dependencias importadas: `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.window.DialogProperties`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkBasicText.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkBasicText.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkBasicText.kt).

Declaraciones: `MiraiLinkBasicText`.

Dependencias importadas: `androidx.compose.foundation.text.BasicText`, `androidx.compose.foundation.text.TextAutoSize`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.text.TextLayoutResult`, `androidx.compose.ui.text.TextStyle`, `androidx.compose.ui.text.style.TextOverflow`, `androidx.compose.ui.unit.TextUnit`, `androidx.compose.ui.unit.sp`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkButton.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkButton.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkButton.kt).

Declaraciones: `MiraiLinkButton`.

Dependencias importadas: `androidx.compose.foundation.layout.RowScope`, `androidx.compose.material3.Button`, `androidx.compose.material3.ButtonDefaults`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.graphics.Color`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/ProfileOptionPickerModal.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/ProfileOptionPickerModal.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/VisualInterestPickerModal.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/VisualInterestPickerModal.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkDialog.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkErrorContent.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkErrorContent.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryDiscoverySettingsSheet.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryDiscoverySettingsSheet.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/SearchSettingsSection.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/SearchSettingsSection.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/HudQualityVerdictSheet.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/HudQualityVerdictSheet.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkCard.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkCard.kt).

Declaraciones: `MiraiLinkCard`.

Dependencias importadas: `androidx.compose.foundation.layout.ColumnScope`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.CardElevation`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.unit.dp`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkIconButton.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkIconButton.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkIconButton.kt).

Declaraciones: `MiraiLinkIconButton`.

Dependencias importadas: `androidx.compose.material3.IconButton`, `androidx.compose.material3.IconButtonColors`, `androidx.compose.material3.IconButtonDefaults`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/emoji/EmojiPickerButton.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/emoji/EmojiPickerButton.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/ChatTopBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/ChatTopBar.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/MiraiLinkTopBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/MiraiLinkTopBar.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkImage.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkImage.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkImage.kt).

Declaraciones: `MiraiLinkImage`.

Dependencias importadas: `androidx.compose.foundation.Image`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.drawBehind`, `androidx.compose.ui.geometry.Offset`, `androidx.compose.ui.geometry.Size`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.graphics.drawscope.Stroke`, `androidx.compose.ui.layout.ContentScale`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.unit.Dp`, `androidx.compose.ui.unit.dp`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/MiraiLinkTopBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/MiraiLinkTopBar.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkOutlinedButton.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkOutlinedButton.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkOutlinedButton.kt).

Declaraciones: `MiraiLinkOutlinedButton`.

Dependencias importadas: `androidx.compose.foundation.BorderStroke`, `androidx.compose.foundation.layout.RowScope`, `androidx.compose.material3.ButtonColors`, `androidx.compose.material3.ButtonDefaults`, `androidx.compose.material3.OutlinedButton`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.graphics.Shape`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/ProfileOptionPickerModal.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/ProfileOptionPickerModal.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/GamerPromptComponents.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/GamerPromptComponents.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkOutlinedIconButton.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkOutlinedIconButton.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkOutlinedIconButton.kt).

Declaraciones: `MiraiLinkOutlinedIconButton`.

Dependencias importadas: `androidx.compose.material3.IconButtonColors`, `androidx.compose.material3.IconButtonDefaults`, `androidx.compose.material3.OutlinedIconButton`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkOutlinedTextField.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkOutlinedTextField.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkOutlinedTextField.kt).

Declaraciones: `MiraiLinkOutlinedTextField`.

Dependencias importadas: `androidx.compose.foundation.text.KeyboardActions`, `androidx.compose.foundation.text.KeyboardOptions`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.OutlinedTextField`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.text.input.VisualTransformation`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MultiSelectDropdown.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MultiSelectDropdown.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorPutCodeOrRecoveryCDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorPutCodeOrRecoveryCDialog.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupDialog.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/SearchSettingsSection.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/SearchSettingsSection.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkScreenContent.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkScreenContent.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkScreenContent.kt).

Declaraciones: `MiraiLinkScreenContent`.

Dependencias importadas: `androidx.compose.animation.AnimatedContent`, `androidx.compose.animation.fadeIn`, `androidx.compose.animation.fadeOut`, `androidx.compose.animation.scaleIn`, `androidx.compose.animation.scaleOut`, `androidx.compose.animation.togetherWith`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkText.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkText.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkText.kt).

Declaraciones: `MiraiLinkText`.

Dependencias importadas: `androidx.compose.material3.LocalContentColor`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Text`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.text.TextStyle`, `androidx.compose.ui.text.font.FontStyle`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.text.style.TextAlign`, `androidx.compose.ui.text.style.TextOverflow`, `androidx.compose.ui.unit.TextUnit`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/ProfileOptionPickerModal.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/ProfileOptionPickerModal.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/ChatList.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/ChatList.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/MessageItem.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/MessageItem.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/MessageListItem.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/MessageListItem.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/demo/DemoModeBanner.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/demo/DemoModeBanner.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/explore/CategoryCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/explore/CategoryCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/map/SearchRadiusMinimap.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/map/SearchRadiusMinimap.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/match/MatchCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/match/MatchCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/match/MatchesRow.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/match/MatchesRow.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/HashtagChip.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/HashtagChip.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkDialog.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkErrorContent.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkErrorContent.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MultiSelectDropdown.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MultiSelectDropdown.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/notifications/NotificationRationaleDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/notifications/NotificationRationaleDialog.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/ChatTopBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/ChatTopBar.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/MiraiLinkTopBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/MiraiLinkTopBar.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorPutCodeOrRecoveryCDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorPutCodeOrRecoveryCDialog.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupCompletedDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupCompletedDialog.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupDialog.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorStatusDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorStatusDialog.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/GamerPromptComponents.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/GamerPromptComponents.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryDiscoverySettingsSheet.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryDiscoverySettingsSheet.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/CurrentPlanCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/CurrentPlanCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/SearchSettingsSection.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/SearchSettingsSection.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkTextButton.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkTextButton.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkTextButton.kt).

Declaraciones: `MiraiLinkTextButton`.

Dependencias importadas: `androidx.compose.foundation.background`, `androidx.compose.foundation.combinedClickable`, `androidx.compose.foundation.layout.padding`, `androidx.compose.material3.ButtonDefaults`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Surface`, `androidx.compose.material3.TextButton`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.rememberUpdatedState`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.text.TextStyle`, `androidx.compose.ui.unit.dp`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/notifications/NotificationRationaleDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/notifications/NotificationRationaleDialog.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkTextField.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkTextField.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkTextField.kt).

Declaraciones: `NoopTextToolbar`, `showMenu`, `hide`, `MiraiLinkTextField`.

Dependencias importadas: `androidx.compose.foundation.text.KeyboardActions`, `androidx.compose.foundation.text.KeyboardOptions`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.TextField`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.CompositionLocalProvider`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.geometry.Rect`, `androidx.compose.ui.platform.LocalTextToolbar`, `androidx.compose.ui.platform.TextToolbar`, `androidx.compose.ui.platform.TextToolbarStatus`, `androidx.compose.ui.text.input.TextFieldValue`, `androidx.compose.ui.text.input.VisualTransformation`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt).

Declaraciones: `MiraiLinkBottomBar`.

Dependencias importadas: `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.NavigationBar`, `androidx.compose.material3.NavigationBarItem`, `androidx.compose.material3.NavigationBarItemDefaults`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.navigation.AppScreen`, `com.feryaeljustice.mirailink.ui.navigation.BottomNavItem`, `com.feryaeljustice.mirailink.ui.navigation.NavigationState`, `com.feryaeljustice.mirailink.ui.navigation.Navigator`, `com.skydoves.compose.stability.runtime.TraceRecomposition`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestCard.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestCard.kt).

Declaraciones: `InterestCard`.

Dependencias importadas: `androidx.compose.foundation.background`, `androidx.compose.foundation.border`, `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.aspectRatio`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Close`, `androidx.compose.material3.Icon`, `androidx.compose.material3.IconButton`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Text`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.graphics.Brush`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.layout.ContentScale`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.text.style.TextAlign`, `androidx.compose.ui.text.style.TextOverflow`, `androidx.compose.ui.unit.dp`, `androidx.compose.ui.unit.sp`, `coil.compose.AsyncImage`, `coil.request.ImageRequest`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.util.InterestImageFallback`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestsGrid.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestsGrid.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestsGrid.kt).

Declaraciones: `InterestsGrid`, `InterestItemData`, `AnimeViewEntry.toInterestItemData`, `GameViewEntry.toInterestItemData`.

Dependencias importadas: `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.padding`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Text`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.ui.viewentries.catalog.AnimeViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.catalog.GameViewEntry`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/ProfileOptionPickerModal.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/ProfileOptionPickerModal.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/ProfileOptionPickerModal.kt).

Declaraciones: `ProfileSingleOptionPickerModal`, `ProfileMultiOptionPickerModal`, `isIndeterminateOption`, `handleToggleOption`.

Dependencias importadas: `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxHeight`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.imePadding`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.lazy.LazyColumn`, `androidx.compose.foundation.lazy.items`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Check`, `androidx.compose.material.icons.filled.Clear`, `androidx.compose.material.icons.filled.Close`, `androidx.compose.material.icons.filled.Search`, `androidx.compose.material3.Checkbox`, `androidx.compose.material3.ExperimentalMaterial3Api`, `androidx.compose.material3.HorizontalDivider`, `androidx.compose.material3.Icon`, `androidx.compose.material3.IconButton`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.ModalBottomSheet`, `androidx.compose.material3.OutlinedTextField`, `androidx.compose.material3.RadioButton`, `androidx.compose.material3.SheetState`, `androidx.compose.material3.Surface`, `androidx.compose.material3.rememberModalBottomSheetState`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateListOf`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.text.style.TextOverflow`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.data.model.response.catalog.CatalogItemOptionDto`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/VisualInterestPickerModal.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/VisualInterestPickerModal.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/VisualInterestPickerModal.kt).

Declaraciones: `VisualInterestPickerModal`.

Dependencias importadas: `androidx.compose.foundation.background`, `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxHeight`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.imePadding`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.lazy.LazyColumn`, `androidx.compose.foundation.lazy.items`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Clear`, `androidx.compose.material.icons.filled.Close`, `androidx.compose.material.icons.filled.Search`, `androidx.compose.material3.Checkbox`, `androidx.compose.material3.ExperimentalMaterial3Api`, `androidx.compose.material3.Icon`, `androidx.compose.material3.IconButton`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.ModalBottomSheet`, `androidx.compose.material3.OutlinedTextField`, `androidx.compose.material3.SheetState`, `androidx.compose.material3.Text`, `androidx.compose.material3.rememberModalBottomSheetState`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.layout.ContentScale`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.text.style.TextOverflow`, `androidx.compose.ui.unit.dp`, `coil.compose.AsyncImage`, `coil.request.ImageRequest`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton`, `com.feryaeljustice.mirailink.ui.util.InterestImageFallback`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/ChatList.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/ChatList.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/ChatList.kt).

Declaraciones: `ChatList`.

Dependencias importadas: `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.PaddingValues`, `androidx.compose.foundation.layout.padding`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.viewentries.chat.ChatPreviewViewEntry`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/DateSeparator.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/DateSeparator.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/DateSeparator.kt).

Declaraciones: `DateSeparator`.

Dependencias importadas: `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.padding`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Text`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.unit.dp`, `java.util.Locale`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/MessageItem.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/MessageItem.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/MessageItem.kt).

Declaraciones: `MessageItem`, `MessageStyle`, `getMessageStyle`.

Dependencias importadas: `androidx.compose.foundation.background`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.widthIn`, `androidx.compose.foundation.layout.wrapContentHeight`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.remember`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.domain.util.formatTimestamp`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/MessageListItem.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/MessageListItem.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/MessageListItem.kt).

Declaraciones: `MessageListItem`.

Dependencias importadas: `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.material3.Badge`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.layout.ContentScale`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.style.TextOverflow`, `androidx.compose.ui.unit.dp`, `coil.compose.AsyncImage`, `coil.request.ImageRequest`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/emoji/EmojiPickerButton.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/emoji/EmojiPickerButton.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/emoji/EmojiPickerButton.kt).

Declaraciones: `TextFieldValue.insertEmojiAtCursor`, `EmojiPickerButton`.

Dependencias importadas: `androidx.compose.material3.Icon`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.TextRange`, `androidx.compose.ui.text.input.TextFieldValue`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton`, `dev.alexdametto.compose_emoji_picker.presentation.EmojiPicker`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/demo/DemoModeBanner.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/demo/DemoModeBanner.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/demo/DemoModeBanner.kt).

Declaraciones: `DemoModeBanner`.

Dependencias importadas: `androidx.compose.animation.AnimatedVisibility`, `androidx.compose.animation.expandVertically`, `androidx.compose.animation.shrinkVertically`, `androidx.compose.foundation.background`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Info`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/explore/CategoryCard.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/explore/CategoryCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/explore/CategoryCard.kt).

Declaraciones: `resolveCategoryIconDrawable`, `getSectionGradient`, `CategoryCarouselCard`, `CategoryGridCard`, `TextCountBadge`.

Dependencias importadas: `androidx.annotation.DrawableRes`, `androidx.compose.foundation.background`, `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Surface`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.graphics.Brush`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.text.style.TextOverflow`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.model.explore.ExploreCategory`, `com.feryaeljustice.mirailink.domain.model.explore.ExploreSectionGroup`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/haptics/HapticHeartbeatLikeModifier.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/haptics/HapticHeartbeatLikeModifier.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/haptics/HapticHeartbeatLikeModifier.kt).

Declaraciones: `Modifier.hapticHeartbeatLikeTrigger`.

Dependencias importadas: `androidx.compose.foundation.gestures.awaitEachGesture`, `androidx.compose.foundation.gestures.awaitFirstDown`, `androidx.compose.foundation.gestures.waitForUpOrCancellation`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.rememberCoroutineScope`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.input.pointer.pointerInput`, `kotlinx.coroutines.Job`, `kotlinx.coroutines.delay`, `kotlinx.coroutines.isActive`, `kotlinx.coroutines.launch`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/haptics/HapticHeartbeatOverlay.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/haptics/HapticHeartbeatOverlay.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/haptics/HapticHeartbeatOverlay.kt).

Declaraciones: `HapticHeartbeatOverlay`.

Dependencias importadas: `androidx.activity.compose.BackHandler`, `androidx.compose.animation.AnimatedVisibility`, `androidx.compose.animation.core.FastOutSlowInEasing`, `androidx.compose.animation.core.LinearEasing`, `androidx.compose.animation.core.RepeatMode`, `androidx.compose.animation.core.animateFloat`, `androidx.compose.animation.core.infiniteRepeatable`, `androidx.compose.animation.core.rememberInfiniteTransition`, `androidx.compose.animation.core.tween`, `androidx.compose.animation.fadeIn`, `androidx.compose.animation.fadeOut`, `androidx.compose.foundation.Canvas`, `androidx.compose.foundation.background`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Favorite`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Text`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.DisposableEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.geometry.Offset`, `androidx.compose.ui.geometry.Size`, `androidx.compose.ui.graphics.Brush`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.graphics.StrokeCap`, `androidx.compose.ui.graphics.drawscope.Stroke`, `androidx.compose.ui.graphics.graphicsLayer`, `androidx.compose.ui.platform.testTag`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.text.style.TextAlign`, `androidx.compose.ui.unit.dp`, `androidx.compose.ui.unit.sp`, `androidx.compose.ui.zIndex`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.model.haptics.HeartbeatAffinity`, `kotlin.math.pow`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/map/SearchRadiusMinimap.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/map/SearchRadiusMinimap.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/map/SearchRadiusMinimap.kt).

Declaraciones: `SearchRadiusMinimap`, `TilePosition`, `metersPerDisplayedPixel`, `selectMapZoom`.

Dependencias importadas: `androidx.compose.foundation.Canvas`, `androidx.compose.foundation.background`, `androidx.compose.foundation.border`, `androidx.compose.foundation.gestures.detectDragGestures`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.BoxWithConstraints`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.offset`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Surface`, `androidx.compose.material3.Icon`, `androidx.compose.material3.IconButton`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Refresh`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.remember`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.geometry.Offset`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.graphics.drawscope.Stroke`, `androidx.compose.ui.input.pointer.pointerInput`, `androidx.compose.ui.layout.ContentScale`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.dp`, `coil.compose.AsyncImage`, `coil.request.ImageRequest`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.util.GeoUtils`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `kotlin.math.asinh`, `kotlin.math.ceil`, `kotlin.math.floor`, `kotlin.math.min`, `kotlin.math.cos`, `kotlin.math.tan`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/SearchSettingsSection.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/SearchSettingsSection.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/match/MatchCard.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/match/MatchCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/match/MatchCard.kt).

Declaraciones: `MatchCard`.

Dependencias importadas: `androidx.compose.foundation.background`, `androidx.compose.foundation.border`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.layout.ContentScale`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.style.TextOverflow`, `androidx.compose.ui.unit.dp`, `coil.compose.AsyncImage`, `coil.request.ImageRequest`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.constants.TEMPORAL_PLACEHOLDER_PICTURE_URL`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.utils.extensions.debounceClickable`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/match/MatchesRow.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/match/MatchesRow.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/match/MatchesRow.kt).

Declaraciones: `MatchesRow`.

Dependencias importadas: `android.util.Log`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.PaddingValues`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.padding`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.layout.onVisibilityChanged`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.viewentries.user.MatchUserViewEntry`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/EditablePhotoGrid.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/EditablePhotoGrid.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/EditablePhotoGrid.kt).

Declaraciones: `EditablePhotoGrid`, `getSlotCenter`, `getSlotTopLeft`.

Dependencias importadas: `androidx.compose.animation.AnimatedVisibility`, `androidx.compose.animation.core.Spring`, `androidx.compose.animation.core.animateFloatAsState`, `androidx.compose.animation.core.spring`, `androidx.compose.animation.fadeIn`, `androidx.compose.animation.fadeOut`, `androidx.compose.animation.scaleIn`, `androidx.compose.animation.scaleOut`, `androidx.compose.foundation.background`, `androidx.compose.foundation.border`, `androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress`, `androidx.compose.foundation.gestures.detectTapGestures`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.BoxWithConstraints`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.aspectRatio`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.offset`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Add`, `androidx.compose.material.icons.filled.Refresh`, `androidx.compose.material.icons.filled.Star`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Surface`, `androidx.compose.material3.Text`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.rememberUpdatedState`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.draw.shadow`, `androidx.compose.ui.geometry.Offset`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.graphics.graphicsLayer`, `androidx.compose.ui.input.pointer.pointerInput`, `androidx.compose.ui.layout.ContentScale`, `androidx.compose.ui.platform.LocalDensity`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.semantics.CustomAccessibilityAction`, `androidx.compose.ui.semantics.contentDescription`, `androidx.compose.ui.semantics.customActions`, `androidx.compose.ui.semantics.semantics`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.unit.IntOffset`, `androidx.compose.ui.unit.dp`, `androidx.compose.ui.unit.sp`, `androidx.compose.ui.zIndex`, `coil.compose.AsyncImage`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.viewentries.media.PhotoSlotViewEntry`, `kotlin.math.roundToInt`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/FullscreenImagePreview.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/FullscreenImagePreview.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/FullscreenImagePreview.kt).

Declaraciones: `FullscreenImagePreview`, `animateToIdentity`, `normalizeDeg`.

Dependencias importadas: `androidx.compose.animation.core.Animatable`, `androidx.compose.animation.core.VectorConverter`, `androidx.compose.animation.core.tween`, `androidx.compose.foundation.BorderStroke`, `androidx.compose.foundation.background`, `androidx.compose.foundation.clickable`, `androidx.compose.foundation.gestures.detectTapGestures`, `androidx.compose.foundation.gestures.rememberTransformableState`, `androidx.compose.foundation.gestures.transformable`, `androidx.compose.foundation.interaction.MutableInteractionSource`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.safeDrawing`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.windowInsetsPadding`, `androidx.compose.foundation.layout.wrapContentSize`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Close`, `androidx.compose.material3.Icon`, `androidx.compose.material3.Surface`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.rememberCoroutineScope`, `androidx.compose.runtime.rememberUpdatedState`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.draw.shadow`, `androidx.compose.ui.geometry.Offset`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.graphics.TransformOrigin`, `androidx.compose.ui.graphics.graphicsLayer`, `androidx.compose.ui.input.pointer.pointerInput`, `androidx.compose.ui.layout.ContentScale`, `androidx.compose.ui.layout.onSizeChanged`, `androidx.compose.ui.unit.Dp`, `androidx.compose.ui.unit.IntSize`, `androidx.compose.ui.unit.dp`, `androidx.compose.ui.window.Dialog`, `androidx.compose.ui.window.DialogProperties`, `androidx.compose.ui.zIndex`, `coil.compose.AsyncImage`, `com.feryaeljustice.mirailink.ui.utils.clampOffset`, `kotlinx.coroutines.launch`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/NoRippleClickableModifier.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/NoRippleClickableModifier.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/NoRippleClickableModifier.kt).

Declaraciones: `Modifier.clickableWithNoRipple`, `NoRippleClickableElement`, `create`, `update`, `hashCode`, `equals`, `InspectorInfo.inspectableProperties`, `NoRippleClickableNode`, `onPointerEvent`, `onCancelPointerInput`.

Dependencias importadas: `androidx.compose.ui.Modifier`, `androidx.compose.ui.input.pointer.PointerEvent`, `androidx.compose.ui.input.pointer.PointerEventPass`, `androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed`, `androidx.compose.ui.node.ModifierNodeElement`, `androidx.compose.ui.node.PointerInputModifierNode`, `androidx.compose.ui.platform.InspectorInfo`, `androidx.compose.ui.unit.IntSize`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/PhotoCarousel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/PhotoCarousel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/PhotoCarousel.kt).

Declaraciones: `PhotoCarouselController`, `previous`, `next`, `openCurrent`, `PhotoCarousel`, `PagerIndicator`.

Dependencias importadas: `androidx.compose.foundation.background`, `androidx.compose.foundation.gestures.detectTapGestures`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.wrapContentHeight`, `androidx.compose.foundation.pager.HorizontalPager`, `androidx.compose.foundation.pager.PagerState`, `androidx.compose.foundation.pager.rememberPagerState`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.Stable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.rememberCoroutineScope`, `androidx.compose.runtime.rememberUpdatedState`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.graphics.RectangleShape`, `androidx.compose.ui.input.pointer.pointerInput`, `androidx.compose.ui.layout.ContentScale`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.dp`, `coil.compose.AsyncImage`, `coil.request.ImageRequest`, `com.feryaeljustice.mirailink.BuildConfig`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.constants.TEMPORAL_PLACEHOLDER_PICTURE_URL`, `com.feryaeljustice.mirailink.domain.util.resolvePhotoUrl`, `kotlinx.coroutines.launch`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/BirthdateField.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/BirthdateField.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/BirthdateField.kt).

Declaraciones: `BirthdateField`.

Dependencias importadas: `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.DateRange`, `androidx.compose.material3.DatePicker`, `androidx.compose.material3.DatePickerDialog`, `androidx.compose.material3.ExperimentalMaterial3Api`, `androidx.compose.material3.Icon`, `androidx.compose.material3.IconButton`, `androidx.compose.material3.OutlinedTextField`, `androidx.compose.material3.Text`, `androidx.compose.material3.TextButton`, `androidx.compose.material3.rememberDatePickerState`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.saveable.rememberSaveable`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.stringResource`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.util.backendDateToMillis`, `com.feryaeljustice.mirailink.domain.util.millisToBackendDate`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/GenderSelector.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/GenderSelector.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/GenderSelector.kt).

Declaraciones: `GenderSelector`.

Dependencias importadas: `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.stringResource`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.model.enum.Gender`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/HashtagChip.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/HashtagChip.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/HashtagChip.kt).

Declaraciones: `HashtagChip`, `TagsSection`.

Dependencias importadas: `androidx.compose.foundation.background`, `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.FlowRow`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkDialog.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkDialog.kt).

Declaraciones: `MiraiLinkDialog`.

Dependencias importadas: `androidx.compose.animation.AnimatedVisibility`, `androidx.compose.material3.AlertDialog`, `androidx.compose.material3.AlertDialogDefaults`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.derivedStateOf`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.remember`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.style.TextAlign`, `androidx.compose.ui.window.DialogProperties`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/appconfig/UpdateGate.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/appconfig/UpdateGate.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorPutCodeOrRecoveryCDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorPutCodeOrRecoveryCDialog.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupCompletedDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupCompletedDialog.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupDialog.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorStatusDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorStatusDialog.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkErrorContent.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkErrorContent.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkErrorContent.kt).

Declaraciones: `UiText.asString`, `MiraiLinkErrorContent`.

Dependencias importadas: `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.padding`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.style.TextAlign`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.UiText`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkSimpleDropdown.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkSimpleDropdown.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkSimpleDropdown.kt).

Declaraciones: `MiraiLinkSimpleDropdown`.

Dependencias importadas: `androidx.compose.material3.DropdownMenuItem`, `androidx.compose.material3.ExperimentalMaterial3Api`, `androidx.compose.material3.ExposedDropdownMenuAnchorType`, `androidx.compose.material3.ExposedDropdownMenuBox`, `androidx.compose.material3.ExposedDropdownMenuDefaults`, `androidx.compose.material3.OutlinedTextField`, `androidx.compose.material3.Text`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Modifier`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkSnackbar.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkSnackbar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkSnackbar.kt).

Declaraciones: `MiraiLinkSnackbarRequest`, `MiraiLinkSnackbar`, `MiraiLinkErrorSnackbar`.

Dependencias importadas: `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.utils.composition.LocalShowSnackbar`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/utils/composition/CompositionProviderUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/utils/composition/CompositionProviderUtils.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MultiSelectDropdown.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MultiSelectDropdown.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MultiSelectDropdown.kt).

Declaraciones: `MultiSelectOption`, `MultiSelectDropdown`.

Dependencias importadas: `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.FlowRow`, `androidx.compose.foundation.layout.fillMaxHeight`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.layout.wrapContentHeight`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.ArrowDropDown`, `androidx.compose.material.icons.filled.Check`, `androidx.compose.material3.AssistChip`, `androidx.compose.material3.AssistChipDefaults`, `androidx.compose.material3.DropdownMenu`, `androidx.compose.material3.DropdownMenuItem`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.geometry.Size`, `androidx.compose.ui.layout.onGloballyPositioned`, `androidx.compose.ui.platform.LocalDensity`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.dp`, `androidx.compose.ui.unit.toSize`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/QrCodeImage.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/QrCodeImage.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/QrCodeImage.kt).

Declaraciones: `QrCodeImage`.

Dependencias importadas: `android.annotation.SuppressLint`, `androidx.compose.foundation.Image`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.remember`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.graphics.asImageBitmap`, `androidx.compose.ui.graphics.toArgb`, `androidx.compose.ui.unit.Dp`, `androidx.compose.ui.unit.dp`, `androidx.core.graphics.createBitmap`, `com.google.zxing.BarcodeFormat`, `com.google.zxing.qrcode.QRCodeWriter`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupDialog.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/ResidenceSelector.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/ResidenceSelector.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/ResidenceSelector.kt).

Declaraciones: `ResidenceSelector`, `ResidenceAutocompleteField`, `String.normalizeResidenceSearch`, `CityAutocompleteField`, `MiraiLinkResult<List<GeographicPlace>>.orEmpty`.

Dependencias importadas: `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Clear`, `androidx.compose.material3.DropdownMenuItem`, `androidx.compose.material3.ExperimentalMaterial3Api`, `androidx.compose.material3.ExposedDropdownMenuAnchorType`, `androidx.compose.material3.ExposedDropdownMenuBox`, `androidx.compose.material3.ExposedDropdownMenuDefaults`, `androidx.compose.material3.Icon`, `androidx.compose.material3.IconButton`, `androidx.compose.material3.OutlinedTextField`, `androidx.compose.material3.Text`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.produceState`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.enums.TextFieldType`, `com.feryaeljustice.mirailink.domain.model.geography.GeographicPlace`, `com.feryaeljustice.mirailink.domain.repository.CatalogRepository`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `org.koin.compose.koinInject`, `java.text.Normalizer`, `java.util.Locale`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/ThemeSwitcher.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/ThemeSwitcher.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/ThemeSwitcher.kt).

Declaraciones: `ThemeSwitcher`.

Dependencias importadas: `androidx.compose.animation.core.animateFloatAsState`, `androidx.compose.animation.core.tween`, `androidx.compose.foundation.background`, `androidx.compose.foundation.border`, `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.BiasAlignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.graphics.Shape`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.Dp`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/MiraiLinkTopBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/MiraiLinkTopBar.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/notifications/NotificationRationaleDialog.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/notifications/NotificationRationaleDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/notifications/NotificationRationaleDialog.kt).

Declaraciones: `NotificationRationaleDialog`.

Dependencias importadas: `androidx.compose.material3.AlertDialog`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.stringResource`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextButton`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/MiraiLinkAppRoot.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/MiraiLinkAppRoot.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/ChatTopBar.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/ChatTopBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/ChatTopBar.kt).

Declaraciones: `ChatTopBar`.

Dependencias importadas: `androidx.compose.foundation.border`, `androidx.compose.foundation.gestures.detectTapGestures`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material3.Icon`, `androidx.compose.material3.IconButtonDefaults`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.rememberUpdatedState`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.input.pointer.pointerInput`, `androidx.compose.ui.layout.ContentScale`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.unit.dp`, `coil.compose.AsyncImage`, `coil.request.ImageRequest`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.util.superCapitalize`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/MiraiLinkTopBar.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/MiraiLinkTopBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/MiraiLinkTopBar.kt).

Declaraciones: `TopBarLayoutDirection`, `MiraiLinkTopBar`, `TopBarConfig`.

Dependencias importadas: `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.size`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Settings`, `androidx.compose.material3.ExperimentalMaterial3Api`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.TopAppBar`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.layout.ContentScale`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkImage`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.molecules.ThemeSwitcher`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorPutCodeOrRecoveryCDialog.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorPutCodeOrRecoveryCDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorPutCodeOrRecoveryCDialog.kt).

Declaraciones: `TwoFactorPutCodeOrRecoveryCDialog`.

Dependencias importadas: `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupCompletedDialog.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupCompletedDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupCompletedDialog.kt).

Declaraciones: `TwoFactorSetupCompletedDialog`.

Dependencias importadas: `android.content.ClipData`, `android.widget.Toast`, `androidx.compose.foundation.background`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.rememberScrollState`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.foundation.verticalScroll`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.CheckCircle`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.OutlinedButton`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.rememberCoroutineScope`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.platform.LocalClipboard`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.platform.toClipEntry`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontFamily`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog`, `kotlinx.coroutines.launch`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupDialog.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupDialog.kt).

Declaraciones: `TwoFactorSetupDialog`.

Dependencias importadas: `android.content.ClipData`, `android.widget.Toast`, `androidx.compose.foundation.gestures.detectTapGestures`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.rememberCoroutineScope`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.input.pointer.pointerInput`, `androidx.compose.ui.platform.LocalClipboard`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.platform.toClipEntry`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog`, `com.feryaeljustice.mirailink.ui.components.molecules.QrCodeImage`, `kotlinx.coroutines.launch`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorStatusDialog.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorStatusDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorStatusDialog.kt).

Declaraciones: `TwoFactorStatusDialog`.

Dependencias importadas: `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.padding`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.CheckCircle`, `androidx.compose.material.icons.filled.Close`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/GamerPromptComponents.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/GamerPromptComponents.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/GamerPromptComponents.kt).

Declaraciones: `GamerPromptCard`, `ChipFlowRow`, `resolveLanguageFlag`, `PersonalCategoryData`, `buildCategorizedPersonalInfo`, `buildPersonalChips`, `CategorizedPersonalInfoSection`, `GamerPromptEditSection`.

Dependencias importadas: `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.ExperimentalLayoutApi`, `androidx.compose.foundation.layout.FlowRow`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxHeight`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.imePadding`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.lazy.LazyColumn`, `androidx.compose.foundation.lazy.items`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Add`, `androidx.compose.material.icons.filled.Close`, `androidx.compose.material.icons.filled.Delete`, `androidx.compose.material.icons.filled.Edit`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.ExperimentalMaterial3Api`, `androidx.compose.material3.Icon`, `androidx.compose.material3.IconButton`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.ModalBottomSheet`, `androidx.compose.material3.OutlinedButton`, `androidx.compose.material3.OutlinedTextField`, `androidx.compose.material3.Surface`, `androidx.compose.material3.TextButton`, `androidx.compose.material3.rememberModalBottomSheetState`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.text.style.TextAlign`, `androidx.compose.ui.text.style.TextOverflow`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.data.model.response.catalog.CatalogItemOptionDto`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.viewentries.user.GamerPromptAnswerViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt).

Declaraciones: `PublicUserCard`, `PublicResidence`, `PublicSectionHeader`, `PublicInfoLine`, `OutlinedOverlayText`.

Dependencias importadas: `androidx.compose.foundation.background`, `androidx.compose.foundation.gestures.detectTapGestures`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.BoxWithConstraints`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.rememberScrollState`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.foundation.verticalScroll`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Favorite`, `androidx.compose.material.icons.filled.Info`, `androidx.compose.material.icons.filled.LocationOn`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Text`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.remember`, `com.feryaeljustice.mirailink.ui.components.catalog.InterestsGrid`, `com.feryaeljustice.mirailink.ui.components.catalog.toInterestItemData`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.graphics.Brush`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.graphics.drawscope.Stroke`, `androidx.compose.ui.graphics.luminance`, `androidx.compose.ui.input.pointer.pointerInput`, `androidx.compose.ui.platform.testTag`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.semantics.clearAndSetSemantics`, `androidx.compose.ui.text.TextStyle`, `androidx.compose.ui.text.font.FontStyle`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.model.enum.Gender`, `com.feryaeljustice.mirailink.domain.util.GeoUtils`, `com.feryaeljustice.mirailink.domain.util.nicknameElseUsername`, `com.feryaeljustice.mirailink.domain.util.toAgeOrNull`, `com.feryaeljustice.mirailink.ui.components.media.PhotoCarousel`, `com.feryaeljustice.mirailink.ui.components.media.PhotoCarouselController`, `com.feryaeljustice.mirailink.ui.utils.extensions.localizedLabel`, `com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt).

Declaraciones: `UserCard`, `ProfileReadOnlySectionHeader`, `ProfileAttributeSelectRow`, `getSingleOptionLabel`, `getMultiOptionLabels`, `UserCardPreview`.

Dependencias importadas: `androidx.compose.foundation.BorderStroke`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.rememberScrollState`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.foundation.text.KeyboardActions`, `androidx.compose.foundation.text.KeyboardOptions`, `androidx.compose.foundation.verticalScroll`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Close`, `androidx.compose.material.icons.filled.Edit`, `androidx.compose.material.icons.filled.Favorite`, `androidx.compose.material.icons.filled.Info`, `androidx.compose.material.icons.filled.LocationOn`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.Icon`, `androidx.compose.material3.IconButtonDefaults`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.alpha`, `androidx.compose.ui.focus.FocusRequester`, `androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1`, `androidx.compose.ui.focus.focusRequester`, `androidx.compose.ui.platform.testTag`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontStyle`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.text.input.ImeAction`, `androidx.compose.ui.text.style.TextDecoration`, `androidx.compose.ui.tooling.preview.Preview`, `androidx.compose.ui.unit.dp`, `androidx.compose.ui.zIndex`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.data.model.response.catalog.CatalogItemOptionDto`, `com.feryaeljustice.mirailink.ui.components.catalog.ProfileMultiOptionPickerModal`, `com.feryaeljustice.mirailink.ui.components.catalog.ProfileSingleOptionPickerModal`, `com.feryaeljustice.mirailink.ui.screens.profile.edit.ProfileMultiAttributeType`, `com.feryaeljustice.mirailink.ui.screens.profile.edit.ProfileSingleAttributeType`, `com.feryaeljustice.mirailink.ui.components.user.GamerPromptCard`, `com.feryaeljustice.mirailink.ui.components.user.GamerPromptEditSection`, `com.feryaeljustice.mirailink.ui.components.user.ChipFlowRow`, `com.feryaeljustice.mirailink.ui.components.user.buildPersonalChips`, `com.feryaeljustice.mirailink.ui.components.user.buildCategorizedPersonalInfo`, `com.feryaeljustice.mirailink.ui.components.user.CategorizedPersonalInfoSection`, `androidx.compose.material3.Surface`, `com.feryaeljustice.mirailink.domain.enums.TagType`, `com.feryaeljustice.mirailink.domain.enums.TextFieldType`, `com.feryaeljustice.mirailink.domain.model.enum.Gender`, `com.feryaeljustice.mirailink.domain.model.geography.GeographicPlace`, `com.feryaeljustice.mirailink.domain.util.nicknameElseUsername`, `com.feryaeljustice.mirailink.domain.util.toAgeOrNull`, `com.feryaeljustice.mirailink.domain.util.toBackendDate`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedIconButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.catalog.InterestsGrid`, `com.feryaeljustice.mirailink.ui.components.catalog.VisualInterestPickerModal`, `com.feryaeljustice.mirailink.ui.components.catalog.toInterestItemData`, `com.feryaeljustice.mirailink.ui.components.media.EditablePhotoGrid`, `com.feryaeljustice.mirailink.ui.components.media.FullscreenImagePreview`, `com.feryaeljustice.mirailink.ui.components.media.PhotoCarousel`, `com.feryaeljustice.mirailink.ui.components.molecules.BirthdateField`, `com.feryaeljustice.mirailink.ui.components.molecules.GenderSelector`, `com.feryaeljustice.mirailink.ui.components.molecules.ResidenceSelector`, `com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileUiState`, `com.feryaeljustice.mirailink.ui.utils.extensions.localizedLabel`, `com.feryaeljustice.mirailink.ui.utils.extensions.shadow`, `com.feryaeljustice.mirailink.ui.viewentries.catalog.AnimeViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.catalog.GameViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt).

Declaraciones: `SwipeDirection`, `UserSwipeCardStack`, `settleCard`, `completeSwipe`, `SwipeActionButtons`, `SwipeActionButton`.

Dependencias importadas: `androidx.compose.animation.animateColorAsState`, `androidx.compose.animation.core.Animatable`, `androidx.compose.animation.core.animateFloatAsState`, `androidx.compose.animation.core.spring`, `androidx.compose.foundation.background`, `androidx.compose.foundation.border`, `androidx.compose.foundation.gestures.detectDragGestures`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Close`, `androidx.compose.material.icons.filled.Favorite`, `androidx.compose.material.icons.filled.Refresh`, `androidx.compose.material3.Icon`, `androidx.compose.material3.IconButton`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.ui.graphics.luminance`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.key`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.rememberCoroutineScope`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.mutableFloatStateOf`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.alpha`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.draw.shadow`, `androidx.compose.ui.zIndex`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.graphics.graphicsLayer`, `androidx.compose.ui.input.pointer.pointerInput`, `androidx.compose.ui.platform.testTag`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.data.mappers.ui.toUserViewEntry`, `com.feryaeljustice.mirailink.domain.usecase.haptics.CalculateHeartbeatAffinityUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.ui.components.haptics.HapticHeartbeatOverlay`, `com.feryaeljustice.mirailink.ui.components.haptics.hapticHeartbeatLikeTrigger`, `com.feryaeljustice.mirailink.ui.haptics.HapticHeartbeatController`, `com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry`, `kotlinx.coroutines.launch`, `org.koin.compose.koinInject`, `kotlin.math.abs`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/error/AppErrorUiMapper.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/error/AppErrorUiMapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/error/AppErrorUiMapper.kt).

Declaraciones: `AppError.toUiError`, `AppError.messageResource`.

Dependencias importadas: `androidx.annotation.StringRes`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.error.AppError`, `com.feryaeljustice.mirailink.domain.error.AuthError`, `com.feryaeljustice.mirailink.domain.error.DataError`, `com.feryaeljustice.mirailink.domain.error.UnknownError`, `com.feryaeljustice.mirailink.domain.error.ValidationError`, `com.feryaeljustice.mirailink.domain.error.LocationError`, `com.feryaeljustice.mirailink.domain.error.SubscriptionError`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/error/RetryableViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/error/RetryableViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/error/RetryableViewModel.kt).

Declaraciones: `RetryableViewModel`, `setRecoveryAction`, `performErrorAction`, `onCleared`.

Dependencias importadas: `androidx.lifecycle.ViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/error/UiError.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/error/UiError.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/error/UiError.kt).

Declaraciones: `UiText`, `Resource`, `ErrorRecovery`, `UiError`, `UiText.asString`, `UiError.asString`.

Dependencias importadas: `androidx.annotation.StringRes`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkErrorContent.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkErrorContent.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkSnackbar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkSnackbar.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatUiState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatUiState.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/haptics/HapticHeartbeatController.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/haptics/HapticHeartbeatController.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/haptics/HapticHeartbeatController.kt).

Declaraciones: `HapticHeartbeatController`, `startHeartbeat`, `stopHeartbeat`, `triggerLikeConfirmation`, `HapticHeartbeatControllerImpl`.

Dependencias importadas: `android.content.Context`, `android.os.Build`, `android.os.VibrationEffect`, `android.os.Vibrator`, `android.os.VibratorManager`, `androidx.annotation.VisibleForTesting`, `kotlin.math.roundToInt`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/AppModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AppScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AppScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AppScreen.kt).

Declaraciones: `ScreensSubgraphs`, `Auth`, `Main`, `AppScreen`, `SplashScreen`, `OnboardingScreen`, `AuthScreen`, `RecoverPasswordScreen`, `VerificationScreen`, `ProfilePictureScreen`, `HomeScreen`, `ExploreScreen`, `CategoryFeedScreen`, `MessagesScreen`, `ReceivedLikesScreen`, `UserProfileDetailScreen`, `ChatScreen`, `AiChatScreen`, `SettingsScreen`, `SearchPreferencesScreen`, `ProfileScreen`, `FeedbackScreen`, `FaqScreen`, `SubscriptionPaywallScreen`, `SubscriptionManageScreen`, `MiraiStudioScreen`, `AppScreen.topLevelTab`.

Dependencias importadas: `androidx.navigation3.runtime.NavKey`, `kotlinx.serialization.SerialName`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AuthenticatedNavigationPolicy.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AuthenticatedNavigationPolicy.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AuthenticatedNavigationPolicy.kt).

Declaraciones: `shouldResetAuthenticatedNavigation`.

Dependencias importadas: `androidx.navigation3.runtime.NavKey`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/BottomNavItem.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/BottomNavItem.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/BottomNavItem.kt).

Declaraciones: `BottomNavItem`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/InitialNavigationAction.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/InitialNavigationAction.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/InitialNavigationAction.kt).

Declaraciones: `InitialNavigationAction`, `GoToHome`, `GoToAuth`, `GoToOnboarding`.

Dependencias importadas: Ninguna detectada.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavAnalyticsViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavAnalyticsViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavAnalyticsViewModel.kt).

Declaraciones: `NavAnalyticsViewModel`, `logScreen`, `logDeepLink`.

Dependencias importadas: `androidx.lifecycle.ViewModel`, `com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker`, `org.koin.core.annotation.KoinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

Declaraciones: `NavWrapper`, `NavKey.debugRouteName`.

Dependencias importadas: `android.content.ClipData`, `android.widget.Toast`, `androidx.compose.animation.core.tween`, `androidx.compose.animation.slideInHorizontally`, `androidx.compose.animation.slideOutHorizontally`, `androidx.compose.animation.togetherWith`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.imePadding`, `androidx.compose.foundation.layout.padding`, `androidx.compose.material3.Scaffold`, `androidx.compose.material3.SnackbarDuration`, `androidx.compose.material3.SnackbarHost`, `androidx.compose.material3.SnackbarHostState`, `androidx.compose.material3.SnackbarResult`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.CompositionLocalProvider`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.rememberCoroutineScope`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.platform.LocalClipboard`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.platform.toClipEntry`, `androidx.compose.ui.res.stringResource`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `androidx.navigation3.runtime.NavKey`, `androidx.navigation3.runtime.entryProvider`, `androidx.navigation3.scene.DialogSceneStrategy`, `androidx.navigation3.ui.NavDisplay`, `com.feryaeljustice.mirailink.BuildConfig`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.constants.deepLinkBaseUrl`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkPrefs`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.components.appconfig.UpdateGate`, `com.feryaeljustice.mirailink.ui.components.bottombars.MiraiLinkBottomBar`, `com.feryaeljustice.mirailink.ui.components.demo.DemoModeBanner`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkSnackbarRequest`, `com.feryaeljustice.mirailink.ui.components.topbars.MiraiLinkTopBar`, `com.feryaeljustice.mirailink.ui.components.topbars.TopBarLayoutDirection`, `com.feryaeljustice.mirailink.ui.screens.ai.chat.AiChatScreen`, `com.feryaeljustice.mirailink.ui.screens.auth.AuthScreen`, `com.feryaeljustice.mirailink.ui.screens.auth.recover.RecoverPasswordScreen`, `com.feryaeljustice.mirailink.ui.screens.auth.verification.VerificationScreen`, `com.feryaeljustice.mirailink.ui.screens.chat.ChatScreen`, `com.feryaeljustice.mirailink.ui.screens.explore.ExploreScreen`, `com.feryaeljustice.mirailink.ui.screens.explore.feed.CategoryFeedScreen`, `com.feryaeljustice.mirailink.ui.screens.explore.feed.CategoryFeedViewModel`, `com.feryaeljustice.mirailink.ui.screens.home.HomeScreen`, `com.feryaeljustice.mirailink.ui.screens.home.search.SearchPreferencesScreen`, `com.feryaeljustice.mirailink.ui.screens.likes.ReceivedLikesScreen`, `com.feryaeljustice.mirailink.ui.screens.messages.MessagesScreen`, `com.feryaeljustice.mirailink.ui.screens.onboarding.OnboardingScreen`, `com.feryaeljustice.mirailink.ui.screens.photo.ProfilePictureScreen`, `com.feryaeljustice.mirailink.ui.screens.profile.ProfileScreen`, `com.feryaeljustice.mirailink.ui.screens.profile.detail.UserProfileDetailScreen`, `com.feryaeljustice.mirailink.ui.screens.settings.SettingsScreen`, `com.feryaeljustice.mirailink.ui.screens.settings.feedback.FeedbackScreen`, `com.feryaeljustice.mirailink.ui.screens.splash.SplashScreen`, `com.feryaeljustice.mirailink.ui.screens.subscription.SubscriptionManageScreen`, `com.feryaeljustice.mirailink.ui.screens.subscription.SubscriptionPaywallScreen`, `com.feryaeljustice.mirailink.ui.utils.composition.LocalShowSnackbar`, `com.feryaeljustice.mirailink.ui.utils.extensions.openPlayStore`, `com.feryaeljustice.mirailink.ui.utils.toast.showToast`, `kotlinx.coroutines.flow.collectLatest`, `kotlinx.coroutines.launch`, `org.koin.androidx.compose.koinViewModel`, `org.koin.compose.koinInject`, `org.koin.core.parameter.parametersOf`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/MiraiLinkAppRoot.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/MiraiLinkAppRoot.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavigationState.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavigationState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavigationState.kt).

Declaraciones: `rememberNavigationState`, `NavigationState`, `currentKey`, `NavigationState.toEntries`.

Dependencias importadas: `androidx.compose.runtime.Composable`, `androidx.compose.runtime.MutableState`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.saveable.rememberSerializable`, `androidx.compose.runtime.setValue`, `androidx.compose.runtime.snapshots.SnapshotStateList`, `androidx.compose.runtime.toMutableStateList`, `androidx.navigation3.runtime.NavBackStack`, `androidx.navigation3.runtime.NavEntry`, `androidx.navigation3.runtime.NavKey`, `androidx.navigation3.runtime.rememberDecoratedNavEntries`, `androidx.navigation3.runtime.rememberNavBackStack`, `androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator`, `androidx.navigation3.runtime.serialization.NavKeySerializer`, `androidx.savedstate.compose.serialization.serializers.MutableStateSerializer`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/Navigator.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/Navigator.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/Navigator.kt).

Declaraciones: `Navigator`, `navigate`, `goBack`, `resetToTopLevel`.

Dependencias importadas: `androidx.navigation3.runtime.NavKey`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatScreen.kt).

Declaraciones: `AiChatScreen`, `submitPrompt`, `AiMessageBubble`.

Dependencias importadas: `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.lazy.LazyColumn`, `androidx.compose.foundation.lazy.itemsIndexed`, `androidx.compose.foundation.lazy.rememberLazyListState`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.automirrored.filled.Send`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.material3.Icon`, `androidx.compose.material3.IconButton`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.OutlinedTextField`, `androidx.compose.material3.Text`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.saveable.rememberSaveable`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.text.input.ImeAction`, `androidx.compose.ui.text.input.TextFieldValue`, `androidx.compose.ui.unit.dp`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.chat.emoji.EmojiPickerButton`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent`, `org.koin.androidx.compose.koinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatUiState.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatUiState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatUiState.kt).

Declaraciones: `AiChatUiState`, `Idle`, `Loading`, `Success`, `Error`, `AiChatMessage`.

Dependencias importadas: `com.feryaeljustice.mirailink.ui.error.UiError`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/ai/chat/AiChatViewModel.kt).

Declaraciones: `AiChatViewModel`, `sendMessage`.

Dependencias importadas: `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.domain.usecase.ai.GenerateContentUseCase`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.ui.error.RetryableViewModel`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asStateFlow`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`, `org.koin.core.annotation.KoinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt).

Declaraciones: `AuthScreen`, `resetAuthUiState`, `mapErrorToString`.

Dependencias importadas: `androidx.compose.animation.AnimatedContent`, `androidx.compose.animation.fadeIn`, `androidx.compose.animation.fadeOut`, `androidx.compose.animation.togetherWith`, `androidx.compose.foundation.Image`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.displayCutout`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.windowInsetsPadding`, `androidx.compose.foundation.rememberScrollState`, `androidx.compose.foundation.text.KeyboardActions`, `androidx.compose.foundation.text.KeyboardOptions`, `androidx.compose.foundation.verticalScroll`, `androidx.compose.material3.Icon`, `androidx.compose.material3.IconButton`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.FilterChip`, `androidx.compose.material3.FilterChipDefaults`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.focus.FocusDirection`, `androidx.compose.ui.platform.LocalFocusManager`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.input.ImeAction`, `androidx.compose.ui.text.input.KeyboardType`, `androidx.compose.ui.text.input.PasswordVisualTransformation`, `androidx.compose.ui.text.input.VisualTransformation`, `androidx.compose.ui.unit.dp`, `androidx.compose.foundation.border`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.util.isEmailValid`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkScreenContent`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextButton`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent`, `com.feryaeljustice.mirailink.ui.components.twofactor.TwoFactorPutCodeOrRecoveryCDialog`, `com.feryaeljustice.mirailink.ui.screens.auth.AuthViewModel.AuthEvent`, `com.feryaeljustice.mirailink.ui.screens.auth.AuthViewModel.AuthUiState`, `com.feryaeljustice.mirailink.ui.screens.auth.verification.VerificationDialog`, `androidx.compose.ui.platform.LocalContext`, `com.feryaeljustice.mirailink.domain.model.enum.Gender`, `com.feryaeljustice.mirailink.domain.util.isAtLeast16YearsOld`, `com.feryaeljustice.mirailink.ui.components.molecules.BirthdateField`, `com.feryaeljustice.mirailink.ui.components.molecules.GenderSelector`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkSnackbarRequest`, `com.feryaeljustice.mirailink.ui.utils.composition.LocalShowSnackbar`, `com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration`, `com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding`, `org.koin.compose.viewmodel.koinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthViewModel.kt).

Declaraciones: `AuthViewModel`, `AuthUiState`, `Idle`, `Loading`, `Success`, `IsAuthenticated`, `VerificationRequired`, `Error`, `AuthFieldError`, `MinLength`, `InvalidEmail`, `InvalidPassword`, `TrivialPassword`, `PasswordsDoNotMatch`, `AuthEvent`, `SwitchToLoginAndRetry`, `resetUsernameError`, `resetEmailError`, `resetPasswordError`, `resetConfirmPasswordError`, `autofillCredentials`, `toggleLoginBy`, `login`, `register`, `handleAuthSession`, `completeAuth`, `completePendingVerification`, `cancelPendingVerification`, `dismissTwoFactorDiag`, `confirmTwoFactorDiag`, `onCodeChangeTwoFactorDiag`, `resetScreenVMState`, `configureRecovery`, `resetTwoFaDiag`, `onLoginSuccess`, `onLoginError`, `validateFields`, `enterDemoMode`.

Dependencias importadas: `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.data.datastore.SessionManager`, `com.feryaeljustice.mirailink.data.mappers.toAuthSessionInfo`, `com.feryaeljustice.mirailink.domain.model.auth.AuthSessionInfo`, `com.feryaeljustice.mirailink.domain.error.AppError`, `com.feryaeljustice.mirailink.domain.error.AuthError`, `com.feryaeljustice.mirailink.domain.error.UnknownError`, `com.feryaeljustice.mirailink.domain.error.ValidationError`, `com.feryaeljustice.mirailink.domain.core.JwtUtils.extractUserId`, `com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker`, `com.feryaeljustice.mirailink.domain.telemetry.CrashReporter`, `com.feryaeljustice.mirailink.domain.usecase.auth.LoginUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.RegisterUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.CheckIsVerifiedUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.GetTwoFactorStatusUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.LoginVerifyTwoFactorLastStepUseCase`, `com.feryaeljustice.mirailink.domain.util.CredentialHelper`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.domain.util.isAtLeast16YearsOld`, `com.feryaeljustice.mirailink.domain.util.isEmailValid`, `com.feryaeljustice.mirailink.domain.util.isNotTrivialPassword`, `com.feryaeljustice.mirailink.domain.util.isPasswordValid`, `kotlinx.coroutines.CoroutineDispatcher`, `com.feryaeljustice.mirailink.ui.error.RetryableViewModel`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.MutableSharedFlow`, `kotlinx.coroutines.flow.SharedFlow`, `kotlinx.coroutines.flow.asSharedFlow`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt).

Declaraciones: `RecoverPasswordScreen`.

Dependencias importadas: `androidx.compose.foundation.Image`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.displayCutout`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.windowInsetsPadding`, `androidx.compose.foundation.rememberScrollState`, `androidx.compose.foundation.verticalScroll`, `androidx.compose.material3.Icon`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.input.PasswordVisualTransformation`, `androidx.compose.ui.unit.dp`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent`, `com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration`, `com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding`, `org.koin.compose.viewmodel.koinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordViewModel.kt).

Declaraciones: `RecoverPasswordViewModel`, `PasswordResetState`, `initEmail`, `initToken`, `onEmailChanged`, `onTokenChanged`, `onPasswordChanged`, `onConfirmPasswordChanged`, `requestReset`, `confirmReset`, `dismissSuccessDialog`, `resetState`.

Dependencias importadas: `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.usecase.users.ConfirmPasswordResetUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.RequestPasswordResetUseCase`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.ui.error.ErrorRecovery`, `com.feryaeljustice.mirailink.ui.error.RetryableViewModel`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.UiText`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.Job`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.update`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`, `org.koin.core.annotation.KoinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt).

Declaraciones: `VerificationScreen`, `VerificationDialog`.

Dependencias importadas: `androidx.compose.foundation.Image`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.displayCutout`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.windowInsetsPadding`, `androidx.compose.foundation.rememberScrollState`, `androidx.compose.foundation.verticalScroll`, `androidx.compose.material3.AlertDialog`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.TextButton`, `androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.dp`, `androidx.compose.ui.window.DialogProperties`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent`, `com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration`, `com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding`, `org.koin.compose.viewmodel.koinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationViewModel.kt).

Declaraciones: `VerificationViewModel`, `VerificationState`, `init`, `startCooldown`, `onTokenChanged`, `checkUserIsVerified`, `requestCode`, `confirmCode`, `resetState`.

Dependencias importadas: `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.domain.usecase.auth.CheckIsVerifiedUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.ConfirmVerificationCodeUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.RequestVerificationCodeUseCase`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.ui.error.RetryableViewModel`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.Job`, `kotlinx.coroutines.delay`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.update`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`, `org.koin.core.annotation.KoinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt).

Declaraciones: `ChatItemModel`, `MessageItemModel`, `DateSeparatorItemModel`, `ChatScreen`.

Dependencias importadas: `androidx.compose.animation.AnimatedVisibility`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.displayCutout`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.layout.windowInsetsPadding`, `androidx.compose.foundation.lazy.LazyColumn`, `androidx.compose.foundation.lazy.items`, `androidx.compose.foundation.lazy.rememberLazyListState`, `androidx.compose.foundation.text.KeyboardActions`, `androidx.compose.foundation.text.KeyboardOptions`, `androidx.compose.material3.AlertDialog`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.DisposableEffect`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.derivedStateOf`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.saveable.rememberSaveable`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringArrayResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.input.ImeAction`, `androidx.compose.ui.text.input.TextFieldValue`, `androidx.compose.ui.unit.dp`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.util.formatDateSeparator`, `com.feryaeljustice.mirailink.domain.util.getFormattedUrl`, `com.feryaeljustice.mirailink.domain.util.nicknameElseUsername`, `com.feryaeljustice.mirailink.domain.util.superCapitalize`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextField`, `com.feryaeljustice.mirailink.ui.components.chat.DateSeparator`, `com.feryaeljustice.mirailink.ui.components.chat.MessageItem`, `com.feryaeljustice.mirailink.ui.components.chat.emoji.EmojiPickerButton`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent`, `com.feryaeljustice.mirailink.ui.components.media.FullscreenImagePreview`, `com.feryaeljustice.mirailink.ui.components.topbars.ChatTopBar`, `com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration`, `com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding`, `com.feryaeljustice.mirailink.ui.viewentries.chat.ChatMessageViewEntry`, `org.koin.compose.viewmodel.koinViewModel`, `java.time.Instant`, `java.time.ZoneId`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AppScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AppScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt).

Declaraciones: `ChatViewModel`, `CHATTYPE`, `initChat`, `initPrivateChat`, `initGroupChat`, `proceedWithPrivateChatSetup`, `markChatAsRead`, `setSenderSync`, `setReceiverSync`, `startMessagePolling`, `startGroupMessagesPolling`, `stopMessagePolling`, `getMessages`, `sendMessage`, `reportUser`, `showError`, `resetChatState`.

Dependencias importadas: `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.data.mappers.toMinimalUserInfo`, `com.feryaeljustice.mirailink.data.mappers.ui.toChatMessageViewEntry`, `com.feryaeljustice.mirailink.data.mappers.ui.toMinimalUserInfoViewEntry`, `com.feryaeljustice.mirailink.domain.error.AppError`, `com.feryaeljustice.mirailink.domain.usecase.chat.CreateGroupChatUseCase`, `com.feryaeljustice.mirailink.domain.usecase.chat.CreatePrivateChatUseCase`, `com.feryaeljustice.mirailink.domain.usecase.chat.GetChatMessagesUseCase`, `com.feryaeljustice.mirailink.domain.usecase.chat.MarkChatAsReadUseCase`, `com.feryaeljustice.mirailink.domain.usecase.chat.SendMessageUseCase`, `com.feryaeljustice.mirailink.domain.usecase.report.ReportUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.GetUserByIdUseCase`, `com.feryaeljustice.mirailink.domain.util.Logger`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.ui.error.RetryableViewModel`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `com.feryaeljustice.mirailink.ui.viewentries.chat.ChatMessageViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.user.MinimalUserInfoViewEntry`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.Job`, `kotlinx.coroutines.delay`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.update`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`, `org.koin.core.annotation.KoinViewModel`, `java.util.UUID`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreScreen.kt).

Declaraciones: `ExploreScreen`.

Dependencias importadas: `androidx.compose.animation.AnimatedContent`, `androidx.compose.animation.fadeIn`, `androidx.compose.animation.fadeOut`, `androidx.compose.animation.scaleIn`, `androidx.compose.animation.scaleOut`, `androidx.compose.animation.togetherWith`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.PaddingValues`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.displayCutout`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.windowInsetsPadding`, `androidx.compose.foundation.lazy.LazyColumn`, `androidx.compose.foundation.lazy.LazyRow`, `androidx.compose.foundation.lazy.items`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.material3.ExperimentalMaterial3Api`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2`, `androidx.compose.material3.pulltorefresh.PullToRefreshBox`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.platform.testTag`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.unit.dp`, `androidx.lifecycle.compose.LifecycleResumeEffect`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.explore.CategoryCarouselCard`, `com.feryaeljustice.mirailink.ui.components.explore.CategoryGridCard`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent`, `com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration`, `com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding`, `org.koin.compose.viewmodel.koinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AppScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AppScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreViewModel.kt).

Declaraciones: `ExploreViewModel`, `ExploreUiState`, `Idle`, `Loading`, `Success`, `Error`, `loadExploreHub`.

Dependencias importadas: `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.domain.repository.ExploreHubData`, `com.feryaeljustice.mirailink.domain.usecase.explore.GetExploreSectionsUseCase`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.ui.error.RetryableViewModel`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asStateFlow`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`, `org.koin.core.annotation.KoinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryDiscoverySettingsSheet.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryDiscoverySettingsSheet.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryDiscoverySettingsSheet.kt).

Declaraciones: `CategoryDiscoverySettingsSheet`.

Dependencias importadas: `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.material3.ExperimentalMaterial3Api`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.ModalBottomSheet`, `androidx.compose.material3.Slider`, `androidx.compose.material3.rememberModalBottomSheetState`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableFloatStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `kotlin.math.roundToInt`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedScreen.kt).

Declaraciones: `CategoryFeedScreen`.

Dependencias importadas: `android.widget.Toast`, `androidx.compose.animation.AnimatedContent`, `androidx.compose.animation.fadeIn`, `androidx.compose.animation.fadeOut`, `androidx.compose.animation.scaleIn`, `androidx.compose.animation.scaleOut`, `androidx.compose.animation.togetherWith`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.displayCutout`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.windowInsetsPadding`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.automirrored.filled.ArrowBack`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.material3.ExperimentalMaterial3Api`, `androidx.compose.material3.Icon`, `androidx.compose.material3.IconButton`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2`, `androidx.compose.material3.pulltorefresh.PullToRefreshBox`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.platform.testTag`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.text.style.TextAlign`, `androidx.compose.ui.unit.dp`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent`, `com.feryaeljustice.mirailink.ui.components.user.UserSwipeCardStack`, `com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration`, `com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding`, `com.feryaeljustice.mirailink.ui.utils.toast.showToast`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AppScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AppScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt).

Declaraciones: `CategoryFeedViewModel`, `CategoryFeedEvent`, `NavigateToPaywall`, `CategoryFeedUiState`, `Idle`, `Loading`, `Success`, `Empty`, `Error`, `openSettingsSheet`, `closeSettingsSheet`, `loadPreferences`, `loadFeed`, `updateRadius`, `swipeRight`, `swipeLeft`, `safeRemoveFirst`, `saveToHistory`, `canUndo`, `undoSwipe`, `updateUiState`.

Dependencias importadas: `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.data.mappers.ui.toUserViewEntry`, `com.feryaeljustice.mirailink.domain.constants.TIME_24_HOURS`, `com.feryaeljustice.mirailink.domain.usecase.explore.GetCategoryFeedUseCase`, `com.feryaeljustice.mirailink.domain.usecase.explore.GetCategoryPreferencesUseCase`, `com.feryaeljustice.mirailink.domain.usecase.explore.UpdateCategoryPreferencesUseCase`, `com.feryaeljustice.mirailink.domain.usecase.swipe.DislikeUserUseCase`, `com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.ui.error.RetryableViewModel`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `com.feryaeljustice.mirailink.domain.error.SubscriptionError`, `com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.flow.MutableSharedFlow`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.SharedFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asSharedFlow`, `kotlinx.coroutines.flow.asStateFlow`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`, `org.koin.core.annotation.KoinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt).

Declaraciones: `HomeScreen`.

Dependencias importadas: `androidx.compose.animation.AnimatedContent`, `androidx.compose.animation.fadeIn`, `androidx.compose.animation.fadeOut`, `androidx.compose.animation.scaleIn`, `androidx.compose.animation.scaleOut`, `androidx.compose.animation.togetherWith`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.displayCutout`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.windowInsetsPadding`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.material3.ExperimentalMaterial3Api`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2`, `androidx.compose.material3.pulltorefresh.PullToRefreshBox`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.rememberCoroutineScope`, `androidx.compose.runtime.getValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.platform.testTag`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.dp`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `androidx.lifecycle.compose.LifecycleResumeEffect`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent`, `com.feryaeljustice.mirailink.ui.components.user.UserSwipeCardStack`, `com.feryaeljustice.mirailink.ui.screens.home.HomeViewModel.HomeUiState`, `com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration`, `com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding`, `com.feryaeljustice.mirailink.ui.utils.hasForegroundLocationPermission`, `com.feryaeljustice.mirailink.ui.utils.readBestCurrentLocation`, `org.koin.compose.viewmodel.koinViewModel`, `kotlinx.coroutines.launch`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt).

Declaraciones: `HomeViewModel`, `HomeUiState`, `Idle`, `Loading`, `Success`, `Error`, `HomeEvent`, `NavigateToPaywall`, `observeSearchPreferences`, `reload`, `loadCurrentUser`, `loadUsers`, `updateActiveLocation`, `updateUiState`, `swipeRight`, `swipeLeft`, `safeRemoveFirst`, `saveToHistory`, `canUndo`, `undoSwipe`.

Dependencias importadas: `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.data.mappers.ui.toUserViewEntry`, `com.feryaeljustice.mirailink.domain.constants.TIME_24_HOURS`, `com.feryaeljustice.mirailink.domain.usecase.feed.GetFeedUseCase`, `com.feryaeljustice.mirailink.domain.usecase.location.SendLocationPingUseCase`, `com.feryaeljustice.mirailink.domain.usecase.settings.GetSearchPreferencesUseCase`, `com.feryaeljustice.mirailink.domain.usecase.swipe.DislikeUserUseCase`, `com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase`, `com.feryaeljustice.mirailink.domain.error.LocationError`, `com.feryaeljustice.mirailink.domain.model.settings.SearchScope`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.domain.error.SubscriptionError`, `com.feryaeljustice.mirailink.ui.error.RetryableViewModel`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.Job`, `kotlinx.coroutines.flow.MutableSharedFlow`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.SharedFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asSharedFlow`, `kotlinx.coroutines.flow.asStateFlow`, `kotlinx.coroutines.flow.distinctUntilChanged`, `kotlinx.coroutines.flow.drop`, `kotlinx.coroutines.flow.first`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`, `org.koin.core.annotation.KoinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesScreen.kt).

Declaraciones: `SearchPreferencesScreen`, `readCurrentLocation`.

Dependencias importadas: `android.Manifest`, `android.app.Activity`, `android.content.pm.PackageManager`, `android.widget.Toast`, `android.location.Geocoder`, `androidx.activity.compose.rememberLauncherForActivityResult`, `androidx.activity.result.contract.ActivityResultContracts`, `androidx.compose.animation.AnimatedVisibility`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.rememberScrollState`, `androidx.compose.foundation.verticalScroll`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Surface`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.rememberCoroutineScope`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.dp`, `androidx.core.app.ActivityCompat`, `androidx.core.content.ContextCompat`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.model.settings.SearchScope`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent`, `com.feryaeljustice.mirailink.ui.screens.settings.components.SearchSettingsSection`, `org.koin.compose.viewmodel.koinViewModel`, `kotlinx.coroutines.launch`, `java.util.Locale`, `com.feryaeljustice.mirailink.ui.utils.readBestCurrentLocation`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesViewModel.kt).

Declaraciones: `SearchPreferencesViewModel`, `reload`, `loadSearchPreferences`, `loadUserLocation`, `updateResidenceCoordinates`, `updateUserCoordinates`, `updateDraftRadius`, `updateDraftScope`, `updateMapCenter`, `updateDraftTargetCountry`, `save`.

Dependencias importadas: `androidx.lifecycle.ViewModel`, `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences`, `com.feryaeljustice.mirailink.domain.model.settings.SearchScope`, `com.feryaeljustice.mirailink.domain.usecase.settings.GetSearchPreferencesUseCase`, `com.feryaeljustice.mirailink.domain.usecase.settings.SaveSearchPreferencesUseCase`, `com.feryaeljustice.mirailink.domain.usecase.location.SendLocationPingUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase`, `com.feryaeljustice.mirailink.domain.util.GeoUtils`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.domain.util.isCountryCodeValid`, `com.feryaeljustice.mirailink.ui.error.ErrorRecovery`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.UiText`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.SharingStarted`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asStateFlow`, `kotlinx.coroutines.flow.combine`, `kotlinx.coroutines.flow.stateIn`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesScreen.kt).

Declaraciones: `ReceivedLikesScreen`, `ReceivedLikeItemCard`, `EmptyLikesState`, `PremiumLockedState`.

Dependencias importadas: `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.PaddingValues`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.lazy.LazyColumn`, `androidx.compose.foundation.lazy.items`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material3.Button`, `androidx.compose.material3.ButtonDefaults`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Surface`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.layout.ContentScale`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.text.style.TextAlign`, `androidx.compose.ui.unit.dp`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `coil.compose.AsyncImage`, `coil.request.ImageRequest`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent`, `com.feryaeljustice.mirailink.ui.utils.toast.showToast`, `org.koin.compose.viewmodel.koinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/likes/ReceivedLikesViewModel.kt).

Declaraciones: `ReceivedLikeItemViewEntry`, `ReceivedLikesUiState`, `ReceivedLikesUiEvent`, `MatchCreated`, `ReceivedLikesViewModel`, `loadLikes`, `matchUser`.

Dependencias importadas: `androidx.lifecycle.ViewModel`, `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.domain.usecase.swipe.GetReceivedLikesUseCase`, `com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.domain.util.calculateAge`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `kotlinx.coroutines.channels.Channel`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asStateFlow`, `kotlinx.coroutines.flow.receiveAsFlow`, `kotlinx.coroutines.flow.update`, `kotlinx.coroutines.launch`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesScreen.kt).

Declaraciones: `MessagesScreen`.

Dependencias importadas: `androidx.compose.animation.AnimatedContent`, `androidx.compose.animation.fadeIn`, `androidx.compose.animation.fadeOut`, `androidx.compose.animation.scaleIn`, `androidx.compose.animation.scaleOut`, `androidx.compose.animation.togetherWith`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.calculateEndPadding`, `androidx.compose.foundation.layout.consumeWindowInsets`, `androidx.compose.foundation.layout.displayCutout`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.layout.windowInsetsPadding`, `androidx.compose.foundation.rememberScrollState`, `androidx.compose.foundation.verticalScroll`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.material3.ExperimentalMaterial3Api`, `androidx.compose.material3.FloatingActionButton`, `androidx.compose.material3.HorizontalDivider`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.PlainTooltip`, `androidx.compose.material3.Scaffold`, `androidx.compose.material3.TooltipAnchorPosition`, `androidx.compose.material3.TooltipBox`, `androidx.compose.material3.TooltipDefaults`, `androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2`, `androidx.compose.material3.pulltorefresh.PullToRefreshBox`, `androidx.compose.material3.rememberTooltipState`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.saveable.rememberSaveable`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.LayoutDirection`, `androidx.compose.ui.unit.dp`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent`, `com.feryaeljustice.mirailink.ui.components.chat.ChatList`, `com.feryaeljustice.mirailink.ui.components.match.MatchesRow`, `com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration`, `com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding`, `org.koin.compose.viewmodel.koinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AppScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AppScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt).

Declaraciones: `MessagesViewModel`, `MessagesUiState`, `Idle`, `Loading`, `Success`, `Error`, `loadData`, `loadMatches`, `loadChats`.

Dependencias importadas: `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.data.mappers.ui.toChatPreviewViewEntry`, `com.feryaeljustice.mirailink.data.mappers.ui.toMatchUserViewEntry`, `com.feryaeljustice.mirailink.domain.usecase.chat.ChatUseCases`, `com.feryaeljustice.mirailink.domain.usecase.match.GetMatchesUseCase`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.domain.util.getFormattedUrl`, `com.feryaeljustice.mirailink.ui.error.RetryableViewModel`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `com.feryaeljustice.mirailink.ui.viewentries.chat.ChatPreviewViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.user.MatchUserViewEntry`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`, `org.koin.core.annotation.KoinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/onboarding/OnboardingScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/onboarding/OnboardingScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/onboarding/OnboardingScreen.kt).

Declaraciones: `OnboardingStep`, `OnboardingScreen`, `OnboardingScreenPreview`, `OnboardingScreenDarkPreview`.

Dependencias importadas: `androidx.annotation.StringRes`, `androidx.compose.animation.AnimatedVisibility`, `androidx.compose.animation.fadeIn`, `androidx.compose.animation.fadeOut`, `androidx.compose.foundation.Image`, `androidx.compose.foundation.background`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.displayCutout`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.layout.windowInsetsPadding`, `androidx.compose.foundation.pager.HorizontalPager`, `androidx.compose.foundation.pager.rememberPagerState`, `androidx.compose.foundation.rememberScrollState`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.foundation.verticalScroll`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.automirrored.filled.ArrowBack`, `androidx.compose.material.icons.automirrored.filled.ArrowForward`, `androidx.compose.material.icons.filled.Check`, `androidx.compose.material3.Button`, `androidx.compose.material3.ButtonDefaults`, `androidx.compose.material3.Icon`, `androidx.compose.material3.IconButton`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Text`, `androidx.compose.material3.TextButton`, `androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.rememberCoroutineScope`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.text.style.TextAlign`, `androidx.compose.ui.tooling.preview.Preview`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.screens.onboarding.components.OnboardingChatIllustration`, `com.feryaeljustice.mirailink.ui.screens.onboarding.components.OnboardingEventsIllustration`, `com.feryaeljustice.mirailink.ui.screens.onboarding.components.OnboardingPillIndicator`, `com.feryaeljustice.mirailink.ui.screens.onboarding.components.OnboardingProfileIllustration`, `com.feryaeljustice.mirailink.ui.screens.onboarding.components.OnboardingRadarConnectionIllustration`, `com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme`, `com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration`, `com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding`, `kotlinx.coroutines.launch`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/onboarding/components/OnboardingIllustrations.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/onboarding/components/OnboardingIllustrations.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/onboarding/components/OnboardingIllustrations.kt).

Declaraciones: `OnboardingRadarConnectionIllustration`, `OnboardingProfileIllustration`, `OnboardingChatIllustration`, `OnboardingEventsIllustration`, `OnboardingInterestTag`, `androidx.compose.ui.graphics.drawscope.DrawScope.drawKirakiraStar`.

Dependencias importadas: `androidx.compose.animation.core.FastOutSlowInEasing`, `androidx.compose.animation.core.RepeatMode`, `androidx.compose.animation.core.animateFloat`, `androidx.compose.animation.core.infiniteRepeatable`, `androidx.compose.animation.core.rememberInfiniteTransition`, `androidx.compose.animation.core.tween`, `androidx.compose.foundation.Canvas`, `androidx.compose.foundation.background`, `androidx.compose.foundation.border`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.ExperimentalLayoutApi`, `androidx.compose.foundation.layout.FlowRow`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.offset`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.automirrored.rounded.Send`, `androidx.compose.material.icons.filled.Favorite`, `androidx.compose.material.icons.filled.LocationOn`, `androidx.compose.material.icons.filled.Lock`, `androidx.compose.material.icons.filled.Person`, `androidx.compose.material.icons.filled.Star`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Surface`, `androidx.compose.material3.Text`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.draw.scale`, `androidx.compose.ui.res.stringResource`, `com.feryaeljustice.mirailink.R`, `androidx.compose.ui.geometry.Offset`, `androidx.compose.ui.graphics.Brush`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.graphics.Path`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.unit.IntOffset`, `androidx.compose.ui.unit.dp`, `androidx.compose.ui.unit.sp`, `kotlin.math.roundToInt`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/onboarding/components/OnboardingPillIndicator.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/onboarding/components/OnboardingPillIndicator.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/onboarding/components/OnboardingPillIndicator.kt).

Declaraciones: `OnboardingPillIndicator`.

Dependencias importadas: `androidx.compose.animation.animateColorAsState`, `androidx.compose.animation.core.Spring`, `androidx.compose.animation.core.animateDpAsState`, `androidx.compose.animation.core.spring`, `androidx.compose.foundation.background`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.unit.dp`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/onboarding/OnboardingScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/onboarding/OnboardingScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureScreen.kt).

Declaraciones: `ProfilePictureScreen`.

Dependencias importadas: `android.util.Log`, `androidx.activity.compose.BackHandler`, `androidx.activity.compose.rememberLauncherForActivityResult`, `androidx.activity.result.contract.ActivityResultContracts`, `androidx.compose.foundation.background`, `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.displayCutout`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.windowInsetsPadding`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.material3.Button`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.rememberUpdatedState`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.semantics.Role`, `androidx.compose.ui.unit.dp`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent`, `com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration`, `com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding`, `org.koin.compose.viewmodel.koinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AuthenticatedNavigationPolicy.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/AuthenticatedNavigationPolicy.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureViewModel.kt).

Declaraciones: `ProfilePictureViewModel`, `uploadImage`, `clearResult`.

Dependencias importadas: `android.net.Uri`, `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.domain.usecase.photos.UploadUserPhotoUseCase`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.ui.error.RetryableViewModel`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`, `org.koin.core.annotation.KoinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt).

Declaraciones: `ProfileScreenPreview`, `ProfileScreen`, `residenceAddress`, `ProfileViewModel.updateResidence`.

Dependencias importadas: `android.Manifest`, `android.content.Context`, `android.content.pm.PackageManager`, `android.location.Geocoder`, `android.location.Location`, `android.location.LocationManager`, `android.net.Uri`, `android.util.Log`, `android.widget.Toast`, `androidx.activity.compose.rememberLauncherForActivityResult`, `androidx.activity.result.contract.ActivityResultContracts`, `androidx.compose.animation.AnimatedContent`, `androidx.compose.animation.fadeIn`, `androidx.compose.animation.fadeOut`, `androidx.compose.animation.scaleIn`, `androidx.compose.animation.scaleOut`, `androidx.compose.animation.togetherWith`, `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.displayCutout`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.windowInsetsPadding`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Text`, `androidx.compose.ui.unit.sp`, `androidx.compose.material3.AlertDialog`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.material3.ExperimentalMaterial3Api`, `androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2`, `androidx.compose.material3.pulltorefresh.PullToRefreshBox`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableIntStateOf`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.rememberCoroutineScope`, `androidx.compose.runtime.saveable.rememberSaveable`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.dp`, `androidx.core.content.ContextCompat`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.data.util.createImageUri`, `com.feryaeljustice.mirailink.domain.enums.TextFieldType`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextButton`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorSnackbar`, `com.feryaeljustice.mirailink.ui.components.user.UserCard`, `com.feryaeljustice.mirailink.ui.screens.profile.ProfileViewModel.ProfileUiState`, `com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileIntent`, `com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileUiEvent`, `androidx.compose.runtime.remember`, `com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration`, `com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding`, `com.feryaeljustice.mirailink.ui.utils.toast.showToast`, `kotlinx.coroutines.Dispatchers`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`, `androidx.compose.ui.draw.blur`, `androidx.compose.foundation.background`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.text.font.FontFamily`, `androidx.compose.ui.text.font.FontWeight`, `com.feryaeljustice.mirailink.data.studio.BitmapOptimizationUtils`, `com.feryaeljustice.mirailink.data.studio.FaceDetectorDataSource`, `com.feryaeljustice.mirailink.data.studio.QualityMetricsCalculator`, `com.feryaeljustice.mirailink.domain.usecase.studio.AnalyzePhotoQualityUseCase`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkSnackbar`, `org.koin.compose.koinInject`, `org.koin.compose.viewmodel.koinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/bottombars/MiraiLinkBottomBar.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt).

Declaraciones: `ProfileViewModel`, `ProfileUiState`, `Idle`, `Loading`, `Success`, `Error`, `getCurrentUser`, `setIsInEditMode`, `onIntent`, `cleanupTempPhotos`, `loadCatalogIfNeeded`.

Dependencias importadas: `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.data.mappers.ui.toAnimeViewEntry`, `com.feryaeljustice.mirailink.data.mappers.ui.toGameViewEntry`, `com.feryaeljustice.mirailink.data.mappers.ui.toPhotoSlotViewEntry`, `com.feryaeljustice.mirailink.data.mappers.ui.toUserViewEntry`, `com.feryaeljustice.mirailink.data.util.deleteTempFile`, `com.feryaeljustice.mirailink.data.util.isTempFile`, `com.feryaeljustice.mirailink.domain.enums.TagType`, `com.feryaeljustice.mirailink.domain.enums.TextFieldType`, `com.feryaeljustice.mirailink.domain.error.ValidationError`, `com.feryaeljustice.mirailink.domain.usecase.catalog.GetAnimesUseCase`, `com.feryaeljustice.mirailink.domain.usecase.catalog.GetGamesUseCase`, `com.feryaeljustice.mirailink.domain.usecase.catalog.GetProfileOptionsUseCase`, `com.feryaeljustice.mirailink.domain.usecase.photos.DeleteUserPhotoUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.UpdateUserProfileUseCase`, `com.feryaeljustice.mirailink.domain.util.Logger`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.ui.error.RetryableViewModel`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileIntent`, `com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileUiEvent`, `com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileUiState`, `com.feryaeljustice.mirailink.ui.screens.profile.edit.ProfileMultiAttributeType`, `com.feryaeljustice.mirailink.ui.screens.profile.edit.ProfileSingleAttributeType`, `com.feryaeljustice.mirailink.ui.viewentries.media.PhotoSlotViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.user.GamerPromptAnswerViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.flow.MutableSharedFlow`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asSharedFlow`, `kotlinx.coroutines.flow.update`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`, `kotlinx.serialization.json.Json`, `org.koin.core.annotation.KoinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt).

Declaraciones: `UserProfileDetailScreen`, `UserProfileDetailContent`, `BottomInteractionBar`.

Dependencias importadas: `android.content.Intent`, `androidx.compose.foundation.background`, `androidx.compose.foundation.clickable`, `androidx.compose.foundation.gestures.detectTapGestures`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.ExperimentalLayoutApi`, `androidx.compose.foundation.layout.FlowRow`, `androidx.compose.foundation.layout.PaddingValues`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.pager.HorizontalPager`, `androidx.compose.foundation.pager.rememberPagerState`, `androidx.compose.foundation.rememberScrollState`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.foundation.verticalScroll`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Close`, `androidx.compose.material.icons.filled.Share`, `androidx.compose.material3.Button`, `androidx.compose.material3.ButtonDefaults`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.material3.ExperimentalMaterial3Api`, `androidx.compose.material3.FilledTonalButton`, `androidx.compose.material3.Icon`, `androidx.compose.material3.IconButton`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Scaffold`, `androidx.compose.material3.Surface`, `androidx.compose.material3.TopAppBar`, `androidx.compose.material3.TopAppBarDefaults`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableFloatStateOf`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.input.pointer.pointerInput`, `androidx.compose.ui.layout.ContentScale`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.unit.dp`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.data.mappers.ui.toUserViewEntry`, `com.feryaeljustice.mirailink.domain.model.haptics.HeartbeatAffinity`, `com.feryaeljustice.mirailink.domain.usecase.haptics.CalculateHeartbeatAffinityUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.ui.components.haptics.HapticHeartbeatOverlay`, `com.feryaeljustice.mirailink.ui.components.haptics.hapticHeartbeatLikeTrigger`, `com.feryaeljustice.mirailink.ui.haptics.HapticHeartbeatController`, `com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry`, `org.koin.compose.koinInject`, `coil.compose.AsyncImage`, `com.feryaeljustice.mirailink.ui.components.user.ChipFlowRow`, `com.feryaeljustice.mirailink.ui.components.user.GamerPromptCard`, `com.feryaeljustice.mirailink.ui.components.user.buildPersonalChips`, `com.feryaeljustice.mirailink.ui.components.user.buildCategorizedPersonalInfo`, `com.feryaeljustice.mirailink.ui.components.user.CategorizedPersonalInfoSection`, `coil.request.ImageRequest`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.util.calculateAge`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.media.FullscreenImagePreview`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent`, `com.feryaeljustice.mirailink.ui.utils.toast.showToast`, `com.feryaeljustice.mirailink.ui.viewentries.user.GamerPromptAnswerViewEntry`, `org.koin.compose.viewmodel.koinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailViewModel.kt).

Declaraciones: `UserProfileDetailUiState`, `UserProfileDetailUiEvent`, `MatchCreated`, `Disliked`, `UserProfileDetailViewModel`, `loadProfile`, `likeUser`, `dislikeUser`.

Dependencias importadas: `androidx.lifecycle.ViewModel`, `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.data.mappers.ui.toUserViewEntry`, `com.feryaeljustice.mirailink.domain.usecase.swipe.DislikeUserUseCase`, `com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.GetUserProfileByUsernameUseCase`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry`, `kotlinx.coroutines.channels.Channel`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asStateFlow`, `kotlinx.coroutines.flow.receiveAsFlow`, `kotlinx.coroutines.flow.update`, `kotlinx.coroutines.launch`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt).

Declaraciones: `ProfileSingleAttributeType`, `ProfileMultiAttributeType`, `EditProfileUiState`, `EditProfileIntent`, `Initialize`, `Save`, `UpdateTextField`, `UpdateResidenceCoordinates`, `SelectResidencePlace`, `EditResidenceText`, `ClearResidenceField`, `UpdateTags`, `ReorderPhoto`, `RemovePhoto`, `UpdatePhoto`, `OpenPhotoActionDialog`, `ClosePhotoDialogs`, `ShowPhotoSourceDialog`, `UpdateProfession`, `SelectSingleAttribute`, `UpdateMultiAttribute`, `AddOrUpdatePrompt`, `RemovePrompt`, `ChangePromptQuestion`, `EditProfileUiEvent`, `ProfileSavedSuccessfully`.

Dependencias importadas: `android.net.Uri`, `com.feryaeljustice.mirailink.data.model.response.catalog.ProfileOptionsResponseDto`, `com.feryaeljustice.mirailink.domain.enums.TagType`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.domain.enums.TextFieldType`, `com.feryaeljustice.mirailink.ui.viewentries.catalog.AnimeViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.catalog.GameViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.media.PhotoSlotViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.user.GamerPromptAnswerViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt).

Declaraciones: `SettingsScreen`, `SettingsSectionTitle`, `SettingsActionCard`.

Dependencias importadas: `android.widget.Toast`, `androidx.compose.animation.AnimatedVisibility`, `androidx.compose.foundation.gestures.detectTapGestures`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.displayCutout`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.windowInsetsPadding`, `androidx.compose.foundation.rememberScrollState`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.foundation.verticalScroll`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.automirrored.filled.ExitToApp`, `androidx.compose.material.icons.filled.Delete`, `androidx.compose.material.icons.filled.Favorite`, `androidx.compose.material.icons.filled.Info`, `androidx.compose.material.icons.filled.Lock`, `androidx.compose.material.icons.filled.Refresh`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Surface`, `androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.rememberUpdatedState`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.graphics.vector.ImageVector`, `androidx.compose.ui.input.pointer.pointerInput`, `androidx.compose.ui.platform.LocalUriHandler`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.unit.dp`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.BuildConfig`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.constants.deepLinkPrivacyPolicyUrl`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent`, `com.feryaeljustice.mirailink.ui.components.twofactor.TwoFactorPutCodeOrRecoveryCDialog`, `com.feryaeljustice.mirailink.ui.components.twofactor.TwoFactorSetupCompletedDialog`, `com.feryaeljustice.mirailink.ui.components.twofactor.TwoFactorSetupDialog`, `com.feryaeljustice.mirailink.ui.components.twofactor.TwoFactorStatusDialog`, `com.feryaeljustice.mirailink.ui.screens.settings.components.CurrentPlanCard`, `com.feryaeljustice.mirailink.ui.screens.settings.twofactor.configure.ConfigureTwoFactorViewModel`, `com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration`, `com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding`, `org.koin.compose.viewmodel.koinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsViewModel.kt).

Declaraciones: `SettingsViewModel`, `logout`, `deleteAccount`.

Dependencias importadas: `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.domain.usecase.auth.LogoutUseCase`, `com.feryaeljustice.mirailink.domain.usecase.users.DeleteAccountUseCase`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.ui.error.RetryableViewModel`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.flow.MutableSharedFlow`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asSharedFlow`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`, `org.koin.core.annotation.KoinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/CurrentPlanCard.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/CurrentPlanCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/CurrentPlanCard.kt).

Declaraciones: `CurrentPlanCard`.

Dependencias importadas: `androidx.compose.foundation.background`, `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Surface`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.graphics.Brush`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/SearchSettingsSection.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/SearchSettingsSection.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/SearchSettingsSection.kt).

Declaraciones: `SearchSettingsSection`.

Dependencias importadas: `androidx.compose.animation.AnimatedVisibility`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.ExperimentalLayoutApi`, `androidx.compose.foundation.layout.FlowRow`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.material3.FilterChip`, `androidx.compose.material3.FilterChipDefaults`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Slider`, `androidx.compose.material3.Surface`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.unit.dp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences`, `com.feryaeljustice.mirailink.domain.model.settings.SearchScope`, `com.feryaeljustice.mirailink.domain.util.isCountryCodeValid`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.map.SearchRadiusMinimap`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/search/SearchPreferencesScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqScreen.kt).

Declaraciones: `FaqScreen`, `FaqAccordionCard`.

Dependencias importadas: `androidx.compose.animation.AnimatedVisibility`, `androidx.compose.animation.core.animateFloatAsState`, `androidx.compose.animation.expandVertically`, `androidx.compose.animation.fadeIn`, `androidx.compose.animation.fadeOut`, `androidx.compose.animation.shrinkVertically`, `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.ExperimentalLayoutApi`, `androidx.compose.foundation.layout.FlowRow`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.displayCutout`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.windowInsetsPadding`, `androidx.compose.foundation.lazy.LazyColumn`, `androidx.compose.foundation.lazy.items`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.KeyboardArrowDown`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.FilterChip`, `androidx.compose.material3.FilterChipDefaults`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.rotate`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.unit.dp`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.model.faq.FaqCategory`, `com.feryaeljustice.mirailink.domain.model.faq.FaqItem`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration`, `com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding`, `org.koin.compose.viewmodel.koinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqViewModel.kt).

Declaraciones: `FaqViewModel`, `loadFaqItems`, `toggleItemExpansion`, `selectCategory`.

Dependencias importadas: `androidx.lifecycle.ViewModel`, `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.domain.model.faq.FaqCategory`, `com.feryaeljustice.mirailink.domain.model.faq.FaqItem`, `com.feryaeljustice.mirailink.domain.usecase.faq.GetFaqItemsUseCase`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asStateFlow`, `kotlinx.coroutines.launch`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackScreen.kt).

Declaraciones: `FeedbackScreen`, `FeedbackHeader`, `FeedbackPromptCard`, `FeedbackPrompt`.

Dependencias importadas: `android.widget.Toast`, `androidx.compose.animation.AnimatedContent`, `androidx.compose.animation.fadeIn`, `androidx.compose.animation.fadeOut`, `androidx.compose.animation.scaleIn`, `androidx.compose.animation.scaleOut`, `androidx.compose.animation.togetherWith`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.displayCutout`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.windowInsetsPadding`, `androidx.compose.foundation.rememberScrollState`, `androidx.compose.foundation.text.KeyboardActions`, `androidx.compose.foundation.text.KeyboardOptions`, `androidx.compose.foundation.verticalScroll`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.automirrored.filled.Send`, `androidx.compose.material.icons.filled.Favorite`, `androidx.compose.material.icons.filled.Info`, `androidx.compose.material.icons.filled.Star`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Surface`, `androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.rememberUpdatedState`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.graphics.vector.ImageVector`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.text.input.ImeAction`, `androidx.compose.ui.unit.dp`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextField`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent`, `com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration`, `com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding`, `org.koin.compose.viewmodel.koinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackViewModel.kt).

Declaraciones: `FeedbackViewModel`, `updateFeedback`, `sendFeedback`, `FeedbackState`.

Dependencias importadas: `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.domain.usecase.feedback.SendFeedbackUseCase`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.ui.error.RetryableViewModel`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.update`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`, `org.koin.core.annotation.KoinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt).

Declaraciones: `ConfigureTwoFactorScreen`.

Dependencias importadas: `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.displayCutout`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.windowInsetsPadding`, `androidx.compose.foundation.rememberScrollState`, `androidx.compose.foundation.verticalScroll`, `androidx.compose.material3.Icon`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.derivedStateOf`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.rememberUpdatedState`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.style.TextOverflow`, `androidx.compose.ui.unit.dp`, `androidx.compose.ui.unit.sp`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkBasicText`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkCard`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent`, `com.feryaeljustice.mirailink.ui.components.twofactor.TwoFactorSetupCompletedDialog`, `com.feryaeljustice.mirailink.ui.components.twofactor.TwoFactorSetupDialog`, `com.feryaeljustice.mirailink.ui.components.twofactor.TwoFactorStatusDialog`, `com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration`, `com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding`, `org.koin.compose.viewmodel.koinViewModel`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorViewModel.kt).

Declaraciones: `ConfigureTwoFactorViewModel`, `onlyCheckTwoFacStatusWithIO`, `onSetupTwoFactorCodeChanged`, `onDisableTwoFactorCodeChanged`, `launchSetupTwoFactorDialog`, `launchDisableTwoFactorDialog`, `confirmSetupTwoFactor`, `confirmDisableTwoFactor`, `checkTwoFacStatus`, `dismissSetupTwoFactorDialog`, `dismissStatusDialog`, `dismissSetupCompletedDialog`, `launchActivationFromStatus`, `dismissDisableTwoFactorDialog`.

Dependencias importadas: `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.DisableTwoFactorUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.GetTwoFactorStatusUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.SetupTwoFactorUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.VerifyTwoFactorUseCase`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.ui.error.RetryableViewModel`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.launch`, `org.koin.core.annotation.KoinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreen.kt).

Declaraciones: `SplashScreen`.

Dependencias importadas: `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.derivedStateOf`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.remember`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.platform.LocalContext`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.BuildConfig`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.components.appconfig.UpdateGate`, `com.feryaeljustice.mirailink.ui.navigation.InitialNavigationAction`, `com.feryaeljustice.mirailink.ui.utils.extensions.openPlayStore`, `org.koin.androidx.compose.koinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt).

Declaraciones: `SplashScreenViewModel`, `SplashUiState`, `Idle`, `Loading`, `Navigate`, `onDismissUpdateGate`.

Dependencias importadas: `androidx.lifecycle.ViewModel`, `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.BuildConfig`, `com.feryaeljustice.mirailink.core.featureflags.FeatureFlagStore`, `com.feryaeljustice.mirailink.data.mappers.ui.toVersionCheckResultViewEntry`, `com.feryaeljustice.mirailink.domain.usecase.CheckAppVersionUseCase`, `com.feryaeljustice.mirailink.domain.usecase.auth.AutologinUseCase`, `com.feryaeljustice.mirailink.domain.usecase.onboarding.CheckOnboardingIsCompleted`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.navigation.InitialNavigationAction`, `com.feryaeljustice.mirailink.ui.viewentries.VersionCheckResultViewEntry`, `kotlinx.coroutines.CoroutineDispatcher`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asStateFlow`, `kotlinx.coroutines.flow.update`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`, `org.koin.core.annotation.KoinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt).

Declaraciones: `MiraiStudioScreen`.

Dependencias importadas: `android.Manifest`, `android.content.pm.PackageManager`, `android.net.Uri`, `androidx.activity.compose.rememberLauncherForActivityResult`, `androidx.activity.result.contract.ActivityResultContracts`, `androidx.camera.core.CameraSelector`, `androidx.camera.core.ImageCapture`, `androidx.compose.foundation.background`, `androidx.compose.foundation.border`, `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.WindowInsets`, `androidx.compose.foundation.layout.displayCutout`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.navigationBarsPadding`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.statusBarsPadding`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.layout.windowInsetsPadding`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.automirrored.filled.ArrowBack`, `androidx.compose.material.icons.filled.Refresh`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.material3.Icon`, `androidx.compose.material3.Text`, `androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.rememberCoroutineScope`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.layout.ContentScale`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.semantics.contentDescription`, `androidx.compose.ui.semantics.semantics`, `androidx.compose.ui.text.font.FontFamily`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.unit.dp`, `androidx.compose.ui.unit.sp`, `androidx.core.content.ContextCompat`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `coil.compose.AsyncImage`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.data.studio.FaceDetectorDataSource`, `com.feryaeljustice.mirailink.data.studio.QualityMetricsCalculator`, `com.feryaeljustice.mirailink.data.studio.StudioPhotoAnalyzer`, `com.feryaeljustice.mirailink.domain.model.studio.MetricStatus`, `com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton`, `com.feryaeljustice.mirailink.ui.screens.studio.components.CameraPreviewView`, `com.feryaeljustice.mirailink.ui.screens.studio.components.FaceBoxOverlay`, `com.feryaeljustice.mirailink.ui.screens.studio.components.HudMetricGauge`, `com.feryaeljustice.mirailink.ui.screens.studio.components.HudQualityVerdictSheet`, `com.feryaeljustice.mirailink.ui.screens.studio.components.LaserScanOverlay`, `com.feryaeljustice.mirailink.ui.screens.studio.components.RuleOfThirdsOverlay`, `com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration`, `com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding`, `android.widget.Toast`, `com.feryaeljustice.mirailink.ui.utils.toast.showToast`, `org.koin.compose.viewmodel.koinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioUiState.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioUiState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioUiState.kt).

Declaraciones: `StudioMode`, `MiraiStudioUiState`.

Dependencias importadas: `android.net.Uri`, `com.feryaeljustice.mirailink.domain.model.studio.FaceBiometrics`, `com.feryaeljustice.mirailink.domain.model.studio.MiraiScanResult`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioViewModel.kt).

Declaraciones: `MiraiStudioViewModel`, `setTargetSlot`, `onCameraPermissionResult`, `switchCamera`, `onLiveFrameAnalysis`, `analyzeUri`, `capturePhoto`, `onImageSaved`, `onError`, `retry`, `confirmSelection`, `showStandardsDialog`, `loadOptimizedBitmap`.

Dependencias importadas: `android.content.Context`, `android.graphics.Bitmap`, `android.net.Uri`, `androidx.camera.core.ImageCapture`, `androidx.camera.core.ImageCaptureException`, `androidx.core.content.ContextCompat`, `androidx.lifecycle.SavedStateHandle`, `androidx.lifecycle.ViewModel`, `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.data.studio.FaceDetectorDataSource`, `com.feryaeljustice.mirailink.data.studio.QualityMetricsCalculator`, `com.feryaeljustice.mirailink.domain.model.studio.FaceBiometrics`, `com.feryaeljustice.mirailink.domain.usecase.studio.AnalyzePhotoQualityUseCase`, `kotlinx.coroutines.Dispatchers`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asStateFlow`, `kotlinx.coroutines.flow.update`, `kotlinx.coroutines.launch`, `kotlinx.coroutines.withContext`, `java.io.File`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/CameraPreviewView.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/CameraPreviewView.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/CameraPreviewView.kt).

Declaraciones: `CameraPreviewView`.

Dependencias importadas: `androidx.camera.core.CameraSelector`, `androidx.camera.core.ImageAnalysis`, `androidx.camera.core.ImageCapture`, `androidx.camera.core.Preview`, `androidx.camera.lifecycle.ProcessCameraProvider`, `androidx.camera.view.PreviewView`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.DisposableEffect`, `androidx.compose.runtime.remember`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.viewinterop.AndroidView`, `androidx.core.content.ContextCompat`, `androidx.lifecycle.compose.LocalLifecycleOwner`, `java.util.concurrent.Executors`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/FaceBoxOverlay.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/FaceBoxOverlay.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/FaceBoxOverlay.kt).

Declaraciones: `FaceBoxOverlay`, `mapX`, `mapY`.

Dependencias importadas: `androidx.compose.foundation.Canvas`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.geometry.Offset`, `androidx.compose.ui.geometry.Size`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.graphics.drawscope.Stroke`, `com.feryaeljustice.mirailink.domain.model.studio.FaceBiometrics`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/HudMetricGauge.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/HudMetricGauge.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/HudMetricGauge.kt).

Declaraciones: `HudMetricGauge`.

Dependencias importadas: `androidx.compose.animation.animateColorAsState`, `androidx.compose.animation.core.animateFloatAsState`, `androidx.compose.foundation.background`, `androidx.compose.foundation.border`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material3.Text`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.text.font.FontFamily`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.unit.dp`, `androidx.compose.ui.unit.sp`, `com.feryaeljustice.mirailink.domain.model.studio.MetricStatus`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/HudQualityVerdictSheet.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/HudQualityVerdictSheet.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/HudQualityVerdictSheet.kt).

Declaraciones: `HudQualityVerdictSheet`.

Dependencias importadas: `androidx.compose.foundation.background`, `androidx.compose.foundation.border`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.heightIn`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.rememberScrollState`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.foundation.verticalScroll`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.CheckCircle`, `androidx.compose.material.icons.filled.Info`, `androidx.compose.material.icons.filled.Warning`, `androidx.compose.material3.ButtonDefaults`, `androidx.compose.material3.Icon`, `androidx.compose.material3.OutlinedButton`, `androidx.compose.material3.Text`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontFamily`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.unit.dp`, `androidx.compose.ui.unit.sp`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.model.studio.MiraiScanResult`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/LaserScanOverlay.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/LaserScanOverlay.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/LaserScanOverlay.kt).

Declaraciones: `LaserScanOverlay`.

Dependencias importadas: `androidx.compose.animation.core.FastOutSlowInEasing`, `androidx.compose.animation.core.RepeatMode`, `androidx.compose.animation.core.animateFloat`, `androidx.compose.animation.core.infiniteRepeatable`, `androidx.compose.animation.core.rememberInfiniteTransition`, `androidx.compose.animation.core.tween`, `androidx.compose.foundation.Canvas`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.getValue`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.geometry.Offset`, `androidx.compose.ui.graphics.Brush`, `androidx.compose.ui.graphics.Color`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/QualityBadgesRow.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/QualityBadgesRow.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/QualityBadgesRow.kt).

Declaraciones: `QualityBadgesRow`, `QualityBadgeChip`.

Dependencias importadas: `androidx.compose.animation.AnimatedVisibility`, `androidx.compose.animation.fadeIn`, `androidx.compose.animation.scaleIn`, `androidx.compose.foundation.background`, `androidx.compose.foundation.border`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.ExperimentalLayoutApi`, `androidx.compose.foundation.layout.FlowRow`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Star`, `androidx.compose.material3.Icon`, `androidx.compose.material3.Text`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontFamily`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.unit.dp`, `androidx.compose.ui.unit.sp`, `com.feryaeljustice.mirailink.domain.model.studio.QualityBadge`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/RuleOfThirdsOverlay.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/RuleOfThirdsOverlay.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/RuleOfThirdsOverlay.kt).

Declaraciones: `RuleOfThirdsOverlay`.

Dependencias importadas: `androidx.compose.foundation.Canvas`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.geometry.Offset`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.graphics.PathEffect`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageScreen.kt).

Declaraciones: `SubscriptionManageScreen`, `ActivePerkRow`.

Dependencias importadas: `android.widget.Toast`, `androidx.compose.foundation.background`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.rememberScrollState`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.foundation.verticalScroll`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.automirrored.filled.ArrowBack`, `androidx.compose.material.icons.filled.Check`, `androidx.compose.material3.ButtonDefaults`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.material3.Icon`, `androidx.compose.material3.IconButton`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.OutlinedButton`, `androidx.compose.material3.Surface`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.platform.LocalUriHandler`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.unit.dp`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog`, `com.feryaeljustice.mirailink.ui.error.asString`, `com.feryaeljustice.mirailink.ui.utils.toast.showToast`, `org.koin.compose.viewmodel.koinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageViewModel.kt).

Declaraciones: `SubscriptionManageUiState`, `SubscriptionManageViewModel`, `refreshStatus`, `requestCancelIntent`, `clearCancelUrl`, `clearError`.

Dependencias importadas: `androidx.lifecycle.ViewModel`, `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanInfo`, `com.feryaeljustice.mirailink.domain.usecase.subscription.CancelSubscriptionIntentUseCase`, `com.feryaeljustice.mirailink.domain.usecase.subscription.GetSubscriptionStatusUseCase`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asStateFlow`, `kotlinx.coroutines.flow.update`, `kotlinx.coroutines.launch`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallScreen.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallScreen.kt).

Declaraciones: `SubscriptionPaywallScreen`, `TierTabButton`, `DurationCardsRow`, `DurationOptionCard`, `PerkItemRow`.

Dependencias importadas: `android.app.Activity`, `android.widget.Toast`, `androidx.compose.animation.animateColorAsState`, `androidx.compose.foundation.background`, `androidx.compose.foundation.border`, `androidx.compose.foundation.clickable`, `androidx.compose.foundation.layout.Arrangement`, `androidx.compose.foundation.layout.Box`, `androidx.compose.foundation.layout.Column`, `androidx.compose.foundation.layout.IntrinsicSize`, `androidx.compose.foundation.layout.Row`, `androidx.compose.foundation.layout.Spacer`, `androidx.compose.foundation.layout.fillMaxSize`, `androidx.compose.foundation.layout.fillMaxWidth`, `androidx.compose.foundation.layout.height`, `androidx.compose.foundation.layout.padding`, `androidx.compose.foundation.layout.size`, `androidx.compose.foundation.layout.width`, `androidx.compose.foundation.rememberScrollState`, `androidx.compose.foundation.shape.CircleShape`, `androidx.compose.foundation.shape.RoundedCornerShape`, `androidx.compose.foundation.verticalScroll`, `androidx.compose.material.icons.Icons`, `androidx.compose.material.icons.filled.Check`, `androidx.compose.material.icons.filled.Close`, `androidx.compose.material3.Button`, `androidx.compose.material3.ButtonDefaults`, `androidx.compose.material3.Card`, `androidx.compose.material3.CardDefaults`, `androidx.compose.material3.CircularProgressIndicator`, `androidx.compose.material3.Icon`, `androidx.compose.material3.IconButton`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.Surface`, `androidx.compose.material3.TextButton`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.clip`, `androidx.compose.ui.graphics.Brush`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.res.painterResource`, `androidx.compose.ui.res.stringResource`, `androidx.compose.ui.text.font.FontWeight`, `androidx.compose.ui.text.style.TextAlign`, `androidx.compose.ui.text.style.TextOverflow`, `androidx.compose.ui.unit.dp`, `androidx.compose.ui.unit.sp`, `androidx.lifecycle.compose.collectAsStateWithLifecycle`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionDuration`, `com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionOfferOption`, `com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanType`, `com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText`, `com.feryaeljustice.mirailink.ui.error.asString`, `com.feryaeljustice.mirailink.ui.utils.toast.showToast`, `org.koin.compose.viewmodel.koinViewModel`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/navigation/NavWrapper.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallViewModel.kt).

Declaraciones: `SubscriptionPaywallUiState`, `SubscriptionPaywallViewModel`, `selectTier`, `selectDuration`, `startPurchase`, `restorePurchases`, `clearMessage`, `clearError`.

Dependencias importadas: `android.app.Activity`, `androidx.lifecycle.ViewModel`, `androidx.lifecycle.viewModelScope`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.data.billing.BillingClientManager`, `com.feryaeljustice.mirailink.data.billing.BillingPurchaseEvent`, `com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionDuration`, `com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionOfferOption`, `com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanType`, `com.feryaeljustice.mirailink.domain.repository.SubscriptionRepository`, `com.feryaeljustice.mirailink.domain.usecase.subscription.LaunchBillingFlowUseCase`, `com.feryaeljustice.mirailink.domain.usecase.subscription.RestorePurchasesUseCase`, `com.feryaeljustice.mirailink.domain.util.MiraiLinkResult`, `com.feryaeljustice.mirailink.ui.error.UiError`, `com.feryaeljustice.mirailink.ui.error.toUiError`, `kotlinx.coroutines.flow.MutableStateFlow`, `kotlinx.coroutines.flow.StateFlow`, `kotlinx.coroutines.flow.asStateFlow`, `kotlinx.coroutines.flow.update`, `kotlinx.coroutines.launch`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/ViewModelModule.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/theme/AppThemeManager.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/theme/AppThemeManager.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/theme/AppThemeManager.kt).

Declaraciones: `AppThemeManager`, `ThemeMode`, `getThemeMode`, `shouldAppAliasBeUpdatedForThemeMode`, `updateAppAliasForThemeMode`.

Dependencias importadas: `android.content.ComponentName`, `android.content.Context`, `android.content.pm.PackageManager`, `com.feryaeljustice.mirailink.BuildConfig`, `com.feryaeljustice.mirailink.core.featureflags.FLAG_ENABLE_CHRISTMAS_THEME`, `com.feryaeljustice.mirailink.core.featureflags.FeatureFlag`, `com.feryaeljustice.mirailink.core.featureflags.FeatureFlagStore`, `kotlinx.coroutines.flow.first`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/di/koin/FeatureFlagModule.kt](../../app/src/main/java/com/feryaeljustice/mirailink/di/koin/FeatureFlagModule.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/MainActivity.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/MainActivity.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/MiraiLinkAppRoot.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/MiraiLinkAppRoot.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/theme/Color.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/theme/Color.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/theme/Color.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `androidx.compose.ui.graphics.Color`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/studio/QualityMetricsCalculator.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/studio/QualityMetricsCalculator.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkBasicText.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkBasicText.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkButton.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkButton.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkImage.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkImage.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkText.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkText.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkTextButton.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/atoms/MiraiLinkTextButton.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/MessageItem.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/MessageItem.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/explore/CategoryCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/explore/CategoryCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/haptics/HapticHeartbeatOverlay.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/haptics/HapticHeartbeatOverlay.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/map/SearchRadiusMinimap.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/map/SearchRadiusMinimap.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/match/MatchCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/match/MatchCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/EditablePhotoGrid.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/EditablePhotoGrid.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/FullscreenImagePreview.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/FullscreenImagePreview.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/PhotoCarousel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/PhotoCarousel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/MiraiLinkDialog.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/QrCodeImage.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/molecules/QrCodeImage.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupDialog.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/twofactor/TwoFactorSetupDialog.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/GamerPromptComponents.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/GamerPromptComponents.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/onboarding/components/OnboardingIllustrations.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/onboarding/components/OnboardingIllustrations.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/CurrentPlanCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/components/CurrentPlanCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/FaceBoxOverlay.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/FaceBoxOverlay.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/HudMetricGauge.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/HudMetricGauge.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/HudQualityVerdictSheet.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/HudQualityVerdictSheet.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/LaserScanOverlay.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/LaserScanOverlay.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/QualityBadgesRow.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/QualityBadgesRow.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/RuleOfThirdsOverlay.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/components/RuleOfThirdsOverlay.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionManageScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/subscription/SubscriptionPaywallScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/theme/Theme.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/theme/Theme.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/utils/extensions/Shadows.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/utils/extensions/Shadows.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/theme/Theme.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/theme/Theme.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/theme/Theme.kt).

Declaraciones: `ColorFamily`, `isContrastAvailable`, `selectSchemeForContrast`, `MiraiLinkTheme`.

Dependencias importadas: `android.app.UiModeManager`, `android.content.Context`, `android.os.Build`, `androidx.compose.foundation.isSystemInDarkTheme`, `androidx.compose.foundation.layout.Box`, `androidx.compose.material3.ColorScheme`, `androidx.compose.material3.MaterialTheme`, `androidx.compose.material3.darkColorScheme`, `androidx.compose.material3.dynamicDarkColorScheme`, `androidx.compose.material3.dynamicLightColorScheme`, `androidx.compose.material3.lightColorScheme`, `androidx.compose.runtime.Composable`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.platform.LocalContext`, `androidx.compose.ui.platform.LocalInspectionMode`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/MiraiLinkTopBar.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/topbars/MiraiLinkTopBar.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/theme/Type.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/theme/Type.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/theme/Type.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `androidx.compose.material3.Typography`, `androidx.compose.ui.text.font.FontFamily`, `androidx.compose.ui.text.googlefonts.Font`, `androidx.compose.ui.text.googlefonts.GoogleFont`, `com.feryaeljustice.mirailink.R`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/util/InterestImageFallback.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/util/InterestImageFallback.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/util/InterestImageFallback.kt).

Declaraciones: `InterestImageFallback`, `getFallbackDrawableRes`.

Dependencias importadas: `android.content.Context`, `androidx.annotation.DrawableRes`, `com.feryaeljustice.mirailink.R`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/VisualInterestPickerModal.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/VisualInterestPickerModal.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/utils/CurrentLocationReader.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/utils/CurrentLocationReader.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/utils/CurrentLocationReader.kt).

Declaraciones: `Context.hasForegroundLocationPermission`, `Context.readBestCurrentLocation`.

Dependencias importadas: `android.Manifest`, `android.content.Context`, `android.content.pm.PackageManager`, `android.location.Location`, `android.location.LocationManager`, `android.location.LocationListener`, `android.os.Build`, `android.os.Looper`, `androidx.core.content.ContextCompat`, `kotlin.coroutines.resume`, `kotlinx.coroutines.suspendCancellableCoroutine`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/utils/DeviceConfiguration.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/utils/DeviceConfiguration.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/utils/DeviceConfiguration.kt).

Declaraciones: `DeviceConfiguration`, `fromWindowSizeClass`, `DeviceConfiguration.requiresDisplayCutoutPadding`.

Dependencias importadas: `androidx.window.core.layout.WindowSizeClass`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/AuthScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/recover/RecoverPasswordScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/auth/verification/VerificationScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/ExploreScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/onboarding/OnboardingScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/onboarding/OnboardingScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/photo/ProfilePictureScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/SettingsScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/faq/FaqScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/feedback/FeedbackScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/settings/twofactor/configure/ConfigureTwoFactorScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/studio/MiraiStudioScreen.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/utils/MainUIUtils.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/utils/MainUIUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/utils/MainUIUtils.kt).

Declaraciones: `Context.findActivity`, `clampOffset`.

Dependencias importadas: `android.app.Activity`, `android.content.Context`, `android.content.ContextWrapper`, `androidx.compose.ui.geometry.Offset`, `androidx.compose.ui.unit.IntSize`, `androidx.compose.ui.util.fastCoerceIn`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/utils/composition/CompositionProviderUtils.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/utils/composition/CompositionProviderUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/utils/composition/CompositionProviderUtils.kt).

Declaraciones: Sin declaración detectada por el extractor; revisar la fuente.

Dependencias importadas: `androidx.compose.runtime.staticCompositionLocalOf`, `com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkSnackbarRequest`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/utils/extensions/ContextExt.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/utils/extensions/ContextExt.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/utils/extensions/ContextExt.kt).

Declaraciones: `Context.openPlayStore`.

Dependencias importadas: `android.content.Context`, `android.content.Intent`, `androidx.core.net.toUri`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/utils/extensions/DataExtensions.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/utils/extensions/DataExtensions.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/utils/extensions/DataExtensions.kt).

Declaraciones: `Gender.localizedLabel`.

Dependencias importadas: `androidx.compose.runtime.Composable`, `androidx.compose.ui.res.stringResource`, `com.feryaeljustice.mirailink.R`, `com.feryaeljustice.mirailink.domain.model.enum.Gender`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/utils/extensions/Debounce.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/utils/extensions/Debounce.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/utils/extensions/Debounce.kt).

Declaraciones: `Modifier.debounceClickable`.

Dependencias importadas: `androidx.compose.foundation.clickable`, `androidx.compose.runtime.Composable`, `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.getValue`, `androidx.compose.runtime.mutableStateOf`, `androidx.compose.runtime.remember`, `androidx.compose.runtime.setValue`, `androidx.compose.ui.Modifier`, `kotlinx.coroutines.delay`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/utils/extensions/Shadows.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/utils/extensions/Shadows.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/utils/extensions/Shadows.kt).

Declaraciones: `Modifier.shadow`.

Dependencias importadas: `android.graphics.BlurMaskFilter`, `androidx.compose.ui.Modifier`, `androidx.compose.ui.draw.drawBehind`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.graphics.Paint`, `androidx.compose.ui.graphics.RectangleShape`, `androidx.compose.ui.graphics.Shape`, `androidx.compose.ui.graphics.drawOutline`, `androidx.compose.ui.graphics.drawscope.drawIntoCanvas`, `androidx.compose.ui.graphics.toArgb`, `androidx.compose.ui.unit.Dp`, `androidx.compose.ui.unit.dp`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/utils/toast/ToastUtils.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/utils/toast/ToastUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/utils/toast/ToastUtils.kt).

Declaraciones: `showToast`.

Dependencias importadas: `android.content.Context`, `android.widget.Toast`.

Consumidores directos por importación o referencia al archivo: No detectados estáticamente; puede usarse por DI, manifest o reflexión.

## app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/VersionCheckResultViewEntry.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/VersionCheckResultViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/VersionCheckResultViewEntry.kt).

Declaraciones: `VersionCheckResultViewEntry`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/AppConfigMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/AppConfigMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt](../../app/src/main/java/com/feryaeljustice/mirailink/state/GlobalMiraiLinkSession.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/splash/SplashScreenViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/catalog/AnimeViewEntry.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/catalog/AnimeViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/catalog/AnimeViewEntry.kt).

Declaraciones: `AnimeViewEntry`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/CatalogViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/CatalogViewEntry.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestsGrid.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestsGrid.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/UserViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/UserViewEntry.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/catalog/GameViewEntry.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/catalog/GameViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/catalog/GameViewEntry.kt).

Declaraciones: `GameViewEntry`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/CatalogViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/CatalogViewEntry.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestsGrid.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/catalog/InterestsGrid.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/UserViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/UserViewEntry.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/chat/ChatMessageViewEntry.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/chat/ChatMessageViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/chat/ChatMessageViewEntry.kt).

Declaraciones: `ChatMessageViewEntry`.

Dependencias importadas: `com.feryaeljustice.mirailink.ui.viewentries.user.MinimalUserInfoViewEntry`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/ChatMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/ChatMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/chat/ChatPreviewViewEntry.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/chat/ChatPreviewViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/chat/ChatPreviewViewEntry.kt).

Declaraciones: `ChatPreviewViewEntry`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/ChatMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/ChatMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/ChatList.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/chat/ChatList.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/media/PhotoSlotViewEntry.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/media/PhotoSlotViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/media/PhotoSlotViewEntry.kt).

Declaraciones: `PhotoSlotViewEntry`.

Dependencias importadas: `android.net.Uri`, `kotlinx.serialization.Contextual`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/MediaMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/MediaMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/EditablePhotoGrid.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/media/EditablePhotoGrid.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/media/UserPhotoViewEntry.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/media/UserPhotoViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/media/UserPhotoViewEntry.kt).

Declaraciones: `UserPhotoViewEntry`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/MediaMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/MediaMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/UserViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/UserViewEntry.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/MatchUserViewEntry.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/MatchUserViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/MatchUserViewEntry.kt).

Declaraciones: `MatchUserViewEntry`.

Dependencias importadas: `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/UserMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/UserMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/match/MatchesRow.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/match/MatchesRow.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/messages/MessagesViewModel.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/MinimalUserInfoViewEntry.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/MinimalUserInfoViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/MinimalUserInfoViewEntry.kt).

Declaraciones: `MinimalUserInfoViewEntry`.

Dependencias importadas: `com.feryaeljustice.mirailink.domain.model.user.UserPhoto`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/UserMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/UserMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/util/UserUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/UserUtils.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/chat/ChatViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/chat/ChatMessageViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/chat/ChatMessageViewEntry.kt).

## app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/UserViewEntry.kt

Fuente: [app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/UserViewEntry.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/viewentries/user/UserViewEntry.kt).

Declaraciones: `UserViewEntry`, `GamerPromptAnswerViewEntry`.

Dependencias importadas: `com.feryaeljustice.mirailink.ui.viewentries.catalog.AnimeViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.catalog.GameViewEntry`, `com.feryaeljustice.mirailink.ui.viewentries.media.UserPhotoViewEntry`, `kotlinx.serialization.Serializable`.

Consumidores directos por importación o referencia al archivo: [app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/UserMappers.kt](../../app/src/main/java/com/feryaeljustice/mirailink/data/mappers/ui/UserMappers.kt), [app/src/main/java/com/feryaeljustice/mirailink/domain/util/UserUtils.kt](../../app/src/main/java/com/feryaeljustice/mirailink/domain/util/UserUtils.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/GamerPromptComponents.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/GamerPromptComponents.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/PublicUserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserCard.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/components/user/UserSwipeCardStack.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/explore/feed/CategoryFeedViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/home/HomeViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/ProfileViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailScreen.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailViewModel.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/detail/UserProfileDetailViewModel.kt), [app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt](../../app/src/main/java/com/feryaeljustice/mirailink/ui/screens/profile/edit/EditProfileUiState.kt).
