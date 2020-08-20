package pro.butovanton.noface.di

import dagger.Component
import pro.butovanton.noface.Repo
import pro.butovanton.noface.viewmodels.MainViewModelFactory

@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

   fun getRepo() : Repo
   fun getMainViewModelFactory() : MainViewModelFactory



}