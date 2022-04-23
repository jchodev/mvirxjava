package com.jerry.mvirxjava.di

import com.jerry.mvirxjava.data.repository.local.MyRepositoryImpl
import com.jerry.mvirxjava.data.repository.local.MyRespository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BindMyRepository {

    @Binds
    abstract fun bindMyRepository(myRepository : MyRepositoryImpl): MyRespository

}