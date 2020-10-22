package pro.butovanton.noface

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Button
import com.android.billingclient.api.*
import io.reactivex.rxjava3.core.Single
import pro.butovanton.noface.di.App
import java.util.ArrayList

class Billing(val app : Context) {

    var mySubs: List<Purchase>? = null
    set(value) {
        field = value
        value?.let { logSubs(it) }
    }
    var subOnServer: SkuDetails? = null

    private lateinit var billingClient : BillingClient
    private val mSkuId = "weekadvert"

    private val purchaseUpdateListener =
        PurchasesUpdatedListener { billingResult, purchases ->

            if (purchases != null) {
                acknowledgePurchase(purchases.get(0))
                consumePurchase(purchases.get(0))
             mySubs = purchases
            }
        }

    init {
        billingClient = BillingClient.newBuilder(app)
            .setListener(purchaseUpdateListener)
            .enablePendingPurchases()
            .build()
        getMySubs()
    }

    fun isValidBilling() : Boolean{
        return mySubs != null && subOnServer != null
    }

    fun isAdvertDontShow(): Boolean {
        return isValidBilling() && mySubs?.size!! > 0
    }

    private fun getMySubs() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.

                    // BillingClient готов. Вы можете запросить покупки здесь.
                    querySkuDetails() //запрос о товарах
                    mySubs = queryPurchases()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }
    private fun queryPurchases(): List<Purchase> {
        val purchasesResult: Purchase.PurchasesResult =
            billingClient.queryPurchases(BillingClient.SkuType.SUBS)
        return purchasesResult.purchasesList!!
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        val acknowledgePurchaseResponseListener = AcknowledgePurchaseResponseListener {
            //sendMail(purchase.getSku(), purchase.getOrderId());
        }
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient.acknowledgePurchase(
                    acknowledgePurchaseParams,
                    acknowledgePurchaseResponseListener
                )
            }
        }
    }

    private fun querySkuDetails() {
        val skuDetailsParamsBuilder = SkuDetailsParams.newBuilder()
        val skuList: MutableList<String> = ArrayList()
        skuList.add(mSkuId)
        skuDetailsParamsBuilder.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
        billingClient.querySkuDetailsAsync(
            skuDetailsParamsBuilder.build()
        ) { billingResult, subOnServerlist ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && subOnServerlist != null) {
                if (subOnServerlist.size > 0)
                    subOnServer = subOnServerlist[0]
                Log.d(App.TAG, "Подписки на сервере : " + subOnServerlist.size)
                //       for (SkuDetails skuDetails : list) {
                //            String sku = skuDetails.getSku();
                //                String price = skuDetails.getPrice();
                //           if ("premium_upgrade".equals(sku)) {
                //                 premiumUpgradePrice = price;
                //              } else if ("gas".equals(sku)) {
                //                  gasPrice = price;
                //              }
                //              }
            }
        }
    }

    private fun consumePurchase(purchase: Purchase) {
        val listener = ConsumeResponseListener { billingResult, purchaseToken ->
                Log.d((App).TAG, "Преобретена подписка.")
            }
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClient.consumeAsync(consumeParams, listener)
    }

    fun launchBilling(activity : Activity) {
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(subOnServer!!)
            .build()
        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    private fun logSubs(mSubs : List<Purchase> ) {
            for (mSub in mSubs)
                Log.d((App).TAG, "Найдена подписка: " + mSub.sku)

    }
}