package com.jerry.mvirxjava.data.repository.local

import javax.inject.Inject

class MyRepositoryImpl  @Inject constructor(): MyRespository {

    override fun getSomething(): String? {
        return "ABC"
    }
}