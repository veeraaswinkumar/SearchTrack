package com.searchtracks.dagger

import com.searchtracks.MyApplication
import com.searchtracks.base.BaseActivity
import com.searchtracks.viewmodel.BaseViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class,DatabaseModule::class])
interface AppComponent {

    //App Components like activity, fragment, application, preference db needs to inject here
    // for the purpose of creating single object and accessing app module

    fun inject(myApplication: MyApplication)

    //Activity Inject
    fun inject(baseActivity: BaseActivity)


    //View Model Inject
    fun inject(baseViewModel: BaseViewModel)



}