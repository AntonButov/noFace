package pro.butovanton.noface

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.internal.jdk8.FlowableFlatMapStream.subscribe

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import pro.butovanton.noface.Models.Room
import pro.butovanton.noface.Models.User
import pro.butovanton.noface.di.AppComponent
import pro.butovanton.noface.di.AppModule
import pro.butovanton.noface.di.DaggerAppComponent
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTest {

    lateinit var testAppomponent: AppComponent
    lateinit var repo : Repo

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
        var user = User(3, 0)
        var room = Room(repo.getKey(), user)

        repo.saveRoom(room)
            .addOnCompleteListener{
                assertTrue(true)
                count.countDown()
        }
        count.await(1, TimeUnit.MINUTES)
    }

    @Test
    fun getRoom() {
        var count = CountDownLatch(1)
       var listener =  object : ValueEventListener {
           override fun onDataChange(snapshot: DataSnapshot) {
                 count.countDown()
           }

           override fun onCancelled(error: DatabaseError) {
               TODO("Not yet implemented")
           }

       }
    repo.ref.addValueEventListener(listener)
    count.await(1, TimeUnit.MINUTES)
    repo.ref.removeEventListener(listener)
    }

    @Test
    fun getRoomChield() {

        var count = CountDownLatch(1)
        var d = repo.getRooms()
            .filter { it.user1.age == 0 }
            .subscribe{
                Log.d("room", it.key.toString())
            }
        count.await(1, TimeUnit.MINUTES)
        d.dispose()
    }

 }
