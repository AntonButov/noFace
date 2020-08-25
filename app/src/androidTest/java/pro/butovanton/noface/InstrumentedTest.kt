package pro.butovanton.noface

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.reactivex.rxjava3.kotlin.subscribeBy

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import pro.butovanton.noface.Models.Room
import pro.butovanton.noface.Models.User
import pro.butovanton.noface.Models.UserApp
import pro.butovanton.noface.di.AppComponent
import pro.butovanton.noface.di.AppModule
import pro.butovanton.noface.di.DaggerAppComponent
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class InstrumentedTest {

    lateinit var testAppomponent: AppComponent
    lateinit var repo : Repo

    val TESTID = "testId"

    @Before
    fun init() {
        testAppomponent = DaggerAppComponent
            .builder()
            .appModule(AppModule())
            .build()
    repo = testAppomponent.getRepo()
    }

     @Test
    fun saveRoom() {
        var count = CountDownLatch(1)
        var user = User(2, 0)
        var userApp = UserApp()
        var room = Room(TESTID, user,userApp)

        repo.saveRoom(room)
            .addOnCompleteListener{
                assertTrue(true)
                count.countDown()
        }
        count.await(1, TimeUnit.MINUTES)
    }

    @Test
    fun getRoomChield() {
        var count = CountDownLatch(1)
        var d = repo.getRooms(User(),UserApp() )
            .subscribeBy ({  assertTrue(false)
                } ,
                {
                    count.countDown()
                }
            )
       if (!count.await(1, TimeUnit.MINUTES)) throw Exception("error getRoom")
        d.dispose()
    }

    @Test
    fun connectToChat() {
       var count = CountDownLatch(1)
       var d = repo.connecToChat()
            .subscribeBy (
                {},{
                    count.countDown()
                },{
                    assertTrue(it.text.equals(""))
            })
        if (!count.await(1, TimeUnit.MINUTES)) throw Exception("error getRoom")
        d.dispose()
        repo.disConnectFromChat()
    }

 }
