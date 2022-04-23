//package com.jerry.mvirxjava.di
//
//import com.jerry.mvirxjava.data.repository.local.MyRepositoryImpl
//import com.jerry.mvirxjava.data.repository.local.MyRespository
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//
//@Module
//@InstallIn(SingletonComponent::class)
//class MyResponsitoryModule {
//
//    @Provides
//    fun provideMyRepository() : MyRespository {
//        return MyRepositoryImpl()
//    }
//}