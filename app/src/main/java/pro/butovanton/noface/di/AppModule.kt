package pro.butovanton.noface.di

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import pro.butovanton.noface.Repo

@Module
class AppModule {

    @Provides
    fun provideRepo() : Repo {
        return Repo(myRef())
    }

    @Provides
    fun myRef() : DatabaseReference {
        return FirebaseDatabase.getInstance().reference.child("chatrooms")
    }
}