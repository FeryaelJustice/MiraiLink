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
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BillingPurchaseEvent {
    data class PurchaseSuccess(val purchase: Purchase) : BillingPurchaseEvent()
    data class PurchaseFailed(val responseCode: Int, val debugMessage: String) : BillingPurchaseEvent()
    data object PurchaseCanceled : BillingPurchaseEvent()
}

class BillingClientManager(
    context: Context,
    private val scope: CoroutineScope,
    private val accountId: () -> String? = { null },
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
        if (!billingClient.isReady) {
            billingClient.startConnection(this)
        }
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            _isConnected.value = true
            querySubscriptionProducts()
        } else {
            _isConnected.value = false
            Log.e(TAG, "Billing setup failed: code=${billingResult.responseCode}, message=${billingResult.debugMessage}")
        }
    }

    override fun onBillingServiceDisconnected() {
        _isConnected.value = false
        Log.w(TAG, "Billing service disconnected")
    }

    fun querySubscriptionProducts() {
        if (!billingClient.isReady) return

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

                val result = billingClient.queryProductDetails(params)
                if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val map = result.productDetailsList?.associateBy { it.productId } ?: emptyMap()
                    _productDetailsMap.value = map
                    _productDetails.value = map[PREMIUM_SUBSCRIPTION_PRODUCT_ID]
                } else {
                    Log.e(TAG, "queryProductDetails error: ${result.billingResult.debugMessage}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception querying product details", e)
            }
        }
    }

    /**
     * Abre Google Play desde el paywall con la oferta seleccionada. Si no coincide el base plan,
     * acepta las dos grafías del plan trimestral y después la primera oferta disponible.
     * El resultado inmediato indica apertura del flujo; la compra llega por onPurchasesUpdated.
     */
    fun launchBillingFlow(
        activity: Activity,
        productId: String = PREMIUM_SUBSCRIPTION_PRODUCT_ID,
        basePlanId: String = BASE_PLAN_MONTHLY,
    ): BillingResult {
        val details = _productDetailsMap.value[productId]
            ?: _productDetails.value
            ?: return BillingResult.newBuilder()
                .setResponseCode(BillingClient.BillingResponseCode.ITEM_UNAVAILABLE)
                .setDebugMessage("ProductDetails for $productId not available")
                .build()

        val offerToken = details.subscriptionOfferDetails?.firstOrNull {
            it.basePlanId == basePlanId
        }?.offerToken ?: details.subscriptionOfferDetails?.firstOrNull {
            (basePlanId == BASE_PLAN_THREE_MONTHS || basePlanId == BASE_PLAN_THREEE_MONTHS) &&
                (it.basePlanId == BASE_PLAN_THREE_MONTHS || it.basePlanId == BASE_PLAN_THREEE_MONTHS)
        }?.offerToken ?: details.subscriptionOfferDetails?.firstOrNull()?.offerToken ?: ""

        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(details)
            .setOfferToken(offerToken)
            .build()

        val account = accountId() ?: return BillingResult.newBuilder().setResponseCode(BillingClient.BillingResponseCode.ERROR).build()
        val accountHash = java.security.MessageDigest.getInstance("SHA-256").digest(account.toByteArray()).joinToString("") { "%02x".format(it) }
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setObfuscatedAccountId(accountHash)
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()

        return billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    fun launchBillingFlow(activity: Activity): BillingResult =
        launchBillingFlow(activity, PREMIUM_SUBSCRIPTION_PRODUCT_ID, BASE_PLAN_MONTHLY)

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
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
     * Emite PurchaseSuccess únicamente para PURCHASED y tras acknowledgment si es necesario.
     * PENDING no genera éxito ni error aquí. SubscriptionRepositoryImpl consume el evento para
     * enviar el token al backend; acknowledgment no equivale a validación del servidor.
     */
    suspend fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            _purchaseEvents.tryEmit(BillingPurchaseEvent.PurchaseSuccess(purchase))
        }
    }

    /**
     * Consulta restauración de compras SUBS. Un cliente desconectado o una respuesta fallida
     * produce la misma lista vacía que no tener compras; el llamador no puede distinguirlos.
     */
    suspend fun queryActivePurchases(): List<Purchase> {
        if (!billingClient.isReady) return emptyList()
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()
        val result = billingClient.queryPurchasesAsync(params)
        return if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            result.purchasesList.filter { it.purchaseState == Purchase.PurchaseState.PURCHASED }
        } else {
            emptyList()
        }
    }
}
