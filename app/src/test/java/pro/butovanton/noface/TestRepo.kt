package pro.butovanton.noface

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import pro.butovanton.noface.di.*
import javax.inject.Inject

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestRepo {

    var repo = App.appcomponent.getRepoTest()

    @Test
    fun getCountRoomsTest() = runBlocking {
        val countRooms = repo.getRoomsCount()
        assertEquals(countRooms, 1)
    }
}


