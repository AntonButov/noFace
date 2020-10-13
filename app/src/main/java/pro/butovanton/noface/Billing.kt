package pro.butovanton.noface

import android.app.Activity
import android.app.Application
import android.util.Log
import android.widget.Button
import com.android.billingclient.api.*
import pro.butovanton.noface.di.App
import java.util.ArrayList

class Billing(val app : Application) {

    private var mySubs: List<Purchase>? = null
    private var subOnServer: SkuDetails? = null

    private var billingClient : BillingClient
    private val mSkuId = "weekadvert"

    private val purchaseUpdateListener =
        PurchasesUpdatedListener { billingResult, purchases ->

            if (purchases != null) {
                acknowledgePurchase(purchases.get(0))
                consumePurchase(purchases.get(0))
            }
            // To be implemented in a later section.
        }

    init {
        billingClient = BillingClient.newBuilder(app)
            .setListener(purchaseUpdateListener)
            .enablePendingPurchases()
            .build()
        getMySubs()
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
        if (purchasesResult.purchasesList!!.size == 0) {
            Log.d((App).TAG, " Подписки не найдены.")
        } else {
            Log.d((App).TAG, " Подписки найдены.")
        }
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
        val listener =
            ConsumeResponseListener { billingResult, purchaseToken ->
                Log.d((App).TAG, "onConsumePurchaseResponse: " + billingResult.getResponseCode().toString());
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
}