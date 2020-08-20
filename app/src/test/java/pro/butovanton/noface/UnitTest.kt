package pro.butovanton.noface

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import pro.butovanton.noface.di.App
import pro.butovanton.noface.di.AppComponent
import pro.butovanton.noface.di.AppModule
import pro.butovanton.noface.di.DaggerAppComponent
import javax.inject.Inject

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {

    lateinit var testAppomponent : AppComponent

    @Before
    fun init() {
       testAppomponent = DaggerAppComponent
            .builder()
            .appModule(AppModule())
            .build()
    }

    @Test
    fun initMyRef() {
        val repo = testAppomponent.getRepo()
            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    assertTrue(true)
                }

                override fun onCancelled(error: DatabaseError) {
                }
    }
            repo.ref.child("chatrooms").addListenerForSingleValueEvent(listener)

        }
    }


