package pro.butovanton.noface

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry

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
