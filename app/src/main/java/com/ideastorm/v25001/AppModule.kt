package com.ideastorm.v25001

import com.ideastorm.v25001.service.ActivityService
import com.ideastorm.v25001.service.IActivityService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get()) }
    single<IActivityService> { ActivityService() }
}
