package pro.butovanton.noface.di

import dagger.Component
import pro.butovanton.noface.Repo
import pro.butovanton.noface.viewmodels.MainViewModelFactory
import javax.inject.Singleton

@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

   @Singleton
   fun getRepo() : Repo
   fun getMainViewModelFactory() : MainViewModelFactory



}