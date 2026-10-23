package com.feryaeljustice.mirailink.data.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryPurchasesAsync
import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter
import com.feryaeljustice.mirailink.domain.error.AppError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

sealed class BillingPurchaseEvent {
    data class PurchaseSuccess(val purchase: Purchase) : BillingPurchaseEvent()
    data class PurchaseFailed(val responseCode: Int, val debugMessage: String) : BillingPurchaseEvent()
    data class VerificationFailed(val error: AppError) : BillingPurchaseEvent()
    data object PurchaseCanceled : BillingPurchaseEvent()
}

class BillingClientManager(
    context: Context,
    private val scope: CoroutineScope,
    private val accountId: () -> String? = { null },
    private val crashReporter: CrashReporter? = null,
) : PurchasesUpdatedListener, BillingClientStateListener {

    companion object {
        private const val TAG = "BillingClientManager"
        const val PLUS_SUBSCRIPTION_PRODUCT_ID = "mirailink_plus"
        const val PREMIUM_SUBSCRIPTION_PRODUCT_ID = "mirailink_premium"

        const val BASE_PLAN_WEEKLY = "weekly-autorenew"
        const val BASE_PLAN_MONTHLY = "monthly-autorenew"
        const val BASE_PLAN_THREE_MONTHS = "three-month-autorenew"
        const val BASE_PLAN_THREEE_MONTHS = "threee-month-autorenew"
    }

    private val applicationContext = context.applicationContext

    private val pendingPurchasesParams = PendingPurchasesParams.newBuilder()
        .enableOneTimeProducts()
        .build()

    private val billingClient: BillingClient = BillingClient.newBuilder(applicationContext)
        .setListener(this)
        .enableAutoServiceReconnection()
        .enablePendingPurchases(pendingPurchasesParams)
        .build()

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _productDetailsMap = MutableStateFlow<Map<String, ProductDetails>>(emptyMap())
    val productDetailsMap: StateFlow<Map<String, ProductDetails>> = _productDetailsMap.asStateFlow()

    private val _productDetails = MutableStateFlow<ProductDetails?>(null)
    val productDetails: StateFlow<ProductDetails?> = _productDetails.asStateFlow()

    private val _purchaseEvents = MutableSharedFlow<BillingPurchaseEvent>(extraBufferCapacity = 5)
    val purchaseEvents: SharedFlow<BillingPurchaseEvent> = _purchaseEvents.asSharedFlow()

    init {
        startConnection()
    }

    fun startConnection() {
        logDiagnostic("startConnection: ready=${billingClient.isReady}")
        if (!billingClient.isReady) {
            billingClient.startConnection(this)
        } else {
            // An already ready client does not trigger our setup callback here.
            _isConnected.value = true
            querySubscriptionProducts()
        }
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        recordBillingResult("onBillingSetupFinished", billingResult)
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            _isConnected.value = true
            querySubscriptionProducts()
        } else {
            _isConnected.value = false
            Log.e(TAG, "Billing setup failed: code=${billingResult.responseCode}, message=${billingResult.debugMessage}")
        }
    }

    override fun onBillingServiceDisconnected() {
        logDiagnostic("onBillingServiceDisconnected: ready=${billingClient.isReady}")
        _isConnected.value = false
        Log.w(TAG, "Billing service disconnected")
    }

    fun querySubscriptionProducts() {
        if (!billingClient.isReady) {
            logDiagnostic("queryProductDetails skipped: client not ready; source=app")
            return
        }

        scope.launch {
            try {
                val productList = listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PLUS_SUBSCRIPTION_PRODUCT_ID)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PREMIUM_SUBSCRIPTION_PRODUCT_ID)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build(),
                )
                val params = QueryProductDetailsParams.newBuilder()
                    .setProductList(productList)
                    .build()

                // The KTX wrapper drops unfetched products. Keep the complete callback result.
                val (billingResult, result) = suspendCancellableCoroutine { continuation ->
                    billingClient.queryProductDetailsAsync(params) { response, products ->
                        if (continuation.isActive) continuation.resume(response to products)
                    }
                }
                recordBillingResult("queryProductDetails", billingResult)
                logDiagnostic(
                    "queryProductDetails: products=" + result.productDetailsList.map { details ->
                        "${details.productId}:basePlans=${details.subscriptionOfferDetails?.map { it.basePlanId }}"
                    } + "; unfetched=" + result.unfetchedProductList.map {
                        "${it.productId}:type=${it.productType}:statusCode=${it.statusCode}"
                    },
                )
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val map = result.productDetailsList.associateBy { it.productId }
                    _productDetailsMap.value = map
                    _productDetails.value = map[PREMIUM_SUBSCRIPTION_PRODUCT_ID]
                } else {
                    Log.e(TAG, "queryProductDetails error: ${billingResult.debugMessage}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception querying product details", e)
                crashReporter?.recordException(e, reason = "Play Billing queryProductDetails exception")
            }
        }
    }

    /**
     * Abre Google Play solo con el producto y el plan seleccionados.
     * El resultado inmediato indica apertura del flujo; la compra llega por onPurchasesUpdated.
     */
    fun launchBillingFlow(
        activity: Activity,
        productId: String = PREMIUM_SUBSCRIPTION_PRODUCT_ID,
        basePlanId: String = BASE_PLAN_MONTHLY,
    ): BillingResult {
        val context = "requestedProduct=$productId; requestedBasePlan=$basePlanId; ready=${billingClient.isReady}"
        logDiagnostic("launchBillingFlow: $context; loadedProducts=${_productDetailsMap.value.keys}")
        val details = _productDetailsMap.value[productId]
            ?: return BillingResult.newBuilder()
                .setResponseCode(BillingClient.BillingResponseCode.ITEM_UNAVAILABLE)
                .setDebugMessage("ProductDetails for $productId not available")
                .build()
                .also { recordBillingResult("launchBillingFlow", it, "app", context) }

        val offerToken = details.subscriptionOfferDetails?.firstOrNull {
            it.basePlanId == basePlanId
        }?.offerToken?.takeIf { it.isNotBlank() }
            ?: return BillingResult.newBuilder()
                .setResponseCode(BillingClient.BillingResponseCode.ITEM_UNAVAILABLE)
                .setDebugMessage("Base plan $basePlanId for $productId not available")
                .build()
                .also { recordBillingResult("launchBillingFlow", it, "app", context) }
        val selectedOffer = details.subscriptionOfferDetails?.firstOrNull { it.offerToken == offerToken }
        logDiagnostic(
            "launchBillingFlow selection: $context; actualProduct=${details.productId}; " +
                "actualBasePlan=${selectedOffer?.basePlanId}; offerId=${selectedOffer?.offerId}; " +
                "hasOfferToken=${offerToken.isNotEmpty()}",
        )

        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(details)
            .setOfferToken(offerToken)
            .build()

        val account = accountId() ?: return BillingResult.newBuilder()
            .setResponseCode(BillingClient.BillingResponseCode.ERROR)
            .setDebugMessage("App account unavailable before launching billing flow")
            .build()
            .also { recordBillingResult("launchBillingFlow", it, "app", context) }
        val accountHash = java.security.MessageDigest.getInstance("SHA-256").digest(account.toByteArray()).joinToString("") { "%02x".format(it) }
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setObfuscatedAccountId(accountHash)
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()

        return billingClient.launchBillingFlow(activity, billingFlowParams)
            .also { recordBillingResult("launchBillingFlow", it, context = context) }
    }

    fun launchBillingFlow(activity: Activity): BillingResult =
        launchBillingFlow(activity, PREMIUM_SUBSCRIPTION_PRODUCT_ID, BASE_PLAN_MONTHLY)

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        recordBillingResult("onPurchasesUpdated", billingResult)
        logDiagnostic("onPurchasesUpdated: purchaseCount=${purchases?.size ?: 0}")
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { purchase ->
                    scope.launch {
                        handlePurchase(purchase)
                    }
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                _purchaseEvents.tryEmit(BillingPurchaseEvent.PurchaseCanceled)
            }
            else -> {
                _purchaseEvents.tryEmit(
                    BillingPurchaseEvent.PurchaseFailed(
                        responseCode = billingResult.responseCode,
                        debugMessage = billingResult.debugMessage,
                    )
                )
            }
        }
    }

    /**
     * Emite PurchaseSuccess únicamente para PURCHASED. El backend verifica y reconoce la compra.
     * PENDING no genera éxito ni error aquí. SubscriptionRepositoryImpl consume el evento para
     * enviar el token al backend; acknowledgment no equivale a validación del servidor.
     */
    fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            _purchaseEvents.tryEmit(BillingPurchaseEvent.PurchaseSuccess(purchase))
        }
    }

    /**
     * Consulta restauración de compras SUBS. Un cliente desconectado o una respuesta fallida
     * produce la misma lista vacía que no tener compras; el llamador no puede distinguirlos.
     */
    suspend fun queryActivePurchases(): List<Purchase> {
        if (!billingClient.isReady) {
            logDiagnostic("queryPurchases skipped: client not ready; source=app")
            return emptyList()
        }
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()
        val result = billingClient.queryPurchasesAsync(params)
        recordBillingResult("queryPurchases", result.billingResult)
        logDiagnostic("queryPurchases: purchaseCount=${result.purchasesList.size}")
        return if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            result.purchasesList.filter { it.purchaseState == Purchase.PurchaseState.PURCHASED }
        } else {
            emptyList()
        }
    }

    // Keep original Play results in diagnostics only. Never log Purchase objects or tokens.
    private fun recordBillingResult(
        operation: String,
        result: BillingResult,
        source: String = "google_play",
        context: String = "",
    ) {
        val message = "$operation: source=$source; responseCode=${result.responseCode}; " +
            "debugMessage=${result.debugMessage}; $context"
        logDiagnostic(message)
        if (result.responseCode != BillingClient.BillingResponseCode.OK &&
            result.responseCode != BillingClient.BillingResponseCode.USER_CANCELED
        ) {
            Log.e(TAG, message)
            crashReporter?.recordException(
                IllegalStateException(message),
                reason = "Play Billing $operation",
                metadata = mapOf(
                    "operation" to operation,
                    "source" to source,
                    "response_code" to result.responseCode,
                    "debug_message" to result.debugMessage,
                    "billing_context" to context,
                ),
            )
        }
    }

    private fun logDiagnostic(message: String) {
        Log.d(TAG, message)
        crashReporter?.log("Play Billing: $message")
    }
}
