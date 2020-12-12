package pro.butovanton.noface

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import pro.butovanton.noface.Models.User
import pro.butovanton.noface.Models.UserApp
import pro.butovanton.noface.di.*
import javax.inject.Inject

class VilidatorUserTest {

    val МУЖСКОЙ = 0
    val ЖЕНСКИЙ = 1
    val ЛЮБОЙ = 2

    val allAgeApp = mutableListOf(true, true,true, true, true)

    lateinit var muser : User
    lateinit var muserApp : UserApp
    lateinit var user : User
    lateinit var userApp : UserApp

    @Test
    fun mm() {
    muser = User(МУЖСКОЙ, 0)
    muserApp = UserApp(МУЖСКОЙ, allAgeApp)
    user = User(МУЖСКОЙ, 0)
    userApp = UserApp(МУЖСКОЙ, allAgeApp)

    val validaterUser = ValidaterUser(muser, muserApp)
    assertTrue(validaterUser.isUserValid(user, userApp))
    }

    @Test
    fun mg() {
        muser = User(МУЖСКОЙ, 0)
        muserApp = UserApp(ЖЕНСКИЙ, allAgeApp)
        user = User(ЖЕНСКИЙ, 0)
        userApp = UserApp(МУЖСКОЙ, allAgeApp)

        val validaterUser = ValidaterUser(muser, muserApp)
        assertTrue(validaterUser.isUserValid(user, userApp))
    }

    @Test
    fun mavm() {
        muser = User(МУЖСКОЙ, 0)
        muserApp = UserApp(ЛЮБОЙ, allAgeApp)
        user = User(ЖЕНСКИЙ, 0)
        userApp = UserApp(МУЖСКОЙ, allAgeApp)

        val validaterUser = ValidaterUser(muser, muserApp)
        assertTrue(validaterUser.isUserValid(user, userApp))
    }

    @Test
    fun mamm() {
        muser = User(МУЖСКОЙ, 0)
        muserApp = UserApp(ЛЮБОЙ, allAgeApp)
        user = User(МУЖСКОЙ, 0)
        userApp = UserApp(МУЖСКОЙ, allAgeApp)

        val validaterUser = ValidaterUser(muser, muserApp)
        assertTrue(validaterUser.isUserValid(user, userApp))
    }

    @Test
    fun gmmm() {
        muser = User(ЖЕНСКИЙ, 0)
        muserApp = UserApp(МУЖСКОЙ, allAgeApp)
        user = User(МУЖСКОЙ, 0)
        userApp = UserApp(МУЖСКОЙ, allAgeApp)

        val validaterUser = ValidaterUser(muser, muserApp)
        assertFalse(validaterUser.isUserValid(user, userApp))
    }

    @Test
    fun aa() {
        muser = User(ЛЮБОЙ, 0)
        muserApp = UserApp(ЛЮБОЙ, allAgeApp)
        user = User(ЛЮБОЙ, 0)
        userApp = UserApp(ЛЮБОЙ, allAgeApp)

        val validaterUser = ValidaterUser(muser, muserApp)
        assertTrue(validaterUser.isUserValid(user, userApp))
    }
//TODO временный тест
    @Test
    fun gmmg() {
        muser = User(ЖЕНСКИЙ, 4)
        muserApp = UserApp(МУЖСКОЙ, mutableListOf(true, false, false, false, false))
        user = User(МУЖСКОЙ, 4)
        userApp = UserApp(ЖЕНСКИЙ, mutableListOf(true, false, false, false, false))

        val validaterUser = ValidaterUser(muser, muserApp)
        assertTrue(validaterUser.isUserValid(user, userApp))
    }
}


