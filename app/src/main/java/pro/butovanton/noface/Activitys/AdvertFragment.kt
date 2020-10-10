package pro.butovanton.noface.Activitys

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.android.billingclient.api.*
import com.android.billingclient.api.Purchase.PurchasesResult
import kotlinx.android.synthetic.main.fragment_advert.*
import pro.butovanton.noface.R
import pro.butovanton.noface.di.App
import java.util.*


class AdvertFragment : Fragment() {

    lateinit var billingClient : BillingClient
    var lpurchase: List<Purchase>? = null
    var skuDetails: SkuDetails? = null
    private val mSkuId = "weekadvert"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_advert, container, false)

        val purchaseUpdateListener =
            PurchasesUpdatedListener { billingResult, purchases ->

                if (purchases != null) {
                    acknowledgePurchase(purchases.get(0))
                    consumePurchase(purchases.get(0))
                }
                // To be implemented in a later section.
            }

        billingClient = BillingClient.newBuilder(activity?.application!!)
            .setListener(purchaseUpdateListener)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.

                    // BillingClient готов. Вы можете запросить покупки здесь.
                    querySkuDetails() //запрос о товарах
                    lpurchase = queryPurchases()
                }
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })

        val adverMountButton = root.findViewById<Button>(R.id.adverMounth)
        adverMountButton.setOnClickListener {
           if (lpurchase?.size == 0) //подписок нет покупаем подписку
                launchBilling(mSkuId)
        }

        return root
    }

    private fun queryPurchases(): List<Purchase> {
        val purchasesResult: PurchasesResult =
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
        ) { billingResult, list ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && list != null) {
                if (list.size > 0)
                   skuDetails = list[0]
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

    fun launchBilling(skuId: String?) {
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails!!)
            .build()
        billingClient.launchBillingFlow(requireActivity(), billingFlowParams)
    }
}