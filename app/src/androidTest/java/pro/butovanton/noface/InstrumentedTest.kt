package pro.butovanton.noface

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subscribers.TestSubscriber
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pro.butovanton.noface.Models.Room
import pro.butovanton.noface.Models.User
import pro.butovanton.noface.Models.UserApp
import pro.butovanton.noface.di.App
import pro.butovanton.noface.di.App.Companion.TAG
import pro.butovanton.noface.di.AppComponent
import pro.butovanton.noface.di.AppModule
import pro.butovanton.noface.di.DaggerAppComponent
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS

@RunWith(AndroidJUnit4::class)
class InstrumentedTest {

    lateinit var testAppomponent: AppComponent
    lateinit var repoTest : RepoTest
    lateinit var mAuth : Auth

    val TESTID = "testId"
/*
    @Before
    fun init() {
        testAppomponent = DaggerAppComponent
            .builder()
            .appModule(AppModule())
            .build()
    repoTest = testAppomponent.getRepoTest()
    mAuth =  (App).appcomponent.getAuth()

    var repo = (App).appcomponent.getRepo()
    var repo1 = (App).appcomponent.getRepo()
    }



     @Test
    fun saveRoom() {
        var count = CountDownLatch(1)
        var user = User(2, 0)
        var userApp = UserApp()
        var room = Room(repoTest.getKey() + "-" + TESTID, user, userApp)
        room.empty = true
        repoTest.setRoom(room)
        repoTest.setInOut(true)
        repoTest.saveRoom(room)
            .addOnCompleteListener{
                assertTrue(true)
                count.countDown()
        }
        count.await(1, TimeUnit.MINUTES)
    }

    @Test
    fun saveRooms() {
        for (i in 1 .. 10)
            saveRoom()
    }

    @Test
    fun findFreeRooms() {
        repoTest.muser = User()
        repoTest.muserApp = UserApp()
       var count =  CountDownLatch(1)
       repoTest.findFreeRoom2()
           .doOnSuccess { Log.e(TAG, "Find: " + it ) }
           .subscribeBy {
            assertTrue(it != null)
            count.countDown()
           }
           count.await(1,TimeUnit.MINUTES)
    }

    @Test
    fun getRoomsAndCreate() {
        var count =  CountDownLatch(1)
        repoTest.getRooms(User(), UserApp())
            .subscribeBy {
                assertTrue(it != null)
                Log.e(TAG, "LoadedAndCreate: " + it )
                count.countDown()
            }
        Thread.sleep(5000)

        repoTest.getRooms(User(), UserApp()).subscribe()

      if (!count.await(1,TimeUnit.MINUTES)) {
          Throwable(" Exep ")}
    }

    @After
    fun deleteAll() {
        var count =  CountDownLatch(1)
        repoTest.getRoomsList()
            .doOnNext {
                    repoTest.deleteRoom(it.key!!)
            }
            .doOnComplete { count.countDown() }
            .subscribe()
    count.await(1,TimeUnit.MINUTES)
    }

    fun  authRest() {
        assertTrue(mAuth.isAuth())
    }

 */

 }
