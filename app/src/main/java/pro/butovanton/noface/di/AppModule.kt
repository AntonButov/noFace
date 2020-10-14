package pro.butovanton.noface.di

import android.app.Application
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import pro.butovanton.noface.Auth
import pro.butovanton.noface.Billing
import pro.butovanton.noface.Repo
import pro.butovanton.noface.RepoTest
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideRepo() : Repo {
        return Repo(myRef())
    }

    @Singleton
    @Provides
    fun provideRepoTest() : RepoTest {
        return RepoTest(myRefTest())
    }

    fun myRef() : DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child("chatrooms")
    }

    fun myRefTest() : DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child("chatroomsTest")
    }

    @Provides
    fun myAuth() : Auth {
        return  Auth()
    }

    @Singleton
    @Provides
    fun provideBilling() : Billing {
        return  Billing(getApp())
    }

    fun getApp() : Application {
        return (App).getApp()
    }
}