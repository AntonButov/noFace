package pro.butovanton.noface.di

import com.google.firebase.database.FirebaseDatabase
import dagger.Component
import pro.butovanton.noface.MainActivity
import pro.butovanton.noface.Repo
import pro.butovanton.noface.viewmodels.MainViewModel

@Component(modules = arrayOf(AppModule::class))

interface AppComponent {
   fun getRepo() : Repo

}