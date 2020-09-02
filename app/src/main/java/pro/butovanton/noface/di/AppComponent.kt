package pro.butovanton.noface.di

import dagger.Component
import pro.butovanton.noface.Auth
import pro.butovanton.noface.Repo
import pro.butovanton.noface.RepoTest
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

   fun getRepo() : Repo
   fun getRepoTest() : RepoTest
   fun getAuth() : Auth
}