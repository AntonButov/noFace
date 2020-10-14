package pro.butovanton.noface

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.android.billingclient.api.Purchase
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subscribers.TestSubscriber
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class TestBilling {

    @Test
    fun testSubs() {
        val app =  InstrumentationRegistry.getInstrumentation().targetContext
        val billing = Billing(app)


    }

 }
